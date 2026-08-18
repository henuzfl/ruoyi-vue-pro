package cn.iocoder.yudao.module.aps.controller.admin.mainplancomponentprogressreport.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 组件物料需求报表分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
public class MainPlanComponentProgressReportPageReqVO extends PageParam {

    @Schema(description = "主物料号")
    private String assemblyMaterialNo;

    @Schema(description = "组件物料号")
    private String componentMaterialNo;

    @Schema(description = "物料描述")
    private String materialDesc;

    @Schema(description = "生产车间")
    private String productionWorkshop;

    @Schema(description = "排产时间开始")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime scheduledDateStart;

    @Schema(description = "排产时间结束")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime scheduledDateEnd;
}