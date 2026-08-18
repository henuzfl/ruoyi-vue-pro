package cn.iocoder.yudao.module.aps.controller.admin.mainplanprogressreport.vo;

import lombok.Data;
import java.time.LocalDateTime;


import java.math.BigDecimal;

@Data
public class OrderShortageRespVO {
    private String componentCode;
    private String componentDesc;
    private BigDecimal requiredQty;
    private BigDecimal shortage;
    private String expectedDate;        // 预计齐套日
    private BigDecimal inTransit;       // 在途数量
    private LocalDateTime scheduleTime;
}