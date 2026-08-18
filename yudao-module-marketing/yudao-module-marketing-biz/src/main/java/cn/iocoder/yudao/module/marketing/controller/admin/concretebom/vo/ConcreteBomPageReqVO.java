package cn.iocoder.yudao.module.marketing.controller.admin.concretebom.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 混凝土BOM分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ConcreteBomPageReqVO extends PageParam {

    @Schema(description = "车型（物料编码或车型名称）")
    private String vehicleModel;

    @Schema(description = "分解油缸（部件名称及路径）", example = "李四")
    private String cylinderName;

    @Schema(description = "SBP编码")
    private String sbpCode;

    @Schema(description = "配置（如数量等）")
    private String config;

    @Schema(description = "导入时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] importTime;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}