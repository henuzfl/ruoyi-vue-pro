package cn.iocoder.yudao.module.marketing.dal.dataobject.scissorliftwplan;

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
 * 高机剪叉周计划 DO
 *
 * @author 柳文
 */
@TableName("marketing_aerial_boom_wplan")
@KeySequence("marketing_aerial_boom_wplan_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScissorLiftWplanDO extends BaseDO {

    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    /**
     * 产品线
     */
    private String productLine;
    /**
     * 精准车型
     */
    private String preciseModel;
    /**
     * 产品型号
     */
    private String productModel;
    /**
     * 精准BOM
     */
    private String preciseBom;
    /**
     * 生产日期
     */
    private LocalDateTime planDate;
    /**
     * 周次
     */
    private String weekNo;
    /**
     * 周起始日期
     */
    private LocalDateTime weekStartDate;
    /**
     * 周结束日期
     */
    private LocalDateTime weekEndDate;
    /**
     * 当日数量
     */
    private Integer dailyQuantity;
    /**
     * 车号范围
     */
    private String carNumberRange;
    /**
     * 生产线条类型
     */
    private String productionLineType;
    /**
     * 板块
     */
    private String plate;
    /**
     * 导入批次时间
     */
    private LocalDateTime importTime;

}