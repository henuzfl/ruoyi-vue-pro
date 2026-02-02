package cn.iocoder.yudao.module.buyer.controller.admin.buyermaterial.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 备料明细汇总 Response VO")
@Data
@ExcelIgnoreUnannotated
public class buyerMaterialRespVO {

    @Schema(description = "需求物料", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("需求物料")
    private String reqMaterial;

    @Schema(description = "客户", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("客户")
    private String customer;

    @Schema(description = "车型")
    @ExcelProperty("车型")
    private String vehicleModel;

    @Schema(description = "总成需求数量", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("总成需求数量")
    private BigDecimal assemblyQuantity;

    @Schema(description = "采购子件物料", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("采购子件物料")
    private String compMaterial;

    @Schema(description = "采购子件描述")
    @ExcelProperty("采购子件描述")
    private String compDesc;

    @Schema(description = "规格型号")
    @ExcelProperty("规格型号")
    private String specModel;

    @Schema(description = "单台用量", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("单台用量")
    private BigDecimal unitUsage;

    @Schema(description = "组件需求数量", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("组件需求数量")
    private BigDecimal compDemandQty;

    @Schema(description = "备料数量", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("备料数量")
    private BigDecimal preparedQty;

    @Schema(description = "实时库存", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("实时库存")
    private BigDecimal stockQty;

    @Schema(description = "差值", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("差值")
    private BigDecimal difference;

    @Schema(description = "库存状态", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("库存状态")
    private String stockStatus;

    @Schema(description = "供应商")
    @ExcelProperty("供应商")
    private String supplier;

    @Schema(description = "采购员")
    @ExcelProperty("采购员")
    private String buyer;

    @Schema(description = "采购组")
    @ExcelProperty("采购组")
    private String procGroup;

    @Schema(description = "需求月份")
    @ExcelProperty("需求月份")
    private String demandMonth;
}