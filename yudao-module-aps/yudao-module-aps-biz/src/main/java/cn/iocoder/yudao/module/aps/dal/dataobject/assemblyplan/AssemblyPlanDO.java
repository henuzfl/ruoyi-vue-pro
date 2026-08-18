package cn.iocoder.yudao.module.aps.dal.dataobject.assemblyplan;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 各车间开装计划 DO
 *
 * @author 柳文
 */
@TableName("aps_assembly_plan")
@KeySequence("aps_assembly_plan_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssemblyPlanDO extends BaseDO {

    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    /**
     * 订单号
     */
    private String orderNo;
    /**
     * 物料编号
     */
    private String materialCode;
    /**
     * 物料描述
     */
    private String materialDesc;
    /**
     * 装配数量（计划数）
     */
    private Long assemblyQuantity;
    /**
     * 已装配数量（完成数）
     */
    private Long assembledQuantity;
    /**
     * 排产时间
     */
    private LocalDateTime scheduleTime;
    /**
     * 车间
     */
    private String workshop;
    /**
     * 导入时间
     */
    private LocalDateTime importTime;

}