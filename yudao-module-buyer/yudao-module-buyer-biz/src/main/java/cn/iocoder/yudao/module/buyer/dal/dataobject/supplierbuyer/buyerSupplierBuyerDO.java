package cn.iocoder.yudao.module.buyer.dal.dataobject.supplierbuyer;

import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 物料供应商采购员对应 DO
 *
 * @author 芋道源码
 */
@TableName("material_supplier_buyer")
@KeySequence("material_supplier_buyer_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class buyerSupplierBuyerDO extends BaseDO {

    /**
     * 主键编号
     */
    @TableId
    private Long id;
    /**
     * 物料编号
     */
    private String materialNo;
    /**
     * 物料描述
     */
    private String materialDesc;
    /**
     * 供应商
     */
    private String supplier;
    /**
     * 采购员
     */
    private String buyer;
    /**
     * 采购组
     */
    private String procurementGroup;
    /**
     * 物料类型
     */
    private String materialType;
    /**
     * 物料分类
     */
    private String materialCategory;
    /**
     * 采购类型
     */
    private String procurementType;
    /**
     * 状态（0=正常，1=停用）
     */
    private Integer status;
    /**
     * 备注
     */
    private String remark;

}