package cn.iocoder.yudao.module.marketing.controller.admin.asmrequirement.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class AsmRequirementImportVO {

    @ExcelProperty(index = 0)  // 第1列：主机单位
    private String hostUnit;

    @ExcelProperty(index = 1)  // 第2列：车型
    private String vehicleModel;

    @ExcelProperty(index = 2)  // 第3列：总成物料编码
    private String assemblyMaterialNo;

    @ExcelProperty(index = 3)  // 第4列：总成物料名称
    private String mainMaterialDesc;

    @ExcelProperty(index = 4)  // 第5列：需求数量
    private BigDecimal requireQuantity;

    @ExcelProperty(index = 5)
    private String requireDateStr;  // 先用 String 接收

    // 在业务代码中手动转换
    public LocalDate getRequireDate() {
        if (requireDateStr == null || requireDateStr.trim().isEmpty()) {
            return null;
        }
        try {
            // 支持 2026/7/1 或 2026-07-01 格式
            String dateStr = requireDateStr.trim().replace("/", "-");
            return LocalDate.parse(dateStr);
        } catch (Exception e) {
            return null;
        }
    }
}