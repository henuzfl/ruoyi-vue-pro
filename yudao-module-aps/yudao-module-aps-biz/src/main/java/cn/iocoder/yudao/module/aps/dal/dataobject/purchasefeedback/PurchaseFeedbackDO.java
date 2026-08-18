package cn.iocoder.yudao.module.aps.dal.dataobject.purchasefeedback;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 采购反馈 DO
 *
 * @author 柳文
 */
@TableName("aps_purchase_feedback")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseFeedbackDO extends BaseDO {

    /**
     * 主键ID（雪花算法生成）
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 排产时间
     */
    private Date scheduleTime;

    /**
     * 采购物料
     */
    private String purchaseMaterial;

    /**
     * 采购反馈备注
     */
    private String feedbackRemark;

}