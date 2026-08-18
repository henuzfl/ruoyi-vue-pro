package cn.iocoder.yudao.module.aps.controller.admin.matchingresult.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;

@Schema(description = "管理后台 - 主计划物料需求匹配新增/修改 Request VO")
@Data
public class MatchingResultSaveReqVO {

    @Schema(description = "主键ID（雪花算法生成）", requiredMode = Schema.RequiredMode.REQUIRED, example = "58")
    private BigDecimal id;

    @Schema(description = "订单号")
    private String orderNo;

    @Schema(description = "排产时间")
    private Date scheduleTime;

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
    private Date basicStartDate;

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
    private Date orderDate;

    @Schema(description = "要求交货日期")
    private Date requiredDeliveryDate;

    @Schema(description = "实际到货日期")
    private Date actualArrivalDate;

    @Schema(description = "供应商名称", example = "张三")
    private String supplierName;

    @Schema(description = "未清订单数量")
    private BigDecimal openOrderQuantity;

    @Schema(description = "分配需求数量")
    private BigDecimal allocatedRequiredQty;

    @Schema(description = "剩余需采购数量")
    private BigDecimal remainRequiredQty;

    @Schema(description = "大小/尺寸")
    private String sizeDimension;

}