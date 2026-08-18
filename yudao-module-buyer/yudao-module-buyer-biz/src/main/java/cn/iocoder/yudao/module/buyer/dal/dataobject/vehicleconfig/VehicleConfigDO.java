package cn.iocoder.yudao.module.buyer.dal.dataobject.vehicleconfig;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

@TableName("buyer_vehicle_config")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VehicleConfigDO extends BaseDO {

    @TableId(type = IdType.ASSIGN_ID)
    private BigDecimal id;

    /** 导入日期 */
    private Date importDate;

    private String orderNo;

    private String vehicleModel;

    @TableField("seq_no_2025")
    private String seqNo2025;

    @TableField("seq_no_2026")
    private String seqNo2026;

    private String requiredArrivalTime;

    private String materialDesc;

    private Long quota1;

    private String quota2;

    private String materialNo;

    private String factory;

    private BigDecimal requiredQuantity;

    private BigDecimal deliveredQuantity;
}