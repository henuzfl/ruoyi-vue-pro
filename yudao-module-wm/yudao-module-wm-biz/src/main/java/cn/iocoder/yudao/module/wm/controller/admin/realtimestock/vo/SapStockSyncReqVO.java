// [file name]: SapStockSyncReqVO.java
// [file content begin]
package cn.iocoder.yudao.module.wm.controller.admin.realtimestock.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Size;

/**
 * SAP库存查询/同步请求VO
 */
@Data
public class SapStockSyncReqVO {

    @Schema(description = "物料号", example = "10000001")
    @Size(max = 40, message = "物料号长度不能超过40个字符")
    private String materialNumber;

    @Schema(description = "工厂", example = "1000")
    @Size(max = 4, message = "工厂长度不能超过4个字符")
    private String plant;

    @Schema(description = "库存地点", example = "1001")
    @Size(max = 4, message = "库存地点长度不能超过4个字符")
    private String storageLocation;

    @Schema(description = "特殊库存标识", example = "E")
    @Size(max = 1, message = "特殊库存标识长度不能超过1个字符")
    private String specialStock;

    @Schema(description = "供应商编码", example = "10000001")
    @Size(max = 10, message = "供应商编码长度不能超过10个字符")
    private String supplierCode;

    @Schema(description = "供应商名称", example = "XXX公司")
    @Size(max = 35, message = "供应商名称长度不能超过35个字符")
    private String supplierName;

    @Schema(description = "业务字段1", example = "")
    @Size(max = 40, message = "业务字段1长度不能超过40个字符")
    private String businessField1;

    @Schema(description = "业务字段2", example = "")
    @Size(max = 40, message = "业务字段2长度不能超过40个字符")
    private String businessField2;

    @Schema(description = "业务字段3", example = "")
    @Size(max = 40, message = "业务字段3长度不能超过40个字符")
    private String businessField3;

    @Schema(description = "业务字段4", example = "")
    @Size(max = 40, message = "业务字段4长度不能超过40个字符")
    private String businessField4;

    @Schema(description = "业务字段5", example = "")
    @Size(max = 40, message = "业务字段5长度不能超过40个字符")
    private String businessField5;

    @Schema(description = "是否覆盖现有数据(用于同步)", example = "false")
    private Boolean overwrite = false;
}