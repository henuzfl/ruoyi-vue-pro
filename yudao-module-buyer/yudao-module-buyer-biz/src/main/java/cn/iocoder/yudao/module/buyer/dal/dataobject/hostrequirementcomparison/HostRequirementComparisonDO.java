package cn.iocoder.yudao.module.buyer.dal.dataobject.hostrequirementcomparison;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

@TableName("buyer_host_requirement_comparison")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HostRequirementComparisonDO extends BaseDO {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;                     // 主键（雪花算法）

    private Data chassisOnlinePlanDate;  // 2026-2-12版本（底盘上线计划日期）
    private String productModel;                  // 车型
    private String seqNo2025;                     // 2025年顺序号
    private String seqNo2026;                     // 2026年顺序号
    private String bareMachineOrderNo;            // 中联订单号（裸机订单号）
    private String drivingUnitOrderNo;            // 底盘订单号（行驶单元订单号）
    private BigDecimal quota;                     // 配额2026.1.25
    private BigDecimal unitQuantity;              // 台套
    private String materialNo;                    // 主机图号（物料号）
    private String teliCode;                      // 特力图号（特力编码）
    private BigDecimal requiredQuantity;          // 配置/需配数量
    private BigDecimal actualOweQuantity;         // 实欠数（可存储计算值，也可不存）
    // 以下字段目前无来源，可先预留
    private LocalDateTime pickingDate;            // 领料日期
    private BigDecimal pickedQuantity;            // 已领数量
    private String closedStatus;                  // 结否
    private String category;                      // 分类
    private String hostPlanReleaseTime;           // 主机计划下达时间（文本）
}