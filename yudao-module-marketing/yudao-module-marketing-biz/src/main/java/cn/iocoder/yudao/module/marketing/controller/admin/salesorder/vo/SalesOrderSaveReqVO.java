package cn.iocoder.yudao.module.marketing.controller.admin.salesorder.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Schema(description = "销售订单新增/修改 Request VO")
@Data
public class SalesOrderSaveReqVO {

    @Schema(description = "主键")
    private Long id;

    private String salesOrganization;
    private String salesDepartment;
    private String soldToParty;
    private String soldToPartyName;
    private String salesRegion;
    private Date orderDate;
    private String orderType;
    private String approvalStatus;
    private String orderNumber;
    private Integer orderItem;
    private String accountSettingGroup;
    private String materialCode;
    private String materialDescription;
    private Date earliestDeliveryDate;
    private BigDecimal orderQuantity;
    private String unit;
    private String shipToParty;
    private String shipToPartyName;
    private String unloadingPoint;
    private String priceListType;
    private Date pricingDate;
    private BigDecimal unitPrice;
    private BigDecimal netSalesValue;
    private BigDecimal subtotal;
    private BigDecimal taxAmount;
    private BigDecimal latestSalesPrice;
    private BigDecimal priceExclTax;
    private BigDecimal latestAmount;
    private BigDecimal netWeight;
    private BigDecimal grossWeight;
    private String weightUnit;
    private String plant;
    private String shippingPoint;
    private String storageLocation;
    private BigDecimal deliveredQuantity;
    private BigDecimal shippedQuantity;
    private BigDecimal invoicedQuantity;
    private String deliveryStatus;
    private String creatorName;
    private Date creationDate;
    private String deliveryBlock;
    private String invoiceBlock;
    private String orderReason;
    private String rejectionReason;
    private String invoiceType;
    private String materialGroup;
}