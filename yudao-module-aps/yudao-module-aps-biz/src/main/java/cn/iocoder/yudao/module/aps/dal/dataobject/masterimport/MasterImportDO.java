package cn.iocoder.yudao.module.aps.dal.dataobject.masterimport;

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
 * 物料主数据导入 DO
 *
 * @author 柳文
 */
@TableName("material_master_import")
@KeySequence("material_master_import_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MasterImportDO extends BaseDO {

    /**
     * 主键编号
     */
    @TableId
    private Long id;
    /**
     * 物料编号
     */
    private String materialNo;
    /**
     * 物料描述
     */
    private String materialDesc;
    /**
     * 物料类型
     */
    private String materialType;
    /**
     * 毛重
     */
    private BigDecimal grossWeight;
    /**
     * 净重
     */
    private BigDecimal netWeight;
    /**
     * 基本单位
     */
    private String baseUom;
    /**
     * 评估类
     */
    private String valuationClass;
    /**
     * 价格控制
     */
    private String priceControl;
    /**
     * 无成本核算
     */
    private String noCostEstimation;
    /**
     * 用QS的成本估算
     */
    private String qsCostEstimate;
    /**
     * 大小量纲
     */
    private String sizeDimension;
    /**
     * 采购类型
     */
    private String procurementType;
    /**
     * 生产仓储地点
     */
    private String productionStorageLocation;
    /**
     * 生产调度员
     */
    private String productionScheduler;
    /**
     * 配送标识
     */
    private String distributionFlag;
    /**
     * 物料分类
     */
    private String materialCategory;
    /**
     * 外部采购仓库地点
     */
    private String externalProcurementStorage;
    /**
     * 计划交货时间
     */
    private Integer plannedDeliveryTime;

}