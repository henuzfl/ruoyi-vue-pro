package cn.iocoder.yudao.module.wm.service.purchaseorder;

import cn.iocoder.yudao.module.wm.dal.dataobject.purchaseorder.PurchaseOrderDO;
import cn.iocoder.yudao.module.wm.util.SapRfcUtils;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.sap.conn.jco.*; // 关键修改：使用JCO 3.0导入
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 采购订单Service业务层实现 - JCO 3.0版本
 */
@Service
@DS("oracle")
@Validated
public class PurchaseOrderServiceImpl implements IPurchaseOrderService {

    private static final Logger log = LoggerFactory.getLogger(PurchaseOrderServiceImpl.class);

    @Autowired
    private SapRfcUtils sapRfcUtils;

    /**
     * 查询SAP采购订单 - JCO 3.0版本
     */
    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> findSapPurchaseOrder(String vendorNo, Date startDate, Date endDate,
                                                    Map<String, Object> otherConditions) {

        Map<String, Object> result = new HashMap<>();

        try {
            // 1. 参数预处理
            String ebeln = (String) otherConditions.get("ebeln");
            String matnr = (String) otherConditions.get("matnr");
            Date eindt_s = (Date) otherConditions.get("eindt_s");
            Date eindt_e = (Date) otherConditions.get("eindt_e");
            String allInfo = (String) otherConditions.get("allInfo");

            // 2. 使用SapRfcUtils获取JCO 3.0目的地
            JCoDestination destination = sapRfcUtils.getDestination();

            // 3. 获取函数对象 (JCO 3.0方式)
            JCoFunction function = destination.getRepository().getFunction("ZFGTLMM0002_GET_PURCH");

            if (function == null) {
                throw new RuntimeException("在SAP中未找到函数: ZFGTLMM0002_GET_PURCH");
            }

            // 4. 设置输入参数
            JCoParameterList input = function.getImportParameterList();

            // 设置固定参数
            input.setValue("WERKS", "6400");

            // 供应商号补零处理
            if (vendorNo != null && vendorNo.length() < 10) {
                int length = vendorNo.length();
                for (int i = 0; i < 10 - length; i++) {
                    vendorNo = "0" + vendorNo;
                }
            }
            input.setValue("LIFNR", vendorNo);

            // 日期格式化设置
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            if (startDate != null) {
                input.setValue("BEDAT_S", sdf.format(startDate));
            }
            if (endDate != null) {
                input.setValue("BEDAT_E", sdf.format(endDate));
            }

            // 设置其他条件参数
            if (StringUtils.isNotEmpty(ebeln)) {
                input.setValue("EBELN", ebeln);
            }
            if (StringUtils.isNotEmpty(matnr)) {
                input.setValue("MATNR", matnr);
            }
            if (eindt_s != null) {
                input.setValue("EINDT_S", sdf.format(eindt_s));
            }
            if (eindt_e != null) {
                input.setValue("EINDT_E", sdf.format(eindt_e));
            }
            if (StringUtils.isNotEmpty(allInfo)) {
                input.setValue("IS_ALL", allInfo);
            }

            // 用户名处理
            String username = (String) otherConditions.get("username");
            if (username != null && username.length() < 10) {
                int length = username.length();
                for (int i = 0; i < 10 - length; i++) {
                    username = "0" + username;
                }
            }
            input.setValue("USERNAME", username);

            // 5. 执行SAP函数 (JCO 3.0方式)
            function.execute(destination);

            // 6. 处理返回结果
            // 6.1 获取EXPORT参数
            JCoParameterList exportParams = function.getExportParameterList();
            if (exportParams != null) {
                result.put("TYPE", exportParams.getValue("RETN_TYPE"));
                result.put("MESSAGE", exportParams.getValue("RETN_MESG"));
            }

            // 6.2 获取TABLE参数（采购订单数据）- JCO 3.0方式
            JCoTable retnTab = function.getTableParameterList().getTable("RETN_TAB");
            if (retnTab != null && !retnTab.isEmpty()) {
                List<PurchaseOrderDO> retnTabList = convertJCoTableToPurchaseOrderList(retnTab);
                result.put("RETNTAB", retnTabList);
            } else {
                result.put("RETNTAB", new ArrayList<>());
            }

        } catch (JCoException e) {
            log.error("查询SAP采购订单时发生JCO异常", e);
            result.put("TYPE", "E");
            result.put("MESSAGE", "SAP连接错误: " + e.getMessage());
            throw new RuntimeException("查询采购订单失败", e);
        } catch (Exception e) {
            log.error("查询SAP采购订单时发生未知异常", e);
            result.put("TYPE", "E");
            result.put("MESSAGE", "系统错误：" + e.getMessage());
            throw new RuntimeException("查询采购订单失败", e);
        }

        return result;
    }

    /**
     * 将JCoTable转换为PurchaseOrder对象列表 - JCO 3.0版本
     */
    private List<PurchaseOrderDO> convertJCoTableToPurchaseOrderList(JCoTable retnTab) {
        List<PurchaseOrderDO> resultList = new ArrayList<>();

        // 保存当前行位置
        int currentRow = retnTab.getRow();

        try {
            // 遍历表格每一行
            for (int i = 0; i < retnTab.getNumRows(); i++) {
                retnTab.setRow(i);
                PurchaseOrderDO item = convertJCoTableRowToPurchaseOrder(retnTab);
                resultList.add(item);
            }
        } finally {
            // 恢复原来的行位置
            retnTab.setRow(currentRow);
        }

        return resultList;
    }

    /**
     * 将JCO Table行转换为PurchaseOrder对象 - JCO 3.0版本
     */
    private PurchaseOrderDO convertJCoTableRowToPurchaseOrder(JCoTable retnTab) {
        PurchaseOrderDO item = new PurchaseOrderDO();

        // 直接映射字段 - 使用JCO 3.0的getString方法
        item.setWERKS(retnTab.getString("WERKS"));
        item.setBEDAT(retnTab.getString("BEDAT"));
        item.setEBELN(retnTab.getString("EBELN"));
        item.setEBELP(retnTab.getString("EBELP"));
        item.setBSART(retnTab.getString("BSART"));
        item.setEKGRP(retnTab.getString("EKGRP"));
        item.setEKNAM(retnTab.getString("EKNAM"));
        item.setLIFNR(retnTab.getString("LIFNR"));
        item.setMATNR(retnTab.getString("MATNR"));
        item.setMAKTX(retnTab.getString("MAKTX"));

        // 处理数值型字段 - 使用JCO 3.0的getDouble方法
        try {
            item.setMENGE(retnTab.getDouble("MENGE"));
            item.setNETPR(retnTab.getDouble("NETPR"));
            item.setKBETR(retnTab.getDouble("KBETR"));
            item.setEMENGE(retnTab.getDouble("EMENGE"));
        } catch (Exception e) {
            log.warn("转换数值字段时发生异常，使用默认值0", e);
            item.setMENGE(0.0);
            item.setNETPR(0.0);
            item.setKBETR(0.0);
            item.setEMENGE(0.0);
        }

        item.setMEINS(retnTab.getString("MEINS"));
        item.setWAERS(retnTab.getString("WAERS"));
        item.setEINDT(retnTab.getString("EINDT"));
        item.setMWSKZ(retnTab.getString("MWSKZ"));
        item.setBEDNR(retnTab.getString("BEDNR"));
        item.setAUFNR(retnTab.getString("AUFNR"));
        item.setLGORT(retnTab.getString("LGORT"));
        item.setBUDAT(retnTab.getString("BUDAT"));
        item.setBELNR(retnTab.getString("BELNR"));
        item.setBUZEI(retnTab.getString("BUZEI"));
        item.setSHKZG(retnTab.getString("SHKZG"));
        item.setRETPO(retnTab.getString("RETPO"));
        item.setNAME1(retnTab.getString("NAME1"));
        item.setSGTXT(retnTab.getString("SGTXT"));
        item.setLFBNR(retnTab.getString("LFBNR"));
        item.setLFPOS(retnTab.getString("LFPOS"));
        item.setSUBMI(retnTab.getString("SUBMI"));
        item.setQMENGE(retnTab.getString("QMENGE"));
        item.setQDMBTR(retnTab.getString("QDMBTR"));
        item.setQDATS(retnTab.getString("QDATS"));
        item.setQTIMS(retnTab.getString("QTIMS"));
        item.setFBDATS(retnTab.getString("FBDATS"));
        item.setARROT(retnTab.getString("ARROT"));
        item.setDLDATS(retnTab.getString("DLDATS"));
        item.setLTTYPE(retnTab.getString("LTTYPE"));
        item.setLTREAS(retnTab.getString("LTREAS"));
        item.setCOMMNT(retnTab.getString("COMMNT"));
        item.setUSRNM(retnTab.getString("USRNM"));
        item.setLTTYPE1(retnTab.getString("LTTYPE1"));
        item.setLTREAS1(retnTab.getString("LTREAS1"));
        item.setCOMMNT1(retnTab.getString("COMMNT1"));
        item.setCRTDAT1(retnTab.getString("CRTDAT1"));
        item.setCRTTIM1(retnTab.getString("CRTTIM1"));
        item.setUSRNM1(retnTab.getString("USRNM1"));

        // 计算字段
        Double menge = item.getMENGE();
        Double emenge1 = menge - item.getEMENGE();
        item.setEMENGE1(emenge1);

        Double netpr = item.getNETPR();
        Double netpr1 = netpr * (1.0 + item.getKBETR());

        // 使用BigDecimal保证精度
        item.setTOTAL1(BigDecimal.valueOf(netpr * menge)
                .setScale(2, BigDecimal.ROUND_HALF_UP)
                .doubleValue());
        item.setTOTAL2(BigDecimal.valueOf(netpr1 * menge)
                .setScale(2, BigDecimal.ROUND_HALF_UP)
                .doubleValue());

        // 临时注释掉依赖代码
        item.setONPASSAGE(0.0);
        item.setCREATING(item.getEMENGE1());

        return item;
    }

    /**
     * 简化版本：直接使用SapRfcUtils的通用执行方法
     */
    public Map<String, Object> findSapPurchaseOrderSimple(String vendorNo, Date startDate, Date endDate,
                                                          Map<String, Object> otherConditions) {
        try {
            // 使用SapRfcUtils的便捷方法
            return sapRfcUtils.callPurchaseOrderFunction(vendorNo, startDate, endDate, otherConditions);

        } catch (Exception e) {
            log.error("简化版SAP查询失败", e);
            Map<String, Object> errorResult = new HashMap<>();
            errorResult.put("TYPE", "E");
            errorResult.put("MESSAGE", "查询失败：" + e.getMessage());
            return errorResult;
        }
    }
}