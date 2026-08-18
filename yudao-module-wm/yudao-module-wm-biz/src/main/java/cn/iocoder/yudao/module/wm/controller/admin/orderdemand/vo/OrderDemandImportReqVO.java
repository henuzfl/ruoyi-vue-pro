package cn.iocoder.yudao.module.wm.controller.admin.orderdemand.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;

@Schema(description = "管理后台 - 订单追溯需求导入 Excel VO")
@Data
public class OrderDemandImportReqVO {

    @Schema(description = "订单号", requiredMode = Schema.RequiredMode.REQUIRED, example = "ORD001")
    private String orderNo;

    @Schema(description = "追溯需求号", example = "TR001")
    private String traceDemandNo;

    @Schema(description = "物料号", requiredMode = Schema.RequiredMode.REQUIRED, example = "MAT001")
    private String materialNo;

    @Schema(description = "物料描述", example = "发动机总成")
    private String materialDescription;

    @Schema(description = "需求数量", example = "100")
    private BigDecimal demandQuantity;

    @Schema(description = "累计出库", example = "50")
    private BigDecimal outboundAccumulated;

    @Schema(description = "未结数量", example = "50")
    private BigDecimal openQuantity;

    @Schema(description = "状态", example = "0")
    private Integer status;

    @Schema(description = "备注", example = "加急")
    private String remark;
}