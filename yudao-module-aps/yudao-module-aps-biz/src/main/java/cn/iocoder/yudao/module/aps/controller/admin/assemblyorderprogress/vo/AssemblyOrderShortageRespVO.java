package cn.iocoder.yudao.module.aps.controller.admin.assemblyorderprogress.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class AssemblyOrderShortageRespVO {
    private String purchaseMaterial;
    private String purchaseMaterialDesc;
    private BigDecimal shortageQty; // 缺口数量
    private String expectedDate;    // 预计到货日
    private String supplierName;
    private Date scheduleTime;      // 排产时间
}