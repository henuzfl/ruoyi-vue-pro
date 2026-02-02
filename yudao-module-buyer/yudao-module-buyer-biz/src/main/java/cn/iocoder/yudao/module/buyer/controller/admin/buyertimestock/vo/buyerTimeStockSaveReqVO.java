package cn.iocoder.yudao.module.buyer.controller.admin.buyertimestock.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;

@Schema(description = "管理后台 - 实时库存新增/修改 Request VO")
@Data
public class buyerTimeStockSaveReqVO {

    @Schema(description = "主键编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "5012")
    private Long id;

    @Schema(description = "物料编号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "物料编号不能为空")
    private String materialNo;

    @Schema(description = "物料描述")
    private String materialDesc;

    @Schema(description = "库存地点", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "库存地点不能为空")
    private String stockLocation;

    @Schema(description = "库存数量", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "库存数量不能为空")
    private BigDecimal stockQuantity;

    @Schema(description = "可用数量")
    private BigDecimal availableQuantity;

    @Schema(description = "状态（0=正常，1=停用）", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "状态（0=正常，1=停用）不能为空")
    private Integer status;

    @Schema(description = "备注", example = "你说的对")
    private String remark;

}