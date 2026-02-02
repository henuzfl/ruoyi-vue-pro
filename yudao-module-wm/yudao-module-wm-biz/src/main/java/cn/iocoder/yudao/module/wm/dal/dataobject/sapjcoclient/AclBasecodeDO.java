package cn.iocoder.yudao.module.wm.dal.dataobject.orderdemand;

import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 订单追溯需求 DO
 *
 * @author 柳文
 */
@TableName("trace_order_demand")
@KeySequence("trace_order_demand_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDemandDO extends BaseDO {

    /**
     * 编号
     */
    @TableId
    private BigDecimal id;
    /**
     * 订单号
     */
    private String orderNo;
    /**
     * 追溯需求号
     */
    private String traceDemandNo;
    /**
     * 物料编码
     */
    private String materialNo;
    /**
     * 物料描述
     */
    private String materialDescription;
    /**
     * 需求数量
     */
    private BigDecimal demandQuantity;
    /**
     * 出库累计数
     */
    private BigDecimal outboundAccumulated;
    /**
     * 未清数量
     */
    private BigDecimal openQuantity;
    /**
     * 状态（0=待处理 1=部分完成 2=已完成）
     */
    private Short status;
    /**
     * 备注
     */
    private String remark;

}