package cn.iocoder.yudao.module.wm.controller.admin.bom.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - BOM新增/修改 Request VO")
@Data
public class BomSaveReqVO {

    @Schema(description = "ID", example = "1")
    private Long id;

    @Schema(description = "工厂代码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "工厂代码不能为空")
    private String werks;

    @Schema(description = "BOM等级")
    private String stufe;

    @Schema(description = "路径标识")
    private String wegxx;

    @Schema(description = "BOM类型")
    private String bmtyp;

    @Schema(description = "可选的BOM")
    private String vwegx;

    @Schema(description = "组件描述")
    private String ojtxb;

    @Schema(description = "物料描述", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "物料描述不能为空")
    private String ojtxp;

    @Schema(description = "物料类型")
    private String mtart;

    @Schema(description = "数量", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "数量不能为空")
    @DecimalMin(value = "0", inclusive = false, message = "数量必须大于0")
    private BigDecimal menge;

    @Schema(description = "单位", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "单位不能为空")
    private String meins;

    @Schema(description = "组件物料号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "组件物料号不能为空")
    private String idnrk;

    @Schema(description = "父物料号")
    private String parentIdnrk;

    @Schema(description = "BOM版本")
    private String version;

    @Schema(description = "生效日期")
    private LocalDateTime validFrom;

    @Schema(description = "失效日期")
    private LocalDateTime validTo;

    @Schema(description = "状态：0-无效，1-有效")
    private Integer status;

    @Schema(description = "备注")
    private String remark;
}