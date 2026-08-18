package cn.iocoder.yudao.module.buyer.controller.admin.hostrequirementcomparisondiff.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Schema(description = "管理后台 - 主机需求对比差异数据 VO")
@Data
public class HostRequirementComparisonDiffVO {

    @Schema(description = "车型")
    @ExcelProperty("车型")
    private String productModel;

    @Schema(description = "出产顺序")
    @ExcelProperty("出产顺序")
    private String productionOrder;

    @Schema(description = "中联订单号")
    @ExcelProperty("中联订单号")
    private String bareMachineOrderNo;

    @Schema(description = "底盘订单号")
    @ExcelProperty("底盘订单号")
    private String drivingUnitOrderNo;

    // ========== 显示月份（当前选择月份）数据 ==========
    @Schema(description = "显示月份日期")
    @ExcelProperty("显示月份")
    private String chassisOnlinePlanDate;

    @Schema(description = "显示月份配额")
    @ExcelProperty("显示月份配额")
    private String quota2;

    @Schema(description = "显示月份主机图号")
    @ExcelProperty("显示月份主机图号")
    private String materialNo;

    @Schema(description = "显示月份特力图号")
    @ExcelProperty("显示月份特力图号")
    private String telicode;

    @Schema(description = "显示月份分解油缸")
    @ExcelProperty("显示月份分解油缸")
    private String cylinderName;

    @Schema(description = "显示月份配置")
    @ExcelProperty("显示月份配置")
    private Integer config;

    @Schema(description = "显示月份需配数量")
    @ExcelProperty("显示月份需配数量")
    private BigDecimal requiredQuantity;

    @Schema(description = "台套")
    @ExcelProperty("台套")
    private BigDecimal unitQuantity;

    // ========== 对比月份数据 ==========
    @Schema(description = "对比月份日期")
    @ExcelProperty("对比月份")
    private String versionDate;

    @Schema(description = "对比月份配额")
    @ExcelProperty("对比月份配额")
    private String quota1;

    @Schema(description = "对比月份主机图号")
    @ExcelProperty("对比月份主机图号")
    private String materialNoCompare;

    @Schema(description = "对比月份特力图号")
    @ExcelProperty("对比月份特力图号")
    private String telicodeCompare;

    @Schema(description = "对比月份配置")
    @ExcelProperty("对比月份配置")
    private Integer configCompare;

    @Schema(description = "对比月份需配数量")
    @ExcelProperty("对比月份需配数量")
    private BigDecimal requiredQuantityCompare;

    // ========== 差异及增量 ==========
    @Schema(description = "配额差异标志")
    @ExcelProperty("配额差异")
    private Boolean quota1Diff;

    @Schema(description = "主机图号差异标志")
    @ExcelProperty("主机图号差异")
    private Boolean materialNoDiff;

    @Schema(description = "配置差异标志")
    @ExcelProperty("配置差异")
    private Boolean configDiff;

    @Schema(description = "需配数量差异标志")
    @ExcelProperty("需配数量差异")
    private Boolean requiredQuantityDiff;

    @Schema(description = "需配数量增量（显示月份 - 对比月份）")
    @ExcelProperty("需配数量增量")
    private BigDecimal requiredQuantityIncrease;
}