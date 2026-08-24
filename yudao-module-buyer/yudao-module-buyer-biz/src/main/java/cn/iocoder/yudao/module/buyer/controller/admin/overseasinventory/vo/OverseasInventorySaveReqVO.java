package cn.iocoder.yudao.module.buyer.controller.admin.overseasinventory.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Schema(description = "管理后台 - 驻外库存新增/修改 Request VO")
@Data
public class OverseasInventorySaveReqVO {
    @Schema(description = "主键")
    private BigDecimal id;
    private String warehouse;
    private String ownerCode;
    private String supplierCode;
    private String supplierName;
    private String itemCode;
    private String itemName;
    private String itemSpecification;
    private Long inventoryQuantity;
    private Long occupiedQuantity;
    private Long availableQuantity;
    private Long frozenQuantity;
}
