package cn.iocoder.yudao.module.wm.controller.admin.orderdemand.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 订单追溯需求分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class OrderDemandPageReqVO extends PageParam {

    @Schema(description = "订单号")
    private String orderNo;

    @Schema(description = "追溯需求号")
    private String traceDemandNo;

    @Schema(description = "物料编码")
    private String materialNo;

    @Schema(description = "物料描述", example = "你猜")
    private String materialDescription;

    @Schema(description = "需求数量")
    private BigDecimal demandQuantity;

    @Schema(description = "出库累计数")
    private BigDecimal outboundAccumulated;

    @Schema(description = "未清数量")
    private BigDecimal openQuantity;

    @Schema(description = "状态（0=待处理 1=部分完成 2=已完成）", example = "1")
    private Short status;

    @Schema(description = "备注", example = "随便")
    private String remark;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}