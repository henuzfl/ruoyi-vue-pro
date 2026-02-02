package cn.iocoder.yudao.module.buyer.dal.dataobject.materialplanmrp;

import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 买家需求预测视图 DO
 *
 * @author 柳文
 */
@TableName("V_BUYER_DEMAND_FORECAST")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MaterialPlanMrpDO extends BaseDO {

    /**
     * 客户
     */
    private String customerName;

    /**
     * 产品线/分类
     */
    private String productLine;

    /**
     * 吨位/米段
     */
    private String tonnageSegment;

    /**
     * 车型
     */
    private String vehicleModel;

    /**
     * 下个月预测值
     */
    private BigDecimal nextMonthForecast;

    /**
     * 下第二个月预测值
     */
    private BigDecimal secondMonthForecast;

    /**
     * 下第三个月预测值
     */
    private BigDecimal thirdMonthForecast;

    /**
     * 下第四个月预测值
     */
    private BigDecimal fourthMonthForecast;

    /**
     * 下第五个月预测值
     */
    private BigDecimal fifthMonthForecast;

    /**
     * 特力油缸编码
     */
    private String cylinderCode;

    /**
     * 单台数量
     */
    private BigDecimal unitQuantity;

    /**
     * 下个月油缸需求数量
     */
    private BigDecimal nextMonthCylinderDemand;

    /**
     * 下第二个月油缸需求数量
     */
    private BigDecimal secondMonthCylinderDemand;

    /**
     * 下第三个月油缸需求数量
     */
    private BigDecimal thirdMonthCylinderDemand;

    /**
     * 下第四个月油缸需求数量
     */
    private BigDecimal fourthMonthCylinderDemand;

    /**
     * 下第五个月油缸需求数量
     */
    private BigDecimal fifthMonthCylinderDemand;

    /**
     * 采购组件物料号
     */
    private String componentMaterialNo;

    /**
     * 组件名称
     */
    private String componentDesc;

    /**
     * 规格型号
     */
    private String specificationModel;

    /**
     * 毛重
     */
    private BigDecimal grossWeight;

    /**
     * 净重
     */
    private BigDecimal netWeight;

    /**
     * 采购组
     */
    private String purchasingGroup;

    /**
     * 物料分类
     */
    private String materialType;

    /**
     * 组件单台数量
     */
    private BigDecimal componentUnitUsage;

    /**
     * 组件总数量
     */
    private BigDecimal componentTotalDemand;

    /**
     * 库存数据
     */
    private BigDecimal stockQuantity;

    /**
     * 下个月已分配库存
     */
    private BigDecimal nextMonthAllocated;

    /**
     * 下第二个月已分配库存
     */
    private BigDecimal secondMonthAllocated;

    /**
     * 下第三个月已分配库存
     */
    private BigDecimal thirdMonthAllocated;

    /**
     * 总已分配库存
     */
    private BigDecimal totalAllocatedStock;

    /**
     * 最终剩余库存
     */
    private BigDecimal finalRemainingStock;

    /**
     * 当月订单数量
     */
    private BigDecimal currentMonthOrderQuantity;

    /**
     * 备料差异
     */
    private BigDecimal materialSupplyStatus;

    /**
     * 备料是否满足
     */
    private String materialSupplyDesc;

}