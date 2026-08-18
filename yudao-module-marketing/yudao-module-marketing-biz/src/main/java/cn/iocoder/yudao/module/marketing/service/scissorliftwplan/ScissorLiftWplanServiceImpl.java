package cn.iocoder.yudao.module.marketing.service.scissorliftwplan;

import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.marketing.controller.admin.scissorliftdplan.vo.ScissorLiftDplanSaveReqVO;
import cn.iocoder.yudao.module.marketing.controller.admin.scissorliftwplan.vo.ScissorLiftWplanPageReqVO;
import cn.iocoder.yudao.module.marketing.controller.admin.scissorliftwplan.vo.ScissorLiftWplanSaveReqVO;
import cn.iocoder.yudao.module.marketing.dal.dataobject.scissorliftdplan.ScissorLiftDplanDO;
import cn.iocoder.yudao.module.marketing.dal.dataobject.scissorliftwplan.ScissorLiftWplanDO;
import cn.iocoder.yudao.module.marketing.dal.mysql.scissorliftwplan.ScissorLiftWplanMapper;
import cn.iocoder.yudao.module.marketing.service.scissorliftdplan.ScissorLiftDplanService;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.baomidou.dynamic.datasource.annotation.DS;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.marketing.enums.ErrorCodeConstants.SCISSOR_LIFT_WPLAN_NOT_EXISTS;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;

@Service
@DS("oracle")
@Validated
@Slf4j
public class ScissorLiftWplanServiceImpl implements ScissorLiftWplanService {

    @Resource
    private ScissorLiftWplanMapper scissorLiftWplanMapper;

    @Resource
    private ScissorLiftDplanService scissorLiftDplanService;   // 注入日计划服务

    // ========== 原有 CRUD 方法（保持不变） ==========
    @Override
    public Long createScissorLiftWplan(ScissorLiftWplanSaveReqVO createReqVO) {
        ScissorLiftWplanDO entity = BeanUtils.toBean(createReqVO, ScissorLiftWplanDO.class);
        scissorLiftWplanMapper.insert(entity);
        return entity.getId();
    }

    @Override
    public void updateScissorLiftWplan(ScissorLiftWplanSaveReqVO updateReqVO) {
        validateScissorLiftWplanExists(updateReqVO.getId());
        ScissorLiftWplanDO updateObj = BeanUtils.toBean(updateReqVO, ScissorLiftWplanDO.class);
        scissorLiftWplanMapper.updateById(updateObj);
    }

    @Override
    public void deleteScissorLiftWplan(Long id) {
        validateScissorLiftWplanExists(id);
        scissorLiftWplanMapper.deleteById(id);
    }

    private void validateScissorLiftWplanExists(Long id) {
        if (scissorLiftWplanMapper.selectById(id) == null) {
            throw exception(SCISSOR_LIFT_WPLAN_NOT_EXISTS);
        }
    }

    @Override
    public ScissorLiftWplanDO getScissorLiftWplan(Long id) {
        return scissorLiftWplanMapper.selectById(id);
    }

    @Override
    public PageResult<ScissorLiftWplanDO> getScissorLiftWplanPage(ScissorLiftWplanPageReqVO pageReqVO) {
        return scissorLiftWplanMapper.selectPage(pageReqVO);
    }

    // ========== 导入功能 ==========

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void importExcel(MultipartFile file, LocalDate importTime) throws IOException {
        log.info("开始导入剪叉周计划，文件：{}，导入时间：{}", file.getOriginalFilename(), importTime);
        ZipSecureFile.setMinInflateRatio(0.001);
        byte[] fileBytes = file.getBytes();

        // 1. 获取所有 Sheet 名称
        List<String> sheetNames = getSheetNames(fileBytes);
        if (sheetNames.isEmpty()) {
            throw new ServiceException(400, "Excel 文件没有找到任何 Sheet");
        }

        // 2. 处理第一个 Sheet：周计划
        List<ScissorLiftWplanDO> wplanData = parseScissorWplanSheet(fileBytes, sheetNames.get(0), importTime);
        if (!wplanData.isEmpty()) {
            batchInsertWplan(wplanData);
            log.info("剪叉周计划数据已保存，共 {} 条", wplanData.size());
        }

        // 3. 处理第二个 Sheet：日计划（如果存在）
        if (sheetNames.size() > 1) {
            parseAndSaveScissorDplanSheet(fileBytes, sheetNames.get(1), importTime);
        }
    }

    // 获取所有 Sheet 名称
    private List<String> getSheetNames(byte[] fileBytes) throws IOException {
        List<String> sheetNames = new ArrayList<>();
        try (InputStream is = new ByteArrayInputStream(fileBytes)) {
            ExcelReader excelReader = EasyExcel.read(is).build();
            for (ReadSheet sheet : excelReader.excelExecutor().sheetList()) {
                sheetNames.add(sheet.getSheetName());
            }
            excelReader.finish();
        }
        return sheetNames;
    }

    // 解析剪叉周计划 Sheet
    private List<ScissorLiftWplanDO> parseScissorWplanSheet(byte[] fileBytes, String sheetName, LocalDate importTime) throws IOException {
        List<ScissorLiftWplanDO> result = new ArrayList<>();
        try (InputStream is = new ByteArrayInputStream(fileBytes)) {
            List<Map<Integer, String>> rows = EasyExcel.read(is).sheet(sheetName).doReadSync();
            if (rows.size() < 4) {
                throw new ServiceException(400, "周计划 Sheet 至少需要4行");
            }
            // 查找表头行（包含“产品线”的行）
            int headerRowIdx = findHeaderRow(rows, "产品线");
            if (headerRowIdx == -1) {
                throw new ServiceException(400, "周计划 Sheet 未找到表头行（需包含“产品线”列）");
            }
            Map<Integer, String> headerRow = rows.get(headerRowIdx);
            // 解析列组（依赖第二行日期数字，若无则用计算值）
            Map<Integer, String> dateRow = rows.get(headerRowIdx + 1);
            List<WeekColumnGroup> columnGroups = parseWeekColumns(headerRow, dateRow);
            log.info("找到表头行索引: {}", headerRowIdx);
            log.info("表头行内容: {}", headerRow);
            log.info("解析到的列组数量: {}", columnGroups.size());
            log.info("数据行数量: {}", rows.size() - headerRowIdx - 1);
            // 数据从表头下一行开始
            for (int i = headerRowIdx + 1; i < rows.size(); i++) {
                Map<Integer, String> row = rows.get(i);
                String productLine = row.get(0);          // A列
                String preciseModel = row.get(1);         // B列
                String preciseBom = row.get(2);           // C列
                if (isBlank(productLine) && isBlank(preciseModel) && isBlank(preciseBom)) continue;

                for (WeekColumnGroup group : columnGroups) {
                    String carNumberRange = row.get(group.getCarNumberCol());
                    for (int d = 0; d < 7; d++) {
                        String val = row.get(group.getDateStartCol() + d);
                        if (isBlank(val)) continue;
                        Integer dailyQty = parseInt(val);
                        if (dailyQty == null || dailyQty <= 0) continue;
                        LocalDate planDate = group.getDates().get(d);
                        if (planDate == null) continue;

                        ScissorLiftWplanDO entity = ScissorLiftWplanDO.builder()
                                .productLine(productLine)
                                .preciseModel(preciseModel)
                                .productModel(null)
                                .preciseBom(preciseBom)
                                .planDate(planDate.atStartOfDay())
                                .weekNo(group.getWeekNo())
                                .weekStartDate(group.getWeekStartDate().atStartOfDay())
                                .weekEndDate(group.getWeekEndDate().atStartOfDay())
                                .dailyQuantity(dailyQty)
                                .carNumberRange(carNumberRange)
                                .productionLineType(null)
                                .plate("高机剪叉")
                                .importTime(importTime.atStartOfDay())
                                .build();
                        result.add(entity);
                    }
                }
            }
        }
        return result;
    }

    // 批量插入周计划（使用 Mapper 自定义方法 insertBatchSomeColumn，需要 Mapper 中定义）
    private void batchInsertWplan(List<ScissorLiftWplanDO> list) {
        if (list == null || list.isEmpty()) return;
        // 为每个实体生成雪花ID（如果还没有ID）
        for (ScissorLiftWplanDO entity : list) {
            if (entity.getId() == null) {
                entity.setId(IdWorker.getId());  // 生成Long型雪花ID
            }
        }
        // 分片批量插入
        int batchSize = 30;
        for (int i = 0; i < list.size(); i += batchSize) {
            int end = Math.min(i + batchSize, list.size());
            List<ScissorLiftWplanDO> subList = list.subList(i, end);
            scissorLiftWplanMapper.insertBatchSomeColumn(subList);
        }
    }

    // 解析并保存日计划 Sheet
    private void parseAndSaveScissorDplanSheet(byte[] fileBytes, String sheetName, LocalDate importTime) throws IOException {
        try (InputStream is = new ByteArrayInputStream(fileBytes)) {
            List<Map<Integer, String>> rows = EasyExcel.read(is).sheet(sheetName).doReadSync();
            if (rows.size() < 2) {
                throw new ServiceException(400, "日计划 Sheet 至少需要2行");
            }
            int headerRowIdx = findHeaderRow(rows, "产品型号");
            if (headerRowIdx == -1) {
                throw new ServiceException(400, "日计划 Sheet 未找到表头行（需包含“产品型号”列）");
            }
            Map<Integer, String> headerMap = rows.get(headerRowIdx);
            // 构建列名到索引的映射
            Map<String, Integer> colNameToIdx = new HashMap<>();
            for (Map.Entry<Integer, String> entry : headerMap.entrySet()) {
                if (entry.getValue() != null && !entry.getValue().trim().isEmpty()) {
                    colNameToIdx.put(entry.getValue().trim(), entry.getKey());
                }
            }

            List<ScissorLiftDplanDO> dplanData = new ArrayList<>();
            for (int i = headerRowIdx + 1; i < rows.size(); i++) {
                Map<Integer, String> row = rows.get(i);
                if (row.values().stream().allMatch(v -> isBlank(v))) continue;
                ScissorLiftDplanDO entity = buildScissorDplanFromRow(row, colNameToIdx, importTime);
                if (entity != null) {
                    dplanData.add(entity);
                    if (dplanData.size() >= 500) {
                        // 批量插入日计划（需要日计划 Service 实现批量插入）
                        saveDplanBatch(dplanData);
                        dplanData.clear();
                    }
                }
            }
            if (!dplanData.isEmpty()) {
                saveDplanBatch(dplanData);
            }
            log.info("剪叉日计划数据已保存，共 {} 条", dplanData.size());
        }
    }

    // 批量保存日计划（循环调用 save 或使用 Mapper 批量插入）
    private void saveDplanBatch(List<ScissorLiftDplanDO> list) {
        for (ScissorLiftDplanDO entity : list) {
            if (entity.getId() == null) {
                entity.setId(IdWorker.getId());
            }
            scissorLiftDplanService.createScissorLiftDplan(BeanUtils.toBean(entity, ScissorLiftDplanSaveReqVO.class));
        }
    }

    // 构建剪叉日计划实体（字段映射）
    private ScissorLiftDplanDO buildScissorDplanFromRow(Map<Integer, String> row,
                                                        Map<String, Integer> colNameToIdx,
                                                        LocalDate importTime) {
        ScissorLiftDplanDO entity = new ScissorLiftDplanDO();
        entity.setImportTime(importTime.atStartOfDay());
        boolean hasData = false;

        Map<String, BiConsumer<ScissorLiftDplanDO, String>> setters = new LinkedHashMap<>();
        setters.put("线别", (e, v) -> e.setLineType(v));
        setters.put("精准车型", (e, v) -> e.setPreciseModel(v));
        setters.put("产品型号", (e, v) -> e.setProductModel(v));
        setters.put("ZPS   BOM", (e, v) -> e.setZpsModel(v));
        setters.put("BOM", (e, v) -> e.setPreciseBom(v));
        setters.put("年度顺序号/车号", (e, v) -> e.setCarNo(v));
        setters.put("订单", (e, v) -> e.setOrderNo(v));
        setters.put("备注", (e, v) -> e.setRemark(v));
        setters.put("内外贸\n" +
                "版本", (e, v) -> e.setTradeVersion(v));
        setters.put("台份", (e, v) -> { try { e.setUnitCount(Integer.parseInt(v)); } catch(Exception ignored) {} });
        setters.put("上线计划", (e, v) -> e.setOnlinePlan(parseDateTime(v)));
        setters.put("整车成台计划", (e, v) -> e.setCompletePlan(parseDateTime(v)));
        setters.put("报缴月/周/日", (e, v) -> e.setReportDate(parseDateTime(v)));
        setters.put("国家", (e, v) -> e.setCountry(v));
        setters.put("营销通知号", (e, v) -> e.setContractNo(v));
        setters.put("营销通知时间", (e, v) -> e.setMarketingNoticeTime(v));
        setters.put("订单开立时间", (e, v) -> e.setOrderCreateTime(parseDateTime(v)));
        // 如果板块未设置，或需要强制覆盖，可以添加以下逻辑
        if (entity.getPlate() == null || entity.getPlate().isEmpty()) {
            entity.setPlate("高机剪叉");
        }

        for (Map.Entry<String, BiConsumer<ScissorLiftDplanDO, String>> entry : setters.entrySet()) {
            Integer colIdx = colNameToIdx.get(entry.getKey());
            if (colIdx != null) {
                String value = row.get(colIdx);
                if (!isBlank(value)) {
                    entry.getValue().accept(entity, value.trim());
                    hasData = true;
                }
            }
        }
        return hasData ? entity : null;
    }

    // ========== 辅助方法 ==========

    private int findHeaderRow(List<Map<Integer, String>> rows, String keyword) {
        for (int i = 0; i < Math.min(rows.size(), 10); i++) {
            Map<Integer, String> row = rows.get(i);
            if (row.values().stream().anyMatch(v -> v != null && v.contains(keyword))) {
                return i;
            }
        }
        return -1;
    }

    private boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
    }

    private Integer parseInt(String str) {
        if (str == null) return null;
        try {
            return Integer.parseInt(str.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private LocalDateTime parseDateTime(String val) {
        if (val == null || (val = val.trim()).isEmpty()) return null;
        if ("\\".equals(val) || "#N/A".equalsIgnoreCase(val)) return null;

        DateTimeFormatter mdFormatter = new DateTimeFormatterBuilder()
                .appendPattern("M/d")
                .parseDefaulting(ChronoField.YEAR, LocalDate.now().getYear())
                .toFormatter(Locale.ENGLISH);

        DateTimeFormatter[] formatters = {
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),
                DateTimeFormatter.ofPattern("yyyy-MM-dd"),
                DateTimeFormatter.ofPattern("M/d/yyyy"),
                DateTimeFormatter.ofPattern("M/d/yy"),
                mdFormatter,
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
            } catch (Exception ignored) {}
        }
        if (val.contains("/") && val.chars().filter(ch -> ch == '/').count() == 1) {
            String[] parts = val.split("/");
            if (parts.length == 2) {
                try {
                    int month = Integer.parseInt(parts[0].trim());
                    int day = Integer.parseInt(parts[1].trim());
                    return LocalDate.of(LocalDate.now().getYear(), month, day).atStartOfDay();
                } catch (Exception e) {}
            }
        }
        log.warn("无法解析日期：{}", val);
        return null;
    }

    // 解析周计划动态列组（参考臂式实现）
    private List<WeekColumnGroup> parseWeekColumns(Map<Integer, String> headerRow1, Map<Integer, String> headerRow2) {
        List<WeekColumnGroup> groups = new ArrayList<>();
        int maxCol = headerRow1.size();
        // 修改：去掉 W，允许全角/半角括号，支持点分隔日期
        Pattern weekPattern = Pattern.compile("(\\d+)周计划\\s*[\\（(]\\s*(\\d+)\\.(\\d+)-(\\d+)\\.(\\d+)\\s*[\\）)]");

        for (int col = 0; col < maxCol; col++) {
            String cell = headerRow1.get(col);
            if (cell == null || cell.trim().isEmpty()) continue;
            // 去除所有空白字符（包括换行、空格）
            String cleanCell = cell.replaceAll("\\s+", "");
            Matcher matcher = weekPattern.matcher(cleanCell);
            if (matcher.find()) {
                int weekNum = Integer.parseInt(matcher.group(1));
                int startMonth = Integer.parseInt(matcher.group(2));
                int startDay = Integer.parseInt(matcher.group(3));
                int endMonth = Integer.parseInt(matcher.group(4));
                int endDay = Integer.parseInt(matcher.group(5));
                int year = LocalDate.now().getYear();
                if (startMonth < LocalDate.now().getMonthValue()) year++;
                LocalDate weekStart = LocalDate.of(year, startMonth, startDay);
                LocalDate weekEnd = LocalDate.of(year, endMonth, endDay);

                int dateStartCol = col;      // 假设该列即为周一
                int quantityCol = col + 7;
                int carNumberCol = col + 8;
                if (quantityCol >= maxCol || carNumberCol >= maxCol) {
                    log.warn("周计划组 {} 列数不足，跳过", cell);
                    continue;
                }

                List<LocalDate> dates = new ArrayList<>();
                for (int d = 0; d < 7; d++) {
                    dates.add(weekStart.plusDays(d));
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
                log.info("成功解析周计划: {}，起始列: {}, 数量列: {}, 车号列: {}", weekNum + "W", dateStartCol, quantityCol, carNumberCol);
            }
        }
        return groups;
    }

    @Data
    private static class WeekColumnGroup {
        private String weekNo;
        private LocalDate weekStartDate;
        private LocalDate weekEndDate;
        private int dateStartCol;
        private int quantityCol;
        private int carNumberCol;
        private List<LocalDate> dates;
    }
}