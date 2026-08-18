package cn.iocoder.yudao.module.aps.controller.admin.monthlysupplydemand.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 月度供需总览表响应 VO
 *
 * @author 柳文
 */
@Schema(description = "管理后台 - 月度供需总览表响应 VO")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MonthlySupplyDemandSummaryRespVO {

    @Schema(description = "主键编号")
    private Long id;

    @Schema(description = "总成物料号")
    private String assemblyMaterialNo;

    @Schema(description = "物料描述")
    private String materialDesc;

    @Schema(description = "月份(YYYY-MM)")
    private String scheduledDate;

    @Schema(description = "需求数量")
    private BigDecimal requireQuantity;

    @Schema(description = "销售出库数量")
    private BigDecimal salesOutQuantity;

    @Schema(description = "实时库存")
    private BigDecimal stockQuantity;

    @Schema(description = "在制/在途数量")
    private BigDecimal wip;

    @Schema(description = "净需求")
    private BigDecimal netRequirement;

    @Schema(description = "主计划数量")
    private BigDecimal scheduledQuantity;

    @Schema(description = "缺口")
    private BigDecimal shortage;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}