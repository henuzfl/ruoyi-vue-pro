package cn.iocoder.yudao.module.marketing.controller.admin.scissorliftwplan.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 高机剪叉周计划分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ScissorLiftWplanPageReqVO extends PageParam {

    @Schema(description = "产品线")
    private String productLine;

    @Schema(description = "精准车型")
    private String preciseModel;

    @Schema(description = "产品型号")
    private String productModel;

    @Schema(description = "精准BOM")
    private String preciseBom;

    @Schema(description = "生产日期")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] planDate;

    @Schema(description = "周次")
    private String weekNo;

    @Schema(description = "周起始日期")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] weekStartDate;

    @Schema(description = "周结束日期")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] weekEndDate;

    @Schema(description = "当日数量")
    private Integer dailyQuantity;

    @Schema(description = "车号范围")
    private String carNumberRange;

    @Schema(description = "生产线条类型", example = "2")
    private String productionLineType;

    @Schema(description = "板块")
    private String plate;

    @Schema(description = "导入批次时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] importTime;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}