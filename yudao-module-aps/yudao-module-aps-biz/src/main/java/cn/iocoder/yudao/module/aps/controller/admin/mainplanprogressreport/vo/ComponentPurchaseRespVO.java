package cn.iocoder.yudao.module.aps.controller.admin.mainplanprogressreport.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ComponentPurchaseRespVO {
    private String purchaseOrder;
    private String supplier;
    private LocalDateTime orderDate;
    private LocalDateTime requiredDate;
    private BigDecimal orderQty;
    private BigDecimal receivedQty;
    private BigDecimal openQty;
}