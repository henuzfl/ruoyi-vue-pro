package cn.iocoder.yudao.module.buyer.dal.dataobject.buyertimestock;

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
 * 实时库存 DO
 *
 * @author 柳文
 */
@TableName("real_time_stock")
@KeySequence("real_time_stock_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class buyerTimeStockDO extends BaseDO {

    /**
     * 主键编号
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID) // 使用雪花算法
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
     * 库存地点
     */
    private String stockLocation;
    /**
     * 库存数量
     */
    private BigDecimal stockQuantity;
    /**
     * 可用数量
     */
    private BigDecimal availableQuantity;
    /**
     * 状态（0=正常，1=停用）
     */
    private Integer status;
    /**
     * 备注
     */
    private String remark;

}