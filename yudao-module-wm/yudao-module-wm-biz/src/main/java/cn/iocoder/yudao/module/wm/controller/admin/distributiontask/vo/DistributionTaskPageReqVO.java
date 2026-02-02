package cn.iocoder.yudao.module.wm.controller.admin.distributiontask.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 配送任务下发分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DistributionTaskPageReqVO extends PageParam {

    @Schema(description = "订单号")
    private String orderNo;

    @Schema(description = "预留号")
    private String reservationNo;

    @Schema(description = "预留行号")
    private String reservationLineNo;

    @Schema(description = "工序号")
    private String processNo;

    @Schema(description = "配送任务单号")
    private String distributionTaskNo;

    @Schema(description = "产品编码")
    private String productCode;

    @Schema(description = "产品名称", example = "张三")
    private String productName;

    @Schema(description = "物料号")
    private String materialNo;

    @Schema(description = "物料名称", example = "张三")
    private String materialName;

    @Schema(description = "数量")
    private BigDecimal quantity;

    @Schema(description = "单位")
    private String unit;

    @Schema(description = "任务状态（0=待下发，1=已下发，2=配送中，3=已完成，4=已取消）", example = "1")
    private Short taskStatus;

    @Schema(description = "配送状态（0=未配送，1=已配送，2=已接收，3=已退回）", example = "2")
    private Short distributionStatus;

    @Schema(description = "配送时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] distributionTime;

    @Schema(description = "配送操作人")
    private String distributionOperator;

    @Schema(description = "配送地点")
    private String deliveryLocation;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}