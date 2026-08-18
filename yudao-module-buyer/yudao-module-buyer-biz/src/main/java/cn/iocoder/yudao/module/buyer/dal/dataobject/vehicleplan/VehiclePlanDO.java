package cn.iocoder.yudao.module.buyer.dal.dataobject.vehicleplan;

import lombok.*;

import java.time.*;
import java.util.*;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 买家车辆营销计划表（主机厂计划） DO
 *
 * @author 柳文
 */
@TableName("buyer_vehicle_plan")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehiclePlanDO extends BaseDO {

    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private BigDecimal id;

    /**
     * 导入日期（年月）
     */
    private Date importDate;

    /**
     * 产品线
     */
    private String productLine;
    /**
     * 产品机型
     */
    private String productModel;
    /**
     * 车型代码
     */
    private String vehicleCode;
    /**
     * 2025年度顺序号/车号
     */
    private String seqNo2025;
    /**
     * 2026年度顺序号/车号
     */
    private String seqNo2026;
    /**
     * 裸机订单号
     */
    private String bareMachineOrderNo;
    /**
     * 行驶单元订单号
     */
    private String drivingUnitOrderNo;
    /**
     * 内外贸（内贸/外贸）
     */
    private String tradeType;
    /**
     * 台份数量
     */
    private BigDecimal unitQuantity;
    /**
     * 底盘上线计划日期
     */
    private LocalDateTime chassisOnlinePlanDate;
    /**
     * 成台完工计划日期
     */
    private LocalDateTime finishedProductPlanDate;

    @TableField("vin")
    private String vin;

    @TableField("blanking_plan_date")
    private String blankingPlanDate;

    @TableField("boom_leg_plan_date")
    private String boomLegPlanDate;

    @TableField("boom_top_bottom_plan_date")
    private String boomTopBottomPlanDate;

    @TableField("turntable_plan_date")
    private String turntablePlanDate;

    @TableField("frame_plan_date")
    private String framePlanDate;
}