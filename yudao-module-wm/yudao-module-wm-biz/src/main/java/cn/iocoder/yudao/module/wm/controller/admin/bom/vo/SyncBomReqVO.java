package cn.iocoder.yudao.module.wm.controller.admin.bom.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class SyncBomReqVO {
    @Schema(description = "物料号")
    private String materialNumber;

    @Schema(description = "工厂")
    private String plant;

    @Schema(description = "是否强制同步（覆盖已存在数据）")
    private Boolean forceSync;

    @Schema(description = "同步范围：ALL-全部，CHANGED-仅变更")
    private String syncScope;
}
