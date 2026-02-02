package cn.iocoder.yudao.module.wm.controller.admin.kittingmaster.vo;

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
public class KittingMasterRespVO {

    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "9944")
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

    @Schema(description = "排产数量")
    @ExcelProperty("排产数量")
    private BigDecimal scheduledQuantity;

    @Schema(description = "生产车间")
    @ExcelProperty("生产车间")
    private String productionWorkshop;

    @Schema(description = "创建时间")
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    // 新增字段
    @Schema(description = "排产时间")
    @ExcelProperty("排产时间")
    private LocalDateTime scheduledDate;

    @Schema(description = "是否齐套")
    @ExcelProperty("是否齐套")
    private String kittingStatus;

    @Schema(description = "总成是否齐套")
    @ExcelProperty("总成是否齐套")
    private String assemblyKittingStatus;

    @Schema(description = "组件物料号")
    @ExcelProperty("组件物料号")
    private String componentMaterialNo;

    @Schema(description = "组件物料名称")
    @ExcelProperty("组件物料名称")
    private String componentDesc;

    @Schema(description = "单件物料数量")
    @ExcelProperty("单件物料数量")
    private BigDecimal unitUsage;

    @Schema(description = "所需组件数量")
    @ExcelProperty("所需组件数量")
    private BigDecimal requiredQty;

    @Schema(description = "明天库存")
    @ExcelProperty("明天库存")
    private BigDecimal stockTomorrow;

    @Schema(description = "后天库存")
    @ExcelProperty("后天库存")
    private BigDecimal stockDayAfterTomorrow;

    @Schema(description = "第三天库存")
    @ExcelProperty("第三天库存")
    private BigDecimal stockThirdDay;

    @Schema(description = "总库存数量")
    @ExcelProperty("总库存数量")
    private BigDecimal stockQuantity;

    @Schema(description = "差额")
    @ExcelProperty("差额")
    private BigDecimal shortageQty;

}