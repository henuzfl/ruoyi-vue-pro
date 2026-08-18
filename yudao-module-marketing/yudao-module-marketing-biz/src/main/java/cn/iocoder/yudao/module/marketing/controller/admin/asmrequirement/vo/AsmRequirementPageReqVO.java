package cn.iocoder.yudao.module.marketing.controller.admin.asmrequirement.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 营销总成需求分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AsmRequirementPageReqVO extends PageParam {
    @Schema(description = "主机单位")
    private String hostUnit;

    @Schema(description = "车型")
    private String vehicleModel;

    @Schema(description = "总成物料编码")
    private String assemblyMaterialNo;

    @Schema(description = "总成物料名称")
    private String mainMaterialDesc;

    @Schema(description = "需求数量")
    private BigDecimal requireQuantity;

    @Schema(description = "需求日期")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] requireDate;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}