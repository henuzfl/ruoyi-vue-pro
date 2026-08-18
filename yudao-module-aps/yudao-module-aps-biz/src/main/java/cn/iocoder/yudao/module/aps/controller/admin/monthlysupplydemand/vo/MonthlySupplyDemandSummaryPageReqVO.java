package cn.iocoder.yudao.module.aps.controller.admin.monthlysupplydemand.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * 月度供需总览表分页请求 VO
 *
 * @author 柳文
 */
@Schema(description = "管理后台 - 月度供需总览表分页请求 VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MonthlySupplyDemandSummaryPageReqVO extends PageParam {

    @Schema(description = "总成物料号", example = "ASSEMBLY-001")
    private String assemblyMaterialNo;

    @Schema(description = "物料描述", example = "液压油缸总成")
    private String materialDesc;

    @Schema(description = "月份（精确查询，格式：YYYY-MM）", example = "2026-07")
    private String scheduledDate;

    @Schema(description = "月份开始（范围查询）", example = "2026-01")
    private String scheduledDateStart;

    @Schema(description = "月份结束（范围查询）", example = "2026-12")
    private String scheduledDateEnd;

    @Schema(description = "创建时间开始")
    private LocalDateTime createTimeStart;

    @Schema(description = "创建时间结束")
    private LocalDateTime createTimeEnd;
}