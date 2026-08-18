package cn.iocoder.yudao.module.marketing.controller.admin.concreteplan.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 混凝土计划需求 Response VO")
@Data
@ExcelIgnoreUnannotated
public class ConcretePlanRespVO {

    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "12817")
    @ExcelProperty("主键ID")
    private Long id;

    @Schema(description = "计划编号", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("计划编号")
    private String planNo;

    @Schema(description = "序号")
    @ExcelProperty("序号")
    private String seqNo;

    @Schema(description = "米段")
    @ExcelProperty("米段")
    private String meter;

    @Schema(description = "型号", example = "张三")
    @ExcelProperty("型号")
    private String modelName;

    @Schema(description = "物料名称", example = "张三")
    @ExcelProperty("物料名称")
    private String materialName;

    @Schema(description = "物料编码")
    @ExcelProperty("物料编码")
    private String materialCode;

    @Schema(description = "生产编号")
    @ExcelProperty("生产编号")
    private String prodNo;

    @Schema(description = "数量")
    @ExcelProperty("数量")
    private BigDecimal quantity;

    @Schema(description = "结构件编号/环保编码")
    @ExcelProperty("结构件编号/环保编码")
    private String structSerialNo;

    @Schema(description = "订单号")
    @ExcelProperty("订单号")
    private String orderNo;

    @Schema(description = "出料批次")
    @ExcelProperty("出料批次")
    private String batchNo;

    @Schema(description = "组单情况", example = "1")
    @ExcelProperty("组单情况")
    private String groupStatus;

    @Schema(description = "支腿类型", example = "2")
    @ExcelProperty("支腿类型")
    private String legType;

    @Schema(description = "国家（国内/海外）", example = "1")
    @ExcelProperty("国家（国内/海外）")
    private String countryType;

    @Schema(description = "计划下达时间")
    @ExcelProperty("计划下达时间")
    private String planIssueTime;

    @Schema(description = "装配上线时间")
    @ExcelProperty("装配上线时间")
    private LocalDateTime assemblyStartTime;

    @Schema(description = "装配下线时间")
    @ExcelProperty("装配下线时间")
    private LocalDateTime assemblyEndTime;

    @Schema(description = "调试下线时间")
    @ExcelProperty("调试下线时间")
    private LocalDateTime debugTime;

    @Schema(description = "涂装下线时间")
    @ExcelProperty("涂装下线时间")
    private LocalDateTime paintingTime;

    @Schema(description = "入库时间")
    @ExcelProperty("入库时间")
    private LocalDateTime warehouseTime;

    @Schema(description = "营运要求产出月份")
    @ExcelProperty("营运要求产出月份")
    private String outputMonth;

    @Schema(description = "特殊要求")
    @ExcelProperty("特殊要求")
    private String specialReq;

    @Schema(description = "涂装要求")
    @ExcelProperty("涂装要求")
    private String paintingReq;

    @Schema(description = "异常说明")
    @ExcelProperty("异常说明")
    private String exceptionNote;

    @Schema(description = "排产时间")
    @ExcelProperty("排产时间")
    private String scheduleTime;

    @Schema(description = "发货要求")
    @ExcelProperty("发货要求")
    private String deliveryReq;

    @Schema(description = "工厂")
    @ExcelProperty("工厂")
    private String factory;

    @Schema(description = "状态", example = "2")
    @ExcelProperty("状态")
    private String status;

    @Schema(description = "客户")
    @ExcelProperty("客户")
    private String customer;

    @Schema(description = "修改后车号")
    @ExcelProperty("修改后车号")
    private String modifiedCarNo;

    @Schema(description = "板块")
    @ExcelProperty("板块")
    private String plate;

    @Schema(description = "导入时间")
    @ExcelProperty("导入时间")
    private LocalDateTime importTime;

    @Schema(description = "创建时间")
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}