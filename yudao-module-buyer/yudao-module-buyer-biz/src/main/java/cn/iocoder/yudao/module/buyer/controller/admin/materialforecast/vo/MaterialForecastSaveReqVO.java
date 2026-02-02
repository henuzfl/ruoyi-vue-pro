package cn.iocoder.yudao.module.buyer.controller.admin.materialforecast.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;

@Schema(description = "管理后台 - 营销材料备料预测新增/修改 Request VO")
@Data
public class MaterialForecastSaveReqVO {

    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "25838")
    private BigDecimal id;

    @Schema(description = "客户名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "赵六")
    @NotEmpty(message = "客户名称不能为空")
    private String customerName;

    @Schema(description = "产品线/分类", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "产品线/分类不能为空")
    private String productLine;

    @Schema(description = "吨位/米段")
    private String tonnageSegment;

    @Schema(description = "车型")
    private String vehicleModel;

    @Schema(description = "预测月份（格式：YYYY-MM）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "预测月份（格式：YYYY-MM）不能为空")
    private String forecastMonth;

    @Schema(description = "预测数量", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "预测数量不能为空")
    private BigDecimal forecastQuantity;

}