package cn.iocoder.yudao.module.buyer.controller.admin.vehicleconfig.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;

@Schema(description = "管理后台 - 主机车型配置 Excel 导入 VO")
@Data
public class VehicleConfigImportReqVO {

    @Schema(description = "导入日期（年月，格式 yyyy-M）", example = "2026-3")
    private String importDate;

    @Schema(description = "订单号", example = "1010401046")
    private String orderNo;

    @Schema(description = "车型", requiredMode = Schema.RequiredMode.REQUIRED, example = "ZTC120V431R（智慧城）")
    private String vehicleModel;

    @Schema(description = "2025年顺序号", example = "2025-18/")
    private String seqNo2025;

    @Schema(description = "2026年顺序号", requiredMode = Schema.RequiredMode.REQUIRED, example = "2026-6/")
    private String seqNo2026;

    @Schema(description = "要求到货时间", example = "4月11日")
    private String requiredArrivalTime;

    @Schema(description = "物料描述", example = "伸缩油缸\\")
    private String materialDesc;

    @Schema(description = "配额1", example = "6400")
    private Long quota1;

    @Schema(description = "配额2", example = "6400")
    private String quota2;

    @Schema(description = "物料号", requiredMode = Schema.RequiredMode.REQUIRED, example = "00630588420100000")
    private String materialNo;

    @Schema(description = "工厂", example = "2088")
    private String factory;

    @Schema(description = "需求数量", example = "1.000")
    private BigDecimal requiredQuantity;

    @Schema(description = "已交货数量", example = "0")
    private BigDecimal deliveredQuantity;
}