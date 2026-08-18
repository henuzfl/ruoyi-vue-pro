package cn.iocoder.yudao.module.aps.controller.admin.mainplanprogressreport.vo;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class DashboardSupplierRespVO {
    private String supplier;
    private Integer openOrderCount;
    private BigDecimal openQty;
    private Integer affectedParts;
    private Integer affectedOrders;
}