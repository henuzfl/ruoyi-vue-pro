package cn.iocoder.yudao.module.wm.controller.admin.realtimestock.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * SAP库存查询结果VO
 */
@Data
public class InventoryResultVO {

    @Schema(description = "物料号", example = "10000001")
    private String materialNumber;

    @Schema(description = "物料描述", example = "原材料A")
    private String materialDescription;

    @Schema(description = "工厂", example = "1000")
    private String plant;

    @Schema(description = "库存地点", example = "1001")
    private String storageLocation;

    @Schema(description = "库存地点描述", example = "原料仓库")
    private String storageLocationDesc;

    @Schema(description = "库存数量", example = "100.00")
    private String stockQuantity;

    @Schema(description = "单位", example = "KG")
    private String unit;

    @Schema(description = "单位描述", example = "公斤")
    private String unitDescription;

    @Schema(description = "特殊库存标识", example = "E")
    private String specialStock;

    @Schema(description = "供应商编码", example = "10000001")
    private String supplierCode;

    @Schema(description = "供应商名称", example = "XXX公司")
    private String supplierName;

    @Schema(description = "库存类型", example = "普通库存")
    private String stockTypeDesc;

    @Schema(description = "库存状态", example = "有库存")
    private String stockStatusDesc;
}