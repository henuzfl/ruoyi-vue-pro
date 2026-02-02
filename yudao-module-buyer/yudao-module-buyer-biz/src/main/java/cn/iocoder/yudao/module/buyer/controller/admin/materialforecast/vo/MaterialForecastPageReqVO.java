package cn.iocoder.yudao.module.buyer.controller.admin.materialforecast.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 营销材料备料预测分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MaterialForecastPageReqVO extends PageParam {

    @Schema(description = "客户名称", example = "赵六")
    private String customerName;

    @Schema(description = "产品线/分类")
    private String productLine;

    @Schema(description = "吨位/米段")
    private String tonnageSegment;

    @Schema(description = "车型")
    private String vehicleModel;

    @Schema(description = "预测月份（格式：YYYY-MM）")
    private String forecastMonth;

    @Schema(description = "预测数量")
    private BigDecimal forecastQuantity;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}