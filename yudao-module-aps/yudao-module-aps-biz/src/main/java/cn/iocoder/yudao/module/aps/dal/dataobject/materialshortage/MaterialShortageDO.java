package cn.iocoder.yudao.module.aps.dal.dataobject.materialshortage;

import com.baomidou.mybatisplus.annotation.*;
import lombok.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import java.math.BigDecimal;

@TableName("material_shortage_report")
@KeySequence("material_shortage_report_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MaterialShortageDO extends BaseDO {

    @TableId
    private Long id;

    /**
     * 成品物料编码
     */
    private String mainMaterialNo;

    /**
     * 成品物料描述
     */
    private String materialDesc;

    /**
     * 组件物料编码
     */
    private String componentMaterialNo;

    /**
     * 组件描述
     */
    private String componentDesc;

    /**
     * 单位用量
     */
    private BigDecimal unitUsage;

    /**
     * 库存数量
     */
    private BigDecimal stockQuantity;

    /**
     * 在途数量
     */
    private BigDecimal transit;

    /**
     * 已发放数量
     */
    private BigDecimal issue;

    /**
     * 缺口数量
     */
    private BigDecimal shortageQty;

    // ===== 新增：总成层级的四个字段 =====

    /**
     * 总成需求数量
     */
    private BigDecimal mainRequirement;

    /**
     * 总成当前库存
     */
    private BigDecimal mainStockQuantity;

    /**
     * 总成在途数量
     */
    private BigDecimal mainTransit;

    /**
     * 总成已交付数量（销售出库）
     */
    private BigDecimal mainDelivered;
}