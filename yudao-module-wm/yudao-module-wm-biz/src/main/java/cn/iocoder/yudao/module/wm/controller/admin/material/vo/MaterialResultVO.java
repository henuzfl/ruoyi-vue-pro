package cn.iocoder.yudao.module.wm.controller.admin.material.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Schema(description = "管理后台 - SAP物料信息 Response VO")
@Data
public class MaterialResultVO {

    // ========== MARA 基础表字段 ==========
    @Schema(description = "物料号", example = "100001")
    private String materialNumber;          // MATNR

    @Schema(description = "物料描述（中文）", example = "液压油缸")
    private String materialDescCn;          // MAKTX

    @Schema(description = "物料类型", example = "FERT")
    private String materialType;            // MTART

    @Schema(description = "基本计量单位", example = "PC")
    private String baseUnit;                // MEINS

    @Schema(description = "毛重", example = "10.5")
    private BigDecimal grossWeight;         // BRGEW

    @Schema(description = "净重", example = "9.2")
    private BigDecimal netWeight;           // NTGEW

    @Schema(description = "大小量纲", example = "M")
    private String sizeDimension;           // GROES

    // ========== MARC 工厂数据字段 ==========
    @Schema(description = "工厂", example = "6400")
    private String plant;                   // WERKS

    @Schema(description = "采购类型", example = "E")
    private String procurementType;         // BESKZ

    @Schema(description = "生产仓储地点", example = "A01")
    private String productionStorageLoc;    // LGPRO

    @Schema(description = "生产调度员", example = "张三")
    private String productionScheduler;     // FEVOR

    @Schema(description = "采购组", example = "001")
    private String purchasingGroup;         // EKGRP

    @Schema(description = "外部采购仓储地点", example = "B01")
    private String externalProcurementStorage; // LGFSB

    @Schema(description = "计划交货时间（天）", example = "3")
    private Integer plannedDeliveryTime;    // PLIFZ

    @Schema(description = "无成本核算标识", example = "X")
    private String noCostEstimation;        // NCOST

    // ========== MBEW 评估数据字段 ==========
    @Schema(description = "评估类", example = "3000")
    private String valuationClass;          // BKLAS

    @Schema(description = "价格控制", example = "S")
    private String priceControl;            // VPRSV

    @Schema(description = "QS成本估算", example = "1250.00")
    private String qsCostEstimate;      // EKALR

    // ========== ZTLMMM0001 自定义表字段 ==========
    @Schema(description = "配送标识", example = "Y")
    private String distributionFlag;        // ZFIELD3

    @Schema(description = "物料分类", example = "A1")
    private String materialCategory;        // ZFIELD4

    // ========== 其他 ==========
    @Schema(description = "最后更改日期（系统执行日期）", example = "20231231")
    private String lastChangeDate;          // LAEDA
}