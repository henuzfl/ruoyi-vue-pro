package cn.iocoder.yudao.module.marketing.controller.admin.concretebom.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 混凝土BOM差异对比 Response VO")
@Data
public class ConcreteBomCompareRespVO {

    @Schema(description = "车型")
    private String vehicleModel;

    @Schema(description = "SBP编码")
    private String sbpCode;

    @Schema(description = "当前批次配置")
    private String currentConfig;

    @Schema(description = "上一批次配置")
    private String previousConfig;

    @Schema(description = "差异状态：无差异/新增/存在差异")
    private String state;
}