package cn.iocoder.yudao.module.buyer.dal.dataobject.buyerinput;

import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 需求输入 DO
 *
 * @author 柳文
 */
@TableName("demand_input")
@KeySequence("demand_input_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class buyerInputDO extends BaseDO {

    /**
     * 主键编号
     */
    @TableId
    private Long id;
    /**
     * 客户
     */
    private String customer;
    /**
     * 车型
     */
    private String vehicleModel;
    /**
     * 总成物料
     */
    private String assemblyMaterial;
    /**
     * 总成数量
     */
    private BigDecimal assemblyQuantity;
    /**
     * 需求月份
     */
    private String demandMonth;
    /**
     * 状态（0=正常，1=停用）
     */
    private Short status;
    /**
     * 备注
     */
    private String remark;

}