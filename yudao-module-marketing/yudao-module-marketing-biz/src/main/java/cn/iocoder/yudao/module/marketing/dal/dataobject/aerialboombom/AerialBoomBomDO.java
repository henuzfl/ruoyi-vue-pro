package cn.iocoder.yudao.module.marketing.dal.dataobject.aerialboombom;

import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 高机臂式/剪叉BOM物料清单 DO
 *
 * @author 柳文
 */
@TableName("marketing_aerial_boom_bom")
@KeySequence("marketing_aerial_boom_bom_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AerialBoomBomDO extends BaseDO {

    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_ID)   // 雪花算法自动生成ID
    private Long id;
    /**
     * 物料编码
     */
    private String materialCode;
    /**
     * 物料描述
     */
    private String materialDesc;
    /**
     * 供应商
     */
    private String supplier;
    /**
     * JIT标识（1表示JIT物料）
     */
    private String jitFlag;
    /**
     * 是否颜色管理（X表示是）
     */
    private String colorManagement;
    /**
     * 是否按需供货（X表示是）
     */
    private String supplyOnDemand;
    /**
     * 适配机型（多机型逗号分隔）
     */
    private String applicableModel;
    /**
     * 备注
     */
    private String remark;
    /**
     * 产品型号（如ZA10RJE）
     */
    private String productModel;
    /**
     * 精准BOM（如ZA10RJE-001）
     */
    private String preciseBom;
    /**
     * 数量
     */
    private BigDecimal quantity;
    /**
     * 物料来源分类（臂式专用物料/剪叉专用物料/剪叉和臂式共用物料/走车物资及选配件）
     */
    private String sourceCategory;
    /**
     * 板块（默认高机）
     */
    private String plate;
    /**
     * 导入批次时间
     */
    private LocalDateTime importTime;

}