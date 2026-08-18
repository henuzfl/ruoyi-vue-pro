package cn.iocoder.yudao.module.wm.controller.admin.bom.vo;

import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - BOM分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class BomPageReqVO extends PageParam {

    @Schema(description = "工厂代码")
    private String werks;

    @Schema(description = "物料号")
    private String idnrk;

    @Schema(description = "物料名称")
    private String ojtxp;

    @Schema(description = "物料类型")
    private String mtart;

    @Schema(description = "BOM等级")
    private String stufe;

    @Schema(description = "BOM类型")
    private String bmtyp;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

    @Schema(description = "查询条件 - 父物料号")
    private String parentMaterial;

    @Schema(description = "查询条件 - 是否只查询顶层BOM")
    private Boolean topLevelOnly;

    // ✅ 添加 status 字段
    @Schema(description = "状态")
    private Integer status; // 可以是 Integer 或 String，根据你的业务需求
}