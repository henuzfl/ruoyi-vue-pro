package cn.iocoder.yudao.module.buyer.dal.dataobject.hostconfig;

import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 主机配置 DO
 *
 * @author 柳文
 */
@TableName("buyer_host_config")
@KeySequence("buyer_host_config_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HostConfigDO extends BaseDO {

    /**
     * 主键ID
     */
    @TableId
    private BigDecimal id;
    /**
     * 车型
     */
    private String vehicleModel;
    /**
     * 特力图号
     */
    private String specialDrawingNo;
    /**
     * 配置数量
     */
    private Long configQuantity;
    /**
     * 事业部
     */
    private String businessUnit;

}