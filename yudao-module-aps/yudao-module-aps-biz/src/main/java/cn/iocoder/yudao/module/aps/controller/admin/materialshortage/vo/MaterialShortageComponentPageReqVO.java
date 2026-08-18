package cn.iocoder.yudao.module.aps.controller.admin.materialshortage.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "管理后台 - 组件缺口分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MaterialShortageComponentPageReqVO extends PageParam {

    @Schema(description = "组件物料编码")
    private String componentMaterialNo;

    @Schema(description = "组件名称")
    private String componentDesc;

    @Schema(description = "是否仅显示有缺口的")
    private Boolean onlyShortage;
}