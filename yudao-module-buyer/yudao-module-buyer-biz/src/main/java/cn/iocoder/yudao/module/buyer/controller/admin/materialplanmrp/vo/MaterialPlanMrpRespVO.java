package cn.iocoder.yudao.module.buyer.controller.admin.materialplanmrp.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;

@Schema(description = "管理后台 - 买家需求预测 Response VO")
@Data
public class MaterialPlanMrpRespVO {

    @Schema(description = "客户")
    private String customerName;

    @Schema(description = "产品线/分类")
    private String productLine;

    @Schema(description = "吨位/米段")
    private String tonnageSegment;

    @Schema(description = "车型")
    private String vehicleModel;

    @Schema(description = "下个月预测值")
    private BigDecimal nextMonthForecast;

    @Schema(description = "下第二个月预测值")
    private BigDecimal secondMonthForecast;

    @Schema(description = "下第三个月预测值")
    private BigDecimal thirdMonthForecast;

    @Schema(description = "下第四个月预测值")
    private BigDecimal fourthMonthForecast;

    @Schema(description = "下第五个月预测值")
    private BigDecimal fifthMonthForecast;

    @Schema(description = "特力油缸编码")
    private String cylinderCode;

    @Schema(description = "单台数量")
    private BigDecimal unitQuantity;

    @Schema(description = "下个月油缸需求数量")
    private BigDecimal nextMonthCylinderDemand;

    @Schema(description = "下第二个月油缸需求数量")
    private BigDecimal secondMonthCylinderDemand;

    @Schema(description = "下第三个月油缸需求数量")
    private BigDecimal thirdMonthCylinderDemand;

    @Schema(description = "下第四个月油缸需求数量")
    private BigDecimal fourthMonthCylinderDemand;

    @Schema(description = "下第五个月油缸需求数量")
    private BigDecimal fifthMonthCylinderDemand;

    @Schema(description = "采购组件物料号")
    private String componentMaterialNo;

    @Schema(description = "组件名称")
    private String componentDesc;

    @Schema(description = "规格型号")
    private String specificationModel;

    @Schema(description = "毛重")
    private BigDecimal grossWeight;

    @Schema(description = "净重")
    private BigDecimal netWeight;

    @Schema(description = "采购组")
    private String purchasingGroup;

    @Schema(description = "物料分类")
    private String materialType;

    @Schema(description = "组件单台数量")
    private BigDecimal componentUnitUsage;

    @Schema(description = "组件总数量")
    private BigDecimal componentTotalDemand;

    @Schema(description = "库存数据")
    private BigDecimal stockQuantity;

    @Schema(description = "下个月已分配库存")
    private BigDecimal nextMonthAllocated;

    @Schema(description = "下第二个月已分配库存")
    private BigDecimal secondMonthAllocated;

    @Schema(description = "下第三个月已分配库存")
    private BigDecimal thirdMonthAllocated;

    @Schema(description = "总已分配库存")
    private BigDecimal totalAllocatedStock;

    @Schema(description = "最终剩余库存")
    private BigDecimal finalRemainingStock;

    @Schema(description = "当月订单数量")
    private BigDecimal currentMonthOrderQuantity;

    @Schema(description = "备料差异")
    private BigDecimal materialSupplyStatus;

    @Schema(description = "备料是否满足")
    private String materialSupplyDesc;
}