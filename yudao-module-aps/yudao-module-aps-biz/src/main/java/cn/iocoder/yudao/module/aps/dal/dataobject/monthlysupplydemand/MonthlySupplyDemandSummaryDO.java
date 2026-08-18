package cn.iocoder.yudao.module.aps.dal.dataobject.monthlysupplydemand;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * 月度供需总览表 DO
 *
 * @author 柳文
 */
@TableName("MONTHLY_SUPPLY_DEMAND_SUMMARY")
@KeySequence("SEQ_MONTHLY_SDS") // Oracle 序列
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MonthlySupplyDemandSummaryDO extends BaseDO {

    /**
     * 主键编号
     */
    @TableId
    private Long id;

    /**
     * 总成物料号
     */
    private String assemblyMaterialNo;

    /**
     * 物料描述
     */
    private String materialDesc;

    /**
     * 月份（YYYY-MM）
     */
    private String scheduledDate;

    /**
     * 需求数量
     */
    private BigDecimal requireQuantity;

    /**
     * 实时库存
     */
    private BigDecimal stockQuantity;

    /**
     * 在制/在途数量
     */
    private BigDecimal wip;

    /**
     * 净需求
     */
    private BigDecimal netRequirement;

    /**
     * 主计划数量
     */
    private BigDecimal scheduledQuantity;

    /**
     * 缺口
     */
    private BigDecimal shortage;

    /**
     * 销售出库数量
     */
    private BigDecimal salesOutQuantity;
}