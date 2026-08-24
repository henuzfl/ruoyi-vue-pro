package cn.iocoder.yudao.module.buyer.controller.admin.overseasinventory.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Schema(description = "管理后台 - 驻外库存分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class OverseasInventoryPageReqVO extends PageParam {
    private String ownerCode;
    private String supplierCode;
    private String itemCode;
    private String itemName;
}
