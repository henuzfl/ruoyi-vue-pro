package cn.iocoder.yudao.module.aps.controller.admin.assemblyplan.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 各车间开装计划新增/修改 Request VO")
@Data
public class AssemblyPlanSaveReqVO {

    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "12326")
    private Long id;

    @Schema(description = "订单号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "订单号不能为空")
    private String orderNo;

    @Schema(description = "物料编号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "物料编号不能为空")
    private String materialCode;

    @Schema(description = "物料描述")
    private String materialDesc;

    @Schema(description = "装配数量（计划数）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "装配数量（计划数）不能为空")
    private Long assemblyQuantity;

    @Schema(description = "已装配数量（完成数）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "已装配数量（完成数）不能为空")
    private Long assembledQuantity;

    @Schema(description = "排产时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "排产时间不能为空")
    private LocalDateTime scheduleTime;

    @Schema(description = "车间")
    private String workshop;

    @Schema(description = "导入时间")
    private LocalDateTime importTime;

}