package cn.iocoder.yudao.module.aps.controller.admin.assemblyorderprogress.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "管理后台 - 总成进度分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AssemblyOrderProgressPageReqVO extends PageParam {

    @Schema(description = "时间维度：day/week/month", required = true)
    private String dimension = "week"; // 默认周

    @Schema(description = "开始日期")
    private String startDate;

    @Schema(description = "结束日期")
    private String endDate;

    @Schema(description = "总成物料编码")
    private String materialCode;

    @Schema(description = "总成物料描述")
    private String materialDesc;

    @Schema(description = "车间")
    private String workshop;
}