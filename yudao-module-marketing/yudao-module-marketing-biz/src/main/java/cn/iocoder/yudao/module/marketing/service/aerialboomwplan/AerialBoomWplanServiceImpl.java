package cn.iocoder.yudao.module.marketing.service.aerialboomwplan;

import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.module.marketing.dal.dataobject.aerialboomdplan.AerialBoomDplanDO;
import cn.iocoder.yudao.module.marketing.service.aerialboomdplan.AerialBoomDplanService;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;
import com.alibaba.excel.EasyExcel;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;   // <-- 这个必须有！
import java.time.format.DateTimeFormatterBuilder;
import java.util.*;
import lombok.Data;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.util.function.BiConsumer;


import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Locale;


import cn.iocoder.yudao.module.marketing.controller.admin.aerialboomwplan.vo.*;
import cn.iocoder.yudao.module.marketing.dal.dataobject.aerialboomwplan.AerialBoomWplanDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.marketing.dal.mysql.aerialboomwplan.AerialBoomWplanMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.marketing.enums.ErrorCodeConstants.*;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.DateUtil;



/**
 * 高机臂式周计划 Service 实现类
 *
 * @author 柳文
 */
@Service
@DS("oracle") // 指定使用 Oracle 数据源
@Validated
@Slf4j
public class AerialBoomWplanServiceImpl
        extends ServiceImpl<AerialBoomWplanMapper, AerialBoomWplanDO>
        implements AerialBoomWplanService {

    @Resource
    private AerialBoomWplanMapper aerialBoomWplanMapper;

    @Resource
    private AerialBoomDplanService aerialBoomDplanService;

    @Override
    public Long createAerialBoomWplan(AerialBoomWplanSaveReqVO createReqVO) {
        // 插入
        AerialBoomWplanDO aerialBoomWplan = BeanUtils.toBean(createReqVO, AerialBoomWplanDO.class);
        aerialBoomWplanMapper.insert(aerialBoomWplan);
        // 返回
        return aerialBoomWplan.getId();
    }

    @Override
    public void updateAerialBoomWplan(AerialBoomWplanSaveReqVO updateReqVO) {
        // 校验存在
        validateAerialBoomWplanExists(updateReqVO.getId());
        // 更新
        AerialBoomWplanDO updateObj = BeanUtils.toBean(updateReqVO, AerialBoomWplanDO.class);
        aerialBoomWplanMapper.updateById(updateObj);
    }

    @Override
    public void deleteAerialBoomWplan(Long id) {
        // 校验存在
        validateAerialBoomWplanExists(id);
        // 删除
        aerialBoomWplanMapper.deleteById(id);
    }

    private void validateAerialBoomWplanExists(Long id) {
        if (aerialBoomWplanMapper.selectById(id) == null) {
            throw exception(AERIAL_BOOM_WPLAN_NOT_EXISTS);
        }
    }

    @Override
    public AerialBoomWplanDO getAerialBoomWplan(Long id) {
        return aerialBoomWplanMapper.selectById(id);
    }

    @Override
    public PageResult<AerialBoomWplanDO> getAerialBoomWplanPage(AerialBoomWplanPageReqVO pageReqVO) {
        return aerialBoomWplanMapper.selectPage(pageReqVO);
    }

//    @Transactional(rollbackFor = Exception.class)
//    public void importExcel(MultipartFile file, LocalDate importTime) throws IOException {
//        log.info("开始导入周计划，文件：{}，导入时间：{}", file.getOriginalFilename(), importTime);
//
//        // 放宽 zip bomb 检测
//        ZipSecureFile.setMinInflateRatio(0.001);
//
//        // 读取所有行（不指定表头）
//        List<Map<Integer, String>> rows = EasyExcel.read(file.getInputStream())
//                .sheet(0)
//                .doReadSync();
//
//        if (rows.size() < 4) {
//            throw new ServiceException(400, "Excel 至少需要4行（颜色说明行、表头行、日期行、数据行）");
//        }
//
//        // 第一行（索引0）是颜色说明，忽略
//        // 第二行（索引1）是周计划标题（产品线、精准车型、产品型号、精准BOM + 周计划标题）
//        // 第三行（索引2）是日期数字、数量、车号
//        Map<Integer, String> headerRow1 = rows.get(0);  // 周计划标题（行0）
//        Map<Integer, String> headerRow2 = rows.get(1);  // 日期数字行（行1）
//
//        List<WeekColumnGroup> columnGroups = parseColumnGroups(headerRow1, headerRow2);
//        log.info("解析到 {} 个列组", columnGroups.size());
//
//        List<AerialBoomWplanDO> allData = new ArrayList<>();
//        final int BATCH_SIZE = 1;
//
//        for (int rowIdx = 0; rowIdx < Math.min(15, rows.size()); rowIdx++) {
//            Map<Integer, String> row = rows.get(rowIdx);
//            log.info("行{}: 前10列 = {},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{},{}",
//                    rowIdx,
//                    row.get(0), row.get(1), row.get(2), row.get(3), row.get(4),
//                    row.get(5), row.get(6), row.get(7), row.get(8), row.get(9), row.get(10), row.get(11), row.get(12), row.get(13), row.get(14),
//                    row.get(15), row.get(16), row.get(17), row.get(18), row.get(19)
//            );
//        }
//        // 数据从第4行开始（索引3）
//        for (int i = 2; i < rows.size(); i++) {
//            Map<Integer, String> row = rows.get(i);
//            String productLine = row.get(0);
//            if ("总合计".equals(productLine)) {
//                continue; // 跳过总合计行
//            }
//            String preciseModel = row.get(1);
//            String productModel = row.get(2);
//            String preciseBom = row.get(3);
//
//            // 跳过合计行（产品线为空）
//            if (isBlank(productLine) && isBlank(preciseModel) && isBlank(productModel) && isBlank(preciseBom)) {
//                continue;
//            }
//
//            for (WeekColumnGroup group : columnGroups) {
//                String carNumberRange = row.get(group.getCarNumberCol());
//                for (int d = 0; d < 7; d++) {
//                    String value = row.get(group.getDateStartCol() + d);
//                    if (value == null || value.trim().isEmpty()) continue;
//                    Integer dailyQty = parseInt(value);
//                    if (dailyQty == null || dailyQty <= 0) continue;
//                    LocalDate planDate = group.getDates().get(d);
//                    if (planDate == null) continue;
//
//                    AerialBoomWplanDO entity = AerialBoomWplanDO.builder()
//                            .productLine(productLine)
//                            .preciseModel(preciseModel)
//                            .productModel(productModel)
//                            .preciseBom(preciseBom)
//                            .planDate(planDate.atStartOfDay())
//                            .weekNo(group.getWeekNo())
//                            .weekStartDate(group.getWeekStartDate().atStartOfDay())
//                            .weekEndDate(group.getWeekEndDate().atStartOfDay())
//                            .dailyQuantity(dailyQty)
//                            .carNumberRange(carNumberRange)
//                            .productionLineType(null)
//                            .plate("高机")
//                            .importTime(importTime.atStartOfDay())
//                            .build();
//                    allData.add(entity);
//
//                    if (allData.size() >= BATCH_SIZE) {
//                        saveBatch(allData);
//                        allData.clear();
//                    }
//                }
//            }
//        }
//        if (!allData.isEmpty()) {
//            saveBatch(allData);
//        }
//        log.info("导入完成，共插入 {} 条记录", allData.size());
//    }
    @Transactional(rollbackFor = Exception.class)
    public void importExcel(MultipartFile file, LocalDate importTime) throws IOException {
        log.info("开始导入（周计划+日计划），文件：{}，导入时间：{}", file.getOriginalFilename(), importTime);
        ZipSecureFile.setMinInflateRatio(0.001);

        byte[] fileBytes = file.getBytes(); // 缓存文件内容

        // 1. 获取所有 Sheet 名称
        List<String> sheetNames = new ArrayList<>();
        try (InputStream is = new ByteArrayInputStream(fileBytes)) {
            ExcelReader excelReader = EasyExcel.read(is).build();
            for (ReadSheet sheet : excelReader.excelExecutor().sheetList()) {
                sheetNames.add(sheet.getSheetName());
            }
            excelReader.finish();
        }
        if (sheetNames.isEmpty()) {
            throw new ServiceException(400, "Excel 文件没有找到任何 Sheet");
        }

        // 2. 处理第一个 Sheet：周计划
        List<AerialBoomWplanDO> wplanData = new ArrayList<>();
        try (InputStream is = new ByteArrayInputStream(fileBytes)) {
            List<Map<Integer, String>> rows = EasyExcel.read(is)
                    .sheet(sheetNames.get(0))
                    .doReadSync();
            if (rows.size() < 4) {
                throw new ServiceException(400, "周计划 Sheet 至少需要4行");
            }
            Map<Integer, String> headerRow1 = rows.get(0);
            Map<Integer, String> headerRow2 = rows.get(1);
            List<WeekColumnGroup> columnGroups = parseColumnGroups(headerRow1, headerRow2);

            for (int i = 2; i < rows.size(); i++) {
                Map<Integer, String> row = rows.get(i);
                String productLine = row.get(0);
                if ("总合计".equals(productLine)) continue;
                String preciseModel = row.get(1);
                String productModel = row.get(2);
                String preciseBom = row.get(3);
                if (isBlank(productLine) && isBlank(preciseModel) && isBlank(productModel) && isBlank(preciseBom))
                    continue;

                for (WeekColumnGroup group : columnGroups) {
                    String carNumberRange = row.get(group.getCarNumberCol());
                    for (int d = 0; d < 7; d++) {
                        String val = row.get(group.getDateStartCol() + d);
                        if (val == null || val.trim().isEmpty()) continue;
                        Integer dailyQty = parseInt(val);
                        if (dailyQty == null || dailyQty <= 0) continue;
                        LocalDate planDate = group.getDates().get(d);
                        if (planDate == null) continue;

                        AerialBoomWplanDO entity = AerialBoomWplanDO.builder()
                                .productLine(productLine)
                                .preciseModel(preciseModel)
                                .productModel(productModel)
                                .preciseBom(preciseBom)
                                .planDate(planDate.atStartOfDay())
                                .weekNo(group.getWeekNo())
                                .weekStartDate(group.getWeekStartDate().atStartOfDay())
                                .weekEndDate(group.getWeekEndDate().atStartOfDay())
                                .dailyQuantity(dailyQty)
                                .carNumberRange(carNumberRange)
                                .productionLineType(null)
                                .plate("高机臂式")
                                .importTime(importTime.atStartOfDay())
                                .build();
                        wplanData.add(entity);
                    }
                }
            }
        }

        // 保存周计划
        if (!wplanData.isEmpty()) {
            this.saveBatch(wplanData, 500);
            log.info("周计划数据已保存，共 {} 条", wplanData.size());
        }

        // 处理第二个 Sheet：日计划（如果存在）
        if (sheetNames.size() > 1) {
            log.info("开始处理日计划 Sheet: {}", sheetNames.get(1));
            try (InputStream is = new ByteArrayInputStream(fileBytes)) {
                // 使用 POI 读取 Excel，完全控制单元格字符串
                Workbook workbook = WorkbookFactory.create(is);
                Sheet sheet = workbook.getSheet(sheetNames.get(1));
                if (sheet == null) {
                    throw new ServiceException(400, "找不到日计划 Sheet: " + sheetNames.get(1));
                }
                List<Map<Integer, String>> rawRows = new ArrayList<>();
                int lastRowNum = sheet.getLastRowNum();
                DataFormatter dataFormatter = new DataFormatter();  // 创建一次，重复使用
                for (int rowIdx = 0; rowIdx <= lastRowNum; rowIdx++) {
                    Row row = sheet.getRow(rowIdx);
                    if (row == null) {
                        rawRows.add(new HashMap<>());
                        continue;
                    }
                    Map<Integer, String> rowMap = new HashMap<>();
                    short lastCellNum = row.getLastCellNum();
                    for (int colIdx = 0; colIdx < lastCellNum; colIdx++) {
                        Cell cell = row.getCell(colIdx);
                        String value = null;
                        if (cell != null) {
                            // 使用 DataFormatter 安全获取字符串，无需关心单元格类型
                            value = dataFormatter.formatCellValue(cell).trim();
                            if (value.isEmpty()) {
                                value = null;
                            }
                        }
                        rowMap.put(colIdx, value);
                    }
                    rawRows.add(rowMap);
                }
                workbook.close();

                log.info("日计划 Sheet 总行数: {}", rawRows.size());
                // 打印前20行内容（调试）
                for (int i = 0; i < Math.min(rawRows.size(), 20); i++) {
                    log.info("第 {} 行: {}", i, rawRows.get(i));
                }

                if (rawRows.size() < 2) {
                    throw new ServiceException(400, "日计划 Sheet 至少需要2行（表头+数据）");
                }

                // 动态寻找包含“线别”的表头行
                int headerRowIndex = -1;
                for (int i = 0; i < Math.min(rawRows.size(), 5); i++) {
                    Map<Integer, String> row = rawRows.get(i);
                    for (String value : row.values()) {
                        if (value != null && value.contains("线别")) {
                            headerRowIndex = i;
                            break;
                        }
                    }
                    if (headerRowIndex != -1) break;
                }
                if (headerRowIndex == -1) {
                    throw new ServiceException(400, "日计划 Sheet 未找到表头行（需包含“线别”列）");
                }
                log.info("日计划表头行索引: {}", headerRowIndex);

                // 构建列名到索引的映射
                Map<Integer, String> colIndexToField = new HashMap<>();
                Map<Integer, String> headerRow = rawRows.get(headerRowIndex);
                for (Map.Entry<Integer, String> entry : headerRow.entrySet()) {
                    String val = entry.getValue() == null ? null : entry.getValue().trim();
                    if (val != null && !val.isEmpty()) {
                        colIndexToField.put(entry.getKey(), val);
                    }
                }

                // 数据从表头的下一行开始
                List<AerialBoomDplanDO> dplanData = new ArrayList<>();
                for (int i = headerRowIndex + 1; i < rawRows.size(); i++) {
                    Map<Integer, String> row = rawRows.get(i);
                    // 跳过全空行
                    boolean allEmpty = true;
                    for (String v : row.values()) {
                        if (v != null && !v.trim().isEmpty()) {
                            allEmpty = false;
                            break;
                        }
                    }
                    if (allEmpty) continue;

                    AerialBoomDplanDO entity = buildDplanFromRow(row, colIndexToField, importTime);
                    if (entity != null) {
                        dplanData.add(entity);
                        if (dplanData.size() >= 500) {
                            for (AerialBoomDplanDO e : dplanData) {
                                aerialBoomDplanService.insertAerialBoomDplan(e);
                            }
                            dplanData.clear();
                        }
                    } else {
                        log.warn("日计划第{}行数据构建失败，原始内容: {}", i, row);
                    }
                }
                if (!dplanData.isEmpty()) {
                    for (AerialBoomDplanDO entity : dplanData) {
                        aerialBoomDplanService.insertAerialBoomDplan(entity);
                    }
                }
                log.info("日计划数据已保存，共 {} 条", dplanData.size());
            } catch (Exception e) {
                log.error("处理日计划 Sheet 异常", e);
                throw new ServiceException(400, "日计划 Sheet 解析失败：" + e.getMessage());
            }
        }
    }

    // 根据映射表和行数据构建 AerialBoomDplanDO
    private AerialBoomDplanDO buildDplanFromRow(Map<Integer, String> row,
                                                Map<Integer, String> colIndexToField,
                                                LocalDate importTime) {
        // 基本空值防护
        if (row == null || colIndexToField == null) {
            log.warn("buildDplanFromRow: row 或 colIndexToField 为 null");
            return null;
        }

        Map<String, String> fieldMapping = new LinkedHashMap<>();
        fieldMapping.put("线别", "lineType");
        fieldMapping.put("精准车型", "preciseModel");
        fieldMapping.put("产品型号", "productModel");
        fieldMapping.put("ZPS型号", "zpsModel");
        fieldMapping.put("精准BOM", "preciseBom");
        fieldMapping.put("车号", "carNo");
        fieldMapping.put("整车订单号", "orderNo");
        fieldMapping.put("说明", "remark");
        fieldMapping.put("内外贸版本", "tradeVersion");
        fieldMapping.put("台份", "unitCount");
        fieldMapping.put("上线计划", "onlinePlan");
        fieldMapping.put("整车成台计划", "completePlan");
        fieldMapping.put("报缴月/周/日", "reportDate");
        fieldMapping.put("国家", "country");
        fieldMapping.put("合同号", "contractNo");
        fieldMapping.put("营销通知时间", "marketingNoticeTime");
        fieldMapping.put("订单开立时间", "orderCreateTime");
        fieldMapping.put("板块", "plate");   // 如果模板没有板块列，可忽略

        AerialBoomDplanDO entity = new AerialBoomDplanDO();
        entity.setImportTime(importTime.atStartOfDay());
        boolean hasData = false;

        try {
            for (Map.Entry<Integer, String> colEntry : colIndexToField.entrySet()) {
                Integer colIdx = colEntry.getKey();
                String headerName = colEntry.getValue();
                if (headerName == null) continue;   // 理论上不会，但防护

                String fieldKey = fieldMapping.get(headerName.trim());
                if (fieldKey == null) continue;

                String cellValue = row.get(colIdx);
                if (cellValue == null || cellValue.trim().isEmpty()) continue;
                cellValue = cellValue.trim();
                hasData = true;

                // 用单独的 try 处理每个字段的转换
                try {
                    switch (fieldKey) {
                        case "lineType": entity.setLineType(cellValue); break;
                        case "preciseModel": entity.setPreciseModel(cellValue); break;
                        case "productModel": entity.setProductModel(cellValue); break;
                        case "zpsModel": entity.setZpsModel(cellValue); break;
                        case "preciseBom": entity.setPreciseBom(cellValue); break;
                        case "carNo": entity.setCarNo(cellValue); break;
                        case "orderNo": entity.setOrderNo(cellValue); break;
                        case "remark": entity.setRemark(cellValue); break;
                        case "tradeVersion": entity.setTradeVersion(cellValue); break;
                        case "unitCount":
                            try {
                                entity.setUnitCount(Integer.parseInt(cellValue));
                            } catch (NumberFormatException e) {
                                log.warn("台份值[{}]不是有效数字，已忽略", cellValue);
                            }
                            break;
                        case "onlinePlan": entity.setOnlinePlan(parseDateTime(cellValue)); break;
                        case "completePlan": entity.setCompletePlan(parseDateTime(cellValue)); break;
                        case "reportDate": entity.setReportDate(parseDateTime(cellValue)); break;
                        case "country": entity.setCountry(cellValue); break;
                        case "contractNo": entity.setContractNo(cellValue); break;
                        case "marketingNoticeTime": entity.setMarketingNoticeTime(cellValue); break;
                        case "orderCreateTime": entity.setOrderCreateTime(parseDateTime(cellValue)); break;
                        case "plate": entity.setPlate(cellValue); break;
                    }
                } catch (Exception e) {
                    log.warn("字段[{}]处理异常，值=[{}]，错误：{}", headerName, cellValue, e.getMessage());
                }
            }
        } catch (Exception e) {
            log.error("buildDplanFromRow 整体异常，行数据：{}", row, e);
            return null;
        }
        // 确保 plate 有值（臂式日计划默认为“高机臂式”）
        entity.setPlate("高机臂式");

        return hasData ? entity : null;
    }

    // 日期解析
    private LocalDateTime parseDateTime(String val) {
        if (val == null || (val = val.trim()).isEmpty()) return null;
        if ("\\".equals(val) || "#N/A".equalsIgnoreCase(val)) return null;

        // 针对 M/d 格式，使用宽松解析并设置当前年份为默认值
        DateTimeFormatter mdFormatter = new DateTimeFormatterBuilder()
                .appendPattern("M/d")
                .parseDefaulting(ChronoField.YEAR, LocalDate.now().getYear())
                .toFormatter(Locale.ENGLISH);

        DateTimeFormatter[] formatters = {
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),
                DateTimeFormatter.ofPattern("yyyy-MM-dd"),
                DateTimeFormatter.ofPattern("M/d/yyyy"),
                DateTimeFormatter.ofPattern("M/d/yy"),
                mdFormatter,                              // M/d with default year
                DateTimeFormatter.ofPattern("yyyy/M/d")
        };

        for (DateTimeFormatter fmt : formatters) {
            try {
                TemporalAccessor temporal = fmt.parse(val);
                if (temporal instanceof LocalDateTime) {
                    return (LocalDateTime) temporal;
                } else {
                    LocalDate date = LocalDate.from(temporal);
                    return date.atStartOfDay();
                }
            } catch (Exception ignored) {
                // 尝试下一个格式
            }
        }

        // 手动处理形如 5/7 的字符串（兜底）
        if (val.contains("/") && val.chars().filter(ch -> ch == '/').count() == 1) {
            String[] parts = val.split("/");
            if (parts.length == 2) {
                try {
                    int month = Integer.parseInt(parts[0].trim());
                    int day = Integer.parseInt(parts[1].trim());
                    return LocalDate.of(LocalDate.now().getYear(), month, day).atStartOfDay();
                } catch (Exception e) { /* ignore */ }
            }
        }

        log.warn("无法解析日期：{}", val);
        return null;
    }

    /**
     * 解析列组
     */
    private List<WeekColumnGroup> parseColumnGroups(Map<Integer, String> headerRow1, Map<Integer, String> headerRow2) {
        log.info("=== 表头第一行（索引1）内容 ===");
        for (int col = 0; col < Math.min(20, headerRow1.size()); col++) {
            log.info("列{}: {}", col, headerRow1.get(col));
        }
        List<WeekColumnGroup> groups = new ArrayList<>();
        int maxCol = headerRow1.size();
        // 正则：允许标题和括号之间有空白或换行符
        Pattern weekPattern = Pattern.compile("(\\d+)W周计划\\s*[（(](\\d+)\\.(\\d+)-(\\d+)\\.(\\d+)[）)]");

        // 打印前几列表头内容，用于调试
        log.info("=== 表头第一行（索引1）内容 ===");
        for (int col = 0; col < Math.min(20, maxCol); col++) {
            log.info("列{}: {}", col, headerRow1.get(col));
        }

        int i = 4; // 前4列是产品信息，从第5列开始（索引4）
        while (i < maxCol) {
            String cell = headerRow1.get(i);
            if (cell == null || cell.trim().isEmpty()) {
                i++;
                continue;
            }
            // 去除可能的换行符和空白
            String cleanCell = cell.replaceAll("\\s+", "");
            Matcher matcher = weekPattern.matcher(cleanCell);
            if (matcher.find()) {
                int weekNum = Integer.parseInt(matcher.group(1));
                int startMonth = Integer.parseInt(matcher.group(2));
                int startDay = Integer.parseInt(matcher.group(3));
                int endMonth = Integer.parseInt(matcher.group(4));
                int endDay = Integer.parseInt(matcher.group(5));
                // 年份推断（使用当前年份，如果跨年则调整）
                int year = LocalDate.now().getYear();
                // 如果起始月份小于当前月份，则可能为下一年（例如12W在3月，当前4月，不调整；但若当前是1月，12W是去年？可简单判断）
                // 更精确：根据周计划中的月份与当前月份比较，如果起始月份 < 当前月份且起始月份小于等于3，可能是次年，但这里简化
                // 直接使用当前年份，因为你的模板年份为2025，当前为2026，需要根据实际调整。建议从表头解析年份或手动配置。
                // 此处简单处理：如果起始月份大于当前月份，年份不变；否则年份+1（假设计划都是未来）
                if (startMonth < LocalDate.now().getMonthValue()) {
                    year++;
                }
                LocalDate weekStart = LocalDate.of(year, startMonth, startDay);
                LocalDate weekEnd = LocalDate.of(year, endMonth, endDay);

                int dateStartCol = i;
                int quantityCol = i + 7;
                int carNumberCol = i + 8;

                if (quantityCol >= maxCol || carNumberCol >= maxCol) {
                    log.warn("周计划组 {} 列数不足，跳过", cell);
                    i++;
                    continue;
                }

                // 构建该周7天的日期列表（直接从第二行获取具体日期，或根据起始日期计算）
                List<LocalDate> dates = new ArrayList<>();
                for (int d = 0; d < 7; d++) {
                    // 优先使用第二行提供的日期数字（如果有），否则用计算值
                    String dateNumStr = headerRow2.get(dateStartCol + d);
                    LocalDate date = null;
                    if (dateNumStr != null && !dateNumStr.trim().isEmpty()) {
                        try {
                            int dayNum = Integer.parseInt(dateNumStr.trim());
                            // 根据周起始日期和偏移计算，确保月份正确
                            date = weekStart.plusDays(d);
                            // 可选：验证 dayNum 是否匹配，不匹配则以计算为准
                        } catch (NumberFormatException e) {
                            date = weekStart.plusDays(d);
                        }
                    } else {
                        date = weekStart.plusDays(d);
                    }
                    dates.add(date);
                }

                WeekColumnGroup group = new WeekColumnGroup();
                group.setWeekNo(weekNum + "W");
                group.setWeekStartDate(weekStart);
                group.setWeekEndDate(weekEnd);
                group.setDateStartCol(dateStartCol);
                group.setQuantityCol(quantityCol);
                group.setCarNumberCol(carNumberCol);
                group.setDates(dates);
                groups.add(group);

                i += 9;
            } else {
                log.warn("未匹配周计划标题，列{} 内容: {}", i, cell);
                i++;
            }
        }
        return groups;
    }



    /**
     * 辅助方法：判断字符串是否为空
     */
    private boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
    }

    /**
     * 辅助方法：安全转换为整数
     */
    private Integer parseInt(String str) {
        if (str == null) return null;
        try {
            return Integer.parseInt(str.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * 周计划列组信息
     */
    @Data
    private static class WeekColumnGroup {
        private String weekNo;              // 周次，如 "12W"
        private LocalDate weekStartDate;    // 周起始日期
        private LocalDate weekEndDate;      // 周结束日期
        private int dateStartCol;           // 第一个日期列的索引
        private int quantityCol;            // 数量列索引（通常不需要）
        private int carNumberCol;           // 车号列索引
        private List<LocalDate> dates;      // 该周7天对应的具体日期

        // getter / setter 方法（省略，请使用 Lombok 或手动生成）
        // 推荐使用 @Data，但内部类不能用 Lombok 外部访问？可以加上 @lombok.Data
    }


    //高机剪叉
//    @Transactional(rollbackFor = Exception.class)
//    public void importGaojiExcel(MultipartFile file, LocalDate importTime) throws IOException {
//        log.info("开始导入高机剪叉（周计划+日计划），文件：{}，导入时间：{}", file.getOriginalFilename(), importTime);
//        ZipSecureFile.setMinInflateRatio(0.001);
//
//        byte[] fileBytes = file.getBytes();
//
//        // 获取所有 Sheet 名称
//        List<String> sheetNames = new ArrayList<>();
//        try (InputStream is = new ByteArrayInputStream(fileBytes)) {
//            ExcelReader excelReader = EasyExcel.read(is).build();
//            for (ReadSheet sheet : excelReader.excelExecutor().sheetList()) {
//                sheetNames.add(sheet.getSheetName());
//            }
//            excelReader.finish();
//        }
//        if (sheetNames.isEmpty()) {
//            throw new ServiceException(400, "Excel 文件没有找到任何 Sheet");
//        }
//
//        // 2. 处理第一个 Sheet：周计划（高机剪叉）
//        List<AerialBoomWplanDO> wplanData = new ArrayList<>();
//        try (InputStream is = new ByteArrayInputStream(fileBytes)) {
//            List<Map<Integer, String>> rows = EasyExcel.read(is)
//                    .sheet(sheetNames.get(0))
//                    .doReadSync();
//            if (rows.size() < 4) {
//                throw new ServiceException(400, "周计划 Sheet 至少需要4行");
//            }
//            // 动态查找表头行：包含“产品线”的行
//            int headerRowIndex = findHeaderRow(rows, "产品线");
//            if (headerRowIndex == -1) {
//                throw new ServiceException(400, "周计划 Sheet 未找到表头行（需包含“产品线”列）");
//            }
//            Map<Integer, String> headerRow = rows.get(headerRowIndex);
//            // 解析列组（周计划动态列）
//            List<WeekColumnGroup> columnGroups = parseGaojiWeekColumns(headerRow, rows.get(headerRowIndex + 1));
//            // 数据从表头下一行开始
//            for (int i = headerRowIndex + 1; i < rows.size(); i++) {
//                Map<Integer, String> row = rows.get(i);
//                String productLine = row.get(0);               // A列：产品线
//                String preciseModel = row.get(1);              // B列：计划车型 -> 精准车型
//                String preciseBom = row.get(2);                // C列：精准车型 -> 精准BOM
//                if (isBlank(productLine) && isBlank(preciseModel) && isBlank(preciseBom)) {
//                    continue;
//                }
//                for (WeekColumnGroup group : columnGroups) {
//                    String carNumberRange = row.get(group.getCarNumberCol());
//                    for (int d = 0; d < 7; d++) {
//                        String val = row.get(group.getDateStartCol() + d);
//                        if (val == null || val.trim().isEmpty()) continue;
//                        Integer dailyQty = parseInt(val);
//                        if (dailyQty == null || dailyQty <= 0) continue;
//                        LocalDate planDate = group.getDates().get(d);
//                        if (planDate == null) continue;
//
//                        AerialBoomWplanDO entity = AerialBoomWplanDO.builder()
//                                .productLine(productLine)
//                                .preciseModel(preciseModel)
//                                .productModel(null)            // 周计划无产品型号
//                                .preciseBom(preciseBom)
//                                .planDate(planDate.atStartOfDay())
//                                .weekNo(group.getWeekNo())
//                                .weekStartDate(group.getWeekStartDate().atStartOfDay())
//                                .weekEndDate(group.getWeekEndDate().atStartOfDay())
//                                .dailyQuantity(dailyQty)
//                                .carNumberRange(carNumberRange)
//                                .productionLineType(null)
//                                .plate("高机剪叉")
//                                .importTime(importTime.atStartOfDay())
//                                .build();
//                        wplanData.add(entity);
//                    }
//                }
//            }
//        }
//
//        if (!wplanData.isEmpty()) {
//            this.saveBatch(wplanData, 500);
//            log.info("高机剪叉周计划数据已保存，共 {} 条", wplanData.size());
//        }
//
//        // 3. 处理日计划 Sheet（通常索引为1）
//        if (sheetNames.size() > 1) {
//            try (InputStream is = new ByteArrayInputStream(fileBytes)) {
//                List<Map<Integer, String>> rows = EasyExcel.read(is)
//                        .sheet(sheetNames.get(1))
//                        .doReadSync();
//                if (rows.size() < 2) {
//                    throw new ServiceException(400, "日计划 Sheet 至少需要2行");
//                }
//                // 动态查找表头行：包含“产品型号”的行
//                int headerRowIndex = findHeaderRow(rows, "产品型号");
//                if (headerRowIndex == -1) {
//                    throw new ServiceException(400, "日计划 Sheet 未找到表头行（需包含“产品型号”列）");
//                }
//                Map<Integer, String> headerMap = rows.get(headerRowIndex);
//                // 构建列名到索引的映射
//                Map<String, Integer> colNameToIdx = new HashMap<>();
//                for (Map.Entry<Integer, String> entry : headerMap.entrySet()) {
//                    if (entry.getValue() != null && !entry.getValue().trim().isEmpty()) {
//                        colNameToIdx.put(entry.getValue().trim(), entry.getKey());
//                    }
//                }
//
//                List<AerialBoomDplanDO> dplanData = new ArrayList<>();
//                for (int i = headerRowIndex + 1; i < rows.size(); i++) {
//                    Map<Integer, String> row = rows.get(i);
//                    // 跳过全空行
//                    if (row.values().stream().allMatch(v -> v == null || v.trim().isEmpty())) continue;
//
//                    AerialBoomDplanDO entity = buildGaojiDplanFromRow(row, colNameToIdx, importTime);
//                    if (entity != null) {
//                        dplanData.add(entity);
//                        if (dplanData.size() >= 500) {
//                            for (AerialBoomDplanDO e : dplanData) {
//                                aerialBoomDplanService.insertAerialBoomDplan(e);
//                            }
//                            dplanData.clear();
//                        }
//                    }
//                }
//                if (!dplanData.isEmpty()) {
//                    for (AerialBoomDplanDO entity : dplanData) {
//                        aerialBoomDplanService.insertAerialBoomDplan(entity);
//                    }
//                }
//                log.info("高机剪叉日计划数据已保存，共 {} 条", dplanData.size());
//            }
//        }
//    }

    //查找表头行
    private int findHeaderRow(List<Map<Integer, String>> rows, String keyword) {
        for (int i = 0; i < Math.min(rows.size(), 10); i++) {
            Map<Integer, String> row = rows.get(i);
            if (row.values().stream().anyMatch(v -> v != null && v.contains(keyword))) {
                return i;
            }
        }
        return -1;
    }
    // 解析高机剪叉周计划的动态列组
//    private List<WeekColumnGroup> parseGaojiWeekColumns(Map<Integer, String> headerRow1, Map<Integer, String> headerRow2) {
//        List<WeekColumnGroup> groups = new ArrayList<>();
//        int maxCol = headerRow1.size();
//        Pattern weekPattern = Pattern.compile("(\\d+)W周计划\\s*[（(](\\d+)\\.(\\d+)-(\\d+)\\.(\\d+)[）)]");
//
//        int i = 3; // 前3列为产品信息，从第4列（索引3）开始
//        while (i < maxCol) {
//            String cell = headerRow1.get(i);
//            if (cell == null || cell.trim().isEmpty()) {
//                i++;
//                continue;
//            }
//            String cleanCell = cell.replaceAll("\\s+", "");
//            Matcher matcher = weekPattern.matcher(cleanCell);
//            if (matcher.find()) {
//                int weekNum = Integer.parseInt(matcher.group(1));
//                int startMonth = Integer.parseInt(matcher.group(2));
//                int startDay = Integer.parseInt(matcher.group(3));
//                int endMonth = Integer.parseInt(matcher.group(4));
//                int endDay = Integer.parseInt(matcher.group(5));
//                int year = LocalDate.now().getYear();
//                if (startMonth < LocalDate.now().getMonthValue()) {
//                    year++;
//                }
//                LocalDate weekStart = LocalDate.of(year, startMonth, startDay);
//                LocalDate weekEnd = LocalDate.of(year, endMonth, endDay);
//
//                int dateStartCol = i;
//                int quantityCol = i + 7;
//                int carNumberCol = i + 8;
//
//                if (quantityCol >= maxCol || carNumberCol >= maxCol) {
//                    log.warn("周计划组 {} 列数不足，跳过", cell);
//                    i++;
//                    continue;
//                }
//
//                List<LocalDate> dates = new ArrayList<>();
//                for (int d = 0; d < 7; d++) {
//                    // 优先使用第二行的日期数字（可选）
//                    String dateNumStr = headerRow2.get(dateStartCol + d);
//                    LocalDate date = weekStart.plusDays(d);
//                    if (dateNumStr != null && !dateNumStr.trim().isEmpty()) {
//                        try {
//                            int dayNum = Integer.parseInt(dateNumStr.trim());
//                            // 验证 dayNum 是否匹配，不匹配则以计算为准
//                        } catch (NumberFormatException e) { /* ignore */ }
//                    }
//                    dates.add(date);
//                }
//
//                WeekColumnGroup group = new WeekColumnGroup();
//                group.setWeekNo(weekNum + "W");
//                group.setWeekStartDate(weekStart);
//                group.setWeekEndDate(weekEnd);
//                group.setDateStartCol(dateStartCol);
//                group.setQuantityCol(quantityCol);
//                group.setCarNumberCol(carNumberCol);
//                group.setDates(dates);
//                groups.add(group);
//
//                i += 9;
//            } else {
//                i++;
//            }
//        }
//        return groups;
//    }
//    //构建高机剪叉日计划实体
//    private AerialBoomDplanDO buildGaojiDplanFromRow(Map<Integer, String> row,
//                                                     Map<String, Integer> colNameToIdx,
//                                                     LocalDate importTime) {
//        AerialBoomDplanDO entity = new AerialBoomDplanDO();
//        entity.setImportTime(importTime.atStartOfDay());
//        boolean hasData = false;
//
//        // 字段映射表（Excel列名 -> DO属性设置器）
//        Map<String, BiConsumer<AerialBoomDplanDO, String>> setters = new LinkedHashMap<>();
//        setters.put("产品型号", (e, v) -> e.setProductModel(v));
//        setters.put("精准车型", (e, v) -> e.setPreciseModel(v));
//        setters.put("BOM", (e, v) -> e.setPreciseBom(v));
//        setters.put("ZPS   BOM", (e, v) -> e.setZpsModel(v));
//        setters.put("年度顺序号/车号", (e, v) -> e.setCarNo(v));
//        setters.put("订单", (e, v) -> e.setOrderNo(v));
//        setters.put("备注", (e, v) -> e.setRemark(v));
//        setters.put("内外贸版本", (e, v) -> e.setTradeVersion(v));
//        setters.put("台份", (e, v) -> { try { e.setUnitCount(Integer.parseInt(v)); } catch(Exception ignored) {} });
//        setters.put("上线计划", (e, v) -> e.setOnlinePlan(parseDateTime(v)));
//        setters.put("整车成台计划", (e, v) -> e.setCompletePlan(parseDateTime(v)));
//        setters.put("报缴月/周/日", (e, v) -> e.setReportDate(parseDateTime(v)));
//        setters.put("发货时间", (e, v) -> e.setDeliveryTime(v));          // 新增字段
//        setters.put("营销通知号", (e, v) -> e.setContractNo(v));
//        setters.put("国家", (e, v) -> e.setCountry(v));
//
//        for (Map.Entry<String, BiConsumer<AerialBoomDplanDO, String>> entry : setters.entrySet()) {
//            Integer colIdx = colNameToIdx.get(entry.getKey());
//            if (colIdx != null) {
//                String value = row.get(colIdx);
//                if (value != null && !value.trim().isEmpty()) {
//                    entry.getValue().accept(entity, value.trim());
//                    hasData = true;
//                }
//            }
//        }
//
//        return hasData ? entity : null;
//    }
}