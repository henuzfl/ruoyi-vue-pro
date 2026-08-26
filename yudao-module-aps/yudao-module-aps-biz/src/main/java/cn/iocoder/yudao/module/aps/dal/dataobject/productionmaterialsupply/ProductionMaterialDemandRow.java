package cn.iocoder.yudao.module.aps.dal.dataobject.productionmaterialsupply;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class ProductionMaterialDemandRow {
    private String productionOrderNo;
    private String assemblyMaterialNo;
    private String assemblyMaterialDesc;
    private BigDecimal assemblyDemandQuantity;
    private Date scheduledDate;
    private String componentMaterialNo;
    private String componentMaterialDesc;
    private BigDecimal demandQuantity;
    private BigDecimal investedQuantity;
    private String procurementType;
}
