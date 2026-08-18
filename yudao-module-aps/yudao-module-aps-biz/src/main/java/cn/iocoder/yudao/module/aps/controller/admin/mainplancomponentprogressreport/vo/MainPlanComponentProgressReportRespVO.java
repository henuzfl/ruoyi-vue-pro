package cn.iocoder.yudao.module.aps.controller.admin.mainplancomponentprogressreport.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;

@Schema(description = "管理后台 - 组件物料需求报表 Response VO")
@Data
public class MainPlanComponentProgressReportRespVO {

    @Schema(description = "总成订单号")
    private String productionOrderNo;

    @Schema(description = "主物料号")
    private String assemblyMaterialNo;

    @Schema(description = "组件物料号")
    private String componentMaterialNo;

    @Schema(description = "组件物料描述")
    private String materialDesc;

    @Schema(description = "生产车间")
    private String productionWorkshop;

    @Schema(description = "总需求")
    private BigDecimal totalRequirement;

    @Schema(description = "采购类型")
    private String procurementType;

    @Schema(description = "库存")
    private BigDecimal stockQuantity;

    @Schema(description = "在制")
    private BigDecimal orderQuantity;

    @Schema(description = "采购未清订单数量")
    private BigDecimal openPoQuantity;

    @Schema(description = "是否满足")
    private String satisfy;

    @Schema(description = "备料")
    private String materialPreparation;

    @Schema(description = "供方")
    private String supplier;
}