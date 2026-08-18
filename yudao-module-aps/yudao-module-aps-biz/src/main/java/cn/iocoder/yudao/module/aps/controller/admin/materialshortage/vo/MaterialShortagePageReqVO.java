package cn.iocoder.yudao.module.aps.controller.admin.materialshortage.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "管理后台 - 物料缺口分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MaterialShortagePageReqVO extends PageParam {

    @Schema(description = "成品物料编码")
    private String mainMaterialNo;

    @Schema(description = "成品物料名称")
    private String materialDesc;

    @Schema(description = "是否仅显示有缺口的")
    private Boolean onlyShortage;
}