package cn.iocoder.yudao.module.buyer.dal.dataobject.monthlynetdemand;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class MonthlyDemandQuantityRow {
    /** 数量来源：SAP、OVERSEAS、OPEN_ORDER。 */
    private String sourceType;
    private String materialNo;
    private BigDecimal quantity;
}
