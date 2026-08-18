package cn.iocoder.yudao.module.marketing.controller.admin.aerialboomdplan.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 高机臂式日计划分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AerialBoomDplanPageReqVO extends PageParam {

    @Schema(description = "线别", example = "1")
    private String lineType;

    @Schema(description = "精准车型")
    private String preciseModel;

    @Schema(description = "产品型号")
    private String productModel;

    @Schema(description = "ZPS型号")
    private String zpsModel;

    @Schema(description = "精准BOM")
    private String preciseBom;

    @Schema(description = "车号")
    private String carNo;

    @Schema(description = "订单号")
    private String orderNo;

    @Schema(description = "备注", example = "随便")
    private String remark;

    @Schema(description = "内外贸版本")
    private String tradeVersion;

    @Schema(description = "台份", example = "25434")
    private Integer unitCount;

    @Schema(description = "上线计划")
    private LocalDateTime onlinePlan;

    @Schema(description = "成台计划")
    private LocalDateTime completePlan;

    @Schema(description = "报缴日期")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] reportDate;

    @Schema(description = "国家")
    private String country;

    @Schema(description = "合同号")
    private String contractNo;

    @Schema(description = "营销通知时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private String[] marketingNoticeTime;

    @Schema(description = "订单开立时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] orderCreateTime;

    @Schema(description = "板块")
    private String plate;

    @Schema(description = "导入批次时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] importTime;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}