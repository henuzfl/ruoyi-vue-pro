package cn.iocoder.yudao.module.buyer.controller.admin.productiontransfer.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - MES转序单信息 Response VO")
@Data
@ExcelIgnoreUnannotated
public class ProductionTransferRespVO {

    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "24805")
    @ExcelProperty("主键ID")
    private BigDecimal id;

    @Schema(description = "订单号", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("订单号")
    private String orderNo;

    @Schema(description = "物料编码", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("物料编码")
    private String materialCode;

    @Schema(description = "物料描述")
    @ExcelProperty("物料描述")
    private String materialDesc;

    @Schema(description = "生产调度员")
    @ExcelProperty("生产调度员")
    private String productionScheduler;

    @Schema(description = "转序发起人")
    @ExcelProperty("转序发起人")
    private String transferInitiator;

    @Schema(description = "发起日期")
    @ExcelProperty("发起日期")
    private LocalDateTime initiatorDate;

    @Schema(description = "数量")
    @ExcelProperty("数量")
    private BigDecimal quantity;

    @Schema(description = "转序单号")
    @ExcelProperty("转序单号")
    private String transferNo;

    @Schema(description = "计划批次")
    @ExcelProperty("计划批次")
    private String batchNo;

    @Schema(description = "签收人")
    @ExcelProperty("签收人")
    private String signer;

    @Schema(description = "签收时间")
    @ExcelProperty("签收时间")
    private LocalDateTime signTime;


}