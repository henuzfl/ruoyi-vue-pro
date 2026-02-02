package cn.iocoder.yudao.module.buyer.controller.admin.materialforecast.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 营销材料备料预测 Response VO")
@Data
@ExcelIgnoreUnannotated
public class MaterialForecastRespVO {

    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "25838")
    @ExcelProperty("主键ID")
    private BigDecimal id;

    @Schema(description = "客户名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "赵六")
    @ExcelProperty("客户名称")
    private String customerName;

    @Schema(description = "产品线/分类", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("产品线/分类")
    private String productLine;

    @Schema(description = "吨位/米段")
    @ExcelProperty("吨位/米段")
    private String tonnageSegment;

    @Schema(description = "车型")
    @ExcelProperty("车型")
    private String vehicleModel;

    @Schema(description = "预测月份（格式：YYYY-MM）", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("预测月份（格式：YYYY-MM）")
    private String forecastMonth;

    @Schema(description = "预测数量", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("预测数量")
    private BigDecimal forecastQuantity;

    @Schema(description = "创建时间")
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}