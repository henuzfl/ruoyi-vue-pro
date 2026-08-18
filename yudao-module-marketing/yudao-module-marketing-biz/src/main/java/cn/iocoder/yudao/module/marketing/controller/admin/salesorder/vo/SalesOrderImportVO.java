package cn.iocoder.yudao.module.marketing.controller.admin.salesorder.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class SalesOrderImportVO {

    @ExcelProperty(index = 0)
    private String salesOrganization;

    @ExcelProperty(index = 1)
    private String salesDepartment;

    @ExcelProperty(index = 2)
    private String soldToParty;

    @ExcelProperty(index = 3)
    private String soldToPartyName;

    @ExcelProperty(index = 4)
    private String salesRegion;

    @ExcelProperty(index = 5)
    private String orderDateStr;

    @ExcelProperty(index = 6)
    private String orderType;

    @ExcelProperty(index = 7)
    private String approvalStatus;

    @ExcelProperty(index = 8)
    private String orderNumber;

    @ExcelProperty(index = 9)
    private Integer orderItem;

    @ExcelProperty(index = 10)
    private String accountSettingGroup;

    @ExcelProperty(index = 11)
    private String materialCode;

    @ExcelProperty(index = 12)
    private String materialDescription;

    @ExcelProperty(index = 13)
    private String earliestDeliveryDateStr;

    @ExcelProperty(index = 14)
    private BigDecimal orderQuantity;

    @ExcelProperty(index = 15)
    private String unit;

    @ExcelProperty(index = 16)
    private String shipToParty;

    @ExcelProperty(index = 17)
    private String shipToPartyName;

    @ExcelProperty(index = 18)
    private String unloadingPoint;

    @ExcelProperty(index = 19)
    private String priceListType;

    @ExcelProperty(index = 20)
    private String pricingDateStr;

    @ExcelProperty(index = 21)
    private BigDecimal unitPrice;

    @ExcelProperty(index = 22)
    private BigDecimal netSalesValue;

    @ExcelProperty(index = 23)
    private BigDecimal subtotal;

    @ExcelProperty(index = 24)
    private BigDecimal taxAmount;

    @ExcelProperty(index = 25)
    private BigDecimal latestSalesPrice;

    @ExcelProperty(index = 26)
    private BigDecimal priceExclTax;

    @ExcelProperty(index = 27)
    private BigDecimal latestAmount;

    @ExcelProperty(index = 28)
    private BigDecimal netWeight;

    @ExcelProperty(index = 29)
    private BigDecimal grossWeight;

    @ExcelProperty(index = 30)
    private String weightUnit;

    @ExcelProperty(index = 31)
    private String plant;

    @ExcelProperty(index = 32)
    private String shippingPoint;

    @ExcelProperty(index = 33)
    private String storageLocation;

    @ExcelProperty(index = 34)
    private BigDecimal deliveredQuantity;

    @ExcelProperty(index = 35)
    private BigDecimal shippedQuantity;

    @ExcelProperty(index = 36)
    private BigDecimal invoicedQuantity;

    @ExcelProperty(index = 37)
    private String deliveryStatus;

    @ExcelProperty(index = 38)
    private String creatorName;

    @ExcelProperty(index = 39)
    private String creationDateStr;

    @ExcelProperty(index = 40)
    private String deliveryBlock;

    @ExcelProperty(index = 41)
    private String invoiceBlock;

    @ExcelProperty(index = 42)
    private String orderReason;

    @ExcelProperty(index = 43)
    private String rejectionReason;

    @ExcelProperty(index = 44)
    private String invoiceType;

    @ExcelProperty(index = 45)
    private String materialGroup;
}