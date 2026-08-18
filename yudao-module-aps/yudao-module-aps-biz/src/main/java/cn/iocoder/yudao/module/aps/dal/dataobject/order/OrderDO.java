package cn.iocoder.yudao.module.aps.dal.dataobject.order;

import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 订单表 - SAP订单信息 DO
 *
 * @author 柳文
 */
@TableName("sub_order")
@KeySequence("sub_order_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDO extends BaseDO {

    private String id;

    /**
     * 订单号(主键)
     */
    @TableId
    private String productionOrderNo;
    /**
     * 物料号
     */
    private String assemblyMaterialNo;
    /**
     * 物料描述
     */
    private String mainMaterialDesc;
    /**
     * 订单类型(如ZY02)
     */
    private String componentOrderType;
    /**
     * 订单数量
     */
    private BigDecimal scheduledQuantity;
    /**
     * 已交货数量(已入库数量)
     */
    private BigDecimal deliveredQuantity;
    /**
     * 创建日期
     */
    private LocalDateTime creationDate;
    /**
     * 创建者/输入者
     */
    private String createdBy;
    /**
     * 系统状态(如REL PCNF等)
     */
    private String systemStatus;
    /**
     * 计划开始日期
     */
    private LocalDateTime scheduledDate;
    /**
     * 实际开始时间(日期+时间)
     */
    private LocalDateTime actualStartTime;
    /**
     * 计划完成日期
     */
    private LocalDateTime basicEndDate;
    /**
     * 工厂代码
     */
    private String plant;
    /**
     * MRP控制员代码
     */
    private String mrpController;
    /**
     * 生产主管
     */
    private String productionWorkshop;
    /**
     * 计量单位(如PC)
     */
    private String unitOfMeasure;
    /**
     * 生产版本(如0001)
     */
    private String productionVersion;
    /**
     * 实际完成日期 (Actual End Date)
     */
    private LocalDateTime actualEndDate;
    /**
     * 处理开始日期 (Process Start Date)
     */
    private LocalDateTime processStartDate;
    /**
     * 提交日期 (Submit Date)
     */
    private LocalDateTime submitDate;
    /**
     * 处理下达 (Process Released)
     */
    private String processReleased;
    /**
     * 集中订单处理 (Central Processing)
     */
    private String centralProc;
    /**
     * 更改日期 (Change Date)
     */
    private LocalDateTime changeDate;
    /**
     * 最后更改人 (Last Changed By)
     */
    private String lastChangedBy;
    /**
     * 订单类别 (Order Category)
     */
    private String orderCategory;
    /**
     * 销售订单 (Sales Order)
     */
    private String salesOrder;
    /**
     * 描述 (Description)
     */
    private String description;
    /**
     * 确认产量
     */
    private BigDecimal confirmedQuantity;

}