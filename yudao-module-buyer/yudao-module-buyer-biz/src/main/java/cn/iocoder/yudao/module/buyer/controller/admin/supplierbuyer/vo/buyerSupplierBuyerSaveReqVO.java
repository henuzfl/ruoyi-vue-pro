package cn.iocoder.yudao.module.buyer.controller.admin.supplierbuyer.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;

@Schema(description = "管理后台 - 物料供应商采购员对应新增/修改 Request VO")
@Data
public class buyerSupplierBuyerSaveReqVO {

    @Schema(description = "主键编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "6846")
    private Long id;

    @Schema(description = "物料编号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "物料编号不能为空")
    private String materialNo;

    @Schema(description = "物料描述")
    private String materialDesc;

    @Schema(description = "供应商", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "供应商不能为空")
    private String supplier;

    @Schema(description = "采购员")
    private String buyer;

    @Schema(description = "采购组")
    private String procurementGroup;

    @Schema(description = "物料类型", example = "2")
    private String materialType;

    @Schema(description = "物料分类")
    private String materialCategory;

    @Schema(description = "采购类型", example = "2")
    private String procurementType;

    @Schema(description = "状态（0=正常，1=停用）", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "状态（0=正常，1=停用）不能为空")
    private Integer status;

    @Schema(description = "备注", example = "随便")
    private String remark;

}