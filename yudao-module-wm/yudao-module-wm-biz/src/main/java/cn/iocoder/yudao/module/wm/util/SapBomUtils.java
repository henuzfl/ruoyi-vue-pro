package cn.iocoder.yudao.module.wm.util;

import com.sap.conn.jco.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * SAP BOM工具类
 * 提供从SAP获取BOM相关数据的工具方法
 */
@Component
@Slf4j
public class SapBomUtils {

    @Autowired
    private SapRfcUtils sapRfcUtils;

    /**
     * 获取BOM所有组件 - 使用ZLZK_HNT_MES_BOM函数
     * @param materialNumber 物料号
     * @param plant 工厂
     * @return BOM组件列表
     */
    public List<Map<String, Object>> getFullBomComponents(String materialNumber, String plant) {
        return getBomComponentsByFunction(materialNumber, plant, null);
    }

    /**
     * 获取BOM组件详细信息
     * @param materialNumber 物料号
     * @param plant 工厂
     * @param date 日期（可选，格式：YYYYMMDD）
     * @return BOM组件列表
     */
    public List<Map<String, Object>> getBomComponentsByFunction(String materialNumber, String plant, String date) {
        List<Map<String, Object>> result = new ArrayList<>();

        try {
            // 参数校验
            validateBomParameters(materialNumber, plant);

            log.info("调用SAP BOM函数 ZLZK_HNT_MES_BOM, 物料: {}, 工厂: {}, 日期: {}",
                    materialNumber, plant, date);

            // 准备参数
            Map<String, Object> params = new HashMap<>();
            params.put("MATNR", materialNumber);
            params.put("WERKS", plant);

            // 日期参数可选
            if (StringUtils.hasText(date)) {
                params.put("DATE", date);
            }

            // 使用SapRfcUtils执行函数
            Map<String, Object> functionResult = sapRfcUtils.executeFunction("ZLZK_HNT_MES_BOM", params);

            if (functionResult.containsKey("ERROR")) {
                throw new RuntimeException("SAP函数调用失败: " + functionResult.get("ERROR_MESSAGE"));
            }

            // 获取ITAB表数据
            Object itabObj = functionResult.get("ITAB");
            if (itabObj instanceof List) {
                result = (List<Map<String, Object>>) itabObj;
            } else if (itabObj instanceof JCoTable) {
                result = convertJCoTableToList((JCoTable) itabObj);
            }

            log.info("成功获取BOM数据: 物料={}, 工厂={}, 组件数={}",
                    materialNumber, plant, result.size());

        } catch (Exception e) {
            log.error("处理BOM数据异常: 物料={}, 工厂={}", materialNumber, plant, e);
            throw new RuntimeException("处理BOM数据异常: " + e.getMessage(), e);
        }

        return result;
    }

    /**
     * 获取简化的BOM结构（只包含关键字段）
     */
    public List<Map<String, Object>> getSimpleBom(String materialNumber, String plant) {
        List<Map<String, Object>> fullBom = getFullBomComponents(materialNumber, plant);

        return fullBom.stream().map(item -> {
            Map<String, Object> simple = new LinkedHashMap<>();

            // 关键字段映射（STPOX结构字段名）
            if (item.containsKey("STUFE")) simple.put("层次", item.get("STUFE"));
            if (item.containsKey("IDNRK")) simple.put("组件物料号", item.get("IDNRK"));
            if (item.containsKey("OJTXB")) simple.put("组件描述", item.get("OJTXB"));
            if (item.containsKey("MENGE")) simple.put("数量", item.get("MENGE"));
            if (item.containsKey("MEINS")) simple.put("单位", item.get("MEINS"));
            if (item.containsKey("POSNR")) simple.put("项目编号", item.get("POSNR"));
            if (item.containsKey("POSTP")) simple.put("项目类型", item.get("POSTP"));
            if (item.containsKey("WERKS")) simple.put("工厂", item.get("WERKS"));
            if (item.containsKey("VWEGX")) simple.put("路径", item.get("VWEGX"));
            if (item.containsKey("WEGXX")) simple.put("路线", item.get("WEGXX"));
            if (item.containsKey("MTART")) simple.put("物料类型", item.get("MTART"));
            if (item.containsKey("BMENG")) simple.put("基本数量", item.get("BMENG"));
            if (item.containsKey("BMEIN")) simple.put("基本单位", item.get("BMEIN"));

            return simple;
        }).collect(Collectors.toList());
    }

    /**
     * 按层次分组获取BOM结构
     */
    public Map<String, List<Map<String, Object>>> getGroupedBomByLevel(String materialNumber, String plant) {
        List<Map<String, Object>> bomComponents = getFullBomComponents(materialNumber, plant);

        return bomComponents.stream()
                .collect(Collectors.groupingBy(
                        item -> String.valueOf(item.getOrDefault("STUFE", "0")),
                        LinkedHashMap::new,
                        Collectors.toList()
                ));
    }

    /**
     * 获取特定类型的BOM组件
     * @param materialNumber 物料号
     * @param plant 工厂
     * @param postp 项目类型（L=库存项目，N=非库存项目）
     * @return 筛选后的组件列表
     */
    public List<Map<String, Object>> getBomComponentsByType(String materialNumber, String plant, String postp) {
        List<Map<String, Object>> allComponents = getFullBomComponents(materialNumber, plant);

        return allComponents.stream()
                .filter(item -> {
                    Object itemPostp = item.get("POSTP");
                    return itemPostp != null && postp.equals(itemPostp.toString());
                })
                .collect(Collectors.toList());
    }

    /**
     * 验证BOM物料是否存在
     */
    public boolean validateBomMaterial(String materialNumber, String plant) {
        try {
            List<Map<String, Object>> bom = getFullBomComponents(materialNumber, plant);
            return !bom.isEmpty();
        } catch (Exception e) {
            log.warn("验证BOM物料失败: {}", materialNumber, e);
            return false;
        }
    }

    /**
     * 获取BOM层数
     */
    public int getBomLevels(String materialNumber, String plant) {
        List<Map<String, Object>> bomComponents = getFullBomComponents(materialNumber, plant);

        if (bomComponents.isEmpty()) {
            return 0;
        }

        // 获取最大层次
        return bomComponents.stream()
                .map(item -> {
                    Object stufe = item.get("STUFE");
                    if (stufe != null) {
                        try {
                            return Integer.parseInt(stufe.toString());
                        } catch (NumberFormatException e) {
                            return 0;
                        }
                    }
                    return 0;
                })
                .max(Integer::compareTo)
                .orElse(0) + 1; // 层次从0开始，层数要+1
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
            JCoRecordMetaData metaData = table.getRecordMetaData();
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

                // 添加额外计算字段
                addCalculatedFields(row);
                result.add(row);
            }
        } catch (Exception e) {
            log.error("转换JCoTable失败", e);
        }

        return result;
    }


    /**
     * 添加计算字段
     */
    private void addCalculatedFields(Map<String, Object> row) {
        try {
            // 计算总数量（考虑基本数量）
            Object mengeObj = row.get("MENGE");
            Object bmengObj = row.get("BMENG");

            if (mengeObj != null && bmengObj != null) {
                try {
                    double menge = Double.parseDouble(mengeObj.toString());
                    double bmeng = Double.parseDouble(bmengObj.toString());
                    if (bmeng > 0) {
                        double totalQty = menge / bmeng;
                        row.put("TOTAL_QTY", String.format("%.4f", totalQty));
                    }
                } catch (NumberFormatException e) {
                    // 忽略转换错误
                }
            }

            // 判断是否为虚拟件
            Object postpObj = row.get("POSTP");
            if (postpObj != null) {
                String postp = postpObj.toString();
                if ("L".equals(postp)) {
                    row.put("IS_VIRTUAL", "N");
                    row.put("IS_VIRTUAL_DESC", "库存项目");
                } else if ("N".equals(postp)) {
                    row.put("IS_VIRTUAL", "Y");
                    row.put("IS_VIRTUAL_DESC", "非库存项目（虚拟件）");
                }
            }

            // 生成唯一标识
            Object idnrkObj = row.get("IDNRK");
            Object posnrObj = row.get("POSNR");
            if (idnrkObj != null && posnrObj != null) {
                row.put("UNIQUE_KEY", idnrkObj.toString() + "_" + posnrObj.toString());
            }

        } catch (Exception e) {
            log.warn("添加计算字段失败", e);
        }
    }

    /**
     * 参数校验
     */
    private void validateBomParameters(String materialNumber, String plant) {
        if (!StringUtils.hasText(materialNumber)) {
            throw new IllegalArgumentException("物料号不能为空");
        }
//        if (!StringUtils.hasText(plant)) {
//            throw new IllegalArgumentException("工厂不能为空");
//        }
//
//        // SAP物料号通常为18位
//        if (materialNumber.length() > 18) {
//            throw new IllegalArgumentException("物料号长度不能超过18位");
//        }
    }
}