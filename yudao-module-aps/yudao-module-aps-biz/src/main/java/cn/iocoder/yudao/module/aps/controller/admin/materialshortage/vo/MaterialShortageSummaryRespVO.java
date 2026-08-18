package cn.iocoder.yudao.module.aps.controller.admin.materialshortage.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import com.alibaba.excel.annotation.ExcelProperty;
import java.math.BigDecimal;

@Schema(description = "管理后台 - 物料缺口汇总 Response VO")
@Data
public class MaterialShortageSummaryRespVO {

    @Schema(description = "成品物料编码", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("成品物料编码")
    private String mainMaterialNo;

    @Schema(description = "成品物料名称")
    @ExcelProperty("成品物料名称")
    private String materialDesc;

    @Schema(description = "总成需求数量")
    @ExcelProperty("总成需求")
    private BigDecimal mainRequirement;

    @Schema(description = "总成当前库存")
    @ExcelProperty("总成库存")
    private BigDecimal mainStockQuantity;

    @Schema(description = "总成在途数量")
    @ExcelProperty("总成在途")
    private BigDecimal mainTransit;

    @Schema(description = "总成已交付数量（销售出库）")
    @ExcelProperty("总成已交付")
    private BigDecimal mainDelivered;


    @Schema(description = "总缺口数量")
    @ExcelProperty("总缺口数量")
    private BigDecimal totalShortageQty;

    @Schema(description = "缺口子件数量")
    @ExcelProperty("缺口子件数量")
    private Integer componentCount;
}