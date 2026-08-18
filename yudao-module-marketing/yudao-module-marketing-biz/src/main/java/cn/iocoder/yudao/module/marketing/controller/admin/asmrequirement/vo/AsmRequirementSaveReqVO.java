package cn.iocoder.yudao.module.marketing.controller.admin.asmrequirement.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 营销总成需求新增/修改 Request VO")
@Data
public class AsmRequirementSaveReqVO {

    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "26395")
    private Long id;

    @Schema(description = "主机单位")
    private String hostUnit;

    @Schema(description = "车型")
    private String vehicleModel;

    @Schema(description = "总成物料编码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "总成物料编码不能为空")
    private String assemblyMaterialNo;

    @Schema(description = "总成物料名称")
    private String mainMaterialDesc;

    @Schema(description = "需求数量")
    private BigDecimal requireQuantity;

    @Schema(description = "需求日期", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "需求日期不能为空")
    private LocalDateTime requireDate;

}