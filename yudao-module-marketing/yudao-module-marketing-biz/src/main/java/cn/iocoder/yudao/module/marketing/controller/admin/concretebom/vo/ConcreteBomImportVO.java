package cn.iocoder.yudao.module.marketing.controller.admin.concretebom.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 混凝土BOM导入VO
 * 与Excel列顺序对应
 */
@Data
public class ConcreteBomImportVO {

    @ExcelProperty(index = 0, value = "车型")
    @Schema(description = "车型（物料编码或车型名称）")
    private String vehicleModel;

    @ExcelProperty(index = 1, value = "分解油缸")
    @Schema(description = "分解油缸（部件名称及路径）")
    private String cylinderName;

    @ExcelProperty(index = 2, value = "SBP编码")
    @Schema(description = "SBP编码")
    private String sbpCode;

    @ExcelProperty(index = 3, value = "配置")
    @Schema(description = "配置（如数量等）")
    private String config;
}