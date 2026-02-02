package cn.iocoder.yudao.module.aps.dal.dataobject.routeimport;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 工艺路线导入 DO
 *
 * @author 柳文
 */
@TableName("process_route_import")
@KeySequence("process_route_import_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RouteImportDO extends BaseDO {

    /**
     * 主键编号
     */
    @TableId
    private Long id;
    /**
     * 物料号
     */
    private String materialNo;
    /**
     * 物料描述
     */
    private String materialDesc;
    /**
     * 工艺路线
     */
    private String processRoute;
    /**
     * 组号
     */
    private String groupNo;
    /**
     * 工厂
     */
    private String plant;
    /**
     * 起始有效日期
     */
    private LocalDateTime validFromDate;
    /**
     * 工作中心
     */
    private String workCenter;
    /**
     * 顺序
     */
    private Long sequenceNo;
    /**
     * 操作序号
     */
    private String operationSeq;
    /**
     * 工序文本
     */
    private String operationText;
    /**
     * 控制码
     */
    private String controlCode;
    /**
     * 人工工时
     */
    private BigDecimal laborHours;
    /**
     * 单位
     */
    private String laborHoursUnit;
    /**
     * 固定制造费用
     */
    private BigDecimal fixedCost;
    /**
     * 单位
     */
    private String fixedCostUnit;
    /**
     * 变动制造费用
     */
    private BigDecimal variableCost;
    /**
     * 单位
     */
    private String variableCostUnit;
    /**
     * 生产周期
     */
    private BigDecimal productionCycle;
    /**
     * 单位
     */
    private String productionCycleUnit;
    /**
     * 更改编号
     */
    private String changeNo;
    /**
     * 删除标记
     */
    private String deleteFlag;
    /**
     * 生产调度员
     */
    private String productionScheduler;
    /**
     * 采购类型
     */
    private String procurementType;
    /**
     * 导入时间
     */
    private LocalDateTime importDate;

}