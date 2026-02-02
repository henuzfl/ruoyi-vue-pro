package cn.iocoder.yudao.module.aps.controller.admin.routeimport.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 工艺路线导入分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class RouteImportPageReqVO extends PageParam {

    @Schema(description = "物料号")
    private String materialNo;

    @Schema(description = "物料描述")
    private String materialDesc;

    @Schema(description = "工艺路线")
    private String processRoute;

    @Schema(description = "组号")
    private String groupNo;

    @Schema(description = "工厂")
    private String plant;

    @Schema(description = "起始有效日期")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] validFromDate;

    @Schema(description = "工作中心")
    private String workCenter;

    @Schema(description = "顺序")
    private Long sequenceNo;

    @Schema(description = "操作序号")
    private String operationSeq;

    @Schema(description = "工序文本")
    private String operationText;

    @Schema(description = "控制码")
    private String controlCode;

    @Schema(description = "人工工时")
    private BigDecimal laborHours;

    @Schema(description = "单位")
    private String laborHoursUnit;

    @Schema(description = "固定制造费用")
    private BigDecimal fixedCost;

    @Schema(description = "单位")
    private String fixedCostUnit;

    @Schema(description = "变动制造费用")
    private BigDecimal variableCost;

    @Schema(description = "单位")
    private String variableCostUnit;

    @Schema(description = "生产周期")
    private BigDecimal productionCycle;

    @Schema(description = "单位")
    private String productionCycleUnit;

    @Schema(description = "更改编号")
    private String changeNo;

    @Schema(description = "删除标记")
    private String deleteFlag;

    @Schema(description = "生产调度员")
    private String productionScheduler;

    @Schema(description = "采购类型", example = "1")
    private String procurementType;

    @Schema(description = "导入时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] importDate;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}