package cn.iocoder.yudao.module.wm.controller.admin.orderdemand.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;

@Schema(description = "管理后台 - 订单追溯需求新增/修改 Request VO")
@Data
public class OrderDemandSaveReqVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "19169")
    private BigDecimal id;

    @Schema(description = "订单号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "订单号不能为空")
    private String orderNo;

    @Schema(description = "追溯需求号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "追溯需求号不能为空")
    private String traceDemandNo;

    @Schema(description = "物料编码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "物料编码不能为空")
    private String materialNo;

    @Schema(description = "物料描述", example = "你猜")
    private String materialDescription;

    @Schema(description = "需求数量", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "需求数量不能为空")
    private BigDecimal demandQuantity;

    @Schema(description = "出库累计数", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "出库累计数不能为空")
    private BigDecimal outboundAccumulated;

    @Schema(description = "未清数量", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "未清数量不能为空")
    private BigDecimal openQuantity;

    @Schema(description = "状态（0=待处理 1=部分完成 2=已完成）", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "状态（0=待处理 1=部分完成 2=已完成）不能为空")
    private Short status;

    @Schema(description = "备注", example = "随便")
    private String remark;

}