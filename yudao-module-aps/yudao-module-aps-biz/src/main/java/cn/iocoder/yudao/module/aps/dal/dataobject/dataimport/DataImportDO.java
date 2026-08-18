package cn.iocoder.yudao.module.aps.dal.dataobject.dataimport;

import lombok.*;
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
 * 营销数据导入 DO
 *
 * @author 柳文
 */
@TableName("marketing_data_import")
@KeySequence("SEQ_MARKETING_DATA_IMPORT_ID") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataImportDO extends BaseDO {

    /**
     * 主键编号
     */
    @TableId
    private Short id;
    /**
     * 主计划单号
     */
    private String danhao;
    /**
     * 总成图号
     */
    private String productCode;
    /**
     * 总成数量
     */
    private BigDecimal quantity;
    /**
     * 创建订单日期
     */
    private LocalDateTime orderDate;
    /**
     * 主计划完成日期
     */
    private LocalDateTime eventTime;
    /**
     * 状态（0=正常，1=停用）
     */
    private Short status;
    /**
     * 备注
     */
    private String remark;



}