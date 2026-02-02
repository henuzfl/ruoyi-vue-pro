package cn.iocoder.yudao.module.aps.controller.admin.bomimport.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 物料BOM导入分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class BomImportPageReqVO extends PageParam {

    @Schema(description = "父节点行号")
    private String parentLineNo;

    @Schema(description = "行号")
    private String lineNo;

    @Schema(description = "层级")
    private Long levelNo;

    @Schema(description = "工厂")
    private String plant;

    @Schema(description = "主物料号")
    private String mainMaterialNo;

    @Schema(description = "组件物料号")
    private String componentMaterialNo;

    @Schema(description = "组件物料描述")
    private String componentDesc;

    @Schema(description = "规格型号")
    private String specModel;

    @Schema(description = "毛重")
    private BigDecimal grossWeight;

    @Schema(description = "净重")
    private BigDecimal netWeight;

    @Schema(description = "组件数量")
    private BigDecimal componentQty;

    @Schema(description = "单台用量")
    private BigDecimal unitUsage;

    @Schema(description = "物料类", example = "2")
    private String materialType;

    @Schema(description = "特殊采购类型", example = "2")
    private String specialProcurementType;

    @Schema(description = "库存地点")
    private String storageLocation;

    @Schema(description = "单位")
    private String unit;

    @Schema(description = "采购类型", example = "2")
    private String procurementType;

    @Schema(description = "采购组")
    private String purchasingGroup;

    @Schema(description = "导入时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] importDate;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}