package cn.iocoder.yudao.module.buyer.controller.admin.productiontransfer.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - MES转序单信息分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ProductionTransferPageReqVO extends PageParam {

    @Schema(description = "订单号")
    private String orderNo;

    @Schema(description = "物料编码")
    private String materialCode;

    @Schema(description = "物料描述")
    private String materialDesc;

    @Schema(description = "生产调度员")
    private String productionScheduler;

    @Schema(description = "转序发起人")
    private String transferInitiator;

    @Schema(description = "发起日期")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] initiatorDate;

    @Schema(description = "数量")
    private BigDecimal quantity;

    @Schema(description = "转序单号")
    private String transferNo;

    @Schema(description = "计划批次")
    private String batchNo;

    @Schema(description = "签收人")
    private String signer;

    @Schema(description = "签收时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] signTime;

}