package cn.iocoder.yudao.module.aps.controller.admin.ordercomponentprogressreport.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.Date;

@Schema(description = "管理后台 - 订单组件需求进度报表分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
public class OrderComponentProgressReportPageReqVO extends PageParam {

    @Schema(description = "生产订单号")
    private String productionOrderNo;

    @Schema(description = "组件物料号")
    private String componentMaterialNo;

    @Schema(description = "组件物料描述")
    private String materialDesc;

    @Schema(description = "生产车间")
    private String productionWorkshop;

    @Schema(description = "计划开始日期开始")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date scheduledDateStart;

    @Schema(description = "计划开始日期结束")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date scheduledDateEnd;

    @Schema(description = "计划完成日期开始")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date basicEndDateStart;

    @Schema(description = "计划完成日期结束")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date basicEndDateEnd;
}