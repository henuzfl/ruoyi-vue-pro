package cn.iocoder.yudao.module.wm.dal.dataobject.materialkittingtool;

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
 * 齐套 DO
 *
 * @author 柳文
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MaterialKittingToolDO extends BaseDO {

    /**
     * 生产订单号
     */
    private String productionOrderNo;

    /**
     * 总成物料号
     */
    private String assemblyMaterialNo;

    /**
     * 主物料描述
     */
    private String mainMaterialDesc;

    /**
     * 排产时间
     */
    private Date scheduledDate;

    /**
     * 是否齐套
     */
    private String kittingStatus;

    /**
     * 是否齐套
     */
    private String assemblyKittingStatus;

    /**
     * 生产车间
     */
    private String productionWorkshop;

    /**
     * 排产数量
     */
    private Integer scheduledQuantity;

    /**
     * 组件物料号
     */
    private String componentMaterialNo;

    /**
     * 组件物料名称
     */
    private String componentDesc;

    /**
     * 单件物料数量
     */
    private Double unitUsage;

    /**
     * 所需组件数量
     */
    private Double requiredQty;

    /**
     * 出库数量
     */
    private Double openquantity;

    /**
     * 配送生成数量
     */
    private Double outboundqty;

    /**
     * 明天库存
     */
    private Double stockTomorrow;

    /**
     * 后天库存
     */
    private Double stockDayAfterTomorrow;

    /**
     * 第三天库存
     */
    private Double stockThirdDay;

    /**
     * 总库存数量
     */
    private Double stockQuantity;

    /**
     * 差额
     */
    private Double shortageQty;

    /**
     * 是否全部出库
     */
    private String openFlag;

    /*
   订单类型
    */
    private String orderMessage;

    /*
    父订单
     */
    private String parentOrderNo;
}