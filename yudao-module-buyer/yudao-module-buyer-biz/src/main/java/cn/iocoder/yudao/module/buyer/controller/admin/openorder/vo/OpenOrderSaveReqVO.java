package cn.iocoder.yudao.module.buyer.controller.admin.openorder.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 采购未清订单新增/修改 Request VO")
@Data
public class OpenOrderSaveReqVO {

    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "22487")
    private BigDecimal id;

    @Schema(description = "订单日期")
    private LocalDateTime orderDate;

    @Schema(description = "采购订单号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "采购订单号不能为空")
    private String buyerOrderNo;

    @Schema(description = "订单行项目", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "订单行项目不能为空")
    private Long lineItem;

    @Schema(description = "物料号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "物料号不能为空")
    private String materialNo;

    @Schema(description = "物料描述")
    private String materialDesc;

    @Schema(description = "订单数量")
    private BigDecimal orderQty;

    @Schema(description = "实收数量")
    private BigDecimal receivedQty;

    @Schema(description = "未清数量")
    private BigDecimal openQty;

    @Schema(description = "单位")
    private String unit;

    @Schema(description = "要求到货日期")
    private LocalDateTime requiredArrivalDate;

    @Schema(description = "实际到货日期")
    private LocalDateTime actualArrivalDate;

    @Schema(description = "供应商描述")
    private String supplierDesc;

    @Schema(description = "客户")
    private String customer;

    @Schema(description = "采购组")
    private String buyerGroup;

    @Schema(description = "凭证类型", example = "2")
    private String documentType;

    @Schema(description = "生产订单号")
    private String productionOrderNo;

    @Schema(description = "品牌信息")
    private String brandInfo;

    @Schema(description = "单价（净价）", example = "18880")
    private BigDecimal unitPrice;

    @Schema(description = "供应商代码")
    private String supplierCode;

    @Schema(description = "收货仓库")
    private String receivingWarehouse;

    @Schema(description = "合计金额（净价）")
    private BigDecimal totalAmount;

    @Schema(description = "采购申请")
    private String buyerReqNo;

}