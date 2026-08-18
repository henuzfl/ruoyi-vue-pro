package cn.iocoder.yudao.module.wm.dal.dataobject.realtimestock;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 实时库存 DO
 */
@TableName("real_time_stock")
@KeySequence("real_time_stock_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RealTimeStockDO extends BaseDO {

    /**
     * 主键编号
     */
    private Long id;

    /**
     * 物料编号
     */
    private String materialNo;

    /**
     * 物料描述
     */
    private String materialDesc;

    /**
     * 库存地点
     */
    private String stockLocation;

    /**
     * 库存数量
     */
    private BigDecimal stockQuantity;

    /**
     * 可用数量
     */
    private BigDecimal availableQuantity;

    /**
     * 状态（0=正常，1=停用）
     */
    private Integer status;

    /**
     * 备注
     */
    private String remark;

    /**
     * 租户ID
     */
    private Long tenantId;

    /**
     * 创建者
     */
    private String creator;

    /**
     * 更新者
     */
    private String updater;

    /**
     * 删除标识（0=否，1=是）
     */
    private Boolean deleted;
}