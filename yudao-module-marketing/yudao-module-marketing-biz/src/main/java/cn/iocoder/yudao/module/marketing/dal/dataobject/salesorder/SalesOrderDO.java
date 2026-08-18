package cn.iocoder.yudao.module.marketing.dal.dataobject.salesorder;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@TableName("sales_order")
@KeySequence("sales_order_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SalesOrderDO extends BaseDO {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    // ===== 业务字段 =====
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