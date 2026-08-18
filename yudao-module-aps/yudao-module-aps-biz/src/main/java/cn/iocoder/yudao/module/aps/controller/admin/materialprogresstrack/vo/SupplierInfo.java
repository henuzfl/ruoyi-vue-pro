package cn.iocoder.yudao.module.aps.controller.admin.materialprogresstrack.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Schema(description = "供应商供应信息")
public class SupplierInfo {
    @Schema(description = "供应商名称")
    private String supplierName;

    @Schema(description = "供应数量")
    private BigDecimal supplyQty;
}