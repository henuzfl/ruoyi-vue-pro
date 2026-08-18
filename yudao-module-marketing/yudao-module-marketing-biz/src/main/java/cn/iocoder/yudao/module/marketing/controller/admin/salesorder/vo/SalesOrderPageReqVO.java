package cn.iocoder.yudao.module.marketing.controller.admin.salesorder.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

@Schema(description = "销售订单分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
public class SalesOrderPageReqVO extends PageParam {

    @Schema(description = "订单编号")
    private String orderNumber;

    @Schema(description = "物料编码")
    private String materialCode;

    @Schema(description = "售达方编码")
    private String soldToParty;

    @Schema(description = "销售组织")
    private String salesOrganization;

    @Schema(description = "工厂")
    private String plant;

    @Schema(description = "交货状态")
    private String deliveryStatus;

    @Schema(description = "订单日期范围")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date[] orderDate;

    @Schema(description = "最早交货日期范围")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date[] earliestDeliveryDate;

    @Schema(description = "订单数量（精确匹配）")
    private BigDecimal orderQuantity;
}