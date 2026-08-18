package cn.iocoder.yudao.module.buyer.controller.admin.codeconfig.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;

@Schema(description = "管理后台 - 主机编码配置新增/修改 Request VO")
@Data
public class CodeConfigSaveReqVO {

    @Schema(description = "主键ID（雪花算法）", requiredMode = Schema.RequiredMode.REQUIRED, example = "136")
    private BigDecimal id;

    @Schema(description = "名称", example = "李四")
    private String name;

    @Schema(description = "主机编码")
    private String hostCode;

    @Schema(description = "特力编码")
    private String teliCode;

}