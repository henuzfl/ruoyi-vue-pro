package cn.iocoder.yudao.module.marketing.dal.dataobject.concretebom;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 混凝土BOM DO
 *
 * @author 柳文
 */
@TableName("concrete_bom")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConcreteBomDO extends BaseDO {

    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    /**
     * 车型（物料编码或车型名称）
     */
    private String vehicleModel;
    /**
     * 分解油缸（部件名称及路径）
     */
    private String cylinderName;
    /**
     * SBP编码
     */
    private String sbpCode;
    /**
     * 配置（如数量等）
     */
    private String config;
    /**
     * 导入时间
     */
    private LocalDateTime importTime;


}