package cn.iocoder.yudao.module.buyer.controller.admin.buyerstock.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 供应商库存分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class buyerStockPageReqVO extends PageParam {

    @Schema(description = "物料编号")
    private String materialNo;

    @Schema(description = "物料描述")
    private String materialDesc;

    @Schema(description = "供应商")
    private String supplier;

    @Schema(description = "库存地点")
    private String stockLocation;

    @Schema(description = "库存数量")
    private BigDecimal stockQuantity;

    @Schema(description = "备料数量")
    private BigDecimal preparedQuantity;

    @Schema(description = "库存月份")
    private String stockMonth;

    @Schema(description = "状态（0=正常，1=停用）", example = "1")
    private Integer status;

    @Schema(description = "备注", example = "随便")
    private String remark;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}