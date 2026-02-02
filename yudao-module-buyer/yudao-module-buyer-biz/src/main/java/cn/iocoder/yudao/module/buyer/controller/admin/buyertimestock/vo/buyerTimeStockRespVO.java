package cn.iocoder.yudao.module.buyer.controller.admin.buyertimestock.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 实时库存 Response VO")
@Data
@ExcelIgnoreUnannotated
public class buyerTimeStockRespVO {

    @Schema(description = "主键编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "5012")
    @ExcelProperty("主键编号")
    private Long id;

    @Schema(description = "物料编号", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("物料编号")
    private String materialNo;

    @Schema(description = "物料描述")
    @ExcelProperty("物料描述")
    private String materialDesc;

    @Schema(description = "库存地点", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("库存地点")
    private String stockLocation;

    @Schema(description = "库存数量", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("库存数量")
    private BigDecimal stockQuantity;

    @Schema(description = "可用数量")
    @ExcelProperty("可用数量")
    private BigDecimal availableQuantity;

    @Schema(description = "状态（0=正常，1=停用）", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("状态（0=正常，1=停用）")
    private Integer status;

    @Schema(description = "备注", example = "你说的对")
    @ExcelProperty("备注")
    private String remark;

    @Schema(description = "创建时间")
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}