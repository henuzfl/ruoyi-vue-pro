package cn.iocoder.yudao.module.buyer.dal.dataobject.overseasinventory;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.math.BigDecimal;

@TableName("buyer_overseas_inventory")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OverseasInventoryDO extends BaseDO {

    @TableId(type = IdType.ASSIGN_ID)
    private BigDecimal id;
    private String warehouse;
    private String ownerCode;
    private String supplierCode;
    private String supplierName;
    private String itemCode;
    private String itemName;
    private String itemSpecification;
    private Long inventoryQuantity;
    private Long occupiedQuantity;
    private Long availableQuantity;
    private Long frozenQuantity;
}
