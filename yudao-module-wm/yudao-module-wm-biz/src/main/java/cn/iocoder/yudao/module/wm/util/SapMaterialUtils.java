package cn.iocoder.yudao.module.wm.util;

import com.sap.conn.jco.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class SapMaterialUtils {

    @Autowired
    private SapRfcUtils sapRfcUtils;

    private static final String SAP_DATE_FORMAT = "yyyyMMdd";
    private static final int BATCH_SIZE = 200;
    private static final String DEFAULT_PLANT = "6400";

    // ========== 对外核心方法 ==========

    public List<Map<String, Object>> searchMaterials(List<String> matnrList, String plant) {
        return searchMaterials(matnrList, plant, null, null);
    }

    public List<Map<String, Object>> searchMaterials(List<String> matnrList, String plant,
                                                     Date fromDate, Date toDate) {
        if (matnrList == null || matnrList.isEmpty()) {
            return Collections.emptyList();
        }

        List<String> distinctMatnrs = matnrList.stream()
                .filter(StringUtils::hasText)
                .distinct()
                .collect(Collectors.toList());

        log.info("批量查询物料主数据，总物料数: {}, 工厂: {}", distinctMatnrs.size(),
                plant != null ? plant : DEFAULT_PLANT);

        List<Map<String, Object>> allResults = new ArrayList<>();
        for (int i = 0; i < distinctMatnrs.size(); i += BATCH_SIZE) {
            int end = Math.min(i + BATCH_SIZE, distinctMatnrs.size());
            List<String> batch = distinctMatnrs.subList(i, end);
            List<Map<String, Object>> batchResult = doSearchMaterials(batch, plant, fromDate, toDate);
            allResults.addAll(batchResult);
        }
        return allResults;
    }

    public List<Map<String, Object>> searchMaterials(List<String> matnrList) {
        return searchMaterials(matnrList, DEFAULT_PLANT, null, null);
    }

    public List<Map<String, Object>> searchMaterialsWithDefaultDate(List<String> matnrList) {
        Calendar cal = Calendar.getInstance();
        Date toDate = cal.getTime();
        cal.add(Calendar.DAY_OF_YEAR, -7);
        Date fromDate = cal.getTime();
        return searchMaterials(matnrList, DEFAULT_PLANT, fromDate, toDate);
    }

    public List<Map<String, Object>> searchMaterial(String matnr, String plant, Date fromDate, Date toDate) {
        return searchMaterials(Collections.singletonList(matnr), plant, fromDate, toDate);
    }

    public List<Map<String, Object>> searchMaterial(String matnr) {
        return searchMaterialsWithDefaultDate(Collections.singletonList(matnr));
    }

    public Map<String, Object> getMaterialInfo(String matnr) {
        List<Map<String, Object>> list = searchMaterial(matnr);
        return list.isEmpty() ? Collections.emptyMap() : list.get(0);
    }

    public List<Map<String, Object>> searchMaterial(String matnr, String plant) {
        return searchMaterials(Collections.singletonList(matnr), plant, null, null);
    }

    /**
     * 按日期范围查询物料主数据（不限制物料号）
     * @param fromDate 开始日期（SAP 日期格式 yyyyMMdd）
     * @param toDate   结束日期
     * @param plant    工厂（可为空，使用默认）
     * @return 物料列表
     */
    public List<Map<String, Object>> searchMaterialsByDate(Date fromDate, Date toDate, String plant) {
        String targetPlant = StringUtils.hasText(plant) ? plant : DEFAULT_PLANT;
        log.info("按日期范围查询物料，日期: {} - {}, 工厂: {}", fromDate, toDate, targetPlant);
        // 如果日期参数都为空，RFC 可能报错“物料号与更新日期至少填一个”，此处确保至少有一个日期
        if (fromDate == null && toDate == null) {
            throw new IllegalArgumentException("日期范围不能同时为空");
        }
        return doSearchMaterialsByDate(fromDate, toDate, targetPlant);
    }

    /**
     * 核心 RFC 调用，支持日期范围
     */
    private List<Map<String, Object>> doSearchMaterialsByDate(Date fromDate, Date toDate, String plant) {
        JCoDestination destination;
        JCoFunction function;
        try {
            destination = sapRfcUtils.getDestination();
            function = destination.getRepository().getFunction("ZFM_TL_SEARCH_MATNR");
            if (function == null) {
                throw new RuntimeException("SAP 中未找到函数: ZFM_TL_SEARCH_MATNR");
            }

            JCoParameterList importParams = function.getImportParameterList();

            // 1. 设置工厂（必填）
            importParams.setValue("IN_WERKS", plant);

            // 2. 设置日期范围表参数 IN_DATE（如果提供了日期）
            if (fromDate != null || toDate != null) {
                JCoTable inDateTable = importParams.getTable("IN_DATE");
                if (inDateTable == null) {
                    throw new RuntimeException("无法获取 IN_DATE 表参数");
                }
                inDateTable.clear();
                inDateTable.appendRow();

                // 设置 SIGN 和 OPTION
                inDateTable.setValue("SIGN", "I");           // I = 包含
                if (fromDate != null && toDate != null && !fromDate.equals(toDate)) {
                    // 日期范围
                    inDateTable.setValue("OPTION", "BT");     // BT = Between
                    inDateTable.setValue("LOW", formatSapDate(fromDate));
                    inDateTable.setValue("HIGH", formatSapDate(toDate));
                    log.info("设置 IN_DATE 范围: {} - {}", formatSapDate(fromDate), formatSapDate(toDate));
                } else if (fromDate != null) {
                    // 单个日期（等于）
                    inDateTable.setValue("OPTION", "EQ");     // EQ = Equal
                    inDateTable.setValue("LOW", formatSapDate(fromDate));
                    log.info("设置 IN_DATE 单个日期: {}", formatSapDate(fromDate));
                } else if (toDate != null) {
                    // 如果只有结束日期，可以使用 LE（小于等于），但一般应同时提供起始日期
                    inDateTable.setValue("OPTION", "LE");
                    inDateTable.setValue("HIGH", formatSapDate(toDate));
                }
            }

            // 3. 物料列表（可选）：不传物料列表，即 IT_MAT 为空表，表示查询所有物料
            JCoTable itMat = importParams.getTable("IT_MAT");
            if (itMat != null) {
                itMat.clear();   // 空表，不限制物料
            }

            // 4. 执行 RFC
            function.execute(destination);

            // 5. 获取返回表
            JCoTable etMara = function.getTableParameterList().getTable("ET_MARA");
            int rowCount = etMara.getNumRows();
            log.info("【SAP查询】返回物料行数: {}", rowCount);

            // 6. 检查返回消息
            String ztype = function.getExportParameterList().getString("ZTYPE");
            String message = function.getExportParameterList().getString("MESSAGE");
            if ("E".equalsIgnoreCase(ztype)) {
                throw new RuntimeException("SAP 返回错误: " + message);
            }

            // 7. 转换结果
            return convertJCoTableToList(etMara);

        } catch (JCoException e) {
            log.error("执行 SAP RFC 失败", e);
            throw new RuntimeException("SAP 物料查询异常: " + e.getMessage(), e);
        }
    }

    // 辅助方法
    private String getString(Map<String, Object> map, String key) {
        Object val = map.get(key);
        return val == null ? "" : val.toString();
    }

    // ========== 核心 RFC 调用 ==========

    private List<Map<String, Object>> doSearchMaterials(List<String> matnrBatch, String plant,
                                                        Date fromDate, Date toDate) {
        log.info("【SAP查询】调用 RFC: ZFM_TL_SEARCH_MATNR，物料数: {}, 工厂: {}", matnrBatch.size(), plant);
        JCoDestination destination;
        JCoFunction function;

        try {
            destination = sapRfcUtils.getDestination();
            function = destination.getRepository().getFunction("ZFM_TL_SEARCH_MATNR");
            if (function == null) {
                throw new RuntimeException("SAP 中未找到函数: ZFM_TL_SEARCH_MATNR");
            }

            String targetPlant = StringUtils.hasText(plant) ? plant : DEFAULT_PLANT;

            // 1. 设置导入参数 IN_WERKS
            JCoParameterList importParams = function.getImportParameterList();
            importParams.setValue("IN_WERKS", targetPlant);

            // 2. 单物料时设置 IN_MATNR（可选，但为了兼容也保留）
            if (matnrBatch.size() == 1) {
                importParams.setValue("IN_MATNR", matnrBatch.get(0));
            }

            // 3. 获取 IT_MAT 表参数
            JCoTable itMat = importParams.getTable("IT_MAT");
            if (itMat == null) {
                throw new RuntimeException("SAP RFC 中未找到导入表参数 IT_MAT");
            }

            // 4. 填充物料列表
            itMat.clear();
            for (String matnr : matnrBatch) {
                itMat.appendRow();
                // 直接使用列索引 0 赋值，无需关心字段名
                itMat.setValue(0, matnr);
            }
            log.debug("IT_MAT 已设置 {} 行", itMat.getNumRows());

            // 5. 执行 RFC
            function.execute(destination);

            // 6. 获取返回表 ET_MARA
            JCoTable etMara = function.getTableParameterList().getTable("ET_MARA");
            int rowCount = etMara.getNumRows();
            log.info("【SAP查询】返回结果行数: {}", rowCount);

            // 7. 获取导出消息
            String ztype = function.getExportParameterList().getString("ZTYPE");
            String message = function.getExportParameterList().getString("MESSAGE");
            if ("E".equalsIgnoreCase(ztype)) {
                throw new RuntimeException("SAP 返回错误: " + message);
            }
            log.debug("SAP 返回消息: {} - {}", ztype, message);

            return convertJCoTableToList(etMara);

        } catch (JCoException e) {
            log.error("执行 SAP RFC 失败", e);
            throw new RuntimeException("SAP 物料查询异常: " + e.getMessage(), e);
        }
    }



    /**
     * 确定 IT_MAT 表的字段名
     * 优先尝试通过元数据获取第一个字段名，失败则返回默认值 "MATNR"
     */
    private String resolveItMatFieldName(JCoTable table) {
        try {
            JCoRecordMetaData metaData = table.getRecordMetaData();
            if (metaData != null && metaData.getFieldCount() > 0) {
                String name = metaData.getName(0);
                if (StringUtils.hasText(name)) {
                    return name;
                }
            }
        } catch (Exception e) {
            log.warn("获取 IT_MAT 元数据失败，使用默认字段名 'MATNR'", e);
        }
        // 默认使用 MATNR
        return "MATNR";
    }

    // ========== 辅助方法 ==========

    private String formatSapDate(Date date) {
        if (date == null) return "";
        return new SimpleDateFormat(SAP_DATE_FORMAT).format(date);
    }

    private List<Map<String, Object>> convertJCoTableToList(JCoTable table) {
        return sapRfcUtils.convertJCoTableToList(table);
    }
}