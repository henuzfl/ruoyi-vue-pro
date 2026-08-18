package cn.iocoder.yudao.module.wm.controller.admin.realtimestock.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "SAP库存同步结果")
public class SyncResultVO {

    @Schema(description = "是否全部成功")
    private Boolean allSuccess;

    @Schema(description = "总处理物料数")
    private Integer total;

    @Schema(description = "成功数量")
    private Integer successCount;

    @Schema(description = "失败数量")
    private Integer failCount;

    @Schema(description = "失败明细列表")
    private List<FailureDetail> failures;

    @Data
    public static class FailureDetail {
        @Schema(description = "物料号")
        private String materialNumber;

        @Schema(description = "错误信息")
        private String errorMessage;
    }
}