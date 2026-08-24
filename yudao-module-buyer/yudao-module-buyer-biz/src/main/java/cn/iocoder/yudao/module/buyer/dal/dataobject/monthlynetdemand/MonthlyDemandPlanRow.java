package cn.iocoder.yudao.module.buyer.dal.dataobject.monthlynetdemand;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class MonthlyDemandPlanRow {
    private String productModel;
    private String seqNo2026;
    private BigDecimal unitQuantity;
}
