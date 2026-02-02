package cn.iocoder.yudao.module.buyer.controller.admin.buyerstock.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;

@Schema(description = "管理后台 - 供应商库存新增/修改 Request VO")
@Data
public class buyerStockSaveReqVO {

    @Schema(description = "主键编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "25646")
    private Long id;

    @Schema(description = "物料编号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "物料编号不能为空")
    private String materialNo;

    @Schema(description = "物料描述")
    private String materialDesc;

    @Schema(description = "供应商")
    private String supplier;

    @Schema(description = "库存地点")
    private String stockLocation;

    @Schema(description = "库存数量")
    private BigDecimal stockQuantity;

    @Schema(description = "备料数量")
    private BigDecimal preparedQuantity;

    @Schema(description = "库存月份")
    private String stockMonth;

    @Schema(description = "状态（0=正常，1=停用）", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "状态（0=正常，1=停用）不能为空")
    private Integer status;

    @Schema(description = "备注", example = "随便")
    private String remark;

}