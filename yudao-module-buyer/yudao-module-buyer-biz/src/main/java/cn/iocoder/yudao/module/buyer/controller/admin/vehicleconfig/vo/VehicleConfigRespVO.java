package cn.iocoder.yudao.module.buyer.controller.admin.vehicleconfig.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.ExcelProperty;

@Schema(description = "管理后台 - 主机车型配置 Response VO")
@Data
public class VehicleConfigRespVO {

    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "10668")
    @ExcelProperty("主键ID")
    private String id;

    @Schema(description = "订单号")
    @ExcelProperty("订单号")
    private String orderNo;

    @Schema(description = "导入日期")
    @ExcelProperty("导入日期")
    private String importDate;

    @Schema(description = "车型")
    @ExcelProperty("车型")
    private String vehicleModel;

    @Schema(description = "2025年顺序号")
    @ExcelProperty("2025年顺序号")
    private String seqNo2025;

    @Schema(description = "2026年顺序号")
    @ExcelProperty("2026年顺序号")
    private String seqNo2026;

    @Schema(description = "要求到货时间")
    @ExcelProperty("要求到货时间")
    private String requiredArrivalTime;

    @Schema(description = "物料描述")
    @ExcelProperty("物料描述")
    private String materialDesc;

    @Schema(description = "配额1")
    @ExcelProperty("配额1")
    private Long quota1;

    @Schema(description = "配额2")
    @ExcelProperty("配额2")
    private String quota2;

    @Schema(description = "物料号")
    @ExcelProperty("物料号")
    private String materialNo;

    @Schema(description = "工厂")
    @ExcelProperty("工厂")
    private String factory;

    @Schema(description = "需求数量")
    @ExcelProperty("需求数量")
    private BigDecimal requiredQuantity;

    @Schema(description = "已交货数量")
    @ExcelProperty("已交货数量")
    private BigDecimal deliveredQuantity;

    @Schema(description = "创建时间")
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;
}