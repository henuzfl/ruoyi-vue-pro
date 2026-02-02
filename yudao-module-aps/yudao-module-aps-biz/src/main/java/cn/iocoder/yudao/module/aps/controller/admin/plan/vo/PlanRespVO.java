package cn.iocoder.yudao.module.aps.controller.admin.plan.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 设备调度 Response VO")
@Data
@ExcelIgnoreUnannotated
public class PlanRespVO {

    @Schema(description = "工站ID", example = "16438")
    @ExcelProperty("工站ID")
    private String stationId;

    @Schema(description = "设备ID", example = "25285")
    @ExcelProperty("设备ID")
    private String deviceId;

    @Schema(description = "设备序号")
    @ExcelProperty("设备序号")
    private Short deviceSeq;

    @Schema(description = "操作说明")
    @ExcelProperty("操作说明")
    private String operationText;

    @Schema(description = "产品编码")
    @ExcelProperty("产品编码")
    private String productCode;

    @Schema(description = "单号")
    @ExcelProperty("单号")
    private String danhao;

    @Schema(description = "线别")
    @ExcelProperty("线别")
    private Integer lineNo;

    @Schema(description = "物料编码")
    @ExcelProperty("物料编码")
    private String materialNo;

    @Schema(description = "事件时间")
    @ExcelProperty("事件时间")
    private LocalDateTime eventTime;

    @Schema(description = "操作序号")
    @ExcelProperty("操作序号")
    private Short operationSeq;

    @Schema(description = "工作日期", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("工作日期")
    private LocalDateTime workDate;

    @Schema(description = "开始时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("开始时间")
    private LocalDateTime startTime;

    @Schema(description = "结束时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("结束时间")
    private LocalDateTime endTime;

    @Schema(description = "数量")
    @ExcelProperty("数量")
    private Short quantity;

    @Schema(description = "尺寸规格")
    @ExcelProperty("尺寸规格")
    private String sizeDimension;

    @Schema(description = "状态")
    @ExcelProperty("状态")
    private Short state;

    @Schema(description = "持续时间（分钟）")
    @ExcelProperty("持续时间（分钟）")
    private Short durationMinutes;

    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "27902")
    @ExcelProperty("主键ID")
    private Short id;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}