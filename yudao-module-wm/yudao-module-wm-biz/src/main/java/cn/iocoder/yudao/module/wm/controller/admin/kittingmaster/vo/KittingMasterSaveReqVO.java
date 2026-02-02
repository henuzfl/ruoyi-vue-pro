package cn.iocoder.yudao.module.wm.controller.admin.kittingmaster.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;

@Schema(description = "管理后台 - 主计划新增/修改 Request VO")
@Data
public class KittingMasterSaveReqVO {

    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "9944")
    private BigDecimal id;

    @Schema(description = "生产订单号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "生产订单号不能为空")
    private String productionOrderNo;

    @Schema(description = "总成物料号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "总成物料号不能为空")
    private String assemblyMaterialNo;

    @Schema(description = "主物料描述")
    private String mainMaterialDesc;

    @Schema(description = "排产数量")
    private BigDecimal scheduledQuantity;

    @Schema(description = "生产车间")
    private String productionWorkshop;

}