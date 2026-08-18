package cn.iocoder.yudao.module.buyer.controller.admin.deliver.vo;

import lombok.Data;
import java.math.BigDecimal;
import org.joda.time.LocalDateTime;

/**
 * 配送与采购报表导入 VO
 * 字段顺序严格按照 Excel 列顺序（从 0 开始）
 */
@Data
public class DeliverImportVO {

    private String plant;                       // 工厂
    private String deliveryOrderNo;             // 配送单号
    private LocalDateTime deliveryDate;         // 配送日期
    private LocalDateTime creationDate;         // 创建日期（业务）
    private String creationTime;                // 创建时间（业务）
    private String createdBy;                   // 创建人（业务）
    private LocalDateTime lastUpdateDate;       // 最后更新日期（业务）
    private String lastUpdateTime;              // 最后更新时间（业务）
    private String lastUpdatedBy;               // 最后更新人（业务）
    private String partOrderNo;                 // 生产订单号
    private String productionWorkshop;          // 生产调度员
    private String partMatCode;                 // 主物料编码
    private String partMatDesc;                 // 主物料描述
    private String reservationNo;               // 预留号
    private Long reservationItem;               // 预留项目
    private BigDecimal plannedIssueQty;          // 应发数量
    private BigDecimal deliveredQty;             // 已发数量
    private BigDecimal undeliveredQty;           // 未配送数量
    private String buyerMaterialNo;             // 物料号
    private String oldMaterialNo;               // 旧物料号
    private String buyerMaterialDesc;           // 物料描述
    private String deliverySupplierCode;        // 供应商编码(配送单)
    private String deliverySupplierName;        // 供应商描述(配送单)
    private String stockSufficientFlag;         // 库存是否满足
    private BigDecimal totalStockQty;            // 总库存
    private BigDecimal currentStockConsumeQty;   // 本次消耗库存
    private String deliveryStorageLoc;          // 库存地点(配送)
    private String poSufficientFlag;            // 采购是否满足
    private String buyerOrderNo;                // 采购订单号
    private Long lineItem;                      // 采购项目
    private String requirementTrackingNo;       // 需求跟踪号
    private BigDecimal orderQty;                 // 采购订单数量
    private BigDecimal openQty;                  // 采购订单未收货数量
    private BigDecimal receivedQty;              // 本次交货数量
    private LocalDateTime poDeliveryDate;       // 采购订单交货日期
    private String poSupplierCode;              // 供应商(采购订单)
    private String supplierDesc;                // 供应商描述(采购订单)
    private String buyerPurchasingGroup;        // 采购组
    private String orderingBuyer;               // 下单采购员
    private String deliveryBuyer;               // 交货采购员
}