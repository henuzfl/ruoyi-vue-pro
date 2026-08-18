package cn.iocoder.yudao.module.aps.controller.admin.mainplanprogressreport.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.util.Date;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 主计划进度报表 VO")
@Data
public class MainPlanProgressReportRespVO {

    @Schema(description = "生产订单号")
    private String productionOrderNo;

    @Schema(description = "总成订单数量")
    private BigDecimal assemblyOrderQuantity;

    @Schema(description = "主物料号")
    private String assemblyMaterialNo;

    @Schema(description = "主物料描述")
    private String mainMaterialDesc;

    @Schema(description = "基本完成日期")
    private Date baseCompletionDate;

    @Schema(description = "生产车间")
    private String productionWorkshop;

    @Schema(description = "订单创建时间")
    private Date orderCreateTime;

    @Schema(description = "排产数量")
    private BigDecimal scheduledQuantity;

    @Schema(description = "排产时间")
    private Date scheduledDate;

    @Schema(description = "车间产出")
    private BigDecimal workshopOutput;

    @Schema(description = "差异")
    private BigDecimal difference;
}