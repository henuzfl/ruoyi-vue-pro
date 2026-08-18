package cn.iocoder.yudao.module.buyer.dal.dataobject.deliver;

import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 配送与采购报表 DO
 *
 * @author 柳文
 */
@TableName("buyer_deliver")
@KeySequence("buyer_deliver_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliverDO extends BaseDO {

    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    /**
     * 工厂
     */
    private String plant;
    /**
     * 配送单号
     */
    private String deliveryOrderNo;
    /**
     * 配送日期
     */
    private LocalDateTime deliveryDate;
    /**
     * 创建日期（业务）
     */
    private LocalDateTime creationDate;
    /**
     * 创建时间（业务）
     */
    private String creationTime;
    /**
     * 创建人（业务）
     */
    private String createdBy;
    /**
     * 最后更新日期（业务）
     */
    private LocalDateTime lastUpdateDate;
    /**
     * 最后更新时间（业务）
     */
    private String lastUpdateTime;
    /**
     * 最后更新人（业务）
     */
    private String lastUpdatedBy;
    /**
     * 生产订单号
     */
    private String partOrderNo;
    /**
     * 生产调度员
     */
    private String productionWorkshop;
    /**
     * 主物料编码
     */
    private String partMatCode;
    /**
     * 主物料描述
     */
    private String partMatDesc;
    /**
     * 预留号
     */
    private String reservationNo;
    /**
     * 预留项目
     */
    private Long reservationItem;
    /**
     * 应发数量
     */
    private BigDecimal plannedIssueQty;
    /**
     * 已发数量
     */
    private BigDecimal deliveredQty;
    /**
     * 未配送数量
     */
    private BigDecimal undeliveredQty;
    /**
     * 物料号
     */
    private String buyerMaterialNo;
    /**
     * 旧物料号
     */
    private String oldMaterialNo;
    /**
     * 物料描述
     */
    private String buyerMaterialDesc;
    /**
     * 供应商编码(配送单)
     */
    private String deliverySupplierCode;
    /**
     * 供应商描述(配送单)
     */
    private String deliverySupplierName;
    /**
     * 库存是否满足
     */
    private String stockSufficientFlag;
    /**
     * 总库存
     */
    private BigDecimal totalStockQty;
    /**
     * 本次消耗库存
     */
    private BigDecimal currentStockConsumeQty;
    /**
     * 库存地点(配送)
     */
    private String deliveryStorageLoc;
    /**
     * 采购是否满足
     */
    private String poSufficientFlag;
    /**
     * 采购订单号
     */
    private String buyerOrderNo;
    /**
     * 采购项目
     */
    private Long lineItem;
    /**
     * 需求跟踪号
     */
    private String requirementTrackingNo;
    /**
     * 采购订单数量
     */
    private BigDecimal orderQty;
    /**
     * 采购订单未收货数量
     */
    private BigDecimal openQty;
    /**
     * 本次交货数量
     */
    private BigDecimal receivedQty;
    /**
     * 采购订单交货日期
     */
    private LocalDateTime poDeliveryDate;
    /**
     * 供应商(采购订单)
     */
    private String poSupplierCode;
    /**
     * 供应商描述(采购订单)
     */
    private String supplierDesc;
    /**
     * 采购组
     */
    private String buyerPurchasingGroup;
    /**
     * 下单采购员
     */
    private String orderingBuyer;
    /**
     * 交货采购员
     */
    private String deliveryBuyer;

}