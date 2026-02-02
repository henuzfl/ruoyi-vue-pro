package cn.iocoder.yudao.module.wm.controller.admin.distributiontask.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 配送任务下发 Response VO")
@Data
@ExcelIgnoreUnannotated
public class DistributionTaskRespVO {

    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "8761")
    @ExcelProperty("主键ID")
    private BigDecimal id;

    @Schema(description = "订单号", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("订单号")
    private String orderNo;

    @Schema(description = "预留号", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("预留号")
    private String reservationNo;

    @Schema(description = "预留行号", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("预留行号")
    private String reservationLineNo;

    @Schema(description = "工序号")
    @ExcelProperty("工序号")
    private String processNo;

    @Schema(description = "配送任务单号")
    @ExcelProperty("配送任务单号")
    private String distributionTaskNo;

    @Schema(description = "产品编码", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("产品编码")
    private String productCode;

    @Schema(description = "产品名称", example = "张三")
    @ExcelProperty("产品名称")
    private String productName;

    @Schema(description = "物料号", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("物料号")
    private String materialNo;

    @Schema(description = "物料名称", example = "张三")
    @ExcelProperty("物料名称")
    private String materialName;

    @Schema(description = "数量", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("数量")
    private BigDecimal quantity;

    @Schema(description = "单位")
    @ExcelProperty("单位")
    private String unit;

    @Schema(description = "任务状态（0=待下发，1=已下发，2=配送中，3=已完成，4=已取消）", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("任务状态（0=待下发，1=已下发，2=配送中，3=已完成，4=已取消）")
    private Short taskStatus;

    @Schema(description = "配送状态（0=未配送，1=已配送，2=已接收，3=已退回）", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @ExcelProperty("配送状态（0=未配送，1=已配送，2=已接收，3=已退回）")
    private Short distributionStatus;

    @Schema(description = "配送时间")
    @ExcelProperty("配送时间")
    private LocalDateTime distributionTime;

    @Schema(description = "配送操作人")
    @ExcelProperty("配送操作人")
    private String distributionOperator;

    @Schema(description = "配送地点")
    @ExcelProperty("配送地点")
    private String deliveryLocation;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}