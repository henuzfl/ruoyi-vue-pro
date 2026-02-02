package cn.iocoder.yudao.module.buyer.controller.admin.supplierbuyer.vo;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 物料供应商采购员对应分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class buyerSupplierBuyerPageReqVO extends PageParam {

    @Schema(description = "物料编号")
    private String materialNo;

    @Schema(description = "物料描述")
    private String materialDesc;

    @Schema(description = "供应商")
    private String supplier;

    @Schema(description = "采购员")
    private String buyer;

    @Schema(description = "采购组")
    private String procurementGroup;

    @Schema(description = "物料类型", example = "2")
    private String materialType;

    @Schema(description = "物料分类")
    private String materialCategory;

    @Schema(description = "采购类型", example = "2")
    private String procurementType;

    @Schema(description = "状态（0=正常，1=停用）", example = "1")
    private Integer status;

    @Schema(description = "备注", example = "随便")
    private String remark;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}