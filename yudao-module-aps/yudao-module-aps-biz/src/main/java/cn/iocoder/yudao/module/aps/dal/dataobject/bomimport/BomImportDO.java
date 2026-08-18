package cn.iocoder.yudao.module.aps.dal.dataobject.bomimport;

import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 物料BOM导入 DO
 *
 * @author 柳文
 */
@TableName("material_bom_import")
@KeySequence("material_bom_import_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BomImportDO extends BaseDO {

    /**
     * 主键编号
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID) // 使用雪花算法
    private Long id;
    /**
     * 父节点行号
     */
    private String parentLineNo;
    /**
     * 行号
     */
    private String lineNo;
    /**
     * 层级
     */
    private Long levelNo;
    /**
     * 工厂
     */
    private String plant;
    /**
     * 主物料号
     */
    private String mainMaterialNo;
    /**
     * 组件物料号
     */
    private String componentMaterialNo;
    /**
     * 组件物料描述
     */
    private String componentDesc;
    /**
     * 规格型号
     */
    private String specModel;
    /**
     * 毛重
     */
    private BigDecimal grossWeight;
    /**
     * 净重
     */
    private BigDecimal netWeight;
    /**
     * 组件数量
     */
    private BigDecimal componentQty;
    /**
     * 单台用量
     */
    private BigDecimal unitUsage;
    /**
     * 物料类
     */
    private String materialType;
    /**
     * 特殊采购类型
     */
    private String specialProcurementType;
    /**
     * 库存地点
     */
    private String storageLocation;
    /**
     * 单位
     */
    private String unit;
    /**
     * 采购类型
     */
    private String procurementType;
    /**
     * 采购组
     */
    private String purchasingGroup;
    /**
     * 导入时间
     */
    private LocalDateTime importDate;

}