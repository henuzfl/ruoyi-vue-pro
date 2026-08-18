package cn.iocoder.yudao.module.buyer.controller.admin.productiontransfer.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - MES转序单信息新增/修改 Request VO")
@Data
public class ProductionTransferSaveReqVO {

    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "24805")
    private BigDecimal id;

    @Schema(description = "订单号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "订单号不能为空")
    private String orderNo;

    @Schema(description = "物料编码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "物料编码不能为空")
    private String materialCode;

    @Schema(description = "物料描述")
    private String materialDesc;

    @Schema(description = "生产调度员")
    private String productionScheduler;

    @Schema(description = "转序发起人")
    private String transferInitiator;

    @Schema(description = "发起日期")
    private LocalDateTime initiatorDate;

    @Schema(description = "数量")
    private BigDecimal quantity;

    @Schema(description = "转序单号")
    private String transferNo;

    @Schema(description = "计划批次")
    private String batchNo;

    @Schema(description = "签收人")
    private String signer;

    @Schema(description = "签收时间")
    private LocalDateTime signTime;
}