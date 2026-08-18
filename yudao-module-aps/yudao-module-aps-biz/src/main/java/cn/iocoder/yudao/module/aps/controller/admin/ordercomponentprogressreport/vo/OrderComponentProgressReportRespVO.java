package cn.iocoder.yudao.module.aps.controller.admin.ordercomponentprogressreport.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

@Schema(description = "管理后台 - 订单组件需求进度报表 Response VO")
@Data
public class OrderComponentProgressReportRespVO {

    @Schema(description = "生产订单号")
    private String productionOrderNo;

    @Schema(description = "主物料号")
    private String assemblyMaterialNo;

    @Schema(description = "组件物料号")
    private String componentMaterialNo;

    @Schema(description = "组件物料描述")
    private String materialDesc;

    @Schema(description = "订单数量")
    private BigDecimal orderQuantity;          // 对应 SQL 中的 orderQuantity

    @Schema(description = "总需求")
    private BigDecimal totalRequirement;

    @Schema(description = "库存")
    private BigDecimal stockQuantity;

    @Schema(description = "在制数量")
    private BigDecimal workInProgress;         // 对应 SQL 中的 workInProgress

    @Schema(description = "采购未清数量")
    private BigDecimal openPoQuantity;         // 对应 SQL 中的 openPoQuantity

    @Schema(description = "是否满足")
    private String satisfy;

    @Schema(description = "生产车间")
    private String productionWorkshop;

    @Schema(description = "采购类型")
    private String procurementType;

    @Schema(description = "排产日期")
    private Date scheduledDate;                // 对应 SQL 中的 scheduledDate

    @Schema(description = "计划完成日期")
    private Date basicEndDate;                 // 可选，根据前端需要添加
}