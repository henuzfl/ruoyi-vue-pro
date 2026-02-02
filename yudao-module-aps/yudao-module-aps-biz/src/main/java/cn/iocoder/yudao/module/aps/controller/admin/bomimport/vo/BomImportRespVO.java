package cn.iocoder.yudao.module.aps.controller.admin.bomimport.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 物料BOM导入 Response VO")
@Data
@ExcelIgnoreUnannotated
public class BomImportRespVO {

    @Schema(description = "主键编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "25624")
    @ExcelProperty("主键编号")
    private Long id;

    @Schema(description = "父节点行号")
    @ExcelProperty("父节点行号")
    private String parentLineNo;

    @Schema(description = "行号", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("行号")
    private String lineNo;

    @Schema(description = "层级", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("层级")
    private Long levelNo;

    @Schema(description = "工厂", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("工厂")
    private String plant;

    @Schema(description = "主物料号", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("主物料号")
    private String mainMaterialNo;

    @Schema(description = "组件物料号", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("组件物料号")
    private String componentMaterialNo;

    @Schema(description = "组件物料描述")
    @ExcelProperty("组件物料描述")
    private String componentDesc;

    @Schema(description = "规格型号")
    @ExcelProperty("规格型号")
    private String specModel;

    @Schema(description = "毛重")
    @ExcelProperty("毛重")
    private BigDecimal grossWeight;

    @Schema(description = "净重")
    @ExcelProperty("净重")
    private BigDecimal netWeight;

    @Schema(description = "组件数量", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("组件数量")
    private BigDecimal componentQty;

    @Schema(description = "单台用量", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("单台用量")
    private BigDecimal unitUsage;

    @Schema(description = "物料类", example = "2")
    @ExcelProperty("物料类")
    private String materialType;

    @Schema(description = "特殊采购类型", example = "2")
    @ExcelProperty("特殊采购类型")
    private String specialProcurementType;

    @Schema(description = "库存地点")
    @ExcelProperty("库存地点")
    private String storageLocation;

    @Schema(description = "单位", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("单位")
    private String unit;

    @Schema(description = "采购类型", example = "2")
    @ExcelProperty("采购类型")
    private String procurementType;

    @Schema(description = "采购组")
    @ExcelProperty("采购组")
    private String purchasingGroup;

    @Schema(description = "导入时间")
    @ExcelProperty("导入时间")
    private LocalDateTime importDate;

    @Schema(description = "创建时间")
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}