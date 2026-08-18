package cn.iocoder.yudao.module.marketing.service.concreteplan;

import cn.hutool.core.util.IdUtil;
import cn.iocoder.yudao.module.marketing.dal.dataobject.concreteplan.ConcretePlanDO;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.DateUtil;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class ConcretePlanImportListener extends AnalysisEventListener<Map<Integer, String>> {

    private final List<ConcretePlanDO> dataList = new ArrayList<>();
    private final int BATCH_SIZE = 500;
    private final LocalDateTime importTime;
    private Map<Integer, String> headerMap;
    private Map<String, String> currentFieldMapping; // 当前 sheet 的字段映射
    private boolean headerParsed = false;
    private final String sheetName;
    private boolean stopParsing = false;
//    private int headerRowIndex = -1;  // 表头行号

    // ==================== 多 Sheet 字段映射配置 ====================
    // key: sheet名称, value: 表头中文名 -> DO字段名
    private static final Map<String, Map<String, String>> SHEET_FIELD_MAPPINGS = new HashMap<>();

    static {
        // 1. 泵车排产 Sheet
        Map<String, String> pumpCarMapping = new LinkedHashMap<>();
        pumpCarMapping.put("序号", "seqNo");
        pumpCarMapping.put("米段", "meter");
        pumpCarMapping.put("物料名称", "materialName");
        pumpCarMapping.put("物料编码", "materialCode");
        pumpCarMapping.put("生产编号", "prodNo");
        pumpCarMapping.put("对应底盘", "correspondingChassis"); // 注意DO中可能没有此字段，如无则忽略或添加
        pumpCarMapping.put("数量", "quantity");
        pumpCarMapping.put("结构件编号", "structSerialNo");
        pumpCarMapping.put("订单号", "orderNo");
        pumpCarMapping.put("出料批次", "batchNo");
        pumpCarMapping.put("支腿类型", "legType");
        pumpCarMapping.put("国内/海外", "countryType");
        pumpCarMapping.put("计划下达时间", "planIssueTime");
        pumpCarMapping.put("装配上线", "assemblyStartTime");
        pumpCarMapping.put("营运要求产出月份", "outputMonth");
        pumpCarMapping.put("特殊要求", "specialReq");
        pumpCarMapping.put("涂装要求", "paintingReq");
        pumpCarMapping.put("异常说明", "exceptionNote");
        pumpCarMapping.put("发货要求", "deliveryReq");
        // 如果有其他列需要映射，请补充
        SHEET_FIELD_MAPPINGS.put("泵车排产", pumpCarMapping);

        // 2. 布料机排产 Sheet
        Map<String, String> clothMachineMapping = new LinkedHashMap<>();
        clothMachineMapping.put("序号", "seqNo");
        clothMachineMapping.put("物料描述", "modelName");
        clothMachineMapping.put("物料名称", "materialName");
        clothMachineMapping.put("物料编码", "materialCode");
        clothMachineMapping.put("生产编号", "prodNo");
        clothMachineMapping.put("数量", "quantity");
        clothMachineMapping.put("订单号", "orderNo");
        clothMachineMapping.put("装配上线", "assemblyStartTime");
        clothMachineMapping.put("结构件编号", "structSerialNo");
        clothMachineMapping.put("备注", "specialReq");
        SHEET_FIELD_MAPPINGS.put("布料机排产", clothMachineMapping);

        // 3. 消防臂架 Sheet (几乎无数据，可空映射)
        Map<String, String> fireArmMapping = new LinkedHashMap<>();
        fireArmMapping.put("序号", "seqNo");
        fireArmMapping.put("物料描述", "modelName");
        fireArmMapping.put("物料名称", "materialName");
        fireArmMapping.put("物料编码", "materialCode");
        fireArmMapping.put("生产编号", "prodNo");
        fireArmMapping.put("数量", "quantity");
        fireArmMapping.put("订单号", "orderNo");
        fireArmMapping.put("装配上线", "assemblyStartTime");
        fireArmMapping.put("结构件编号", "structSerialNo");
        fireArmMapping.put("备注", "specialReq");
        SHEET_FIELD_MAPPINGS.put("消防臂架", fireArmMapping);

        // 4. 车载泵 Sheet
        Map<String, String> truckPumpMapping = new LinkedHashMap<>();
        truckPumpMapping.put("序号", "seqNo");
        truckPumpMapping.put("物料名称", "materialName");
        truckPumpMapping.put("物料编码", "materialCode");
        truckPumpMapping.put("生产编号", "prodNo");
        truckPumpMapping.put("订单号", "orderNo");
        truckPumpMapping.put("数量", "quantity");
        truckPumpMapping.put("组单情况", "groupStatus");
        truckPumpMapping.put("异常", "exceptionNote");
        truckPumpMapping.put("011号计划排产", "planIssueTime");
        truckPumpMapping.put("装配上线", "assemblyStartTime");
        truckPumpMapping.put("产出月份", "outputMonth");
        truckPumpMapping.put("排产时间", "scheduleTime");
        truckPumpMapping.put("涂装要求", "paintingReq");
        truckPumpMapping.put("备注", "specialReq");
        truckPumpMapping.put("发货要求", "deliveryReq");
        SHEET_FIELD_MAPPINGS.put("车载泵", truckPumpMapping);

        // 5. 砼泵 Sheet
        Map<String, String> concretePumpMapping = new LinkedHashMap<>();
        concretePumpMapping.put("序号", "seqNo");
        concretePumpMapping.put("物料描述", "materialName");
        concretePumpMapping.put("物料编码", "materialCode");
        concretePumpMapping.put("生产编号", "prodNo");
        concretePumpMapping.put("环保编码", "structSerialNo");
        concretePumpMapping.put("订单号", "orderNo");
        concretePumpMapping.put("组单情况", "groupStatus");
        concretePumpMapping.put("数量", "quantity");
        concretePumpMapping.put("011号计划排产", "planIssueTime");
        concretePumpMapping.put("装配上线", "assemblyStartTime");
        concretePumpMapping.put("产出月份", "outputMonth");
        concretePumpMapping.put("异常", "exceptionNote");
        concretePumpMapping.put("排产时间", "scheduleTime");
        concretePumpMapping.put("特殊说明", "specialReq");
        concretePumpMapping.put("涂装要求", "paintingReq");
        SHEET_FIELD_MAPPINGS.put("砼泵", concretePumpMapping);

        // 6. 湿喷机 Sheet
        Map<String, String> wetShotcreteMapping = new LinkedHashMap<>();
        wetShotcreteMapping.put("序号", "seqNo");
        wetShotcreteMapping.put("工厂", "factory");
        wetShotcreteMapping.put("产品类型", "modelName");
        wetShotcreteMapping.put("型号", "modelName");
        wetShotcreteMapping.put("物料编码", "materialCode");
        wetShotcreteMapping.put("生产编号", "prodNo");
        wetShotcreteMapping.put("数量", "quantity");
        wetShotcreteMapping.put("状态", "status");
        wetShotcreteMapping.put("客户", "customer");
        wetShotcreteMapping.put("环保编码", "structSerialNo");
        wetShotcreteMapping.put("订单号", "orderNo");
        wetShotcreteMapping.put("装配上线", "assemblyStartTime");
        wetShotcreteMapping.put("装配下线", "assemblyEndTime");
        wetShotcreteMapping.put("调试下线", "debugTime");
        wetShotcreteMapping.put("涂装下线", "paintingTime");
        wetShotcreteMapping.put("入库", "warehouseTime");
        wetShotcreteMapping.put("产出月份", "outputMonth");
        wetShotcreteMapping.put("涂装要求", "paintingReq");
        wetShotcreteMapping.put("修改后车号", "modifiedCarNo");
        SHEET_FIELD_MAPPINGS.put("湿喷机", wetShotcreteMapping);
    }

    public ConcretePlanImportListener(LocalDateTime importTime, String sheetName) {
        this.importTime = importTime;
        this.sheetName = sheetName;
    }


    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        this.headerMap = headMap;
        this.currentFieldMapping = SHEET_FIELD_MAPPINGS.getOrDefault(sheetName, new LinkedHashMap<>());
        log.info("解析到表头共 {} 列，Sheet: {}", headerMap.size(), sheetName);
        // 可选：打印表头调试信息
        // for (Map.Entry<Integer, String> entry : headerMap.entrySet()) {
        //     log.debug("列索引 {} -> [{}]", entry.getKey(), entry.getValue());
        // }
    }

    @Override
    public void invoke(Map<Integer, String> rowMap, AnalysisContext context) {
        if (stopParsing) return;
        if (headerMap == null) {
            log.warn("表头未初始化，跳过数据行");
            return;
        }

        // 跳过全空行
        boolean allEmpty = rowMap.values().stream()
                .allMatch(v -> v == null || v.trim().isEmpty());
        if (allEmpty) return;

        // 检测关键词行，遇到后停止解析该Sheet
        boolean hasStopKeyword = rowMap.values().stream()
                .filter(Objects::nonNull)
                .anyMatch(v -> v.contains("编制") || v.contains("审核") || v.contains("批准"));
        if (hasStopKeyword) {
            log.info("检测到关键词行，停止解析 Sheet: {}", sheetName);
            stopParsing = true;
            return;
        }

        ConcretePlanDO entity = new ConcretePlanDO();
        entity.setImportTime(importTime);
        entity.setPlate(sheetName);

        for (Map.Entry<Integer, String> entry : headerMap.entrySet()) {
            Integer colIndex = entry.getKey();
            String headerName = entry.getValue();
            if (headerName == null || headerName.trim().isEmpty()) continue;

            String trimmedHeader = headerName.trim();
            String fieldName = currentFieldMapping.get(trimmedHeader);
            if (fieldName == null) continue;

            String cellValue = rowMap.get(colIndex);
            if (cellValue == null || cellValue.trim().isEmpty()) continue;

            setFieldValue(entity, fieldName, cellValue.trim());
        }

        if (!hasBusinessData(entity)) {
            log.debug("跳过无业务数据的行");
            return;
        }

        dataList.add(entity);
        if (dataList.size() >= BATCH_SIZE) {
            // 批量保存由外部处理
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        log.info("Excel 解析完成，共 {} 条有效数据", dataList.size());
    }

    public List<ConcretePlanDO> getDataList() {
        return dataList;
    }

    /**
     * 判断实体是否包含有效业务数据
     * 根据 ConcretePlanDO 的关键业务字段检查（至少一个非空）
     */
    private boolean hasBusinessData(ConcretePlanDO entity) {
        if (entity.getPlanNo() == null) {
            if (entity.getSeqNo() != null) {
                entity.setPlanNo(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + "-" + entity.getSeqNo());
            } else {
                // 如果 seqNo 也为空，使用雪花ID的后8位作为临时计划编号
                entity.setPlanNo(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) + "-" + IdUtil.getSnowflakeNextIdStr().substring(0, 8));
            }
        }
        return entity.getAssemblyStartTime() != null   // 新增：装配上线时间作为有效业务数据
                || entity.getSeqNo() != null
                || entity.getMeter() != null
                || entity.getModelName() != null
                || entity.getMaterialName() != null
                || entity.getMaterialCode() != null
                || entity.getProdNo() != null
                || entity.getOrderNo() != null
                || entity.getBatchNo() != null
                || entity.getStructSerialNo() != null
                || entity.getQuantity() != null;
    }

    /**
     * 根据字段名设置 DO 的值（支持基本类型和日期转换）
     */
    private void setFieldValue(ConcretePlanDO entity, String fieldName, String value) {
        try {
            Field field = ConcretePlanDO.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            Class<?> fieldType = field.getType();

            if (fieldType == String.class) {
                field.set(entity, value);
            } else if (fieldType == BigDecimal.class) {
                field.set(entity, new BigDecimal(value));
            } else if (fieldType == Integer.class || fieldType == int.class) {
                field.set(entity, Integer.parseInt(value));
            } else if (fieldType == Long.class || fieldType == long.class) {
                field.set(entity, Long.parseLong(value));
            } else if (fieldType == LocalDateTime.class) {
                LocalDateTime dateTime = parseDateTime(value);
                if (dateTime != null) {
                    field.set(entity, dateTime);
                } else {
                    log.warn("日期格式转换失败，字段: {}，值: {}", fieldName, value);
                }
            }
            // 其他类型可继续扩展
        } catch (NoSuchFieldException e) {
            log.debug("字段 {} 在 ConcretePlanDO 中不存在，忽略", fieldName);
        } catch (IllegalAccessException e) {
            log.warn("字段赋值失败: {} -> {}, 错误: {}", fieldName, value, e.getMessage());
        } catch (NumberFormatException e) {
            log.warn("数字格式转换失败: {} -> {}", fieldName, value);
        }
    }

    /**
     * 解析日期时间，支持 Excel 序列数（如 "46315"）和各种日期字符串
     */
    private LocalDateTime parseDateTime(String val) {
        if (val == null || val.isEmpty()) return null;
        val = val.trim();

        // 0. 中文月日格式
        Pattern chineseDatePattern = Pattern.compile("(\\d{1,2})月(\\d{1,2})日");
        Matcher m = chineseDatePattern.matcher(val);
        if (m.matches()) {
            int month = Integer.parseInt(m.group(1));
            int day = Integer.parseInt(m.group(2));
            int year = LocalDate.now().getYear();
            try {
                LocalDate date = LocalDate.of(year, month, day);
                return date.atStartOfDay();
            } catch (DateTimeException e) {
                log.warn("月日组合无效: {}/{}", month, day);
            }
        }

        // 1. Excel序列数
        if (val.matches("\\d+(\\.0)?")) {
            try {
                double serial = Double.parseDouble(val);
                Date date = DateUtil.getJavaDate(serial);
                if (date != null) {
                    return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
                }
            } catch (NumberFormatException e) {
                log.warn("序列数解析失败: {}", val);
            }
        }
        // 2. 新增：尝试解析月/日格式（无年份），自动补全当前年份
        Pattern monthDayPattern = Pattern.compile("^(\\d{1,2})/(\\d{1,2})$");
        Matcher mdMatcher = monthDayPattern.matcher(val);
        if (mdMatcher.matches()) {
            int month = Integer.parseInt(mdMatcher.group(1));
            int day = Integer.parseInt(mdMatcher.group(2));
            int year = LocalDate.now().getYear();
            try {
                LocalDate date = LocalDate.of(year, month, day);
                return date.atStartOfDay();
            } catch (DateTimeException e) {
                log.warn("月日组合无效: {}/{}", month, day);
            }
        }

        // 3. 尝试解析带年份的日期（使用 LocalDate 解析）
        DateTimeFormatter[] dateFormatters = {
                DateTimeFormatter.ofPattern("yyyy-MM-dd"),
                DateTimeFormatter.ofPattern("yyyy/MM/dd"),
                DateTimeFormatter.ofPattern("yyyy/M/d"),
                DateTimeFormatter.ofPattern("yyyy/M/dd"),
                DateTimeFormatter.ofPattern("yyyy/MM/d"),
                DateTimeFormatter.ofPattern("M/d/yyyy"),
                DateTimeFormatter.ofPattern("yyyy-M-d")
        };
        for (DateTimeFormatter f : dateFormatters) {
            try {
                LocalDate date = LocalDate.parse(val, f);
                return date.atStartOfDay();
            } catch (Exception ignored) {}
        }

        // 4. 尝试解析带时间的格式
        DateTimeFormatter[] dateTimeFormatters = {
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),
                DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")
        };
        for (DateTimeFormatter f : dateTimeFormatters) {
            try {
                return LocalDateTime.parse(val, f);
            } catch (Exception ignored) {}
        }

        log.warn("日期解析失败: {}", val);
        return null;
    }
}