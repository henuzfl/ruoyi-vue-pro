package cn.iocoder.yudao.module.aps.controller.admin.mainplanprogressreport.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;

@Schema(description = "管理后台 - 月度整体统计 Response VO")
@Data
public class MonthlyStatisticsRespVO {

    @Schema(description = "已完成总数量")
    private BigDecimal totalCompleted;

    @Schema(description = "未完成总数量")
    private BigDecimal totalUnfinished;
}