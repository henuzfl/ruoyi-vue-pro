package cn.iocoder.yudao.module.aps.dal.dataobject.plan;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 设备调度 DO
 *
 * @author 芋道源码
 */
@TableName("device_schedule")
@KeySequence("device_schedule_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlanDO extends BaseDO {

    /**
     * 工站ID
     */
    private String stationId;
    /**
     * 设备ID
     */
    private String deviceId;
    /**
     * 设备序号
     */
    private Short deviceSeq;
    /**
     * 操作说明
     */
    private String operationText;
    /**
     * 产品编码
     */
    private String productCode;
    /**
     * 单号
     */
    private String danhao;
    /**
     * 线别
     */
    private Integer lineNo;
    /**
     * 物料编码
     */
    private String materialNo;
    /**
     * 事件时间
     */
    private LocalDateTime eventTime;
    /**
     * 操作序号
     */
    private Short operationSeq;
    /**
     * 工作日期
     */
    private LocalDateTime workDate;
    /**
     * 开始时间
     */
    private LocalDateTime startTime;
    /**
     * 结束时间
     */
    private LocalDateTime endTime;
    /**
     * 数量
     */
    private Short quantity;
    /**
     * 尺寸规格
     */
    private String sizeDimension;
    /**
     * 状态
     */
    private Short state;
    /**
     * 持续时间（分钟）
     */
    private Short durationMinutes;
    /**
     * 主键ID
     */
    @TableId
    private Short id;

}