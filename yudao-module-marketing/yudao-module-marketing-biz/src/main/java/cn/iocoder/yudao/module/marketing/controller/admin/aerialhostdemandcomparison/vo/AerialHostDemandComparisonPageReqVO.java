package cn.iocoder.yudao.module.marketing.controller.admin.aerialhostdemandcomparison.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Schema(description = "管理后台 - 主机需求对比分析分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
public class AerialHostDemandComparisonPageReqVO extends PageParam {

    @Schema(description = "当前导入批次时间（格式 yyyy-MM-dd）", requiredMode = Schema.RequiredMode.REQUIRED)
    private String currentDate;

    @Schema(description = "对比导入批次时间（格式 yyyy-MM-dd）", requiredMode = Schema.RequiredMode.REQUIRED)
    private String compareDate;

    @Schema(description = "物料编码（模糊查询）")
    private String materialCode;

    @Schema(description = "产品型号（模糊查询）")
    private String productModel;

    @Schema(description = "板块")
    private String Plate;
}