package cn.iocoder.yudao.module.aps.controller.admin.assempart.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 总成与子件关联表管理分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AssemPartPageReqVO extends PageParam {

    @Schema(description = "总成订单号")
    private String orderNo;

    @Schema(description = "总成数量")
    private BigDecimal quantity;

    @Schema(description = "零部件订单号")
    private String componentOrder;

    @Schema(description = "零部件数量")
    private BigDecimal allocQty;

    @Schema(description = "计划时间（范围）")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] scheduleTime;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;
}