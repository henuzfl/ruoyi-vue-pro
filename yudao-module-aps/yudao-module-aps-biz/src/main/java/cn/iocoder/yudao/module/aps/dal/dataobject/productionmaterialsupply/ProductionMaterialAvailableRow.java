package cn.iocoder.yudao.module.aps.dal.dataobject.productionmaterialsupply;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductionMaterialAvailableRow {
    private String materialNo;
    private BigDecimal stockQuantity;
    private BigDecimal productionTransit;
}
