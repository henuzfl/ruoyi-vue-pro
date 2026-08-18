package cn.iocoder.yudao.module.wm.util;   // 或者放在合适包下


import cn.iocoder.yudao.module.wm.controller.admin.openordersync.vo.SapPurchaseParamDTO;
import cn.iocoder.yudao.module.wm.controller.admin.openordersync.vo.SapPurchaseResultDTO;
import com.sap.conn.jco.JCoTable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class SapPurchaseUtils {

    @Autowired
    private SapRfcUtils sapRfcUtils;

    private static final String FUNCTION_NAME = "ZFGTLMM0002_GET_PURCH";
    private static final DateTimeFormatter SAP_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd");

    /**
     * 调用 SAP 采购订单接口
     * @param param 查询参数
     * @return 订单行列表
     */
    public List<SapPurchaseResultDTO> getPurchaseOrders(SapPurchaseParamDTO param) {
        if (param == null || !StringUtils.hasText(param.getWerks())) {
            throw new IllegalArgumentException("工厂不能为空");
        }

        Map<String, Object> sapParams = new HashMap<>();
        sapParams.put("WERKS", param.getWerks());
        sapParams.put("IS_ALL", "N");
        if (StringUtils.hasText(param.getLifnr())) sapParams.put("LIFNR", param.getLifnr());
        if (StringUtils.hasText(param.getEbeln())) sapParams.put("EBELN", param.getEbeln());
        if (StringUtils.hasText(param.getMatnr())) sapParams.put("MATNR", param.getMatnr());
        if (StringUtils.hasText(param.getLgort())) sapParams.put("LGORT", param.getLgort());
        if (StringUtils.hasText(param.getUsername())) sapParams.put("USERNAME", param.getUsername());

        // 日期转换 (SAP 期望格式 YYYYMMDD 字符串)
        if (param.getBedatS() != null) {
            sapParams.put("BEDAT_S", param.getBedatS().format(SAP_DATE_FORMAT));
        }
        if (param.getBedatE() != null) {
            sapParams.put("BEDAT_E", param.getBedatE().format(SAP_DATE_FORMAT));
        }
        if (param.getEindtS() != null) {
            sapParams.put("EINDT_S", param.getEindtS().format(SAP_DATE_FORMAT));
        }
        if (param.getEindtE() != null) {
            sapParams.put("EINDT_E", param.getEindtE().format(SAP_DATE_FORMAT));
        }

        Map<String, Object> result = sapRfcUtils.executeFunction(FUNCTION_NAME, sapParams);

        // 检查返回结果
        if (result.containsKey("ERROR")) {
            String msg = (String) result.getOrDefault("ERROR_MESSAGE", "未知错误");
            throw new RuntimeException("SAP 函数调用失败: " + msg);
        }

        String retnType = (String) result.get("RETN_TYPE");
        if (!"S".equals(retnType)) {
            String retnMesg = (String) result.get("RETN_MESG");
            throw new RuntimeException("SAP 业务返回错误: " + retnMesg);
        }

        // 获取表数据 ITAB (对应 RETN_TAB)
        Object tableObj = result.get("RETN_TAB");
        List<Map<String, Object>> rawList = new ArrayList<>();
        if (tableObj instanceof List) {
            rawList = (List<Map<String, Object>>) tableObj;
        } else if (tableObj instanceof JCoTable) {
            rawList = convertJCoTableToList((JCoTable) tableObj);
        }

        // 转换为 DTO 列表
        return rawList.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private List<Map<String, Object>> convertJCoTableToList(JCoTable table) {
        List<Map<String, Object>> result = new ArrayList<>();
        if (table == null || table.isEmpty()) return result;
        for (int i = 0; i < table.getNumRows(); i++) {
            table.setRow(i);
            Map<String, Object> row = new LinkedHashMap<>();
            for (int j = 0; j < table.getRecordMetaData().getFieldCount(); j++) {
                String name = table.getRecordMetaData().getName(j);
                row.put(name, table.getValue(name));
            }
            result.add(row);
        }
        return result;
    }

    private SapPurchaseResultDTO convertToDTO(Map<String, Object> raw) {
        SapPurchaseResultDTO dto = new SapPurchaseResultDTO();
        dto.setWerks(getString(raw, "WERKS"));
        dto.setBedat(getLocalDate(raw, "BEDAT"));
        dto.setEbeln(getString(raw, "EBELN"));
        dto.setEbelp(getString(raw, "EBELP"));
        dto.setBsart(getString(raw, "BSART"));
        dto.setEkgrp(getString(raw, "EKGRP"));
        dto.setLifnr(getString(raw, "LIFNR"));
        dto.setMatnr(getString(raw, "MATNR"));
        dto.setMaktx(getString(raw, "MAKTX"));
        dto.setMenge(getBigDecimal(raw, "MENGE"));
        dto.setWaers(getString(raw, "WAERS"));
        dto.setMeins(getString(raw, "MEINS"));
        dto.setNetpr(getBigDecimal(raw, "NETPR"));
        dto.setEindt(getLocalDate(raw, "EINDT"));
        dto.setMwskz(getString(raw, "MWSKZ"));
        dto.setBednr(getString(raw, "BEDNR"));
        dto.setLgort(getString(raw, "LGORT"));
        dto.setRetpo(getString(raw, "RETPO"));
        dto.setSubmi(getString(raw, "SUBMI"));
        dto.setEmenge(getBigDecimal(raw, "EMENGE"));
        dto.setBudat(getLocalDate(raw, "BUDAT"));
        dto.setEknam(getString(raw, "EKNAM"));
        dto.setAufnr(getString(raw, "AUFNR"));
        dto.setName1(getString(raw, "NAME1"));
        dto.setKbetr(getBigDecimal(raw, "KBETR"));
        return dto;
    }

    private String getString(Map<String, Object> map, String key) {
        Object v = map.get(key);
        return v == null ? null : v.toString();
    }

    private BigDecimal getBigDecimal(Map<String, Object> map, String key) {
        Object v = map.get(key);
        if (v == null) return null;
        if (v instanceof BigDecimal) return (BigDecimal) v;
        try {
            return new BigDecimal(v.toString());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private LocalDate getLocalDate(Map<String, Object> map, String key) {
        Object v = map.get(key);
        if (v == null) return null;
        if (v instanceof LocalDate) return (LocalDate) v;
        // SAP 可能返回 Date 或 String
        if (v instanceof java.util.Date) {
            return ((java.util.Date) v).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        }
        String str = v.toString();
        if (str.length() == 8) {
            return LocalDate.parse(str, DateTimeFormatter.BASIC_ISO_DATE);
        }
        return null;
    }
}