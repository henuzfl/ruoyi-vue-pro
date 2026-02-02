package cn.iocoder.yudao.module.aps.controller.admin.mainplan.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 主计划 Response VO")
@Data
@ExcelIgnoreUnannotated
public class MainPlanRespVO {

    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "16649")
    @ExcelProperty("主键ID")
    private BigDecimal id;

    @Schema(description = "生产订单号", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("生产订单号")
    private String productionOrderNo;

    @Schema(description = "总成物料号", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("总成物料号")
    private String assemblyMaterialNo;

    @Schema(description = "主物料描述")
    @ExcelProperty("主物料描述")
    private String mainMaterialDesc;

    @Schema(description = "排产时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("排产时间")
    private LocalDateTime scheduledDate;

    @Schema(description = "排产数量")
    @ExcelProperty("排产数量")
    private BigDecimal scheduledQuantity;

    @Schema(description = "生产车间")
    @ExcelProperty("生产车间")
    private String productionWorkshop;

    @Schema(description = "创建时间")
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}