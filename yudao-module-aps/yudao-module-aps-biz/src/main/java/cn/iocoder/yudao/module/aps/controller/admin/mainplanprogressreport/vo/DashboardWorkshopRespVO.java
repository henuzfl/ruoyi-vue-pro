package cn.iocoder.yudao.module.aps.controller.admin.mainplanprogressreport.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DashboardWorkshopRespVO {
    private String workshop;
    private Integer orderCount;
    private BigDecimal planQty;
    private BigDecimal completeQty;
    private BigDecimal completionRate;
    private Integer shortageOrders;
}