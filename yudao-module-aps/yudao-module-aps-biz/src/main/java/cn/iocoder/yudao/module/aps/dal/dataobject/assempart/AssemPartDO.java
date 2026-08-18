package cn.iocoder.yudao.module.aps.dal.dataobject.assempart;

import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 总成与子件关联表管理 DO
 *
 * @author 柳文
 */
@TableName("aps_assem_part")
@KeySequence("aps_assem_part_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssemPartDO extends BaseDO {

    /**
     * 总成订单号
     */
    private String orderNo;
    /**
     * 总成数量
     */
    private BigDecimal quantity;
    /**
     * 计划时间
     */
    private Date scheduleTime;
    /**
     * 零部件订单号
     */
    private String componentOrder;
    /**
     * 零部件数量
     */
    private BigDecimal allocQty;
    /**
     * 编号
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

}