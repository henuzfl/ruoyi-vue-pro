package cn.iocoder.yudao.module.marketing.dal.dataobject.aerialboomwplan;

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
 * 高机臂式周计划 DO
 *
 * @author 柳文
 */
@TableName("marketing_aerial_boom_wplan")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AerialBoomWplanDO extends BaseDO {

    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_ID)   // 雪花算法自动生成ID
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