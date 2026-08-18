// MainPlanProgressReportPageReqVO.java
package cn.iocoder.yudao.module.aps.controller.admin.mainplanprogressreport.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;
import java.util.Date;

@Schema(description = "管理后台 - 主计划进度报表分页请求 VO")
@Data
@EqualsAndHashCode(callSuper = true)
public class MainPlanProgressReportPageReqVO extends PageParam {

    @Schema(description = "生产订单号")
    private String productionOrderNo;

    @Schema(description = "总成物料号")
    private String assemblyMaterialNo;

    @Schema(description = "主物料描述")
    private String mainMaterialDesc;

    @Schema(description = "排产时间开始")
    private Date scheduledDateStart;

    @Schema(description = "排产时间结束")
    private Date scheduledDateEnd;

    @Schema(description = "排产数量")
    private BigDecimal scheduledQuantity;

    @Schema(description = "生产车间")
    private String productionWorkshop;

    @Schema(description = "创建时间开始")
    private Date createTimeStart;

    @Schema(description = "创建时间结束")
    private Date createTimeEnd;
}