package cn.iocoder.yudao.module.buyer.controller.admin.hostrequirementcomparison.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 主机需求对比新增/修改 Request VO")
@Data
public class HostRequirementComparisonSaveReqVO {

    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private Long id;

    @Schema(description = "2026-2-12版本（底盘上线计划日期）")
    private String chassisOnlinePlanDate;

    @Schema(description = "车型")
    private String productModel;

    @Schema(description = "2025年顺序号")
    private String seqNo2025;

    @Schema(description = "2026年顺序号")
    private String seqNo2026;

    @Schema(description = "中联订单号")
    private String bareMachineOrderNo;

    @Schema(description = "底盘订单号")
    private String drivingUnitOrderNo;

    @Schema(description = "配额2026.1.25")
    private BigDecimal quota;

    @Schema(description = "台套")
    private BigDecimal unitQuantity;

    @Schema(description = "主机图号")
    private String materialNo;

    @Schema(description = "特力图号")
    private String teliCode;

    @Schema(description = "需配数量")
    private BigDecimal requiredQuantity;

    @Schema(description = "领料日期")
    private LocalDateTime pickingDate;

    @Schema(description = "已领数量")
    private BigDecimal pickedQuantity;

    @Schema(description = "结否")
    private String closedStatus;

    @Schema(description = "分类")
    private String category;

    @Schema(description = "主机计划下达时间")
    private String hostPlanReleaseTime;
}