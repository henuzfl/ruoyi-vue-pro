package cn.iocoder.yudao.module.marketing.controller.admin.salesorder.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Schema(description = "销售订单 Response VO")
@Data
public class SalesOrderRespVO {

    @Schema(description = "主键")
    @ExcelProperty("主键")
    private Long id;

    @Schema(description = "销售组织")
    @ExcelProperty("销售组织")
    private String salesOrganization;

    @Schema(description = "销售部门")
    @ExcelProperty("销售部门")
    private String salesDepartment;

    @Schema(description = "售达方编码")
    @ExcelProperty("售达方编码")
    private String soldToParty;

    @Schema(description = "售达方名称")
    @ExcelProperty("售达方名称")
    private String soldToPartyName;

    @Schema(description = "销售地区")
    @ExcelProperty("销售地区")
    private String salesRegion;

    @Schema(description = "订单日期")
    @ExcelProperty("订单日期")
    private Date orderDate;

    @Schema(description = "订单类型")
    @ExcelProperty("订单类型")
    private String orderType;

    @Schema(description = "审批状态")
    @ExcelProperty("审批状态")
    private String approvalStatus;

    @Schema(description = "订单编号")
    @ExcelProperty("订单编号")
    private String orderNumber;

    @Schema(description = "行项目")
    @ExcelProperty("行项目")
    private Integer orderItem;

    @Schema(description = "科目设置组")
    @ExcelProperty("科目设置组")
    private String accountSettingGroup;

    @Schema(description = "物料编码")
    @ExcelProperty("物料编码")
    private String materialCode;

    @Schema(description = "物料描述")
    @ExcelProperty("物料描述")
    private String materialDescription;

    @Schema(description = "最早交货日期")
    @ExcelProperty("最早交货日期")
    private Date earliestDeliveryDate;

    @Schema(description = "订单数量")
    @ExcelProperty("订单数量")
    private BigDecimal orderQuantity;

    @Schema(description = "计量单位")
    @ExcelProperty("计量单位")
    private String unit;

    @Schema(description = "送达方编码")
    @ExcelProperty("送达方编码")
    private String shipToParty;

    @Schema(description = "送达方名称")
    @ExcelProperty("送达方名称")
    private String shipToPartyName;

    @Schema(description = "卸货点")
    @ExcelProperty("卸货点")
    private String unloadingPoint;

    @Schema(description = "价格清单类型")
    @ExcelProperty("价格清单类型")
    private String priceListType;

    @Schema(description = "定价日期")
    @ExcelProperty("定价日期")
    private Date pricingDate;

    @Schema(description = "单价")
    @ExcelProperty("单价")
    private BigDecimal unitPrice;

    @Schema(description = "销售净值")
    @ExcelProperty("销售净值")
    private BigDecimal netSalesValue;

    @Schema(description = "小计（含税）")
    @ExcelProperty("小计")
    private BigDecimal subtotal;

    @Schema(description = "税额")
    @ExcelProperty("税额")
    private BigDecimal taxAmount;

    @Schema(description = "最新销售价格")
    @ExcelProperty("最新销售价格")
    private BigDecimal latestSalesPrice;

    @Schema(description = "不含税价")
    @ExcelProperty("不含税价")
    private BigDecimal priceExclTax;

    @Schema(description = "最新金额")
    @ExcelProperty("最新金额")
    private BigDecimal latestAmount;

    @Schema(description = "净重")
    @ExcelProperty("净重")
    private BigDecimal netWeight;

    @Schema(description = "毛重")
    @ExcelProperty("毛重")
    private BigDecimal grossWeight;

    @Schema(description = "重量单位")
    @ExcelProperty("重量单位")
    private String weightUnit;

    @Schema(description = "工厂")
    @ExcelProperty("工厂")
    private String plant;

    @Schema(description = "装运点")
    @ExcelProperty("装运点")
    private String shippingPoint;

    @Schema(description = "库存地点")
    @ExcelProperty("库存地点")
    private String storageLocation;

    @Schema(description = "已交货数量")
    @ExcelProperty("已交货数量")
    private BigDecimal deliveredQuantity;

    @Schema(description = "已发货数量")
    @ExcelProperty("已发货数量")
    private BigDecimal shippedQuantity;

    @Schema(description = "已开票数量")
    @ExcelProperty("已开票数量")
    private BigDecimal invoicedQuantity;

    @Schema(description = "交货状态")
    @ExcelProperty("交货状态")
    private String deliveryStatus;

    @Schema(description = "创建者（业务）")
    @ExcelProperty("创建者")
    private String creatorName;

    @Schema(description = "创建日期（业务）")
    @ExcelProperty("创建日期")
    private Date creationDate;

    @Schema(description = "交货冻结状态")
    @ExcelProperty("交货冻结状态")
    private String deliveryBlock;

    @Schema(description = "开票冻结状态")
    @ExcelProperty("开票冻结状态")
    private String invoiceBlock;

    @Schema(description = "订单原因")
    @ExcelProperty("订单原因")
    private String orderReason;

    @Schema(description = "拒绝原因")
    @ExcelProperty("拒绝原因")
    private String rejectionReason;

    @Schema(description = "发票类型")
    @ExcelProperty("发票类型")
    private String invoiceType;

    @Schema(description = "物料组")
    @ExcelProperty("物料组")
    private String materialGroup;
}