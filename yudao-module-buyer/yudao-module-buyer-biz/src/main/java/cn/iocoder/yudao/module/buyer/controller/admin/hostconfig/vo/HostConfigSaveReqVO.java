package cn.iocoder.yudao.module.buyer.controller.admin.hostconfig.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;

@Schema(description = "管理后台 - 主机配置新增/修改 Request VO")
@Data
public class HostConfigSaveReqVO {

    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "20478")
    private BigDecimal id;

    @Schema(description = "车型", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "车型不能为空")
    private String vehicleModel;

    @Schema(description = "特力图号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "特力图号不能为空")
    private String specialDrawingNo;

    @Schema(description = "配置数量", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "配置数量不能为空")
    private Long configQuantity;

    @Schema(description = "事业部", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "事业部不能为空")
    private String businessUnit;

}