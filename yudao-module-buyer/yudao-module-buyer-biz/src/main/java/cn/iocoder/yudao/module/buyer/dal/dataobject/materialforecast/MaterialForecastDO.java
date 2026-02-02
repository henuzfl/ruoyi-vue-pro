package cn.iocoder.yudao.module.buyer.dal.dataobject.materialforecast;

import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 营销材料备料预测 DO
 *
 * @author 柳文
 */
@TableName("buyer_material_forecast")
@KeySequence("buyer_material_forecast_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MaterialForecastDO extends BaseDO {

    /**
     * 主键ID
     */
    @TableId
    private BigDecimal id;
    /**
     * 客户名称
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
     * 预测月份（格式：YYYY-MM）
     */
    private String forecastMonth;
    /**
     * 预测数量
     */
    private BigDecimal forecastQuantity;

}