package cn.iocoder.yudao.module.buyer.controller.admin.buyerinput.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 需求输入分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class buyerInputPageReqVO extends PageParam {

    @Schema(description = "客户")
    private String customer;

    @Schema(description = "车型")
    private String vehicleModel;

    @Schema(description = "总成物料")
    private String assemblyMaterial;

    @Schema(description = "总成数量")
    private BigDecimal assemblyQuantity;

    @Schema(description = "需求月份")
    private String demandMonth;

    @Schema(description = "状态（0=正常，1=停用）", example = "1")
    private Short status;

    @Schema(description = "备注", example = "你说的对")
    private String remark;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}