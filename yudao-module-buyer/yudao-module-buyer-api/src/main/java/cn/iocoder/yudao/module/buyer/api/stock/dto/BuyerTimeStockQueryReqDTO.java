package cn.iocoder.yudao.module.buyer.api.stock.dto;

import lombok.Data;

@Data
public class BuyerTimeStockQueryReqDTO {
    private Integer pageNo;
    private Integer pageSize;
    private String materialNo;
    private String factory;
}