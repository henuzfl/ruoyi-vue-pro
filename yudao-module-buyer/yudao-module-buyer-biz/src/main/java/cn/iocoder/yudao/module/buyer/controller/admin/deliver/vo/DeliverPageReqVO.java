package cn.iocoder.yudao.module.buyer.controller.admin.deliver.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 配送与采购报表分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DeliverPageReqVO extends PageParam {

    @Schema(description = "工厂")
    private String plant;

    @Schema(description = "配送单号")
    private String deliveryOrderNo;

    @Schema(description = "配送日期")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] deliveryDate;

    @Schema(description = "创建日期（业务）")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] creationDate;

    @Schema(description = "创建时间（业务）")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private String[] creationTime;

    @Schema(description = "创建人（业务）")
    private String createdBy;

    @Schema(description = "最后更新日期（业务）")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] lastUpdateDate;

    @Schema(description = "最后更新时间（业务）")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private String[] lastUpdateTime;

    @Schema(description = "最后更新人（业务）")
    private String lastUpdatedBy;

    @Schema(description = "生产订单号")
    private String partOrderNo;

    @Schema(description = "生产调度员")
    private String productionWorkshop;

    @Schema(description = "主物料编码")
    private String partMatCode;

    @Schema(description = "主物料描述")
    private String partMatDesc;

    @Schema(description = "预留号")
    private String reservationNo;

    @Schema(description = "预留项目")
    private Long reservationItem;

    @Schema(description = "应发数量")
    private BigDecimal plannedIssueQty;

    @Schema(description = "已发数量")
    private BigDecimal deliveredQty;

    @Schema(description = "未配送数量")
    private BigDecimal undeliveredQty;

    @Schema(description = "物料号")
    private String buyerMaterialNo;

    @Schema(description = "旧物料号")
    private String oldMaterialNo;

    @Schema(description = "物料描述")
    private String buyerMaterialDesc;

    @Schema(description = "供应商编码(配送单)")
    private String deliverySupplierCode;

    @Schema(description = "供应商描述(配送单)", example = "王五")
    private String deliverySupplierName;

    @Schema(description = "库存是否满足")
    private String stockSufficientFlag;

    @Schema(description = "总库存")
    private BigDecimal totalStockQty;

    @Schema(description = "本次消耗库存")
    private BigDecimal currentStockConsumeQty;

    @Schema(description = "库存地点(配送)")
    private String deliveryStorageLoc;

    @Schema(description = "采购是否满足")
    private String poSufficientFlag;

    @Schema(description = "采购订单号")
    private String buyerOrderNo;

    @Schema(description = "采购项目")
    private Long lineItem;

    @Schema(description = "需求跟踪号")
    private String requirementTrackingNo;

    @Schema(description = "采购订单数量")
    private BigDecimal orderQty;

    @Schema(description = "采购订单未收货数量")
    private BigDecimal openQty;

    @Schema(description = "本次交货数量")
    private BigDecimal receivedQty;

    @Schema(description = "采购订单交货日期")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] poDeliveryDate;

    @Schema(description = "供应商(采购订单)")
    private String poSupplierCode;

    @Schema(description = "供应商描述(采购订单)")
    private String supplierDesc;

    @Schema(description = "采购组")
    private String buyerPurchasingGroup;

    @Schema(description = "下单采购员")
    private String orderingBuyer;

    @Schema(description = "交货采购员")
    private String deliveryBuyer;

    @Schema(description = "创建时间（系统）")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}