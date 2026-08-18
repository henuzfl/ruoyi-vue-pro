package cn.iocoder.yudao.module.aps.controller.admin.matchingresult.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import com.alibaba.excel.annotation.format.DateTimeFormat;   // EasyExcel 的注解
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 主计划物料需求匹配 Response VO")
@Data
@ExcelIgnoreUnannotated
public class MatchingResultRespVO {

    @Schema(description = "主键ID（雪花算法生成）", requiredMode = Schema.RequiredMode.REQUIRED, example = "58")
    //@ExcelProperty("主键ID（雪花算法生成）")
    private BigDecimal id;

    @Schema(description = "订单号")
    @ExcelProperty("订单号")
    private String orderNo;

    @Schema(description = "排产时间")
    @ExcelProperty("排产时间")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat("yyyy-MM-dd")
    private Date scheduleTime;

    @Schema(description = "物料编码")
    @ExcelProperty("物料编码")
    private String materialCode;

    @Schema(description = "物料描述")
    @ExcelProperty("物料描述")
    private String materialDesc;

    @Schema(description = "责任车间（总成）")
    @ExcelProperty("责任车间（总成）")
    private String workshop;

    @Schema(description = "数量")
    @ExcelProperty("数量")
    private BigDecimal quantity;

    @Schema(description = "已完成数量")
    @ExcelProperty("已完成数量")
    private BigDecimal completedQuantity;

    @Schema(description = "库存")
    @ExcelProperty("库存")
    private BigDecimal stock;

    @Schema(description = "转序（数量）")
    @ExcelProperty("转序（数量）")
    private BigDecimal transferOrder;

    @Schema(description = "零部件订单")
    @ExcelProperty("零部件订单")
    private String componentOrder;

    @Schema(description = "零部件编码")
    @ExcelProperty("零部件编码")
    private String componentCode;

    @Schema(description = "零部件描述")
    @ExcelProperty("零部件描述")
    private String componentDesc;

    @Schema(description = "责任车间（零部件）")
    @ExcelProperty("责任车间（零部件）")
    private String componentWorkshop;

    @Schema(description = "基本开始日期")
    @ExcelProperty("基本开始日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat("yyyy-MM-dd")
    private Date basicStartDate;

    @Schema(description = "需求数量（零部件）")
    @ExcelProperty("需求数量（零部件）")
    private BigDecimal requiredQuantity;

//    @Schema(description = "本次未完成数量")
//    @ExcelProperty("本次未完成数量")
//    private BigDecimal unfinishedQuantity;

    @Schema(description = "采购物料")
    @ExcelProperty("采购物料")
    private String purchaseMaterial;

    @Schema(description = "采购物料描述")
    @ExcelProperty("采购物料描述")
    private String purchaseMaterialDesc;

    @Schema(description = "大小/尺寸")
    @ExcelProperty("大小/尺寸")
    private String sizeDimension;

    @Schema(description = "需求数量（采购）")
    @ExcelProperty("需求数量（采购）")
    private BigDecimal purchaseRequiredQty;

    @Schema(description = "已配送数量")
    @ExcelProperty("已配送数量")
    private BigDecimal deliveredQuantity;

    @Schema(description = "待配送数量")
    @ExcelProperty("待配送数量")
    private BigDecimal toDeliverQuantity;

    // 放在适当位置（如待配送数量之后）
    @Schema(description = "齐套数量")
// 不加 @ExcelProperty，导出时不显示
    private BigDecimal kitQty;

    @Schema(description = "物料齐套数量")
    @ExcelProperty("物料齐套数量")
    private BigDecimal kitQtySingle;

    @Schema(description = "分配需求数量")
    @ExcelProperty("分配需求数量")
    private BigDecimal allocatedRequiredQty;

    @Schema(description = "剩余需采购数量")
    @ExcelProperty("剩余需采购数量")
    private BigDecimal remainRequiredQty;

    @Schema(description = "未清订单数量")
    @ExcelProperty("未清订单数量")
    private BigDecimal openOrderQuantity;

    @Schema(description = "采购订单")
    @ExcelProperty("采购订单")
    private String purchaseOrder;

    @Schema(description = "行号")
    @ExcelProperty("行号")
    private Long lineNumber;

    @Schema(description = "下单日期")
    @ExcelProperty("下单日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat("yyyy-MM-dd")
    private Date orderDate;

    @Schema(description = "要求交货日期")
    @ExcelProperty("要求交货日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat("yyyy-MM-dd")
    private Date requiredDeliveryDate;

    @Schema(description = "实际到货日期")
    @ExcelProperty("实际到货日期")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat("yyyy-MM-dd")
    private Date actualArrivalDate;

    @Schema(description = "供应商名称", example = "张三")
    @ExcelProperty("供应商名称")
    private String supplierName;

    // ---------- 新增字段 ----------
    @Schema(description = "采购反馈备注（多条合并）")
    @ExcelProperty("采购反馈备注")
    private String feedbackRemarks;

}