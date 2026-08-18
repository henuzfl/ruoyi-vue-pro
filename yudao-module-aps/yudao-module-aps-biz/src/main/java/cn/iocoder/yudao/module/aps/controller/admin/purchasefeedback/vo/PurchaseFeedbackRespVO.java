package cn.iocoder.yudao.module.aps.controller.admin.purchasefeedback.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Schema(description = "管理后台 - 采购反馈 Response VO")
@Data
@ExcelIgnoreUnannotated
public class PurchaseFeedbackRespVO {

    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "58")
    private BigDecimal id;

    @Schema(description = "订单号")
    @ExcelProperty("订单号")
    private String orderNo;

    @Schema(description = "排产时间")
    @ExcelProperty("排产时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat("yyyy-MM-dd")
    private Date scheduleTime;

    @Schema(description = "采购物料")
    @ExcelProperty("采购物料")
    private String purchaseMaterial;

    @Schema(description = "采购反馈备注")
    @ExcelProperty("采购反馈备注")
    private String feedbackRemark;

    // BaseDO 中的审计字段不需要在 RespVO 中展示，但若需可自行添加
}