package cn.iocoder.yudao.module.marketing.controller.admin.aerialboombom.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 高机臂式/剪叉BOM物料清单 Response VO")
@Data
@ExcelIgnoreUnannotated
public class AerialBoomBomRespVO {

    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "21523")
    @ExcelProperty("主键ID")
    private Long id;

    @Schema(description = "物料编码", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("物料编码")
    private String materialCode;

    @Schema(description = "物料描述")
    @ExcelProperty("物料描述")
    private String materialDesc;

    @Schema(description = "供应商")
    @ExcelProperty("供应商")
    private String supplier;

    @Schema(description = "JIT标识（1表示JIT物料）")
    @ExcelProperty("JIT标识（1表示JIT物料）")
    private String jitFlag;

    @Schema(description = "是否颜色管理（X表示是）")
    @ExcelProperty("是否颜色管理（X表示是）")
    private String colorManagement;

    @Schema(description = "是否按需供货（X表示是）")
    @ExcelProperty("是否按需供货（X表示是）")
    private String supplyOnDemand;

    @Schema(description = "适配机型（多机型逗号分隔）")
    @ExcelProperty("适配机型（多机型逗号分隔）")
    private String applicableModel;

    @Schema(description = "备注", example = "你说的对")
    @ExcelProperty("备注")
    private String remark;

    @Schema(description = "产品型号（如ZA10RJE）", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("产品型号（如ZA10RJE）")
    private String productModel;

    @Schema(description = "精准BOM（如ZA10RJE-001）", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("精准BOM（如ZA10RJE-001）")
    private String preciseBom;

    @Schema(description = "数量")
    @ExcelProperty("数量")
    private BigDecimal quantity;

    @Schema(description = "物料来源分类（臂式专用物料/剪叉专用物料/剪叉和臂式共用物料/走车物资及选配件）")
    @ExcelProperty("物料来源分类（臂式专用物料/剪叉专用物料/剪叉和臂式共用物料/走车物资及选配件）")
    private String sourceCategory;

    @Schema(description = "板块（默认高机）")
    @ExcelProperty("板块（默认高机）")
    private String plate;

    @Schema(description = "导入批次时间")
    @ExcelProperty("导入批次时间")
    private LocalDateTime importTime;

    @Schema(description = "创建时间")
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}