package cn.iocoder.yudao.module.buyer.service.monthlynetdemand;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.buyer.controller.admin.monthlynetdemand.vo.MonthlyNetDemandPageReqVO;
import cn.iocoder.yudao.module.buyer.controller.admin.monthlynetdemand.vo.MonthlyNetDemandRespVO;
import cn.iocoder.yudao.module.buyer.dal.dataobject.monthlynetdemand.MonthlyDemandCodeMappingRow;
import cn.iocoder.yudao.module.buyer.dal.dataobject.monthlynetdemand.MonthlyDemandConfigRow;
import cn.iocoder.yudao.module.buyer.dal.dataobject.monthlynetdemand.MonthlyDemandPlanRow;
import cn.iocoder.yudao.module.buyer.dal.dataobject.monthlynetdemand.MonthlyDemandQuantityRow;
import cn.iocoder.yudao.module.buyer.dal.mysql.monthlynetdemand.MonthlyNetDemandMapper;
import com.baomidou.dynamic.datasource.annotation.DS;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 月净需求 Service 实现。
 *
 * <p>Mapper 仅负责读取和聚合基础数据；车型匹配、编码映射、需求计算和库存抵扣等业务规则
 * 统一在本类完成，便于后续调整计算口径。</p>
 */
@Service
@DS("oracle")
@Validated
@Slf4j
public class MonthlyNetDemandServiceImpl implements MonthlyNetDemandService {
    private static final BigDecimal ZERO = BigDecimal.ZERO;
    private static final Pattern NUMBER_PATTERN = Pattern.compile("\\d+");
    /** Oracle 单个 IN 条件最多允许 1000 个表达式，预留少量空间后按 900 个分批。 */
    private static final int MATERIAL_QUERY_BATCH_SIZE = 900;

    private volatile ConfigCache configCache;
    private volatile MappingCache mappingCache;

    @Resource
    private MonthlyNetDemandMapper monthlyNetDemandMapper;

    @Override
    public PageResult<MonthlyNetDemandRespVO> getPage(MonthlyNetDemandPageReqVO reqVO) {
        long start = System.currentTimeMillis();
        // 先计算所选月份的完整结果，再按页面条件过滤、排序和内存分页。
        List<MonthlyNetDemandRespVO> results = calculate(reqVO.getPlanMonth());
        long calculateEnd = System.currentTimeMillis();
        results = results.stream().filter(item -> matches(item, reqVO)).collect(Collectors.toList());
        results.sort(Comparator.comparing(MonthlyNetDemandRespVO::getMapped).reversed()
                .thenComparing(MonthlyNetDemandRespVO::getNetDemand, Comparator.reverseOrder())
                .thenComparing(MonthlyNetDemandRespVO::getMaterialNo,
                        Comparator.nullsLast(String::compareTo))
                .thenComparing(MonthlyNetDemandRespVO::getHostCode,
                        Comparator.nullsLast(String::compareTo)));

        int from = Math.min((reqVO.getPageNo() - 1) * reqVO.getPageSize(), results.size());
        int to = Math.min(from + reqVO.getPageSize(), results.size());
        log.info("月净需求分页完成，月份={}，计算耗时={}ms，过滤排序分页耗时={}ms，总耗时={}ms，结果数={}",
                reqVO.getPlanMonth(), calculateEnd - start, System.currentTimeMillis() - calculateEnd,
                System.currentTimeMillis() - start, results.size());
        return new PageResult<>(new ArrayList<>(results.subList(from, to)), (long) results.size());
    }

    /**
     * 计算指定计划月份的全部物料净需求。
     * 公式：净需求 = max(总需求 - SAP可用库存 - 驻外库存 - 在制订单, 0)。
     */
    private List<MonthlyNetDemandRespVO> calculate(String planMonth) {
        long start = System.currentTimeMillis();
        // 主机计划通过预计算的 plan_month 走索引查询，并限定为最新导入版本。
        List<MonthlyDemandPlanRow> plans = monthlyNetDemandMapper.selectPlanRows(planMonth);
        long planEnd = System.currentTimeMillis();
        ConfigCache currentConfigCache = getConfigCache();
        long configEnd = System.currentTimeMillis();
        MappingCache currentMappingCache = getMappingCache();
        long mappingEnd = System.currentTimeMillis();

        Map<String, DemandAccumulator> demandMap = new LinkedHashMap<>();
        for (MonthlyDemandPlanRow plan : plans) {
            if (!StringUtils.hasText(plan.getProductModel())) {
                continue;
            }
            List<MonthlyDemandConfigRow> matched = currentConfigCache.exactConfigs
                    .get(key(plan.getProductModel(), plan.getSeqNo2026()));
            if (matched == null || matched.isEmpty()) {
                // 精确匹配失败时，沿用需求对比模块规则，回退到该车型最大顺序号的配置。
                matched = currentConfigCache.fallbackConfigs
                        .getOrDefault(plan.getProductModel(), Collections.emptyList());
            }
            BigDecimal unitQuantity = value(plan.getUnitQuantity());
            for (MonthlyDemandConfigRow config : matched) {
                MonthlyDemandCodeMappingRow mapping = currentMappingCache.codeMappings.get(config.getHostCode());
                boolean mapped = mapping != null && StringUtils.hasText(mapping.getMaterialNo());
                // 已映射数据按特力编码合并；未映射数据按主机编码单独保留，避免需求被静默丢弃。
                String groupKey = mapped ? mapping.getMaterialNo() : "UNMAPPED:" + config.getHostCode();
                DemandAccumulator accumulator = demandMap.computeIfAbsent(groupKey,
                        ignored -> new DemandAccumulator(mapped,
                                mapped ? mapping.getMaterialNo() : null,
                                mapped && StringUtils.hasText(mapping.getMaterialName())
                                        ? mapping.getMaterialName() : config.getConfigMaterialNo()));
                accumulator.hostCodes.add(config.getHostCode());
                // 单项需求 = 主机计划台份数量 × 车型配置单台物料数量。
                accumulator.totalDemand = accumulator.totalDemand.add(
                        unitQuantity.multiply(value(config.getUnitRequiredQuantity())));
            }
        }
        long demandEnd = System.currentTimeMillis();

        // 只查询本月需求实际涉及的已映射物料，避免库存和订单表的全表聚合。
        List<String> materialNos = demandMap.values().stream().filter(item -> item.mapped)
                .map(item -> item.materialNo).filter(StringUtils::hasText).distinct().collect(Collectors.toList());
        InventoryQuantities inventory = queryInventoryQuantities(materialNos);
        long inventoryEnd = System.currentTimeMillis();

        List<MonthlyNetDemandRespVO> results = new ArrayList<>(demandMap.size());
        for (DemandAccumulator accumulator : demandMap.values()) {
            // 未映射物料无法可靠关联库存和订单，按约定三类抵扣数量均视为 0。
            BigDecimal sap = accumulator.mapped
                    ? inventory.sapStocks.getOrDefault(accumulator.materialNo, ZERO) : ZERO;
            BigDecimal overseas = accumulator.mapped
                    ? inventory.overseasStocks.getOrDefault(accumulator.materialNo, ZERO) : ZERO;
            BigDecimal openOrder = accumulator.mapped
                    ? inventory.openOrders.getOrDefault(accumulator.materialNo, ZERO) : ZERO;
            BigDecimal netDemand = accumulator.totalDemand.subtract(sap).subtract(overseas).subtract(openOrder);

            MonthlyNetDemandRespVO item = new MonthlyNetDemandRespVO();
            item.setPlanMonth(planMonth);
            item.setHostCode(String.join(",", accumulator.hostCodes));
            item.setMaterialNo(accumulator.materialNo);
            item.setMaterialName(accumulator.materialName);
            item.setMapped(accumulator.mapped);
            item.setTotalDemand(accumulator.totalDemand);
            item.setSapAvailableStock(sap);
            item.setOverseasAvailableStock(overseas);
            item.setInProcessOrderQuantity(openOrder);
            item.setNetDemand(netDemand.max(ZERO));
            results.add(item);
        }
        log.info("月净需求计算阶段耗时，月份={}，计划查询={}ms，配置缓存={}ms，编码缓存={}ms，需求匹配={}ms，" +
                        "库存汇总={}ms，结果组装={}ms，计划行数={}，物料数={}", planMonth, planEnd - start,
                configEnd - planEnd, mappingEnd - configEnd, demandEnd - mappingEnd,
                inventoryEnd - demandEnd, System.currentTimeMillis() - inventoryEnd, plans.size(), materialNos.size());
        return results;
    }

    /**
     * 获取最新车型配置缓存。每次仅查询一个版本值，导入日期变化时才重新加载全部配置。
     */
    private ConfigCache getConfigCache() {
        String version = monthlyNetDemandMapper.selectConfigVersion();
        ConfigCache current = configCache;
        if (current != null && Objects.equals(current.version, version)) {
            return current;
        }
        synchronized (this) {
            current = configCache;
            if (current != null && Objects.equals(current.version, version)) {
                return current;
            }
            List<MonthlyDemandConfigRow> configs = monthlyNetDemandMapper.selectConfigRows().stream()
                    .filter(item -> StringUtils.hasText(item.getVehicleModel()))
                    .filter(item -> StringUtils.hasText(item.getSeqNo2026()))
                    .filter(item -> StringUtils.hasText(item.getHostCode()))
                    .collect(Collectors.toList());
            Map<String, List<MonthlyDemandConfigRow>> exactConfigs = configs.stream()
                    .collect(Collectors.groupingBy(item -> key(item.getVehicleModel(), item.getSeqNo2026())));
            current = new ConfigCache(version, exactConfigs, buildFallbackConfigs(configs));
            configCache = current;
            log.info("月净需求车型配置缓存已刷新，版本={}，配置行数={}", version, configs.size());
            return current;
        }
    }

    /** 编码配置更新时间变化后自动刷新缓存，保证维护或重新导入后无需重启服务。 */
    private MappingCache getMappingCache() {
        String version = monthlyNetDemandMapper.selectCodeMappingVersion();
        MappingCache current = mappingCache;
        if (current != null && Objects.equals(current.version, version)) {
            return current;
        }
        synchronized (this) {
            current = mappingCache;
            if (current != null && Objects.equals(current.version, version)) {
                return current;
            }
            Map<String, MonthlyDemandCodeMappingRow> mappings = monthlyNetDemandMapper.selectCodeMappings().stream()
                    .filter(item -> StringUtils.hasText(item.getHostCode()))
                    .collect(Collectors.toMap(MonthlyDemandCodeMappingRow::getHostCode, Function.identity(),
                            (first, second) -> first));
            current = new MappingCache(version, mappings);
            mappingCache = current;
            log.info("月净需求编码映射缓存已刷新，版本={}，映射数={}", version, mappings.size());
            return current;
        }
    }

    /** 分批查询物料库存，规避 Oracle IN 条件 1000 项限制。 */
    private InventoryQuantities queryInventoryQuantities(List<String> materialNos) {
        InventoryQuantities result = new InventoryQuantities();
        for (int i = 0; i < materialNos.size(); i += MATERIAL_QUERY_BATCH_SIZE) {
            List<String> batch = materialNos.subList(i,
                    Math.min(i + MATERIAL_QUERY_BATCH_SIZE, materialNos.size()));
            for (MonthlyDemandQuantityRow row : monthlyNetDemandMapper.selectInventoryQuantities(batch)) {
                if (!StringUtils.hasText(row.getMaterialNo())) {
                    continue;
                }
                Map<String, BigDecimal> target = result.mapBySource(row.getSourceType());
                if (target != null) {
                    target.merge(row.getMaterialNo(), value(row.getQuantity()), BigDecimal::add);
                }
            }
        }
        return result;
    }

    /** 按车型选出数值顺序最大的配置集合，供精确匹配失败时回退。 */
    private Map<String, List<MonthlyDemandConfigRow>> buildFallbackConfigs(List<MonthlyDemandConfigRow> configs) {
        Map<String, List<MonthlyDemandConfigRow>> byModel = configs.stream()
                .collect(Collectors.groupingBy(MonthlyDemandConfigRow::getVehicleModel));
        Map<String, List<MonthlyDemandConfigRow>> result = new HashMap<>();
        byModel.forEach((model, rows) -> {
            String latestSequence = rows.stream().map(MonthlyDemandConfigRow::getSeqNo2026)
                    .max(this::compareSequence).orElse(null);
            result.put(model, rows.stream().filter(row -> Objects.equals(latestSequence, row.getSeqNo2026()))
                    .collect(Collectors.toList()));
        });
        return result;
    }

    /**
     * 比较类似“2026-6/”的业务顺序号，依次比较字符串中出现的各段数字。
     */
    private int compareSequence(String left, String right) {
        List<BigInteger> leftNumbers = numbers(left);
        List<BigInteger> rightNumbers = numbers(right);
        for (int i = 0; i < Math.max(leftNumbers.size(), rightNumbers.size()); i++) {
            BigInteger l = i < leftNumbers.size() ? leftNumbers.get(i) : BigInteger.ZERO;
            BigInteger r = i < rightNumbers.size() ? rightNumbers.get(i) : BigInteger.ZERO;
            int compared = l.compareTo(r);
            if (compared != 0) {
                return compared;
            }
        }
        return Comparator.nullsFirst(Comparator.<String>naturalOrder()).compare(left, right);
    }

    private List<BigInteger> numbers(String value) {
        List<BigInteger> result = new ArrayList<>();
        if (value == null) {
            return result;
        }
        Matcher matcher = NUMBER_PATTERN.matcher(value);
        while (matcher.find()) {
            result.add(new BigInteger(matcher.group()));
        }
        return result;
    }

    private boolean matches(MonthlyNetDemandRespVO item, MonthlyNetDemandPageReqVO reqVO) {
        if (StringUtils.hasText(reqVO.getMaterialNo())
                && !contains(item.getMaterialNo(), reqVO.getMaterialNo())
                && !contains(item.getHostCode(), reqVO.getMaterialNo())) {
            return false;
        }
        if (StringUtils.hasText(reqVO.getMaterialName())
                && !contains(item.getMaterialName(), reqVO.getMaterialName())) {
            return false;
        }
        return reqVO.getMapped() == null || reqVO.getMapped().equals(item.getMapped());
    }

    private boolean contains(String source, String keyword) {
        return source != null && source.toLowerCase(Locale.ROOT).contains(keyword.toLowerCase(Locale.ROOT));
    }

    private String key(String model, String sequence) {
        return String.valueOf(model) + '\u0000' + String.valueOf(sequence);
    }

    private BigDecimal value(BigDecimal value) {
        return value == null ? ZERO : value;
    }

    /**
     * 单个最终物料的需求累计器。一个特力编码可能对应多个主机编码，因此保留主机编码集合。
     */
    private static final class DemandAccumulator {
        private final boolean mapped;
        private final String materialNo;
        private final String materialName;
        private final Set<String> hostCodes = new LinkedHashSet<>();
        private BigDecimal totalDemand = ZERO;

        private DemandAccumulator(boolean mapped, String materialNo, String materialName) {
            this.mapped = mapped;
            this.materialNo = materialNo;
            this.materialName = materialName;
        }
    }

    private static final class ConfigCache {
        private final String version;
        private final Map<String, List<MonthlyDemandConfigRow>> exactConfigs;
        private final Map<String, List<MonthlyDemandConfigRow>> fallbackConfigs;

        private ConfigCache(String version, Map<String, List<MonthlyDemandConfigRow>> exactConfigs,
                            Map<String, List<MonthlyDemandConfigRow>> fallbackConfigs) {
            this.version = version;
            this.exactConfigs = exactConfigs;
            this.fallbackConfigs = fallbackConfigs;
        }
    }

    private static final class MappingCache {
        private final String version;
        private final Map<String, MonthlyDemandCodeMappingRow> codeMappings;

        private MappingCache(String version, Map<String, MonthlyDemandCodeMappingRow> codeMappings) {
            this.version = version;
            this.codeMappings = codeMappings;
        }
    }

    private static final class InventoryQuantities {
        private final Map<String, BigDecimal> sapStocks = new HashMap<>();
        private final Map<String, BigDecimal> overseasStocks = new HashMap<>();
        private final Map<String, BigDecimal> openOrders = new HashMap<>();

        private Map<String, BigDecimal> mapBySource(String sourceType) {
            if ("SAP".equals(sourceType)) {
                return sapStocks;
            }
            if ("OVERSEAS".equals(sourceType)) {
                return overseasStocks;
            }
            if ("OPEN_ORDER".equals(sourceType)) {
                return openOrders;
            }
            return null;
        }
    }
}
