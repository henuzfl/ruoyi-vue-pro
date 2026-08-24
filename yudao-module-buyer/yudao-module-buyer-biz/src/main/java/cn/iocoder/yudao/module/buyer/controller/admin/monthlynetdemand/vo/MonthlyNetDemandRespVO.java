package cn.iocoder.yudao.module.buyer.controller.admin.monthlynetdemand.vo;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class MonthlyNetDemandRespVO {
    private String planMonth;
    private String hostCode;
    private String materialNo;
    private String materialName;
    private Boolean mapped;
    private BigDecimal totalDemand;
    private BigDecimal sapAvailableStock;
    private BigDecimal overseasAvailableStock;
    private BigDecimal inProcessOrderQuantity;
    private BigDecimal netDemand;
}
