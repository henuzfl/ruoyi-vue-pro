package cn.iocoder.yudao.module.wm.controller.admin.realtimestock.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * SAP库存批量同步请求VO
 */
@Data
public class SapStockBatchSyncReqVO {

    @Schema(description = "同步任务列表", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "同步任务列表不能为空")
    private List<SapStockSyncReqVO> syncTasks;

    @Schema(description = "是否覆盖现有数据", example = "false")
    private Boolean overwrite = false;
}
// [file content end]