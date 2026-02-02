package cn.iocoder.yudao.module.aps.controller.admin.bomimport.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 物料BOM导入新增/修改 Request VO")
@Data
public class BomImportSaveReqVO {

    @Schema(description = "主键编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "25624")
    private Long id;

    @Schema(description = "父节点行号")
    private String parentLineNo;

    @Schema(description = "行号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "行号不能为空")
    private String lineNo;

    @Schema(description = "层级", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "层级不能为空")
    private Long levelNo;

    @Schema(description = "工厂", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "工厂不能为空")
    private String plant;

    @Schema(description = "主物料号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "主物料号不能为空")
    private String mainMaterialNo;

    @Schema(description = "组件物料号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "组件物料号不能为空")
    private String componentMaterialNo;

    @Schema(description = "组件物料描述")
    private String componentDesc;

    @Schema(description = "规格型号")
    private String specModel;

    @Schema(description = "毛重")
    private BigDecimal grossWeight;

    @Schema(description = "净重")
    private BigDecimal netWeight;

    @Schema(description = "组件数量", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "组件数量不能为空")
    private BigDecimal componentQty;

    @Schema(description = "单台用量", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "单台用量不能为空")
    private BigDecimal unitUsage;

    @Schema(description = "物料类", example = "2")
    private String materialType;

    @Schema(description = "特殊采购类型", example = "2")
    private String specialProcurementType;

    @Schema(description = "库存地点")
    private String storageLocation;

    @Schema(description = "单位", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "单位不能为空")
    private String unit;

    @Schema(description = "采购类型", example = "2")
    private String procurementType;

    @Schema(description = "采购组")
    private String purchasingGroup;

    @Schema(description = "导入时间")
    private LocalDateTime importDate;

}