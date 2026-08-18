package cn.iocoder.yudao.module.marketing.dal.dataobject.asmrequirement;

import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 营销总成需求 DO
 *
 * @author 柳文
 */
@TableName("marketing_asm_requirement")
@KeySequence("marketing_asm_requirement_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AsmRequirementDO extends BaseDO {

    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    /**
     * 总成物料编码
     */
    private String assemblyMaterialNo;
    /**
     * 总成物料名称
     */
    private String mainMaterialDesc;
    /**
     * 需求数量
     */
    private BigDecimal requireQuantity;
    /**
     * 需求日期
     */
    private LocalDateTime requireDate;

    /**
     * 主机单位
     */
    private String hostUnit;

    /**
     * 车型
     */
    private String vehicleModel;

}