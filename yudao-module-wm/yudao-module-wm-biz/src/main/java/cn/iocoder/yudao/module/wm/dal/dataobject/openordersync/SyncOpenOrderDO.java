package cn.iocoder.yudao.module.wm.dal.dataobject.openordersync;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 采购未清订单 DO
 *
 * @author 柳文
 */
@TableName("buyer_open_order")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SyncOpenOrderDO extends BaseDO {

    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    /**
     * 订单日期
     */
    private LocalDateTime orderDate;
    /**
     * 采购订单号
     */
    private String buyerOrderNo;
    /**
     * 订单行项目
     */
    private Long lineItem;
    /**
     * 物料号
     */
    private String materialNo;
    /**
     * 物料描述
     */
    private String materialDesc;
    /**
     * 订单数量
     */
    private BigDecimal orderQty;
    /**
     * 实收数量
     */
    private BigDecimal receivedQty;
    /**
     * 未清数量
     */
    private BigDecimal openQty;
    /**
     * 单位
     */
    private String unit;
    /**
     * 要求到货日期
     */
    private LocalDateTime requiredArrivalDate;
    /**
     * 实际到货日期
     */
    private LocalDateTime actualArrivalDate;
    /**
     * 供应商描述
     */
    private String supplierDesc;
    /**
     * 客户
     */
    private String customer;
    /**
     * 采购组
     */
    private String buyerGroup;
    /**
     * 凭证类型
     */
    private String documentType;
    /**
     * 生产订单号
     */
    private String productionOrderNo;
    /**
     * 品牌信息
     */
    private String brandInfo;
    /**
     * 单价（净价）
     */
    private BigDecimal unitPrice;
    /**
     * 供应商代码
     */
    private String supplierCode;
    /**
     * 收货仓库
     */
    private String receivingWarehouse;
    /**
     * 合计金额（净价）
     */
    private BigDecimal totalAmount;
    /**
     * 采购申请
     */
    private String buyerReqNo;

}