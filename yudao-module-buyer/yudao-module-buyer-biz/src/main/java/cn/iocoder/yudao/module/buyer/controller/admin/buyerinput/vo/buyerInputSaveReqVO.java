package cn.iocoder.yudao.module.buyer.controller.admin.buyerinput.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;

@Schema(description = "管理后台 - 需求输入新增/修改 Request VO")
@Data
public class buyerInputSaveReqVO {

    @Schema(description = "主键编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "5730")
    private Long id;

    @Schema(description = "客户", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "客户不能为空")
    private String customer;

    @Schema(description = "车型")
    private String vehicleModel;

    @Schema(description = "总成物料", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "总成物料不能为空")
    private String assemblyMaterial;

    @Schema(description = "总成数量", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "总成数量不能为空")
    private BigDecimal assemblyQuantity;

    @Schema(description = "需求月份")
    private String demandMonth;

    @Schema(description = "状态（0=正常，1=停用）", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "状态（0=正常，1=停用）不能为空")
    private Short status;

    @Schema(description = "备注", example = "你说的对")
    private String remark;

}