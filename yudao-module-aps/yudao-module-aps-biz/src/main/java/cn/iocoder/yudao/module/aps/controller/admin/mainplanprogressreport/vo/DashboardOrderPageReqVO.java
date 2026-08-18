package cn.iocoder.yudao.module.aps.controller.admin.mainplanprogressreport.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "驾驶舱订单分页查询")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DashboardOrderPageReqVO extends PageParam {
    @Schema(description = "开始时间")
    private String startDate;
    @Schema(description = "结束时间")
    private String endDate;
    @Schema(description = "车间")
    private String workshop;
    @Schema(description = "供应商")
    private String supplier;
    @Schema(description = "物料编码")
    private String materialCode;
    @Schema(description = "仅看异常")
    private Boolean onlyAbnormal;
}