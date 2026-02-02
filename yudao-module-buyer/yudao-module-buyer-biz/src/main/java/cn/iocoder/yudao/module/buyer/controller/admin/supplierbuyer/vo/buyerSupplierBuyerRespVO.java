package cn.iocoder.yudao.module.buyer.controller.admin.supplierbuyer.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 物料供应商采购员对应 Response VO")
@Data
@ExcelIgnoreUnannotated
public class buyerSupplierBuyerRespVO {

    @Schema(description = "主键编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "6846")
    @ExcelProperty("主键编号")
    private Long id;

    @Schema(description = "物料编号", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("物料编号")
    private String materialNo;

    @Schema(description = "物料描述")
    @ExcelProperty("物料描述")
    private String materialDesc;

    @Schema(description = "供应商", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("供应商")
    private String supplier;

    @Schema(description = "采购员")
    @ExcelProperty("采购员")
    private String buyer;

    @Schema(description = "采购组")
    @ExcelProperty("采购组")
    private String procurementGroup;

    @Schema(description = "物料类型", example = "2")
    @ExcelProperty("物料类型")
    private String materialType;

    @Schema(description = "物料分类")
    @ExcelProperty("物料分类")
    private String materialCategory;

    @Schema(description = "采购类型", example = "2")
    @ExcelProperty("采购类型")
    private String procurementType;

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