package cn.iocoder.yudao.module.buyer.controller.admin.buyerstock.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 供应商库存 Response VO")
@Data
@ExcelIgnoreUnannotated
public class buyerStockRespVO {

    @Schema(description = "主键编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "25646")
    @ExcelProperty("主键编号")
    private Long id;

    @Schema(description = "物料编号", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("物料编号")
    private String materialNo;

    @Schema(description = "物料描述")
    @ExcelProperty("物料描述")
    private String materialDesc;

    @Schema(description = "供应商")
    @ExcelProperty("供应商")
    private String supplier;

    @Schema(description = "库存地点")
    @ExcelProperty("库存地点")
    private String stockLocation;

    @Schema(description = "库存数量")
    @ExcelProperty("库存数量")
    private BigDecimal stockQuantity;

    @Schema(description = "备料数量")
    @ExcelProperty("备料数量")
    private BigDecimal preparedQuantity;

    @Schema(description = "库存月份")
    @ExcelProperty("库存月份")
    private String stockMonth;

    @Schema(description = "状态（0=正常，1=停用）", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("状态（0=正常，1=停用）")
    private Integer status;

    @Schema(description = "备注", example = "随便")
    @ExcelProperty("备注")
    private String remark;

    @Schema(description = "创建时间")
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}