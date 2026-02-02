package cn.iocoder.yudao.module.buyer.controller.admin.buyermaterial.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 备料明细汇总分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class buyerMaterialPageReqVO extends PageParam {

    @Schema(description = "需求物料")
    private String reqMaterial;

    @Schema(description = "客户")
    private String customer;

    @Schema(description = "车型")
    private String vehicleModel;

    @Schema(description = "总成需求数量")
    private String assemblyQty;

    @Schema(description = "采购子件物料")
    private String compMaterial;

    @Schema(description = "采购子件描述")
    private String compDesc;

    @Schema(description = "规格型号")
    private String specModel;

    @Schema(description = "单台用量")
    private String unitUsage;

    @Schema(description = "组件需求数量")
    private String compDemandQty;

    @Schema(description = "备料数量本月")
    private String preparedQty1;

    @Schema(description = "备料数量近一个月")
    private String preparedQty2;

    @Schema(description = "备料数量近二个月")
    private String preparedQty3;

    @Schema(description = "备料数量近三个月")
    private String preparedQty4;

    @Schema(description = "备料数量近四个月")
    private String preparedQty5;

    @Schema(description = "实时库存")
    private String stockQty;

    @Schema(description = "差值")
    private String difference;

    @Schema(description = "库存状态")
    private String stockStatus;

    @Schema(description = "供应商")
    private String supplier;

    @Schema(description = "采购员")
    private String buyer;

    @Schema(description = "采购组")
    private String procGroup;

    @Schema(description = "需求月份")
    private String demandMonth;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;
}