package cn.iocoder.yudao.module.marketing.controller.admin.concreteplan.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 混凝土计划需求分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ConcretePlanPageReqVO extends PageParam {

    @Schema(description = "计划编号")
    private Long planNo;

    @Schema(description = "序号")
    private String seqNo;

    @Schema(description = "米段")
    private String meter;

    @Schema(description = "型号", example = "张三")
    private String modelName;

    @Schema(description = "物料名称", example = "张三")
    private String materialName;

    @Schema(description = "物料编码")
    private String materialCode;

    @Schema(description = "生产编号")
    private String prodNo;

    @Schema(description = "数量")
    private BigDecimal quantity;

    @Schema(description = "结构件编号/环保编码")
    private String structSerialNo;

    @Schema(description = "订单号")
    private String orderNo;

    @Schema(description = "出料批次")
    private String batchNo;

    @Schema(description = "组单情况", example = "1")
    private String groupStatus;

    @Schema(description = "支腿类型", example = "2")
    private String legType;

    @Schema(description = "国家（国内/海外）", example = "1")
    private String countryType;

    @Schema(description = "计划下达时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private String[] planIssueTime;

    @Schema(description = "装配上线时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] assemblyStartTime;

    @Schema(description = "装配下线时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] assemblyEndTime;

    @Schema(description = "调试下线时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] debugTime;

    @Schema(description = "涂装下线时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] paintingTime;

    @Schema(description = "入库时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] warehouseTime;

    @Schema(description = "营运要求产出月份")
    private String outputMonth;

    @Schema(description = "特殊要求")
    private String specialReq;

    @Schema(description = "涂装要求")
    private String paintingReq;

    @Schema(description = "异常说明")
    private String exceptionNote;

    @Schema(description = "排产时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private String[] scheduleTime;

    @Schema(description = "发货要求")
    private String deliveryReq;

    @Schema(description = "工厂")
    private String factory;

    @Schema(description = "状态", example = "2")
    private String status;

    @Schema(description = "客户")
    private String customer;

    @Schema(description = "修改后车号")
    private String modifiedCarNo;

    @Schema(description = "板块")
    private String plate;

    @Schema(description = "导入时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] importTime;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}