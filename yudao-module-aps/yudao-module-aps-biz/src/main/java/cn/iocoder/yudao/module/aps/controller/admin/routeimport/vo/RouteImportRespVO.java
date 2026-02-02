package cn.iocoder.yudao.module.aps.controller.admin.routeimport.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 工艺路线导入 Response VO")
@Data
@ExcelIgnoreUnannotated
public class RouteImportRespVO {

    @Schema(description = "主键编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "16518")
    @ExcelProperty("主键编号")
    private Long id;

    @Schema(description = "物料号", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("物料号")
    private String materialNo;

    @Schema(description = "物料描述")
    @ExcelProperty("物料描述")
    private String materialDesc;

    @Schema(description = "工艺路线")
    @ExcelProperty("工艺路线")
    private String processRoute;

    @Schema(description = "组号")
    @ExcelProperty("组号")
    private String groupNo;

    @Schema(description = "工厂")
    @ExcelProperty("工厂")
    private String plant;

    @Schema(description = "起始有效日期")
    @ExcelProperty("起始有效日期")
    private LocalDateTime validFromDate;

    @Schema(description = "工作中心", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("工作中心")
    private String workCenter;

    @Schema(description = "顺序")
    @ExcelProperty("顺序")
    private Long sequenceNo;

    @Schema(description = "操作序号", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("操作序号")
    private String operationSeq;

    @Schema(description = "工序文本")
    @ExcelProperty("工序文本")
    private String operationText;

    @Schema(description = "控制码")
    @ExcelProperty("控制码")
    private String controlCode;

    @Schema(description = "人工工时")
    @ExcelProperty("人工工时")
    private BigDecimal laborHours;

    @Schema(description = "单位")
    @ExcelProperty("单位")
    private String laborHoursUnit;

    @Schema(description = "固定制造费用")
    @ExcelProperty("固定制造费用")
    private BigDecimal fixedCost;

    @Schema(description = "单位")
    @ExcelProperty("单位")
    private String fixedCostUnit;

    @Schema(description = "变动制造费用")
    @ExcelProperty("变动制造费用")
    private BigDecimal variableCost;

    @Schema(description = "单位")
    @ExcelProperty("单位")
    private String variableCostUnit;

    @Schema(description = "生产周期")
    @ExcelProperty("生产周期")
    private BigDecimal productionCycle;

    @Schema(description = "单位")
    @ExcelProperty("单位")
    private String productionCycleUnit;

    @Schema(description = "更改编号")
    @ExcelProperty("更改编号")
    private String changeNo;

    @Schema(description = "删除标记")
    @ExcelProperty("删除标记")
    private String deleteFlag;

    @Schema(description = "生产调度员")
    @ExcelProperty("生产调度员")
    private String productionScheduler;

    @Schema(description = "采购类型", example = "1")
    @ExcelProperty("采购类型")
    private String procurementType;

    @Schema(description = "导入时间")
    @ExcelProperty("导入时间")
    private LocalDateTime importDate;

    @Schema(description = "创建时间")
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}