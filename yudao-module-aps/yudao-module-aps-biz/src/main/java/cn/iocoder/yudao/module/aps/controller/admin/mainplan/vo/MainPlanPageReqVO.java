package cn.iocoder.yudao.module.aps.controller.admin.mainplan.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 主计划分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MainPlanPageReqVO extends PageParam {

    @Schema(description = "生产订单号")
    private String productionOrderNo;

    @Schema(description = "总成物料号")
    private String assemblyMaterialNo;

    @Schema(description = "主物料描述")
    private String mainMaterialDesc;

    @Schema(description = "排产时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] scheduledDate;

    @Schema(description = "排产数量")
    private BigDecimal scheduledQuantity;

    @Schema(description = "生产车间")
    private String productionWorkshop;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}