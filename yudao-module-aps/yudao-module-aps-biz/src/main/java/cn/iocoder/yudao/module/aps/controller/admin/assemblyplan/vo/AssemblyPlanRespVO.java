package cn.iocoder.yudao.module.aps.controller.admin.assemblyplan.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 各车间开装计划 Response VO")
@Data
@ExcelIgnoreUnannotated
public class AssemblyPlanRespVO {

    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "12326")
    @ExcelProperty("主键ID")
    private Long id;

    @Schema(description = "订单号", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("订单号")
    private String orderNo;

    @Schema(description = "物料编号", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("物料编号")
    private String materialCode;

    @Schema(description = "物料描述")
    @ExcelProperty("物料描述")
    private String materialDesc;

    @Schema(description = "装配数量（计划数）", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("装配数量（计划数）")
    private Long assemblyQuantity;

    @Schema(description = "已装配数量（完成数）", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("已装配数量（完成数）")
    private Long assembledQuantity;

    @Schema(description = "排产时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("排产时间")
    private LocalDateTime scheduleTime;

    @Schema(description = "车间")
    @ExcelProperty("车间")
    private String workshop;

    @Schema(description = "导入时间")
    @ExcelProperty("导入时间")
    private LocalDateTime importTime;

    @Schema(description = "创建时间")
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}