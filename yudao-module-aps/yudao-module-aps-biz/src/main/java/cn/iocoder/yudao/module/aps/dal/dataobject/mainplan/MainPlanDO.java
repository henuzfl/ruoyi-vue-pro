package cn.iocoder.yudao.module.aps.dal.dataobject.mainplan;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 主计划 DO
 *
 * @author 柳文
 */
@TableName("aps_main_plan")
@KeySequence("aps_main_plan_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(value = {"handler", "hibernateLazyInitializer", "createUser", "updateUser"})
public class MainPlanDO extends BaseDO {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.INPUT)
    private BigDecimal id;
    /**
     * 生产订单号
     */
    private String productionOrderNo;
    /**
     * 总成物料号
     */
    private String assemblyMaterialNo;
    /**
     * 主物料描述
     */
    private String mainMaterialDesc;
    /**
     * 排产时间
     */
    private LocalDateTime scheduledDate;
    /**
     * 排产数量
     */
    private BigDecimal scheduledQuantity;
    /**
     * 生产车间
     */
    private String productionWorkshop;
    /**
     * 已排产数量
     */
    private BigDecimal completedQuantity;


}