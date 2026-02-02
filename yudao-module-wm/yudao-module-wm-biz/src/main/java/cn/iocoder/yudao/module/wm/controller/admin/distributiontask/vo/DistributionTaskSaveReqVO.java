package cn.iocoder.yudao.module.wm.controller.admin.distributiontask.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 配送任务下发新增/修改 Request VO")
@Data
public class DistributionTaskSaveReqVO {

    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "8761")
    private BigDecimal id;

    @Schema(description = "订单号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "订单号不能为空")
    private String orderNo;

    @Schema(description = "预留号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "预留号不能为空")
    private String reservationNo;

    @Schema(description = "预留行号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "预留行号不能为空")
    private String reservationLineNo;

    @Schema(description = "工序号")
    private String processNo;

    @Schema(description = "配送任务单号")
    private String distributionTaskNo;

    @Schema(description = "产品编码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "产品编码不能为空")
    private String productCode;

    @Schema(description = "产品名称", example = "张三")
    private String productName;

    @Schema(description = "物料号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "物料号不能为空")
    private String materialNo;

    @Schema(description = "物料名称", example = "张三")
    private String materialName;

    @Schema(description = "数量", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "数量不能为空")
    private BigDecimal quantity;

    @Schema(description = "单位")
    private String unit;

    @Schema(description = "任务状态（0=待下发，1=已下发，2=配送中，3=已完成，4=已取消）", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "任务状态（0=待下发，1=已下发，2=配送中，3=已完成，4=已取消）不能为空")
    private Short taskStatus;

    @Schema(description = "配送状态（0=未配送，1=已配送，2=已接收，3=已退回）", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @NotNull(message = "配送状态（0=未配送，1=已配送，2=已接收，3=已退回）不能为空")
    private Short distributionStatus;

    @Schema(description = "配送时间")
    private LocalDateTime distributionTime;

    @Schema(description = "配送操作人")
    private String distributionOperator;

    @Schema(description = "配送地点")
    private String deliveryLocation;

}