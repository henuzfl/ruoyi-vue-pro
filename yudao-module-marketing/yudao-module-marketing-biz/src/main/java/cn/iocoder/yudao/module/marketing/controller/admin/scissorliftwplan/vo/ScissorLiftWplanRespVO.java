package cn.iocoder.yudao.module.marketing.controller.admin.scissorliftwplan.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 高机剪叉周计划 Response VO")
@Data
@ExcelIgnoreUnannotated
public class ScissorLiftWplanRespVO {

    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "18666")
    @ExcelProperty("主键ID")
    private Long id;

    @Schema(description = "产品线")
    @ExcelProperty("产品线")
    private String productLine;

    @Schema(description = "精准车型")
    @ExcelProperty("精准车型")
    private String preciseModel;

    @Schema(description = "产品型号")
    @ExcelProperty("产品型号")
    private String productModel;

    @Schema(description = "精准BOM")
    @ExcelProperty("精准BOM")
    private String preciseBom;

    @Schema(description = "生产日期", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("生产日期")
    private LocalDateTime planDate;

    @Schema(description = "周次")
    @ExcelProperty("周次")
    private String weekNo;

    @Schema(description = "周起始日期")
    @ExcelProperty("周起始日期")
    private LocalDateTime weekStartDate;

    @Schema(description = "周结束日期")
    @ExcelProperty("周结束日期")
    private LocalDateTime weekEndDate;

    @Schema(description = "当日数量")
    @ExcelProperty("当日数量")
    private Integer dailyQuantity;

    @Schema(description = "车号范围")
    @ExcelProperty("车号范围")
    private String carNumberRange;

    @Schema(description = "生产线条类型", example = "2")
    @ExcelProperty("生产线条类型")
    private String productionLineType;

    @Schema(description = "板块")
    @ExcelProperty("板块")
    private String plate;

    @Schema(description = "导入批次时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("导入批次时间")
    private LocalDateTime importTime;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}