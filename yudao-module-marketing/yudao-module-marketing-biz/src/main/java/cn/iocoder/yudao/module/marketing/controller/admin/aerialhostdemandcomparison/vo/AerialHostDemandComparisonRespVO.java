package cn.iocoder.yudao.module.marketing.controller.admin.aerialhostdemandcomparison.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;

@Schema(description = "管理后台 - 主机需求对比分析 Response VO")
@Data
public class AerialHostDemandComparisonRespVO {

    @Schema(description = "物料编码")
    @ExcelProperty("物料编码")
    private String materialCode;

    @Schema(description = "特力编码")
    @ExcelProperty("特力编码")
    private String teliCode;

    @Schema(description = "物料描述")
    @ExcelProperty("物料描述")
    private String materialDesc;

    @Schema(description = "产品型号")
    @ExcelProperty("产品型号")
    private String productModel;

    @Schema(description = "上线计划日期")
    @ExcelProperty("上线计划日期")
    private String onlinePlan;

    @Schema(description = "当前批次数量")
    @ExcelProperty("当前批次数量")
    private BigDecimal currentQty;

    @Schema(description = "对比批次数量")
    @ExcelProperty("对比批次数量")
    private BigDecimal compareQty;

    @Schema(description = "差异（当前-对比）")
    @ExcelProperty("差异")
    private BigDecimal diffQty;
}