package cn.iocoder.yudao.module.aps.controller.admin.mainplanprogressreport.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class DashboardOrderRespVO {
    private String orderNo;
    private String materialCode;
    private String materialDesc;
    private String workshop;
    private BigDecimal quantity;
    private BigDecimal totalQty;
    private BigDecimal completedQuantity;
    private BigDecimal transferOrder;
    private Integer shortageCount;      // 缺料零件种数
    private String warning;             // 预警信息
    private Date scheduleTime;   // 排产时间
}