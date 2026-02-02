package cn.iocoder.yudao.module.buyer.dal.dataobject.buyerstock;

import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 供应商库存 DO
 *
 * @author 柳文
 */
@TableName("supplier_stock")
@KeySequence("supplier_stock_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class buyerStockDO extends BaseDO {

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
     * 库存地点
     */
    private String stockLocation;
    /**
     * 库存数量
     */
    private BigDecimal stockQuantity;
    /**
     * 备料数量
     */
    private BigDecimal preparedQuantity;
    /**
     * 库存月份
     */
    private String stockMonth;
    /**
     * 状态（0=正常，1=停用）
     */
    private Integer status;
    /**
     * 备注
     */
    private String remark;

}