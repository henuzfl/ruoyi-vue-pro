package cn.iocoder.yudao.module.marketing.controller.admin.aerialboombom.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 高机臂式/剪叉BOM物料清单分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AerialBoomBomPageReqVO extends PageParam {

    @Schema(description = "物料编码")
    private String materialCode;

    @Schema(description = "物料描述")
    private String materialDesc;

    @Schema(description = "供应商")
    private String supplier;

    @Schema(description = "JIT标识（1表示JIT物料）")
    private String jitFlag;

    @Schema(description = "是否颜色管理（X表示是）")
    private String colorManagement;

    @Schema(description = "是否按需供货（X表示是）")
    private String supplyOnDemand;

    @Schema(description = "适配机型（多机型逗号分隔）")
    private String applicableModel;

    @Schema(description = "备注", example = "你说的对")
    private String remark;

    @Schema(description = "产品型号（如ZA10RJE）")
    private String productModel;

    @Schema(description = "精准BOM（如ZA10RJE-001）")
    private String preciseBom;

    @Schema(description = "数量")
    private BigDecimal quantity;

    @Schema(description = "物料来源分类（臂式专用物料/剪叉专用物料/剪叉和臂式共用物料/走车物资及选配件）")
    private String sourceCategory;

    @Schema(description = "板块（默认高机臂式）")
    private String plate;

    @Schema(description = "导入批次时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] importTime;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}