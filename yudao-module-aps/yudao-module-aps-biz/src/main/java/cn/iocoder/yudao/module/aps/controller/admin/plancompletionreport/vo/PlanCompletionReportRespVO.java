package cn.iocoder.yudao.module.aps.controller.admin.plancompletionreport.vo;

import com.alibaba.excel.annotation.format.DateTimeFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 计划完成情况报表 Response VO")
@Data
public class PlanCompletionReportRespVO {

    @Schema(description = "计划日期")
    @DateTimeFormat("yyyy-MM-dd")
    private LocalDateTime planDate;

    @Schema(description = "车间")
    private String workshop;

    @Schema(description = "日计划数量")
    private Long dailyPlanQty;

    @Schema(description = "日实际完成数量")
    private Long dailyActualQty;

    @Schema(description = "计划内完成数量")
    private Long plannedCompletion;

    @Schema(description = "计划内完成率(%)")
    private Double plannedCompletionRate;

    @Schema(description = "累计完成数量")
    private Long cumCompletionQty;

    @Schema(description = "延期完成数量")
    private Long delayedCompletion;

    @Schema(description = "当日未完数量")
    private Long unfinishedQty;

    @Schema(description = "累计未完数量")
    private Long cumUnfinishedQty;

    @Schema(description = "主计划数量")
    private Long mainPlanQty;

    @Schema(description = "主计划计划内完成数量")
    private Long mainPlannedCompletion;

    @Schema(description = "主计划完成率(%)")
    private Double mainPlannedCompletionRate;

    @Schema(description = "紧急件未完订单")
    private Long urgentUnfinishedQty;
}