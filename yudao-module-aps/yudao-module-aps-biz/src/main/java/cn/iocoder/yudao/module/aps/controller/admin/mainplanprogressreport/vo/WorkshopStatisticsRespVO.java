package cn.iocoder.yudao.module.aps.controller.admin.mainplanprogressreport.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;

@Schema(description = "管理后台 - 车间统计 Response VO")
@Data
public class WorkshopStatisticsRespVO {

    @Schema(description = "车间名称（总成生产主管）")
    private String workshopName;

    @Schema(description = "总成订单数量")
    private BigDecimal totalOrderQty;

    @Schema(description = "总成订单确认数量")
    private BigDecimal totalConfirmQty;

    @Schema(description = "未完成数量（订单数量 - 确认数量）")
    private BigDecimal totalUnfinishedQty;
}