package cn.iocoder.yudao.module.buyer.controller.admin.hostrequirementcomparisondiff.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Schema(description = "管理后台 - 主机需求对比 Response VO")
@Data
public class HostRequirementComparisonDiffRespVO {

    @Schema(description = "2026-2-12版本")
    @ExcelProperty("2026-2-12版本")
    private String chassisOnlinePlanDate;

    @Schema(description = "车型")
    @ExcelProperty("车型")
    private String productModel;

    @Schema(description = "出产顺序")
    @ExcelProperty("出产顺序")
    private String productionOrder;   // 可由 seqNo2025 + "-" + seqNo2026 拼接，或后端直接返回

    @Schema(description = "中联订单号")
    @ExcelProperty("中联订单号")
    private String bareMachineOrderNo;

    @Schema(description = "底盘订单号")
    @ExcelProperty("底盘订单号")
    private String drivingUnitOrderNo;

    @Schema(description = "配额2026.1.25")
    @ExcelProperty("配额2026.1.25")
    private String quota1;

    @Schema(description = "配额2026.2.13")
    @ExcelProperty("配额2026.2.13")
    private String quota2;

    @Schema(description = "台套")
    @ExcelProperty("台套")
    private BigDecimal unitQuantity;

    @Schema(description = "2026-1-20版本")
    @ExcelProperty("2026-1-20版本")
    private String versionDate;

    @Schema(description = "分解油缸")
    @ExcelProperty("分解油缸")
    private String cylinderName;

    @Schema(description = "主机图号")
    @ExcelProperty("主机图号")
    private String materialNo;

    @Schema(description = "特力图号")
    @ExcelProperty("特力图号")
    private String teliCode;

    @Schema(description = "配置")
    @ExcelProperty("配置")
    private Integer config;

    @Schema(description = "需配数量")
    @ExcelProperty("需配数量")
    private BigDecimal requiredQuantity;
}