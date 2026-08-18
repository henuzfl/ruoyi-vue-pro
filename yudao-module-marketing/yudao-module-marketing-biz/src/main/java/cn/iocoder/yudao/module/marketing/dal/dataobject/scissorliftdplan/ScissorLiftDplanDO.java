package cn.iocoder.yudao.module.marketing.dal.dataobject.scissorliftdplan;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 高机剪叉日计划 DO
 *
 * @author 柳文
 */
@TableName("marketing_aerial_boom_dplan")
@KeySequence("marketing_aerial_boom_dplan_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScissorLiftDplanDO extends BaseDO {

    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    /**
     * 线别
     */
    private String lineType;
    /**
     * 精准车型
     */
    private String preciseModel;
    /**
     * 产品型号
     */
    private String productModel;
    /**
     * ZPS型号
     */
    private String zpsModel;
    /**
     * 精准BOM
     */
    private String preciseBom;
    /**
     * 车号
     */
    private String carNo;
    /**
     * 订单号
     */
    private String orderNo;
    /**
     * 备注
     */
    private String remark;
    /**
     * 内外贸版本
     */
    private String tradeVersion;
    /**
     * 台份
     */
    private Integer unitCount;
    /**
     * 上线计划
     */
    private LocalDateTime onlinePlan;
    /**
     * 成台计划
     */
    private LocalDateTime completePlan;
    /**
     * 报缴日期
     */
    private LocalDateTime reportDate;
    /**
     * 国家
     */
    private String country;
    /**
     * 合同号
     */
    private String contractNo;
    /**
     * 营销通知时间
     */
    private String marketingNoticeTime;
    /**
     * 订单开立时间
     */
    private LocalDateTime orderCreateTime;
    /**
     * 板块
     */
    private String plate;
    /**
     * 导入批次时间
     */
    private LocalDateTime importTime;

}