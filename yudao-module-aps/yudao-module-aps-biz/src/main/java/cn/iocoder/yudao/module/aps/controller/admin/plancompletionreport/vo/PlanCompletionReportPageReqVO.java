package cn.iocoder.yudao.module.aps.controller.admin.plancompletionreport.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 计划完成情况报表分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
public class PlanCompletionReportPageReqVO extends PageParam {

    @Schema(description = "计划日期范围（开始）")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime beginPlanDate;

    @Schema(description = "计划日期范围（结束）")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime endPlanDate;

    @Schema(description = "车间（Y01/Y02/Y16等）")
    private String workshop;
}