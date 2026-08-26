package cn.iocoder.yudao.module.aps.dal.dataobject.productionmaterialsupply;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class ProductionMaterialPurchaseRow {
    private String productionOrderNo;
    private String materialNo;
    private String materialDesc;
    private Date orderDate;
    private String buyerOrderNo;
    private Long lineItem;
    private BigDecimal openQuantity;
    private Date requiredArrivalDate;
    private String supplierDesc;
}
