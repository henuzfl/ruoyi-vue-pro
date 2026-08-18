// SapPurchaseResultDTO.java - SAP 返回的每一行数据 (对应 ZSTLMM0001)
package cn.iocoder.yudao.module.wm.controller.admin.openordersync.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class SapPurchaseResultDTO {
    private String werks;        // 工厂
    private LocalDate bedat;     // 采购凭证日期
    private String ebeln;        // 采购订单号
    private String ebelp;        // 行项目
    private String bsart;        // 凭证类型
    private String ekgrp;        // 采购组
    private String lifnr;        // 供应商编码
    private String matnr;        // 物料编码
    private String maktx;        // 物料描述
    private BigDecimal menge;    // 订货数量
    private String waers;        // 货币
    private String meins;        // 单位
    private BigDecimal netpr;    // 净单价
    private LocalDate eindt;     // 交货日期
    private String mwskz;        // 税率
    private String bednr;        // 品牌信息
    private String lgort;        // 收货仓库
    private String retpo;        // 退货项目
    private String submi;        // 汇总号
    private BigDecimal emenge;   // 实收数量
    private LocalDate budat;     // 首次到货日期
    private String eknam;        // 采购组描述
    private String aufnr;        // 生产订单号
    private String name1;        // 供应商描述
    private BigDecimal kbetr;    // 税率百分比
    // 供应商反馈等字段可根据需要添加
}