package cn.iocoder.yudao.module.aps.controller.admin.materialshortage.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import com.alibaba.excel.annotation.ExcelProperty;
import java.math.BigDecimal;

@Schema(description = "管理后台 - 组件缺口汇总 Response VO")
@Data
public class MaterialShortageComponentSummaryRespVO {

    @Schema(description = "组件物料编码", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("组件物料编码")
    private String componentMaterialNo;

    @Schema(description = "组件名称")
    @ExcelProperty("组件名称")
    private String componentDesc;

    @Schema(description = "需求数量（所有总成对该组件的需求总量）")
    @ExcelProperty("需求数量")
    private BigDecimal totalRequirement;

    @Schema(description = "库存数量")
    @ExcelProperty("库存数量")
    private BigDecimal stockQuantity;

    @Schema(description = "在途数量")
    @ExcelProperty("在途数量")
    private BigDecimal transit;

    @Schema(description = "已投料数量")
    @ExcelProperty("已投料数量")
    private BigDecimal totalIssue;

    @Schema(description = "缺口数量")
    @ExcelProperty("缺口数量")
    private BigDecimal shortageQty;

    @Schema(description = "涉及成品数（哪些总成用到该组件）")
    @ExcelProperty("涉及成品数")
    private Integer mainCount;

    @Schema(description = "涉及成品列表（用于展示）")
    private String mainMaterialNos;
}