package cn.iocoder.yudao.module.wm.util;

import com.sap.conn.jco.JCoTable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * SAP库存查询工具类
 * 提供从SAP获取库存相关数据的工具方法
 */
@Component
@Slf4j
public class SapInventoryUtils {

    @Autowired
    private SapRfcUtils sapRfcUtils;

    /**
     * 根据条件查询SAP库存
     * @param params 查询参数
     * @return 库存数据列表
     */
    public List<Map<String, Object>> searchInventory(Map<String, Object> params) {
        List<Map<String, Object>> result = new ArrayList<>();

        try {
            log.info("调用SAP库存函数 Z_CY_SEARCH_CKKC_FROM_SAP, 参数: {}", params);

            // 使用SapRfcUtils执行函数
            Map<String, Object> functionResult = sapRfcUtils.executeFunction("Z_CY_SEARCH_CKKC_FROM_SAP", params);

            // 检查错误
            if ("X".equals(functionResult.get("ERROR"))) {
                String errorMsg = (String) functionResult.get("ERMSG");
                throw new RuntimeException("SAP库存查询失败: " + errorMsg);
            }

            // 获取RE_DATA表数据
            Object reDataObj = functionResult.get("RE_DATA");
            if (reDataObj instanceof List) {
                result = (List<Map<String, Object>>) reDataObj;
            } else if (reDataObj instanceof JCoTable) {
                result = convertJCoTableToList((JCoTable) reDataObj);
            }

            log.info("成功获取库存数据: 记录数={}", result.size());

        } catch (Exception e) {
            log.error("处理库存数据异常", e);
            throw new RuntimeException("处理库存数据异常: " + e.getMessage(), e);
        }

        return result;
    }

    /**
     * 按条件查询库存（简化参数）
     */
    public List<Map<String, Object>> searchInventory(String materialNumber, String plant,
                                                     String storageLocation, String specialStock) {
        Map<String, Object> params = new HashMap<>();

        if (StringUtils.hasText(materialNumber)) {
            params.put("MATNR", materialNumber);
        }
        if (StringUtils.hasText(plant)) {
            params.put("WERKS", plant);
        }
        if (StringUtils.hasText(storageLocation)) {
            params.put("LGORT", storageLocation);
        }
        if (StringUtils.hasText(specialStock)) {
            params.put("SOBKZ", specialStock);
        }

        return searchInventory(params);
    }

    /**
     * 按条件查询库存（简化参数），支持过滤零库存
     */
    public List<Map<String, Object>> searchInventory(String materialNumber, String plant,
                                                     String storageLocation, String specialStock,
                                                     boolean filterZeroStock) {
        Map<String, Object> params = new HashMap<>();
        if (StringUtils.hasText(materialNumber)) {
            params.put("MATNR", materialNumber);
        }
        if (StringUtils.hasText(plant)) {
            params.put("WERKS", plant);
        }
        if (StringUtils.hasText(storageLocation)) {
            params.put("LGORT", storageLocation);
        }
        if (StringUtils.hasText(specialStock)) {
            params.put("SOBKZ", specialStock);
        }
        // 新增：过滤零库存
        if (filterZeroStock) {
            params.put("BFIELD1", "X");
        }
        return searchInventory(params);
    }

    /**
     * 获取物料库存汇总
     */
    public Map<String, Object> getMaterialInventorySummary(String materialNumber, String plant) {
        List<Map<String, Object>> inventoryList = searchInventory(materialNumber, plant, null, null);

        Map<String, Object> summary = new HashMap<>();
        double totalStock = 0;
        Map<String, Double> locationStock = new HashMap<>();

        for (Map<String, Object> item : inventoryList) {
            Object stockObj = item.get("LABST");
            if (stockObj != null) {
                try {
                    double stock = Double.parseDouble(stockObj.toString());
                    totalStock += stock;

                    // 按库存地点统计
                    Object locationObj = item.get("LGORT");
                    if (locationObj != null) {
                        String location = locationObj.toString();
                        locationStock.put(location, locationStock.getOrDefault(location, 0.0) + stock);
                    }
                } catch (NumberFormatException e) {
                    // 忽略转换错误
                }
            }
        }

        summary.put("materialNumber", materialNumber);
        summary.put("plant", plant);
        summary.put("totalStock", totalStock);
        summary.put("locationStock", locationStock);
        summary.put("recordCount", inventoryList.size());

        return summary;
    }

    /**
     * 检查物料是否有库存
     */
    public boolean checkMaterialHasStock(String materialNumber, String plant) {
        List<Map<String, Object>> inventoryList = searchInventory(materialNumber, plant, null, null);

        for (Map<String, Object> item : inventoryList) {
            Object stockObj = item.get("LABST");
            if (stockObj != null) {
                try {
                    double stock = Double.parseDouble(stockObj.toString());
                    if (stock > 0) {
                        return true;
                    }
                } catch (NumberFormatException e) {
                    // 忽略转换错误
                }
            }
        }

        return false;
    }

    // ==================== 私有方法 ====================

    /**
     * JCoTable转List<Map>
     */
    private List<Map<String, Object>> convertJCoTableToList(JCoTable table) {
        List<Map<String, Object>> result = new ArrayList<>();

        if (table == null || table.isEmpty()) {
            return result;
        }

        try {
            // 获取所有字段名
            List<String> fieldNames = new ArrayList<>();
            com.sap.conn.jco.JCoRecordMetaData metaData = table.getRecordMetaData();
            for (int i = 0; i < metaData.getFieldCount(); i++) {
                fieldNames.add(metaData.getName(i));
            }

            // 处理每一行
            for (int i = 0; i < table.getNumRows(); i++) {
                table.setRow(i);
                Map<String, Object> row = new LinkedHashMap<>();

                // 处理所有字段
                for (String fieldName : fieldNames) {
                    try {
                        Object value = table.getValue(fieldName);
                        row.put(fieldName, value);
                    } catch (Exception e) {
                        log.warn("获取字段值失败: {}", fieldName, e);
                    }
                }

                // 添加额外字段
                addInventoryCalculatedFields(row);
                result.add(row);
            }
        } catch (Exception e) {
            log.error("转换JCoTable失败", e);
        }

        return result;
    }

    /**
     * 添加库存计算字段
     */
    private void addInventoryCalculatedFields(Map<String, Object> row) {
        try {
            // 判断库存类型
            Object sobkzObj = row.get("SOBKZ");
            if (sobkzObj != null) {
                String sobkz = sobkzObj.toString();
                switch (sobkz) {
                    case "E": // 销售订单库存
                        row.put("STOCK_TYPE", "E");
                        row.put("STOCK_TYPE_DESC", "销售订单库存");
                        break;
                    case "K": // 供应商寄售库存
                        row.put("STOCK_TYPE", "K");
                        row.put("STOCK_TYPE_DESC", "供应商寄售库存");
                        break;
                    case "O": // 项目库存
                        row.put("STOCK_TYPE", "O");
                        row.put("STOCK_TYPE_DESC", "项目库存");
                        break;
                    case "Q": // 寄售库存
                        row.put("STOCK_TYPE", "Q");
                        row.put("STOCK_TYPE_DESC", "寄售库存");
                        break;
                    default: // 普通库存
                        row.put("STOCK_TYPE", "");
                        row.put("STOCK_TYPE_DESC", "普通库存");
                        break;
                }
            } else {
                row.put("STOCK_TYPE", "");
                row.put("STOCK_TYPE_DESC", "普通库存");
            }

            // 判断库存状态
            Object labstObj = row.get("LABST");
            if (labstObj != null) {
                try {
                    double stock = Double.parseDouble(labstObj.toString());
                    if (stock > 0) {
                        row.put("STOCK_STATUS", "AVAILABLE");
                        row.put("STOCK_STATUS_DESC", "有库存");
                    } else {
                        row.put("STOCK_STATUS", "NO_STOCK");
                        row.put("STOCK_STATUS_DESC", "无库存");
                    }
                } catch (NumberFormatException e) {
                    row.put("STOCK_STATUS", "UNKNOWN");
                    row.put("STOCK_STATUS_DESC", "库存状态未知");
                }
            }

            // 生成唯一标识
            Object matnrObj = row.get("MATNR");
            Object werksObj = row.get("WERKS");
            Object lgortObj = row.get("LGORT");
            if (matnrObj != null && werksObj != null && lgortObj != null) {
                row.put("UNIQUE_KEY",
                        matnrObj.toString() + "_" +
                                werksObj.toString() + "_" +
                                lgortObj.toString());
            }

        } catch (Exception e) {
            log.warn("添加库存计算字段失败", e);
        }
    }
}
// [file content end]