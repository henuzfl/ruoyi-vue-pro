package cn.iocoder.yudao.module.marketing.controller.admin.aerialhostdemandcomparison.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;

@Schema(description = "管理后台 - 主机需求对比分析（周级别）Response VO")
@Data
public class AerialHostDemandComparisonWeekRespVO {
    @Schema(description = "物料编码")
    private String materialCode;
    @Schema(description = "特力编码")
    private String teliCode;
    @Schema(description = "物料描述")
    private String materialDesc;
    @Schema(description = "产品型号")
    private String productModel;
    @Schema(description = "周次标识（如 2026-18W）")
    private String weekKey;
    @Schema(description = "周起始日期")
    private String weekStartDate;
    @Schema(description = "周结束日期")
    private String weekEndDate;
    @Schema(description = "当前批次数量")
    private BigDecimal currentQty;
    @Schema(description = "对比批次数量")
    private BigDecimal compareQty;
    @Schema(description = "差异")
    private BigDecimal diffQty;
}