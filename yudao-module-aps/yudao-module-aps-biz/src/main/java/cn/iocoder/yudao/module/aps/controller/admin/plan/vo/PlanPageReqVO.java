package cn.iocoder.yudao.module.aps.controller.admin.plan.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 设备调度分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class PlanPageReqVO extends PageParam {

    @Schema(description = "工站ID", example = "16438")
    private String stationId;

    @Schema(description = "设备ID", example = "25285")
    private String deviceId;

    @Schema(description = "设备序号")
    private Short deviceSeq;

    @Schema(description = "操作说明")
    private String operationText;

    @Schema(description = "产品编码")
    private String productCode;

    @Schema(description = "单号")
    private String danhao;

    @Schema(description = "线别")
    private Integer lineNo;

    @Schema(description = "物料编码")
    private String materialNo;

    @Schema(description = "事件时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] eventTime;

    @Schema(description = "操作序号")
    private Short operationSeq;

    @Schema(description = "工作日期")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] workDate;

    @Schema(description = "开始时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] startTime;

    @Schema(description = "结束时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] endTime;

    @Schema(description = "数量")
    private Short quantity;

    @Schema(description = "尺寸规格")
    private String sizeDimension;

    @Schema(description = "状态")
    private Short state;

    @Schema(description = "持续时间（分钟）")
    private Short durationMinutes;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}