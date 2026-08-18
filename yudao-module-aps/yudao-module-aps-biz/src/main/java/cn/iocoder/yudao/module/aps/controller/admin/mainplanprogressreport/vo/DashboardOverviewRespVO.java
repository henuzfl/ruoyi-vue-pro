package cn.iocoder.yudao.module.aps.controller.admin.mainplanprogressreport.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;

@Schema(description = "驾驶舱概览卡片数据")
@Data
public class DashboardOverviewRespVO {
    @Schema(description = "总订单数")
    private Integer totalOrders;

    @Schema(description = "计划总量")
    private BigDecimal totalPlanQty;

    @Schema(description = "完工总量")
    private BigDecimal totalCompletedQty;

    @Schema(description = "完工率")
    private BigDecimal completionRate;

    @Schema(description = "缺料订单数")
    private Integer shortageOrders;

    @Schema(description = "物料齐套率")
    private BigDecimal kitRate;

    @Schema(description = "数据更新时间")
    private String lastUpdateTime;

    @Schema(description = "需求总量")
    private Long totalRequiredQty;           // 所有物料需求总量（分母）

    @Schema(description = "转序完成且缺料的订单的物料缺口总量")
    private Long completedShortageQty;       // 转序完成且缺料的订单的物料缺口总量（分子）


    private BigDecimal planQtyTrend;
    private BigDecimal completeQtyTrend;
    private BigDecimal completionRateTrend;
    private BigDecimal materialRateTrend;
    private BigDecimal shortageOrdersTrend;
    private BigDecimal kitRateTrend;
}