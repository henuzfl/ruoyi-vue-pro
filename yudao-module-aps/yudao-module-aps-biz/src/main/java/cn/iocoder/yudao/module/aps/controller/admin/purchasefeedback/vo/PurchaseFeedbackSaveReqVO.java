package cn.iocoder.yudao.module.aps.controller.admin.purchasefeedback.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.Date;

@Schema(description = "管理后台 - 采购反馈新增/修改 Request VO")
@Data
public class PurchaseFeedbackSaveReqVO {

    @Schema(description = "主键ID", example = "58")
    private Long id;

    @Schema(description = "订单号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "订单号不能为空")
    private String orderNo;

    @Schema(description = "排产时间")
    private Date scheduleTime;

    @Schema(description = "采购物料")
    private String purchaseMaterial;

    @Schema(description = "采购反馈备注")
    private String feedbackRemark;

}