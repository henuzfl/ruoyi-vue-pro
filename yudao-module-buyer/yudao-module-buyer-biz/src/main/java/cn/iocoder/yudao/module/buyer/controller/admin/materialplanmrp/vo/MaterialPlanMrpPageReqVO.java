package cn.iocoder.yudao.module.buyer.controller.admin.materialplanmrp.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.math.BigDecimal;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 买家需求预测分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MaterialPlanMrpPageReqVO extends PageParam {

    @Schema(description = "客户")
    private String customerName;

    @Schema(description = "产品线/分类")
    private String productLine;

    @Schema(description = "吨位/米段")
    private String tonnageSegment;

    @Schema(description = "车型")
    private String vehicleModel;

    @Schema(description = "特力油缸编码")
    private String cylinderCode;

    @Schema(description = "采购组件物料号")
    private String componentMaterialNo;

    @Schema(description = "组件名称")
    private String componentDesc;

    @Schema(description = "规格型号")
    private String specificationModel;

    @Schema(description = "采购组")
    private String purchasingGroup;

    @Schema(description = "物料分类")
    private String materialType;

    @Schema(description = "备料是否满足")
    private String materialSupplyDesc;

    @Schema(description = "下个月预测值")
    private BigDecimal nextMonthForecast;

    @Schema(description = "下第二个月预测值")
    private BigDecimal secondMonthForecast;

    @Schema(description = "下第三个月预测值")
    private BigDecimal thirdMonthForecast;

    @Schema(description = "下第四个月预测值")
    private BigDecimal fourthMonthForecast;

    @Schema(description = "下第五个月预测值")
    private BigDecimal fifthMonthForecast;

    @Schema(description = "组件总数量")
    private BigDecimal componentTotalDemand;

    @Schema(description = "库存数据")
    private BigDecimal stockQuantity;

    @Schema(description = "当月订单数量")
    private BigDecimal currentMonthOrderQuantity;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;
}