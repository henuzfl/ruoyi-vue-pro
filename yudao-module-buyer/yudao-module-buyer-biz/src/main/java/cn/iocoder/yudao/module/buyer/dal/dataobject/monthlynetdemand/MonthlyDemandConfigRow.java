package cn.iocoder.yudao.module.buyer.dal.dataobject.monthlynetdemand;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class MonthlyDemandConfigRow {
    private String vehicleModel;
    private String seqNo2026;
    private String hostCode;
    private String configMaterialNo;
    private BigDecimal unitRequiredQuantity;
}
