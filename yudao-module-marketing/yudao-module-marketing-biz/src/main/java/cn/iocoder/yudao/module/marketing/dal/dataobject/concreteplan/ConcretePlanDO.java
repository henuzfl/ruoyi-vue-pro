package cn.iocoder.yudao.module.marketing.dal.dataobject.concreteplan;

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
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 混凝土计划需求 DO
 *
 * @author 管理员
 */
@TableName("marketing_concrete_plan")
@KeySequence("marketing_concrete_plan_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConcretePlanDO extends BaseDO {

    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    /**
     * 计划编号
     */
    private String planNo;
    /**
     * 序号
     */
    private String seqNo;
    /**
     * 米段
     */
    private String meter;
    /**
     * 型号
     */
    private String modelName;
    /**
     * 物料名称
     */
    private String materialName;
    /**
     * 物料编码
     */
    private String materialCode;
    /**
     * 生产编号
     */
    private String prodNo;
    /**
     * 数量
     */
    private BigDecimal quantity;
    /**
     * 结构件编号/环保编码
     */
    private String structSerialNo;
    /**
     * 订单号
     */
    private String orderNo;
    /**
     * 出料批次
     */
    private String batchNo;
    /**
     * 组单情况
     */
    private String groupStatus;
    /**
     * 支腿类型
     */
    private String legType;
    /**
     * 国家（国内/海外）
     */
    private String countryType;
    /**
     * 计划下达时间
     */
    private String planIssueTime;
    /**
     * 装配上线时间
     */
    private LocalDateTime assemblyStartTime;
    /**
     * 装配下线时间
     */
    private LocalDateTime assemblyEndTime;
    /**
     * 调试下线时间
     */
    private LocalDateTime debugTime;
    /**
     * 涂装下线时间
     */
    private LocalDateTime paintingTime;
    /**
     * 入库时间
     */
    private LocalDateTime warehouseTime;
    /**
     * 营运要求产出月份
     */
    private String outputMonth;
    /**
     * 特殊要求
     */
    private String specialReq;
    /**
     * 涂装要求
     */
    private String paintingReq;
    /**
     * 异常说明
     */
    private String exceptionNote;
    /**
     * 排产时间
     */
    private String scheduleTime;
    /**
     * 发货要求
     */
    private String deliveryReq;
    /**
     * 工厂
     */
    private String factory;
    /**
     * 状态
     */
    private String status;
    /**
     * 客户
     */
    private String customer;
    /**
     * 修改后车号
     */
    private String modifiedCarNo;
    /**
     * 板块
     */
    private String plate;
    /**
     * 导入时间
     */
    private LocalDateTime importTime;

}