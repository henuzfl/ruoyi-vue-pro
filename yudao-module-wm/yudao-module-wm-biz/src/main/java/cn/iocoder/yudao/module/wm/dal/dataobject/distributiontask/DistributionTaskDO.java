package cn.iocoder.yudao.module.wm.dal.dataobject.distributiontask;

import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 配送任务下发 DO
 *
 * @author 柳文
 */
@TableName("wm_distribution_task")
@KeySequence("wm_distribution_task_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DistributionTaskDO extends BaseDO {

    /**
     * 主键ID
     */
    @TableId
    private BigDecimal id;
    /**
     * 订单号
     */
    private String orderNo;
    /**
     * 预留号
     */
    private String reservationNo;
    /**
     * 预留行号
     */
    private String reservationLineNo;
    /**
     * 工序号
     */
    private String processNo;
    /**
     * 配送任务单号
     */
    private String distributionTaskNo;
    /**
     * 产品编码
     */
    private String productCode;
    /**
     * 产品名称
     */
    private String productName;
    /**
     * 物料号
     */
    private String materialNo;
    /**
     * 物料名称
     */
    private String materialName;
    /**
     * 数量
     */
    private BigDecimal quantity;
    /**
     * 单位
     */
    private String unit;
    /**
     * 任务状态（0=待下发，1=已下发，2=配送中，3=已完成，4=已取消）
     */
    private Short taskStatus;
    /**
     * 配送状态（0=未配送，1=已配送，2=已接收，3=已退回）
     */
    private Short distributionStatus;
    /**
     * 配送时间
     */
    private LocalDateTime distributionTime;
    /**
     * 配送操作人
     */
    private String distributionOperator;
    /**
     * 配送地点
     */
    private String deliveryLocation;

}