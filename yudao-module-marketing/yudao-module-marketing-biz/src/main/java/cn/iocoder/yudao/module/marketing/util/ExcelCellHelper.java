package cn.iocoder.yudao.module.marketing.util;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExcelCellHelper {

    /**
     * 获取单元格的原始字符串值（公式会求值）
     */
    public static String getCellStringValue(Cell cell, FormulaEvaluator evaluator) {
        if (cell == null) return null;
        CellType cellType = cell.getCellType();
        if (cellType == CellType.FORMULA) {
            // 强制求值
            CellValue cellValue = evaluator.evaluate(cell);
            switch (cellValue.getCellType()) {
                case STRING: return cellValue.getStringValue();
                case NUMERIC: return String.valueOf(cellValue.getNumberValue());
                case BOOLEAN: return String.valueOf(cellValue.getBooleanValue());
                default: return null;
            }
        } else if (cellType == CellType.STRING) {
            return cell.getStringCellValue();
        } else if (cellType == CellType.NUMERIC) {
            // 数字转字符串（保留原样）
            return String.valueOf(cell.getNumericCellValue());
        } else if (cellType == CellType.BOOLEAN) {
            return String.valueOf(cell.getBooleanCellValue());
        }
        return null;
    }

    /**
     * 获取单元格的日期值（支持序列数、日期字符串、日期单元格）
     */
    public static Date getCellDateValue(Cell cell, FormulaEvaluator evaluator) {
        if (cell == null) return null;
        CellType cellType = cell.getCellType();
        if (cellType == CellType.FORMULA) {
            CellValue cellValue = evaluator.evaluate(cell);
            if (cellValue.getCellType() == CellType.NUMERIC) {
                double num = cellValue.getNumberValue();
                return DateUtil.getJavaDate(num);  // 序列数转日期
            }
            return null;
        } else if (cellType == CellType.NUMERIC) {
            if (DateUtil.isCellDateFormatted(cell)) {
                return cell.getDateCellValue();
            } else {
                // 当作Excel序列数处理
                double numericValue = cell.getNumericCellValue();
                return DateUtil.getJavaDate(numericValue);
            }
        } else if (cellType == CellType.STRING) {
            String str = cell.getStringCellValue().trim();
            if (str.isEmpty()) return null;
            // 尝试解析常见日期格式（可根据需要扩展）
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try {
                return sdf.parse(str);
            } catch (ParseException e) {
                // 也可以尝试其他格式
            }
        }
        return null;
    }

    /**
     * 获取合并单元格的真实值（从合并区域左上角取值）
     */
    public static String getMergedCellStringValue(Sheet sheet, Row row, int columnIndex, FormulaEvaluator evaluator) {
        int rowNum = row.getRowNum();
        for (CellRangeAddress merged : sheet.getMergedRegions()) {
            if (merged.isInRange(rowNum, columnIndex)) {
                Row firstRow = sheet.getRow(merged.getFirstRow());
                Cell firstCell = firstRow.getCell(merged.getFirstColumn());
                return getCellStringValue(firstCell, evaluator);
            }
        }
        Cell cell = row.getCell(columnIndex);
        return getCellStringValue(cell, evaluator);
    }

    /**
     * 判断整行是否为空（所有单元格都为空或空白字符）
     */
    public static boolean isRowEmpty(Row row) {
        if (row == null) return true;
        for (Cell cell : row) {
            if (cell != null) {
                String value = getCellRawString(cell);
                if (value != null && !value.trim().isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    private static String getCellRawString(Cell cell) {
        if (cell == null) return null;
        switch (cell.getCellType()) {
            case STRING: return cell.getStringCellValue();
            case NUMERIC: return String.valueOf(cell.getNumericCellValue());
            case BOOLEAN: return String.valueOf(cell.getBooleanCellValue());
            case FORMULA: return cell.getCellFormula();
            default: return null;
        }
    }
}