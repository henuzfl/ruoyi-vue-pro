package cn.iocoder.yudao.module.buyer.dal.dataobject.codeconfig;

import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 主机编码配置 DO
 *
 * @author 柳文
 */
@TableName("buyer_code_config")
@KeySequence("buyer_code_config_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CodeConfigDO extends BaseDO {

    /**
     * 主键ID（雪花算法）
     */
    @TableId(type = IdType.ASSIGN_ID)
    private BigDecimal id;
    /**
     * 名称
     */
    private String name;
    /**
     * 主机编码
     */
    private String hostCode;
    /**
     * 特力编码
     */
    private String teliCode;

}