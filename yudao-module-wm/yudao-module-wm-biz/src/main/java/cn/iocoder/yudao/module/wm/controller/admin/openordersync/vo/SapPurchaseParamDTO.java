// SapPurchaseParamDTO.java - 输入参数
package cn.iocoder.yudao.module.wm.controller.admin.openordersync.vo;

import lombok.Data;

import java.time.LocalDate;

@Data
public class SapPurchaseParamDTO {
    private String werks;        // 工厂 (必填)
    private String lifnr;        // 供应商
    private LocalDate bedatS;    // 下单日期起
    private LocalDate bedatE;    // 下单日期止
    private String ebeln;        // 采购订单号
    private String matnr;        // 物料号
    private LocalDate eindtS;    // 交货日期起
    private LocalDate eindtE;    // 交货日期止
    private String lgort;        // 库存地点
    private String isAll;        // 是否全量 (Y/N)
    private String username;     // 用户名（用于日志）
}