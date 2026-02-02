package cn.iocoder.yudao.module.wm.dal.dataobject.purchaseorder;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.math.BigDecimal;

/**
 * 订单追溯需求 DO
 *
 * @author 柳文
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseOrderDO extends BaseDO {

    private Boolean SELECTED;

    private String WERKS;

    private String BEDAT;

    private String EBELN;

    private String EBELP;

    private String BSART;

    private String EKGRP;

    private String EKNAM;

    private String LIFNR;

    private String MATNR;

    private String MAKTX;

    private Double MENGE;

    private String MEINS;

    private Double NETPR;

    private String WAERS;

    private String EINDT;

    private String MWSKZ;

    private Double KBETR;

    private String BEDNR;

    private Double EMENGE;

    private Double EMENGE1;

    private String AUFNR;

    private String LGORT;

    private String BUDAT;

    private String BELNR;

    private String BUZEI;

    private String SHKZG;

    private String RETPO;

    private String NAME1;

    private String SGTXT;

    private String LFBNR;

    private String LFPOS;

    private String SUBMI;

    private String QMENGE;

    private String QDMBTR;

    private String QDATS;

    private String QTIMS;

    private String FBDATS;

    private String ARROT;

    private String DLDATS;

    private String LTTYPE;

    private String LTREAS;

    private String COMMNT;

    private String USRNM;

    private String LTTYPE1;

    private String LTREAS1;

    private String COMMNT1;

    private String CRTDAT1;

    private String CRTTIM1;

    private String USRNM1;

    private Double TOTAL1;

    private Double TOTAL2;

    // 已创建未收货的数量
    private Double ONPASSAGE;

    // 拟发货数量
    private Double CREATING;



}