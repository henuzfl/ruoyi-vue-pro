package cn.iocoder.yudao.module.wm.dal.dataobject.bom;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@TableName("wm_bom")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class BomDO extends BaseDO {

    private Long id;
    private String werks;          // 工厂
    private String stufe;          // BOM层次
    private String wegxx;          // 路线
    private String bmtyp;          // BOM类型
    private String vwegx;          // 路径
    private String ojtxb;          // 组件物料描述
    private String ojtxp;          // 项目描述
    private String mtart;          // 物料类型
    private BigDecimal menge;      // 组件数量
    private String meins;          // 基本计量单位
    private String idnrk;          // 组件物料号

    // 新增字段
    private String posnr;          // BOM项目号（关键字段）
    private String postp;          // BOM项目类型
    private String stlnr;          // BOM编号
    private String stlal;          // 替代BOM
    private String stlan;          // BOM用途
    private BigDecimal bmeng;      // 基本数量
    private String bmein;          // 基本计量单位
    private String matkl;          // 物料组
    private String charg;          // 批次
    private String sobkz;          // 特殊采购标识
    private String rgekz;          // 反冲标识
    private String sobes;          // 特殊采购类型
    private BigDecimal sanfe;      // 组件报废
    private String aufpl;          // 工艺路线号
    private String aplzl;          // 工艺路线计数器
    private String lgort;          // 库存地点

    private String parentIdnrk;    // 父物料号
    private String version;        // 版本
    private LocalDateTime validFrom; // 有效起始日期
    private LocalDateTime validTo;   // 有效结束日期
    private Integer status;        // 状态
    private String remark;         // 备注

    // 同步相关
    private String syncTime;       // 同步时间
    private String syncSource;     // 同步来源（SAP/手工）
    private String syncStatus;     // 同步状态

    // 扩展字段
    private String extField1;
    private String extField2;
    private String extField3;
    private String extField4;
    private String extField5;
}