package cn.iocoder.yudao.module.wm.dal.dataobject.kittingmaster;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
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
public class KittingMasterDO extends BaseDO {

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
    private String assemblyKittingStatus;
    /**
     * 是否齐套
     */
    private String kittingStatus;  // 或其它类型，根据业务确定

    /**
     * 生产车间
     */
    private String productionWorkshop;

    /**
     * 排产数量
     */
    private Integer scheduledQuantity;

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