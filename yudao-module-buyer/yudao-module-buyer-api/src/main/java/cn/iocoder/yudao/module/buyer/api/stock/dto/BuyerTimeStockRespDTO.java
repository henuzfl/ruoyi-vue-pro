package cn.iocoder.yudao.module.buyer.api.stock.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class BuyerTimeStockRespDTO {
    private Long id;
    private String materialNo;
    private BigDecimal quantity;
    private LocalDateTime updateTime;
    // 其他需要的字段
}