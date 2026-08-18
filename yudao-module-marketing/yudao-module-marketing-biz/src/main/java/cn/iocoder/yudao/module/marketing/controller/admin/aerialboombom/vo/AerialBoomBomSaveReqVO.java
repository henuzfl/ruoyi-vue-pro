package cn.iocoder.yudao.module.marketing.controller.admin.aerialboombom.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 高机臂式/剪叉BOM物料清单新增/修改 Request VO")
@Data
public class AerialBoomBomSaveReqVO {

    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "21523")
    private Long id;

    @Schema(description = "物料编码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "物料编码不能为空")
    private String materialCode;

    @Schema(description = "物料描述")
    private String materialDesc;

    @Schema(description = "供应商")
    private String supplier;

    @Schema(description = "JIT标识（1表示JIT物料）")
    private String jitFlag;

    @Schema(description = "是否颜色管理（X表示是）")
    private String colorManagement;

    @Schema(description = "是否按需供货（X表示是）")
    private String supplyOnDemand;

    @Schema(description = "适配机型（多机型逗号分隔）")
    private String applicableModel;

    @Schema(description = "备注", example = "你说的对")
    private String remark;

    @Schema(description = "产品型号（如ZA10RJE）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "产品型号（如ZA10RJE）不能为空")
    private String productModel;

    @Schema(description = "精准BOM（如ZA10RJE-001）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "精准BOM（如ZA10RJE-001）不能为空")
    private String preciseBom;

    @Schema(description = "数量")
    private BigDecimal quantity;

    @Schema(description = "物料来源分类（臂式专用物料/剪叉专用物料/剪叉和臂式共用物料/走车物资及选配件）")
    private String sourceCategory;

    @Schema(description = "板块（默认高机）")
    private String plate;

    @Schema(description = "导入批次时间")
    private LocalDateTime importTime;

}