package cn.iocoder.yudao.module.buyer.controller.admin.vehicleconfig.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import javax.validation.constraints.*;
import java.math.BigDecimal;

@Schema(description = "管理后台 - 主机车型配置新增/修改 Request VO")
@Data
public class VehicleConfigSaveReqVO {

    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "10668")
    private BigDecimal id;

    @Schema(description = "订单号")
    private String orderNo;

    @Schema(description = "车型")
    private String vehicleModel;

    @Schema(description = "2025年顺序号")
    private String seqNo2025;

    @Schema(description = "2026年顺序号")
    private String seqNo2026;

    @Schema(description = "要求到货时间")
    private String requiredArrivalTime;

    @Schema(description = "物料描述")
    private String materialDesc;

    @Schema(description = "配额1")
    private Long quota1;

    @Schema(description = "配额2")
    private String quota2;

    @Schema(description = "物料号")
    private String materialNo;

    @Schema(description = "工厂")
    private String factory;

    @Schema(description = "需求数量")
    private BigDecimal requiredQuantity;

    @Schema(description = "已交货数量")
    private BigDecimal deliveredQuantity;
}