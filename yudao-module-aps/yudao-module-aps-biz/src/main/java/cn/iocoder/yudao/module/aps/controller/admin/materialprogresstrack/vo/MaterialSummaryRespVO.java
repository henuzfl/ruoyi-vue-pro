package cn.iocoder.yudao.module.aps.controller.admin.materialprogresstrack.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Schema(description = "物料进度套表 - 主表汇总行")
public class MaterialSummaryRespVO {
    @Schema(description = "物料编码")
    private String materialCode;

    @Schema(description = "物料描述")
    private String materialDesc;

    @Schema(description = "车间")
    private String workshop;

    @Schema(description = "需求月份（yyyy-MM）")
    private String demandMonth;

    @Schema(description = "需求数量汇总")
    private BigDecimal totalDemand;

    @Schema(description = "已完工数量汇总")
    private BigDecimal totalCompleted;

    @Schema(description = "库存数量汇总")
    private BigDecimal stock;
}