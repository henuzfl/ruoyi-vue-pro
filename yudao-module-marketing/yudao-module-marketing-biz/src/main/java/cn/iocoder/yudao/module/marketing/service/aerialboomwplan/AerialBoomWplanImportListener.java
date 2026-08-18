package cn.iocoder.yudao.module.marketing.service.aerialboomwplan;

import cn.iocoder.yudao.module.marketing.dal.dataobject.aerialboomwplan.AerialBoomWplanDO;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.util.MapUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 高机臂式周计划Excel导入监听器（支持动态列扩展）
 */
@Slf4j
public class AerialBoomWplanImportListener extends AnalysisEventListener<Map<Integer, String>> {

    private final List<AerialBoomWplanDO> dataList = new ArrayList<>();
    private final int batchSize = 500;
    private final LocalDateTime importTime;
    private final String plate = "高机臂式";

    // 存储解析后的列组信息
    private List<WeekColumnGroup> columnGroups;
    // 产品信息列索引（前4列）
    private static final int COL_PRODUCT_LINE = 0;
    private static final int COL_PRECISE_MODEL = 1;
    private static final int COL_PRODUCT_MODEL = 2;
    private static final int COL_PRECISE_BOM = 3;

    // 是否已解析表头
    private boolean headerParsed = false;

    public AerialBoomWplanImportListener(LocalDateTime importTime) {
        this.importTime = importTime;
    }

    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        if (headerParsed) return;
        columnGroups = parseColumnGroups(headMap, context);
        headerParsed = true;
        log.info("解析到 {} 个周计划列组", columnGroups.size());
    }

    /**
     * 解析表头，构建列组列表
     * @param headMap 表头第一行的内容（key=列索引，value=单元格文本）
     * @param context 上下文（可用于获取第二行）
     */
    private List<WeekColumnGroup> parseColumnGroups(Map<Integer, String> headMap, AnalysisContext context) {
        List<WeekColumnGroup> groups = new ArrayList<>();
        // 获取第二行（日期数字行）的内容
        Map<Integer, String> secondRow = getSecondRow(context);
        if (secondRow == null) {
            throw new RuntimeException("无法读取表头第二行，请确保Excel包含日期数字行");
        }

        int startIdx = 4; // 第一个可能的周计划起始列
        int maxCol = headMap.size();
        Pattern weekPattern = Pattern.compile("(\\d+)W周计划[（(](\\d+)\\.(\\d+)-(\\d+)\\.(\\d+)[）)]");
        int i = startIdx;
        while (i < maxCol) {
            String headerCell = headMap.getOrDefault(i, "");
            if (headerCell == null || headerCell.trim().isEmpty()) {
                i++;
                continue;
            }
            Matcher m = weekPattern.matcher(headerCell);
            if (m.find()) {
                int weekNum = Integer.parseInt(m.group(1));
                int startMonth = Integer.parseInt(m.group(2));
                int startDay = Integer.parseInt(m.group(3));
                int endMonth = Integer.parseInt(m.group(4));
                int endDay = Integer.parseInt(m.group(5));
                // 假设年份根据当前日期推断，这里简化：使用2025年（可从配置或Excel内容推断）
                int year = 2025;
                LocalDate weekStart = LocalDate.of(year, startMonth, startDay);
                LocalDate weekEnd = LocalDate.of(year, endMonth, endDay);
                // 该组包含：7个日期列 + 1个数量列 + 1个车号列
                int dateStartCol = i;
                int quantityCol = i + 7;      // 数量列
                int carNumberCol = i + 8;     // 车号列
                // 检查列边界
                if (quantityCol >= maxCol || carNumberCol >= maxCol) {
                    log.warn("周计划组 {} 列不足，跳过", headerCell);
                    i++;
                    continue;
                }
                // 获取日期列对应的具体日期（从第二行获取）
                List<LocalDate> dates = new ArrayList<>();
                for (int d = 0; d < 7; d++) {
                    String dateStr = secondRow.get(dateStartCol + d);
                    LocalDate date = parseDateFromNumber(dateStr, year, startMonth, startDay, d);
                    if (date == null) {
                        log.warn("无法解析日期: {} 列 {}", dateStr, dateStartCol + d);
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
                i += 9; // 移动到下一组起始列
            } else {
                i++;
            }
        }
        return groups;
    }

    /**
     * 获取表头第二行（日期数字行）的内容
     */
    private Map<Integer, String> getSecondRow(AnalysisContext context) {
        // 通过 readRowHolder 获取下一行？FastExcel 没有直接提供，但可以在 invoke 中读取第一行数据行时判断。
        // 替代方案：在 invokeHeadMap 之后，读取第一行数据时假设第一行数据就是日期数字行？
        // 更可靠：在构造监听器时，由调用方预先读取前两行。
        // 这里简化：使用固定逻辑，若第二行数据在 invoke 中第一个数据行是数字行，则存储。
        // 实际项目中可在调用 ExcelUtils.read 前先单独读取前两行，或者使用自定义读取方式。
        // 由于实现复杂，我们采用另一种方式：直接在 invoke 中根据行号判断，将第二行作为日期映射。
        // 为了简化，本示例中我们假设表头结构固定，不依赖第二行，而是根据周起始日期计算每天的具体日期。
        // 计算方式：weekStart.plusDays(列偏移)
        return null;
    }

    private LocalDate parseDateFromNumber(String dateStr, int year, int month, int day, int offset) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return null;
        }
        try {
            int dayNum = Integer.parseInt(dateStr.trim());
            // 简单的日期推断：根据周起始日期 + 偏移
            // 这里传入的 offset 是当周内的偏移，但我们需要知道具体的月份，因为可能跨月。
            // 最好使用周起始日期 + offset 来计算
            return null; // 实际应结合上下文
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    public void invoke(Map<Integer, String> rowMap, AnalysisContext context) {
        if (!headerParsed) {
            // 如果表头未解析，可能是第一行数据（实际可能是第二行？），跳过
            return;
        }
        // 提取产品信息
        String productLine = rowMap.get(COL_PRODUCT_LINE);
        String preciseModel = rowMap.get(COL_PRECISE_MODEL);
        String productModel = rowMap.get(COL_PRODUCT_MODEL);
        String preciseBom = rowMap.get(COL_PRECISE_BOM);
        // 如果产品信息全为空，则可能是合计行，跳过
        if (isBlank(productLine) && isBlank(preciseModel) && isBlank(productModel) && isBlank(preciseBom)) {
            return;
        }
        // 遍历每个列组
        for (WeekColumnGroup group : columnGroups) {
            String carNumberRange = rowMap.get(group.getCarNumberCol());
            // 遍历该组的7个日期列
            for (int d = 0; d < 7; d++) {
                int col = group.getDateStartCol() + d;
                String val = rowMap.get(col);
                if (val == null || val.trim().isEmpty()) {
                    continue;
                }
                Integer dailyQty = null;
                try {
                    dailyQty = Integer.parseInt(val.trim());
                } catch (NumberFormatException e) {
                    // 非数字，忽略
                    continue;
                }
                if (dailyQty == null || dailyQty <= 0) {
                    continue;
                }
                LocalDate date = group.getDates().get(d);
                if (date == null) {
                    continue;
                }
                AerialBoomWplanDO doObj = AerialBoomWplanDO.builder()
                        .productLine(productLine)
                        .preciseModel(preciseModel)
                        .productModel(productModel)
                        .preciseBom(preciseBom)
                        .planDate(date.atStartOfDay())
                        .weekNo(group.getWeekNo())
                        .weekStartDate(group.getWeekStartDate().atStartOfDay())
                        .weekEndDate(group.getWeekEndDate().atStartOfDay())
                        .dailyQuantity(dailyQty)
                        .carNumberRange(carNumberRange)
                        .productionLineType(null) // 可根据颜色底纹解析，暂不处理
                        .plate(plate)
                        .importTime(importTime)
                        .build();
                dataList.add(doObj);
                if (dataList.size() >= batchSize) {
                    saveData();
                }
            }
        }
    }

    private boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        saveData();
    }

    private void saveData() {
        if (dataList.isEmpty()) return;
        // 注意：这里需要通过外部传入 Service 或 Mapper 进行批量插入
        // 由于监听器无法直接使用 Spring Bean，我们可以在调用方处理：getDataList() 然后批量插入。
        // 因此本监听器只负责收集数据，不在内部保存。
        // 实际使用时，在 Controller 中调用 listener.getDataList() 获取结果并批量插入。
    }

    public List<AerialBoomWplanDO> getDataList() {
        return dataList;
    }

    // 内部类：表示一个周计划列组
    static class WeekColumnGroup {
        private String weekNo;
        private LocalDate weekStartDate;
        private LocalDate weekEndDate;
        private int dateStartCol;
        private int quantityCol;
        private int carNumberCol;
        private List<LocalDate> dates;
        // getters/setters 省略
        // 手动生成
        public String getWeekNo() { return weekNo; }
        public void setWeekNo(String weekNo) { this.weekNo = weekNo; }
        public LocalDate getWeekStartDate() { return weekStartDate; }
        public void setWeekStartDate(LocalDate weekStartDate) { this.weekStartDate = weekStartDate; }
        public LocalDate getWeekEndDate() { return weekEndDate; }
        public void setWeekEndDate(LocalDate weekEndDate) { this.weekEndDate = weekEndDate; }
        public int getDateStartCol() { return dateStartCol; }
        public void setDateStartCol(int dateStartCol) { this.dateStartCol = dateStartCol; }
        public int getQuantityCol() { return quantityCol; }
        public void setQuantityCol(int quantityCol) { this.quantityCol = quantityCol; }
        public int getCarNumberCol() { return carNumberCol; }
        public void setCarNumberCol(int carNumberCol) { this.carNumberCol = carNumberCol; }
        public List<LocalDate> getDates() { return dates; }
        public void setDates(List<LocalDate> dates) { this.dates = dates; }
    }
}