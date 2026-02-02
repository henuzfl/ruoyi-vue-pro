package cn.iocoder.yudao.module.buyer.dal.dataobject.buyermaterial;

import lombok.*;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

import java.util.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;



/**
 * 备料明细汇总 DO
 *
 * @author 柳文
 */
@TableName("MAT_PREP_SUMMARY_VIEW") // 对应视图名
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class buyerMateriaDO extends BaseDO {

    /**
     * 需求物料
     */
    @TableField("req_material")
    private String reqMaterial;

    /**
     * 客户
     */
    @TableField("customer")
    private String customer;

    /**
     * 车型
     */
    @TableField("vehicle_model")
    private String vehicleModel;

    /**
     * 总成需求数量
     */
    @TableField("assembly_qty")
    private BigDecimal assemblyQty;

    /**
     * 采购子件物料
     */
    @TableField("comp_material")
    private String compMaterial;

    /**
     * 采购子件描述
     */
    @TableField("comp_desc")
    private String compDesc;

    /**
     * 规格型号
     */
    @TableField("spec_model")
    private String specModel;

    /**
     * 单台用量
     */
    @TableField("unit_usage")
    private BigDecimal unitUsage;

    /**
     * 组件需求数量
     */
    @TableField("comp_demand_qty")
    private BigDecimal compDemandQty;

    /**
     * 备料数量
     */
    @TableField("prepared_qty")
    private BigDecimal preparedQty;

    /**
     * 实时库存
     */
    @TableField("stock_qty")
    private BigDecimal stockQty;

    /**
     * 差值
     */
    @TableField("difference")
    private BigDecimal difference;

    /**
     * 库存状态
     */
    @TableField("stock_status")
    private String stockStatus;

    /**
     * 供应商
     */
    @TableField("supplier")
    private String supplier;

    /**
     * 采购员
     */
    @TableField("buyer")
    private String buyer;

    /**
     * 采购组
     */
    @TableField("proc_group")
    private String procGroup;

    /**
     * 需求月份
     */
    @TableField("demand_month")
    private String demandMonth;
}