package cn.iocoder.yudao.module.aps.controller.admin.masterimport.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;

@Schema(description = "管理后台 - 物料主数据导入新增/修改 Request VO")
@Data
public class MasterImportSaveReqVO {

    @Schema(description = "主键编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "15227")
    private Long id;

    @Schema(description = "物料编号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "物料编号不能为空")
    private String materialNo;

    @Schema(description = "物料描述", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "物料描述不能为空")
    private String materialDesc;

    @Schema(description = "物料类型", example = "1")
    private String materialType;

    @Schema(description = "毛重")
    private BigDecimal grossWeight;

    @Schema(description = "净重")
    private BigDecimal netWeight;

    @Schema(description = "基本单位")
    private String baseUom;

    @Schema(description = "评估类")
    private String valuationClass;

    @Schema(description = "价格控制")
    private String priceControl;

    @Schema(description = "无成本核算")
    private String noCostEstimation;

    @Schema(description = "用QS的成本估算")
    private String qsCostEstimate;

    @Schema(description = "大小量纲")
    private String sizeDimension;

    @Schema(description = "采购类型", example = "1")
    private String procurementType;

    @Schema(description = "生产仓储地点")
    private String productionStorageLocation;

    @Schema(description = "生产调度员")
    private String productionScheduler;

    @Schema(description = "配送标识")
    private String distributionFlag;

    @Schema(description = "物料分类")
    private String materialCategory;

    @Schema(description = "外部采购仓库地点")
    private String externalProcurementStorage;

    @Schema(description = "计划交货时间")
    private Integer plannedDeliveryTime;

}