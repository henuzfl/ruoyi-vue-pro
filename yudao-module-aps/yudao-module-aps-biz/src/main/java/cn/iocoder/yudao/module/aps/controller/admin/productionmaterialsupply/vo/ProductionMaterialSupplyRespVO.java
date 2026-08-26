package cn.iocoder.yudao.module.aps.controller.admin.productionmaterialsupply.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Schema(description = "管理后台 - 生产订单物料供需 Response VO")
@Data
public class ProductionMaterialSupplyRespVO {

    @ExcelProperty("生产订单")
    private String productionOrderNo;
    @ExcelProperty("总成物料号")
    private String assemblyMaterialNo;
    @ExcelProperty("总成物料描述")
    private String assemblyMaterialDesc;
    @ExcelProperty("总成需求数量")
    private BigDecimal assemblyDemandQuantity;
    @ExcelProperty("主计划排产日期")
    private Date scheduledDate;
    @ExcelProperty("组件编码")
    private String componentMaterialNo;
    @ExcelProperty("组件物料描述")
    private String componentMaterialDesc;
    @ExcelProperty("组件需求数量")
    private BigDecimal demandQuantity;
    @ExcelProperty("采购类型")
    private String procurementType;
    @ExcelProperty("投料日期")
    private Date investmentDate;
    @ExcelProperty("日期来源")
    private String dateSource;
    @ExcelProperty("已投料数量")
    private BigDecimal investedQuantity;
    @ExcelProperty("分配库存")
    private BigDecimal stockQuantity;
    @ExcelProperty("生产订单在途")
    private BigDecimal productionTransit;
    @ExcelProperty("采购订单在途")
    private BigDecimal purchaseTransit;
    @ExcelProperty("适用在途")
    private BigDecimal applicableTransit;
    @ExcelProperty("缺口")
    private BigDecimal shortageQuantity;
    @ExcelProperty("采购下单日期")
    private String purchaseOrderDateSummary;
    @ExcelProperty("采购订单")
    private String purchaseOrderSummary;
    @ExcelProperty("项目号")
    private String projectNo;
    @ExcelProperty("采购物料号")
    private String purchaseMaterialNo;
    @ExcelProperty("采购物料描述")
    private String purchaseMaterialDesc;
    @ExcelProperty("采购订单交货期")
    private String deliveryDateSummary;
    @ExcelProperty("供应商")
    private String supplierSummary;
}
