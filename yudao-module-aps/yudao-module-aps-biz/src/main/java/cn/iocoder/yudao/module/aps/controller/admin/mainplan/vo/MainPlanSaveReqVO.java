package cn.iocoder.yudao.module.aps.controller.admin.mainplan.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 主计划新增/修改 Request VO")
@Data
public class MainPlanSaveReqVO {

    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "16649")
    private BigDecimal id;

    @Schema(description = "生产订单号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "生产订单号不能为空")
    private String productionOrderNo;

    @Schema(description = "总成物料号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "总成物料号不能为空")
    private String assemblyMaterialNo;

    @Schema(description = "主物料描述")
    private String mainMaterialDesc;

    @Schema(description = "排产时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "排产时间不能为空")
    private LocalDateTime scheduledDate;

    @Schema(description = "排产数量")
    private BigDecimal scheduledQuantity;

    @Schema(description = "生产车间")
    private String productionWorkshop;

}