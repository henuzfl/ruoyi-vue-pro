package cn.iocoder.yudao.module.aps.controller.admin.purchasefeedback.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 采购反馈分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class PurchaseFeedbackPageReqVO extends PageParam {

    @Schema(description = "订单号")
    private String orderNo;

    @Schema(description = "采购物料")
    private String purchaseMaterial;

    @Schema(description = "采购反馈备注（模糊匹配）")
    private String feedbackRemark;

    @Schema(description = "排产时间范围")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private Date[] scheduleTime;

}