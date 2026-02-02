package cn.iocoder.yudao.module.aps.controller.admin.plan.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 设备调度新增/修改 Request VO")
@Data
public class PlanSaveReqVO {

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
    private LocalDateTime eventTime;

    @Schema(description = "操作序号")
    private Short operationSeq;

    @Schema(description = "工作日期", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "工作日期不能为空")
    private LocalDateTime workDate;

    @Schema(description = "开始时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "开始时间不能为空")
    private LocalDateTime startTime;

    @Schema(description = "结束时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "结束时间不能为空")
    private LocalDateTime endTime;

    @Schema(description = "数量")
    private Short quantity;

    @Schema(description = "尺寸规格")
    private String sizeDimension;

    @Schema(description = "状态")
    private Short state;

    @Schema(description = "持续时间（分钟）")
    private Short durationMinutes;

    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "27902")
    private Short id;

}