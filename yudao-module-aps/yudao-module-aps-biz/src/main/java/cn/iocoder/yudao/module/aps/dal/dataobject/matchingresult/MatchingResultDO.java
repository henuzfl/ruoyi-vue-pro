package cn.iocoder.yudao.module.aps.dal.dataobject.matchingresult;

import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 主计划物料需求匹配 DO
 *
 * @author 柳文
 */
@TableName("aps_matching_result")
@KeySequence("aps_matching_result_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MatchingResultDO extends BaseDO {

    /**
     * 主键ID（雪花算法生成）
     */
    @TableId
    private BigDecimal id;
    /**
     * 订单号
     */
    private String orderNo;
    /**
     * 排产时间
     */
    private Date scheduleTime;
    /**
     * 物料编码
     */
    private String materialCode;
    /**
     * 物料描述
     */
    private String materialDesc;
    /**
     * 责任车间（总成）
     */
    private String workshop;
    /**
     * 数量
     */
    private BigDecimal quantity;
    /**
     * 已完成数量
     */
    private BigDecimal completedQuantity;
    /**
     * 库存
     */
    private BigDecimal stock;
    /**
     * 转序（数量）
     */
    private BigDecimal transferOrder;
    /**
     * 零部件订单
     */
    private String componentOrder;
    /**
     * 零部件编码
     */
    private String componentCode;
    /**
     * 零部件描述
     */
    private String componentDesc;
    /**
     * 责任车间（零部件）
     */
    private String componentWorkshop;
    /**
     * 基本开始日期
     */
    private Date basicStartDate;
    /**
     * 需求数量（零部件）
     */
    private BigDecimal requiredQuantity;
    /**
     * 本次未完成数量
     */
    private BigDecimal unfinishedQuantity;
    /**
     * 采购物料
     */
    private String purchaseMaterial;
    /**
     * 采购物料描述
     */
    private String purchaseMaterialDesc;
    /**
     * 需求数量（采购）
     */
    private BigDecimal purchaseRequiredQty;
    /**
     * 已配送数量
     */
    private BigDecimal deliveredQuantity;
    /**
     * 待配送数量
     */
    private BigDecimal toDeliverQuantity;
    /**
     * 采购订单
     */
    private String purchaseOrder;
    /**
     * 行号
     */
    private Long lineNumber;
    /**
     * 下单日期
     */
    private Date orderDate;
    /**
     * 要求交货日期
     */
    private Date requiredDeliveryDate;
    /**
     * 实际到货日期
     */
    private Date actualArrivalDate;
    /**
     * 供应商名称
     */
    private String supplierName;
    /**
     * 未清订单数量
     */
    private BigDecimal openOrderQuantity;
    /**
     * 分配需求数量（按未清订单顺序拆分后的需求）
     */
    private BigDecimal allocatedRequiredQty;

    /**
     * 剩余需采购数量（分配需求 - 已配送 - 待配送，与0取较大值）
     */
    private BigDecimal remainRequiredQty;

    /**
     * 大小/尺寸
     */
    private String sizeDimension;

    /**
     * 齐套数量
     */
    private BigDecimal kitQty;

    /**
     * 物料齐套数量
     */
    private BigDecimal kitQtySingle;

}