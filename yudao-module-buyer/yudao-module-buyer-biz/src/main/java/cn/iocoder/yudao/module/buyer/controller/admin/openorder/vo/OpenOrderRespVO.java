package cn.iocoder.yudao.module.buyer.controller.admin.openorder.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 采购未清订单 Response VO")
@Data
@ExcelIgnoreUnannotated
public class OpenOrderRespVO {

    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "22487")
    @ExcelProperty("主键ID")
    private BigDecimal id;

    @Schema(description = "订单日期")
    @ExcelProperty("订单日期")
    private LocalDateTime orderDate;

    @Schema(description = "采购订单号", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("采购订单号")
    private String buyerOrderNo;

    @Schema(description = "订单行项目", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("订单行项目")
    private Long lineItem;

    @Schema(description = "物料号", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("物料号")
    private String materialNo;

    @Schema(description = "物料描述")
    @ExcelProperty("物料描述")
    private String materialDesc;

    @Schema(description = "订单数量")
    @ExcelProperty("订单数量")
    private BigDecimal orderQty;

    @Schema(description = "实收数量")
    @ExcelProperty("实收数量")
    private BigDecimal receivedQty;

    @Schema(description = "未清数量")
    @ExcelProperty("未清数量")
    private BigDecimal openQty;

    @Schema(description = "单位")
    @ExcelProperty("单位")
    private String unit;

    @Schema(description = "要求到货日期")
    @ExcelProperty("要求到货日期")
    private LocalDateTime requiredArrivalDate;

    @Schema(description = "实际到货日期")
    @ExcelProperty("实际到货日期")
    private LocalDateTime actualArrivalDate;

    @Schema(description = "供应商描述")
    @ExcelProperty("供应商描述")
    private String supplierDesc;

    @Schema(description = "客户")
    @ExcelProperty("客户")
    private String customer;

    @Schema(description = "采购组")
    @ExcelProperty("采购组")
    private String buyerGroup;

    @Schema(description = "凭证类型", example = "2")
    @ExcelProperty("凭证类型")
    private String documentType;

    @Schema(description = "生产订单号")
    @ExcelProperty("生产订单号")
    private String productionOrderNo;

    @Schema(description = "品牌信息")
    @ExcelProperty("品牌信息")
    private String brandInfo;

    @Schema(description = "单价（净价）", example = "18880")
    @ExcelProperty("单价（净价）")
    private BigDecimal unitPrice;

    @Schema(description = "供应商代码")
    @ExcelProperty("供应商代码")
    private String supplierCode;

    @Schema(description = "收货仓库")
    @ExcelProperty("收货仓库")
    private String receivingWarehouse;

    @Schema(description = "合计金额（净价）")
    @ExcelProperty("合计金额（净价）")
    private BigDecimal totalAmount;

    @Schema(description = "采购申请")
    @ExcelProperty("采购申请")
    private String buyerReqNo;

    @Schema(description = "创建时间")
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}