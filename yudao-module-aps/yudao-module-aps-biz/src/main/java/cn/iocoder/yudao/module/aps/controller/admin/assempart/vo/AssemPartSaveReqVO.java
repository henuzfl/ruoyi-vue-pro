package cn.iocoder.yudao.module.aps.controller.admin.assempart.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;

@Schema(description = "管理后台 - 总成与子件关联表管理新增/修改 Request VO")
@Data
public class AssemPartSaveReqVO {

    @Schema(description = "总成订单号")
    private String orderNo;        // 原 assemOrderNo

    @Schema(description = "总成数量")
    private BigDecimal quantity;   // 原 assemQty

    @Schema(description = "零部件订单号")
    private String componentOrder; // 原 partOrderNo

    @Schema(description = "零部件数量")
    private BigDecimal allocQty;

    @Schema(description = "计划时间（总成计划日期）")
    private Date scheduleTime;     // 新增字段

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "4653")
    private Long id;
}