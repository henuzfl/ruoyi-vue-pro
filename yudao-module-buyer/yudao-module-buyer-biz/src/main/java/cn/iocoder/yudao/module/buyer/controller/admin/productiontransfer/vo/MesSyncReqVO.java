package cn.iocoder.yudao.module.buyer.controller.admin.productiontransfer.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import javax.validation.constraints.NotBlank;

@Schema(description = "管理后台 - MES同步请求 VO")
@Data
public class MesSyncReqVO {

    @Schema(description = "工厂编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "6400")
    @NotBlank(message = "工厂编号不能为空")
    private String plantNo;

    @Schema(description = "开始时间", requiredMode = Schema.RequiredMode.REQUIRED, example = "2026-01-01")
    @NotBlank(message = "开始时间不能为空")
    private String beginTime;

    @Schema(description = "结束时间", requiredMode = Schema.RequiredMode.REQUIRED, example = "2026-03-31")
    @NotBlank(message = "结束时间不能为空")
    private String endTime;

    @Schema(description = "计划员", example = "")
    private String plannerName;

    @Schema(description = "订单号", example = "")
    private String orderNo;
}