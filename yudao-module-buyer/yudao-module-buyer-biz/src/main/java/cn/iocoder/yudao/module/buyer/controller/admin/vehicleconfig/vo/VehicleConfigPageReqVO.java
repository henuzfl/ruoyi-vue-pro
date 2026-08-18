package cn.iocoder.yudao.module.buyer.controller.admin.vehicleconfig.vo;

import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.Date;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 主机车型配置分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class VehicleConfigPageReqVO extends PageParam {

    @Schema(description = "导入日期（格式 yyyy-MM-dd）")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date importDate;

    @Schema(description = "车型")
    private String vehicleModel;

    @Schema(description = "2026年顺序号")
    private String seqNo2026;

    @Schema(description = "物料号")
    private String materialNo;

    @Schema(description = "订单号")
    private String orderNo;

    @Schema(description = "工厂")
    private String factory;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;
}