package cn.iocoder.yudao.module.wm.controller.admin.orderdemand.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 订单追溯需求 Response VO")
@Data
@ExcelIgnoreUnannotated
public class OrderDemandRespVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "19169")
    @ExcelProperty("编号")
    private BigDecimal id;

    @Schema(description = "订单号", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("订单号")
    private String orderNo;

    @Schema(description = "追溯需求号", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("追溯需求号")
    private String traceDemandNo;

    @Schema(description = "物料编码", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("物料编码")
    private String materialNo;

    @Schema(description = "物料描述", example = "你猜")
    @ExcelProperty("物料描述")
    private String materialDescription;

    @Schema(description = "需求数量", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("需求数量")
    private BigDecimal demandQuantity;

    @Schema(description = "出库累计数", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("出库累计数")
    private BigDecimal outboundAccumulated;

    @Schema(description = "未清数量", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("未清数量")
    private BigDecimal openQuantity;

    @Schema(description = "状态（0=待处理 1=部分完成 2=已完成）", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("状态（0=待处理 1=部分完成 2=已完成）")
    private Short status;

    @Schema(description = "备注", example = "随便")
    @ExcelProperty("备注")
    private String remark;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}