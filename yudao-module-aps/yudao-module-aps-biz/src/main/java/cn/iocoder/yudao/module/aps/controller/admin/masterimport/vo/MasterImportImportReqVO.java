package cn.iocoder.yudao.module.aps.controller.admin.masterimport.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 物料主数据导入 Excel VO")
@Data
public class MasterImportImportReqVO {

    @Schema(description = "物料号", requiredMode = Schema.RequiredMode.REQUIRED, example = "MAT001")
    private String materialNo;

    @Schema(description = "物料描述", example = "发动机总成")
    private String materialDesc;

    @Schema(description = "物料类型", example = "FERT")
    private String materialType;

    @Schema(description = "毛重")
    private BigDecimal grossWeight;

    @Schema(description = "净重")
    private BigDecimal netWeight;

    @Schema(description = "基本计量单位", example = "PC")
    private String baseUom;

    @Schema(description = "评估类", example = "7900")
    private String valuationClass;

    @Schema(description = "价格控制", example = "S")
    private String priceControl;

    @Schema(description = "无成本核算", example = "X")
    private String noCostEstimation;

    @Schema(description = "QS成本估算", example = "1")
    private String qsCostEstimate;

    @Schema(description = "大小/尺寸", example = "100*200")
    private String sizeDimension;

    @Schema(description = "采购类型", example = "E")
    private String procurementType;

    @Schema(description = "生产存储地点", example = "P001")
    private String productionStorageLocation;

    @Schema(description = "生产调度员", example = "张三")
    private String productionScheduler;

    @Schema(description = "分销标识", example = "1")
    private String distributionFlag;

    @Schema(description = "物料组", example = "01")
    private String materialCategory;

    @Schema(description = "外部采购存储", example = "S001")
    private String externalProcurementStorage;

    @Schema(description = "计划交货时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime plannedDeliveryTime;

    @Schema(description = "采购组", example = "001")
    private String purchasingGroup;
}