package cn.iocoder.yudao.module.buyer.controller.admin.hostrequirementcomparisondiff.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "管理后台 - 主机需求对比分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class HostRequirementComparisonPageReqVO extends PageParam {

    @Schema(description = "车型")
    private String productModel;

    @Schema(description = "2026年顺序号")
    private String seqNo2026;

    @Schema(description = "中联订单号")
    private String bareMachineOrderNo;

    @Schema(description = "主机图号")
    private String materialNo;

    @Schema(description = "当前版本日期（格式 yyyy-MM-dd）")
    private String currentDate;

    @Schema(description = "对比版本日期（格式 yyyy-MM-dd）")
    private String compareDate;
}