package cn.iocoder.yudao.module.aps.controller.admin.materialprogresstrack.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;
import java.util.List;

@Data
@Schema(description = "物料进度套表 - 子件明细")
public class MaterialChildrenRespVO {
    @Schema(description = "子件物料号")
    private String childMaterialCode;

    @Schema(description = "子件描述")
    private String childMaterialDesc;

    @Schema(description = "需求数量汇总")
    private BigDecimal totalDemand;

    @Schema(description = "已出库数量")
    private BigDecimal totalIssued;

    @Schema(description = "库存数量（若无则填0）")
    private BigDecimal stockQty;

    @Schema(description = "缺口数量")
    private BigDecimal shortageQty;

    @Schema(description = "供应商列表")
    private List<SupplierInfo> suppliers;
}