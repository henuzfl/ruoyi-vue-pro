package cn.iocoder.yudao.module.aps.controller.admin.materialshortage.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import com.alibaba.excel.annotation.ExcelProperty;
import java.math.BigDecimal;

@Schema(description = "管理后台 - 物料缺口明细 Response VO")
@Data
public class MaterialShortageDetailRespVO {

    @Schema(description = "组件物料编码")
    @ExcelProperty("组件物料编码")
    private String componentMaterialNo;

    @Schema(description = "组件名称")
    @ExcelProperty("组件名称")
    private String componentDesc;

    @Schema(description = "单位用量")
    @ExcelProperty("单位用量")
    private BigDecimal unitUsage;

    @Schema(description = "库存数量")
    @ExcelProperty("库存数量")
    private BigDecimal stockQuantity;

    @Schema(description = "在途数量")
    @ExcelProperty("在途数量")
    private BigDecimal transit;

    @Schema(description = "已发放数量")
    @ExcelProperty("已发放数量")
    private BigDecimal issue;

    @Schema(description = "缺口数量")
    @ExcelProperty("缺口数量")
    private BigDecimal shortageQty;
}