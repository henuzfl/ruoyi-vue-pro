package cn.iocoder.yudao.module.buyer.dal.dataobject.productiontransfer;

import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * MES转序单信息 DO
 *
 * @author 柳文
 */
@TableName("buyer_production_transfer")
@KeySequence("buyer_production_transfer_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductionTransferDO extends BaseDO {

    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private BigDecimal id;
    /**
     * 订单号
     */
    private String orderNo;
    /**
     * 物料编码
     */
    private String materialCode;
    /**
     * 物料描述
     */
    private String materialDesc;
    /**
     * 生产调度员
     */
    private String productionScheduler;
    /**
     * 转序发起人
     */
    private String transferInitiator;
    /**
     * 发起日期
     */
    private LocalDateTime initiatorDate;
    /**
     * 数量
     */
    private BigDecimal quantity;
    /**
     * 转序单号
     */
    private String transferNo;
    /**
     * 计划批次
     */
    private String batchNo;
    /**
     * 签收人
     */
    private String signer;
    /**
     * 签收时间
     */
    private LocalDateTime signTime;
}