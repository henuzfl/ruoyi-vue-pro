package cn.iocoder.yudao.module.buyer.controller.admin.codeconfig.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 主机编码配置 Excel 导入 VO")
@Data
public class CodeConfigImportReqVO {
    @Schema(description = "名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "伸缩油缸I")
    private String name;
    @Schema(description = "主机编码", requiredMode = Schema.RequiredMode.REQUIRED, example = "00630848420100000")
    private String hostCode;
    @Schema(description = "特力编码", requiredMode = Schema.RequiredMode.REQUIRED, example = "00630848420100000")
    private String teliCode;
}