package cn.iocoder.yudao.module.aps.controller.admin.matchingresult.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 主计划物料需求匹配分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MatchingResultPageReqVO extends PageParam {

    @Schema(description = "订单号组")
    private List<String> orderNos;   // 改名并改类型

//    @Schema(description = "订单号")
//    private String orderNo;

    @Schema(description = "排产时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private Date[] scheduleTime;

    @Schema(description = "物料编码")
    private String materialCode;

    @Schema(description = "物料描述")
    private String materialDesc;

    @Schema(description = "责任车间（总成）")
    private String workshop;

    @Schema(description = "数量")
    private BigDecimal quantity;

    @Schema(description = "已完成数量")
    private BigDecimal completedQuantity;

    @Schema(description = "库存")
    private BigDecimal stock;

    @Schema(description = "转序（数量）")
    private BigDecimal transferOrder;

    @Schema(description = "零部件订单")
    private String componentOrder;

    @Schema(description = "零部件编码")
    private String componentCode;

    @Schema(description = "零部件描述")
    private String componentDesc;

    @Schema(description = "责任车间（零部件）")
    private String componentWorkshop;

    @Schema(description = "基本开始日期")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private Date[] basicStartDate;

    @Schema(description = "需求数量（零部件）")
    private BigDecimal requiredQuantity;

    @Schema(description = "本次未完成数量")
    private BigDecimal unfinishedQuantity;

    @Schema(description = "采购物料")
    private String purchaseMaterial;

    @Schema(description = "采购物料描述")
    private String purchaseMaterialDesc;

    @Schema(description = "需求数量（采购）")
    private BigDecimal purchaseRequiredQty;

    @Schema(description = "已配送数量")
    private BigDecimal deliveredQuantity;

    @Schema(description = "待配送数量")
    private BigDecimal toDeliverQuantity;

    @Schema(description = "齐套数量")
    private BigDecimal kitQty;

    @Schema(description = "物料齐套数量")
    private BigDecimal kitQtySingle;

    @Schema(description = "采购订单")
    private String purchaseOrder;

    @Schema(description = "行号")
    private Long lineNumber;

    @Schema(description = "下单日期")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private Date[] orderDate;

    @Schema(description = "要求交货日期")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private Date[] requiredDeliveryDate;

    @Schema(description = "实际到货日期")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private Date[] actualArrivalDate;

    @Schema(description = "供应商名称", example = "张三")
    private String supplierName;

    @Schema(description = "未清订单数量")
    private BigDecimal openOrderQuantity;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private Date[] createTime;

    @Schema(description = "大小/尺寸")
    private String sizeDimension;

}