package cn.iocoder.yudao.module.aps.service.mainplanprogressreport;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.aps.controller.admin.mainplanprogressreport.vo.*;
import cn.iocoder.yudao.module.aps.dal.mysql.mainplanprogressreport.MainPlanProgressReportMapper;
import com.baomidou.dynamic.datasource.annotation.DS;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import org.apache.commons.lang3.time.DateUtils;

@Service
@DS("oracle")
@Validated
@Slf4j
public class MainPlanProgressReportServiceImpl implements MainPlanProgressReportService {

    @Resource
    private MainPlanProgressReportMapper mainPlanProgressReportMapper;

    @Resource
    private JdbcTemplate jdbcTemplate;

    /**
     * 获取当月转序汇总数据
     */
    private Map<String, BigDecimal> getTransferSummary() {
        String sql = "SELECT LTRIM(order_no, '0') AS order_no, material_code, SUM(quantity) AS workshop_output " +
                "FROM buyer_production_transfer " +
                "WHERE deleted = '0' AND TO_CHAR(INITIATOR_DATE, 'YYYY-MM') = TO_CHAR(SYSDATE, 'YYYY-MM') " +
                "GROUP BY order_no, material_code";
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql);
        Map<String, BigDecimal> summary = new HashMap<>();
        for (Map<String, Object> row : rows) {
            String orderNo = row.get("order_no").toString();
            String materialCode = row.get("material_code").toString();
            String key = orderNo + "|" + materialCode;
            BigDecimal value = new BigDecimal(row.get("workshop_output").toString());
            summary.put(key, value);
            log.debug("转序汇总: key={}, value={}", key, value);
        }
        return summary;
    }

    /**
     * 按订单+物料分组，按排产日期顺序分配转序产出
     */
    private List<MainPlanProgressReportRespVO> assignWorkshopOutput(
            List<MainPlanProgressReportRespVO> planList,
            Map<String, BigDecimal> transferMap) {

        if (planList == null || planList.isEmpty()) {
            return planList;
        }

        // 按订单号+物料号分组
        Map<String, List<MainPlanProgressReportRespVO>> groupMap = planList.stream()
                .collect(Collectors.groupingBy(
                        p -> p.getProductionOrderNo() + "|" + p.getAssemblyMaterialNo(),
                        LinkedHashMap::new,
                        Collectors.toList()
                ));

        List<MainPlanProgressReportRespVO> result = new ArrayList<>();
        for (Map.Entry<String, List<MainPlanProgressReportRespVO>> entry : groupMap.entrySet()) {
            String key = entry.getKey();
            List<MainPlanProgressReportRespVO> group = entry.getValue();

            // 组内排序：按排产日期、创建时间
            group.sort(Comparator.comparing(MainPlanProgressReportRespVO::getScheduledDate,
                    Comparator.nullsLast(Comparator.naturalOrder()))
                    .thenComparing(MainPlanProgressReportRespVO::getOrderCreateTime,
                            Comparator.nullsLast(Comparator.naturalOrder())));

            BigDecimal totalTransfer = transferMap.getOrDefault(key, BigDecimal.ZERO);
            BigDecimal remaining = totalTransfer;

            log.debug("分组 key={}, 转序总量={}, 组内记录数={}", key, totalTransfer, group.size());

            for (MainPlanProgressReportRespVO record : group) {
                BigDecimal scheduledQty = record.getScheduledQuantity();
                if (remaining.compareTo(BigDecimal.ZERO) <= 0) {
                    record.setWorkshopOutput(BigDecimal.ZERO);
                    record.setDifference(scheduledQty);
                } else {
                    if (remaining.compareTo(scheduledQty) >= 0) {
                        record.setWorkshopOutput(scheduledQty);
                        record.setDifference(BigDecimal.ZERO);
                        remaining = remaining.subtract(scheduledQty);
                    } else {
                        record.setWorkshopOutput(remaining);
                        record.setDifference(scheduledQty.subtract(remaining));
                        remaining = BigDecimal.ZERO;
                    }
                }
                log.debug("分配结果: 订单={}, 物料={}, 排产日期={}, 计划={}, 产出={}, 差异={}",
                        record.getProductionOrderNo(), record.getAssemblyMaterialNo(),
                        record.getScheduledDate(), scheduledQty,
                        record.getWorkshopOutput(), record.getDifference());
                result.add(record);
            }

            if (remaining.compareTo(BigDecimal.ZERO) > 0 && !group.isEmpty()) {
                MainPlanProgressReportRespVO last = group.get(group.size() - 1);
                BigDecimal newOutput = last.getWorkshopOutput().add(remaining);
                last.setWorkshopOutput(newOutput);
                last.setDifference(last.getScheduledQuantity().subtract(newOutput));
                log.warn("分组 {} 分配后仍有剩余产出 {}, 追加到最后一条记录", key, remaining);
            }
        }

        // 按订单号+物料号排序，保持输出稳定
        result.sort(Comparator.comparing(MainPlanProgressReportRespVO::getProductionOrderNo)
                .thenComparing(MainPlanProgressReportRespVO::getAssemblyMaterialNo));
        return result;
    }

    /**
     * 内存过滤：根据分页请求中的条件筛选记录
     */
    private List<MainPlanProgressReportRespVO> filterByConditions(
            List<MainPlanProgressReportRespVO> allList,
            MainPlanProgressReportPageReqVO reqVO) {

        return allList.stream()
                .filter(record -> {
                    // 生产订单号（支持前导零忽略匹配）
                    if (reqVO.getProductionOrderNo() != null && !reqVO.getProductionOrderNo().isEmpty()) {
                        String dbOrder = record.getProductionOrderNo();
                        String input = reqVO.getProductionOrderNo();
                        // 去除前导零后比较
                        if (!dbOrder.replaceFirst("^0+", "").equals(input.replaceFirst("^0+", ""))) {
                            return false;
                        }
                    }
                    // 总成物料号
                    if (reqVO.getAssemblyMaterialNo() != null && !reqVO.getAssemblyMaterialNo().isEmpty()) {
                        if (!reqVO.getAssemblyMaterialNo().equals(record.getAssemblyMaterialNo())) {
                            return false;
                        }
                    }
                    // 主物料描述（模糊匹配）
                    if (reqVO.getMainMaterialDesc() != null && !reqVO.getMainMaterialDesc().isEmpty()) {
                        if (record.getMainMaterialDesc() == null ||
                                !record.getMainMaterialDesc().contains(reqVO.getMainMaterialDesc())) {
                            return false;
                        }
                    }
                    // 排产日期范围
                    if (reqVO.getScheduledDateStart() != null) {
                        if (record.getScheduledDate() == null ||
                                record.getScheduledDate().before(reqVO.getScheduledDateStart())) {
                            return false;
                        }
                    }
                    if (reqVO.getScheduledDateEnd() != null) {
                        if (record.getScheduledDate() == null ||
                                record.getScheduledDate().after(reqVO.getScheduledDateEnd())) {
                            return false;
                        }
                    }
                    // 排产数量
                    if (reqVO.getScheduledQuantity() != null) {
                        if (record.getScheduledQuantity() == null ||
                                record.getScheduledQuantity().compareTo(reqVO.getScheduledQuantity()) != 0) {
                            return false;
                        }
                    }
                    // 生产车间
                    if (reqVO.getProductionWorkshop() != null && !reqVO.getProductionWorkshop().isEmpty()) {
                        if (!reqVO.getProductionWorkshop().equals(record.getProductionWorkshop())) {
                            return false;
                        }
                    }
                    // 创建时间范围
                    if (reqVO.getCreateTimeStart() != null) {
                        if (record.getOrderCreateTime() == null ||
                                record.getOrderCreateTime().before(reqVO.getCreateTimeStart())) {
                            return false;
                        }
                    }
                    if (reqVO.getCreateTimeEnd() != null) {
                        if (record.getOrderCreateTime() == null ||
                                record.getOrderCreateTime().after(reqVO.getCreateTimeEnd())) {
                            return false;
                        }
                    }
                    return true;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<MainPlanProgressReportRespVO> getProgressReport() {
        // 全量无筛选，用于导出
        List<MainPlanProgressReportRespVO> originalList = mainPlanProgressReportMapper.selectProgressReport();
        Map<String, BigDecimal> transferMap = getTransferSummary();
        return assignWorkshopOutput(originalList, transferMap);
    }

    @Override
    public PageResult<MainPlanProgressReportRespVO> getProgressReportPage(MainPlanProgressReportPageReqVO pageReqVO) {
        // 1. 获取所有原始数据（未筛选）
        List<MainPlanProgressReportRespVO> allOriginal = mainPlanProgressReportMapper.selectProgressReport();

        // 2. 内存中筛选
        List<MainPlanProgressReportRespVO> filtered = filterByConditions(allOriginal, pageReqVO);

        // 3. 获取转序汇总
        Map<String, BigDecimal> transferMap = getTransferSummary();

        // 4. 分配产出
        List<MainPlanProgressReportRespVO> assigned = assignWorkshopOutput(filtered, transferMap);

        // 5. 手动分页
        int pageNo = pageReqVO.getPageNo();
        int pageSize = pageReqVO.getPageSize();
        int start = (pageNo - 1) * pageSize;
        int end = Math.min(start + pageSize, assigned.size());

        List<MainPlanProgressReportRespVO> pageList;
        if (start >= assigned.size()) {
            pageList = Collections.emptyList();
        } else {
            pageList = assigned.subList(start, end);
        }

        return new PageResult<>(pageList, (long) assigned.size());
    }



    @Override
    public List<DashboardWorkshopRespVO> getWorkshopStats(String startDate, String endDate, String workshop, String supplier) {
        return mainPlanProgressReportMapper.selectWorkshopStats(startDate, endDate, workshop, supplier);
    }

    @Override
    public List<DashboardSupplierRespVO> getSupplierStats(String startDate, String endDate, String workshop, String supplier) {
        return mainPlanProgressReportMapper.selectSupplierStats(startDate, endDate, workshop, supplier);
    }

    @Override
    public List<DashboardMaterialShortageRespVO> getMaterialShortage(String startDate, String endDate, String workshop, String supplier) {
        return mainPlanProgressReportMapper.selectMaterialShortage(startDate, endDate, workshop, supplier);
    }

    @Override
    public PageResult<DashboardOrderRespVO> getOrderPage(DashboardOrderPageReqVO reqVO) {
        // 1. 先查总数
        Long total = mainPlanProgressReportMapper.selectOrderPageCount(reqVO);
        if (total == null || total == 0) {
            return new PageResult<>(Collections.emptyList(), 0L);
        }

        // 2. 分页查询列表
        List<DashboardOrderRespVO> list = mainPlanProgressReportMapper.selectOrderPage(reqVO);
        return new PageResult<>(list, total);
    }

    @Override
    public List<OrderShortageRespVO> getOrderShortages(String orderNo) {
        return mainPlanProgressReportMapper.selectOrderShortages(orderNo);
    }

    @Override
    public List<ComponentPurchaseRespVO> getComponentPurchases(String componentCode,String orderNo) {
        return mainPlanProgressReportMapper.selectComponentPurchases(componentCode,orderNo);
    }

    @Override
    public DashboardOverviewRespVO getOverview(String startDate, String endDate, String workshop, String supplier) {
        // 1. 获取数据库中最新的创建日期（今天）
        Date latestDate = mainPlanProgressReportMapper.selectLatestCreateDate();
        if (latestDate == null) {
            // 无任何数据，返回空对象
            return new DashboardOverviewRespVO();
        }

        // 格式化日期
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
        String today = sdf.format(latestDate);
        String yesterday = sdf.format(org.apache.commons.lang3.time.DateUtils.addDays(latestDate, -1));

        // 2. 查询今天的数据（使用 selectOverviewByCreateDate）
        DashboardOverviewRespVO todayData = mainPlanProgressReportMapper.selectOverviewByCreateDate(startDate, endDate, workshop, supplier, today);
        if (todayData == null) {
            todayData = new DashboardOverviewRespVO();
        }

        // 3. 查询昨天的数据
        DashboardOverviewRespVO yesterdayData = mainPlanProgressReportMapper.selectOverviewByCreateDate(startDate, endDate, workshop, supplier, yesterday);
        // 如果昨天没有数据，则将 yesterdayData 置为 null，后续趋势统一为 0
        if (yesterdayData == null) {
            yesterdayData = new DashboardOverviewRespVO(); // 使用空对象，所有值为 null 或 0
        }

        // 4. 辅助方法：获取安全的 BigDecimal 值（null -> 0）
        java.util.function.Function<BigDecimal, BigDecimal> safe = v -> v == null ? BigDecimal.ZERO : v;

        // 5. 计算各指标的趋势（今天相对于昨天的变化百分比）
        // 计划总量趋势
        BigDecimal planQtyToday = safe.apply(todayData.getTotalPlanQty());
        BigDecimal planQtyYest = safe.apply(yesterdayData.getTotalPlanQty());
        BigDecimal planQtyTrend = calcTrend(planQtyToday, planQtyYest);

        // 完工总量趋势
        BigDecimal completeQtyToday = safe.apply(todayData.getTotalCompletedQty());
        BigDecimal completeQtyYest = safe.apply(yesterdayData.getTotalCompletedQty());
        BigDecimal completeQtyTrend = calcTrend(completeQtyToday, completeQtyYest);

        // 完工率趋势
        BigDecimal completionRateToday = safe.apply(todayData.getCompletionRate());
        BigDecimal completionRateYest = safe.apply(yesterdayData.getCompletionRate());
        BigDecimal completionRateTrend = calcTrend(completionRateToday, completionRateYest);

        // 投料未完成率（需要根据 totalRequiredQty 和 completedShortageQty 先计算出百分比）
        BigDecimal materialRateToday = calcMaterialShortageRate(todayData);
        BigDecimal materialRateYest = calcMaterialShortageRate(yesterdayData);
        BigDecimal materialRateTrend = calcTrend(materialRateToday, materialRateYest);

        // 缺料订单数趋势
        Integer shortageToday = todayData.getShortageOrders();
        Integer shortageYest = yesterdayData.getShortageOrders();
        BigDecimal shortageOrdersTrend = calcTrend(
                shortageToday == null ? BigDecimal.ZERO : BigDecimal.valueOf(shortageToday),
                shortageYest == null ? BigDecimal.ZERO : BigDecimal.valueOf(shortageYest)
        );

        // 物料齐套率趋势
        BigDecimal kitRateToday = safe.apply(todayData.getKitRate());
        BigDecimal kitRateYest = safe.apply(yesterdayData.getKitRate());
        BigDecimal kitRateTrend = calcTrend(kitRateToday, kitRateYest);

        // 6. 将趋势值设置到 todayData 中
        todayData.setPlanQtyTrend(planQtyTrend);
        todayData.setCompleteQtyTrend(completeQtyTrend);
        todayData.setCompletionRateTrend(completionRateTrend);
        todayData.setMaterialRateTrend(materialRateTrend);
        todayData.setShortageOrdersTrend(shortageOrdersTrend);
        todayData.setKitRateTrend(kitRateTrend);

        return todayData;
    }

    /**
     * 计算环比趋势百分比：(today - yesterday) / yesterday * 100
     */
    private BigDecimal calcTrend(BigDecimal today, BigDecimal yesterday) {
        if (yesterday == null || yesterday.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        return today.subtract(yesterday)
                .divide(yesterday, 4, BigDecimal.ROUND_HALF_UP)
                .multiply(BigDecimal.valueOf(100));
    }

    /**
     * 计算投料未完成率 = completedShortageQty / totalRequiredQty * 100
     */
    private BigDecimal calcMaterialShortageRate(DashboardOverviewRespVO data) {
        if (data == null) return BigDecimal.ZERO;
        Long totalRequired = data.getTotalRequiredQty();
        Long completedShortage = data.getCompletedShortageQty();
        if (totalRequired == null || totalRequired == 0) {
            return BigDecimal.ZERO;
        }
        if (completedShortage == null) {
            completedShortage = 0L;
        }
        return BigDecimal.valueOf(completedShortage)
                .multiply(BigDecimal.valueOf(100))
                .divide(BigDecimal.valueOf(totalRequired), 2, BigDecimal.ROUND_HALF_UP);
    }
}