package cn.iocoder.yudao.module.wm.util;

import com.sap.conn.jco.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
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

    // BOM表头相关常量
    private static final String BOM_FUNCTION_NAME = "ZTLPP0001_BOMEXP"; // BOM展开函数
    private static final String DEFAULT_CAPID = "PP01"; // BOM应用（PP01=生产BOM）
    private static final String DEFAULT_DATUV = "99991231"; // 默认有效日期
    private static final String DEFAULT_MEHRS = "X"; // 多层展开

    /**
     * 获取BOM所有组件（完整版）
     * @param materialNumber 物料号
     * @param plant 工厂
     * @return BOM组件列表
     */
    public List<Map<String, Object>> getFullBomComponents(String materialNumber, String plant) {
        return getBomComponentsByFunction(materialNumber, plant, DEFAULT_CAPID, DEFAULT_DATUV, DEFAULT_MEHRS);
    }

    /**
     * 获取BOM组件详细信息
     * @param materialNumber 物料号
     * @param plant 工厂
     * @param capid BOM应用
     * @param datuv 有效日期
     * @param mehRs 多层标志
     * @return BOM组件列表
     */
    public List<Map<String, Object>> getBomComponentsByFunction(String materialNumber, String plant,
                                                                String capid, String datuv, String mehRs) {
        List<Map<String, Object>> result = new ArrayList<>();
        JCoDestination destination = null;

        try {
            // 参数校验
            validateBomParameters(materialNumber, plant);

            // 获取SAP连接
            destination = sapRfcUtils.getDestination();

            // 获取BOM函数
            JCoRepository repository = destination.getRepository();
            JCoFunction function = repository.getFunction(BOM_FUNCTION_NAME);

            if (function == null) {
                throw new RuntimeException("SAP函数 " + BOM_FUNCTION_NAME + " 不存在或不可访问");
            }

            // 设置输入参数
            JCoParameterList importParams = function.getImportParameterList();
            importParams.setValue("CAPID", StringUtils.hasText(capid) ? capid : DEFAULT_CAPID);
            importParams.setValue("DATUV", StringUtils.hasText(datuv) ? datuv : DEFAULT_DATUV);
            importParams.setValue("MEHRS", StringUtils.hasText(mehRs) ? mehRs : DEFAULT_MEHRS);
            importParams.setValue("MTNRV", materialNumber);
            importParams.setValue("WERKS", plant);

            log.debug("调用SAP BOM函数: CAPID={}, MTNRV={}, WERKS={}",
                    importParams.getString("CAPID"), materialNumber, plant);

            // 执行函数
            function.execute(destination);

            // 获取STB表（BOM项目表）
            JCoParameterList tableParamList = function.getTableParameterList();
            JCoTable stbTable = tableParamList.getTable("STB");

            if (stbTable == null || stbTable.isEmpty()) {
                log.warn("未找到BOM数据: 物料={}, 工厂={}", materialNumber, plant);
                return result;
            }

            // 处理所有字段
            result = processStbTable(stbTable);

            log.info("成功获取BOM数据: 物料={}, 工厂={}, 组件数={}",
                    materialNumber, plant, result.size());

        } catch (JCoException e) {
            log.error("SAP BOM查询失败: 物料={}, 工厂={}", materialNumber, plant, e);
            throw new RuntimeException("SAP BOM查询失败: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("处理BOM数据异常: 物料={}, 工厂={}", materialNumber, plant, e);
            throw new RuntimeException("处理BOM数据异常", e);
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

            // 关键字段
            simple.put("层次", item.get("STUFE"));
            simple.put("组件物料号", item.get("IDNRK"));
            simple.put("组件描述", item.get("OJTXB"));
            simple.put("数量", item.get("MENGE"));
            simple.put("单位", item.get("MEINS"));
            simple.put("项目编号", item.get("POSNR"));
            simple.put("项目类型", item.get("POSTP"));
            simple.put("工厂", item.get("WERKS"));

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
                        item -> String.valueOf(item.get("STUFE")),
                        LinkedHashMap::new,  // 保持层次顺序
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
                .filter(item -> postp.equals(item.get("POSTP")))
                .collect(Collectors.toList());
    }

    /**
     * 获取BOM单层展开
     */
    public List<Map<String, Object>> getSingleLevelBom(String materialNumber, String plant) {
        return getBomComponentsByFunction(materialNumber, plant, DEFAULT_CAPID, DEFAULT_DATUV, "");
    }

    /**
     * 获取BOM表头信息
     */
    public Map<String, Object> getBomHeader(String materialNumber, String plant, String bomUsage) {
        try {
            JCoDestination destination = sapRfcUtils.getDestination();
            JCoRepository repository = destination.getRepository();
            JCoFunction function = repository.getFunction("CSAP_MAT_BOM_READ");

            if (function == null) {
                log.warn("标准BOM读取函数不存在，尝试其他方法");
                return getBomHeaderFromCustom(materialNumber, plant, bomUsage);
            }

            JCoParameterList importParams = function.getImportParameterList();
            importParams.setValue("MATERIAL", materialNumber);
            importParams.setValue("PLANT", plant);
            importParams.setValue("BOMUSAGE", bomUsage != null ? bomUsage : "1");

            function.execute(destination);

            Map<String, Object> header = new HashMap<>();
            JCoParameterList exportParams = function.getExportParameterList();

            // 修正：JCO 3.x中遍历导出参数的正确方式
            if (exportParams != null) {
                JCoMetaData metaData = exportParams.getMetaData();
                for (int i = 0; i < metaData.getFieldCount(); i++) {
                    String fieldName = metaData.getName(i);
                    header.put(fieldName, exportParams.getValue(fieldName));
                }

//                for (int i = 0; i < metaData.getFieldCount(); i++) {
//                    String fieldName = metaData.getName(i);
//                    header.put(fieldName, exportParams.getValue(fieldName));
//                }
            }

            return header;

        } catch (Exception e) {
            log.error("获取BOM表头失败", e);
            return Collections.emptyMap();
        }
    }

    /**
     * 验证BOM物料是否存在
     */
    public boolean validateBomMaterial(String materialNumber, String plant) {
        try {
            List<Map<String, Object>> bom = getSingleLevelBom(materialNumber, plant);
            return !bom.isEmpty();
        } catch (Exception e) {
            log.warn("验证BOM物料失败: {}", materialNumber, e);
            return false;
        }
    }

    /**
     * 获取BOM变更历史
     */
    public List<Map<String, Object>> getBomChangeHistory(String materialNumber, String plant) {
        try {
            JCoDestination destination = sapRfcUtils.getDestination();
            JCoRepository repository = destination.getRepository();
            JCoFunction function = repository.getFunction("CSAP_MAT_BOM_CHANGE_GET");

            if (function == null) {
                return Collections.emptyList();
            }

            JCoParameterList importParams = function.getImportParameterList();
            importParams.setValue("MATERIAL", materialNumber);
            importParams.setValue("PLANT", plant);

            function.execute(destination);

            JCoParameterList tableParams = function.getTableParameterList();
            JCoTable changeTable = tableParams.getTable("CHANGE_ITEMS");
            return convertJCoTableToList(changeTable);

        } catch (Exception e) {
            log.error("获取BOM变更历史失败", e);
            return Collections.emptyList();
        }
    }

    // ==================== 私有方法 ====================

    /**
     * 处理STB表，获取所有字段
     */
    private List<Map<String, Object>> processStbTable(JCoTable stbTable) {
        List<Map<String, Object>> result = new ArrayList<>();

        if (stbTable == null || stbTable.isEmpty()) {
            return result;
        }

        // 获取表结构元数据
        JCoRecordMetaData metaData = stbTable.getMetaData();
        List<String> fieldNames = new ArrayList<>();

        // 获取所有字段名
        for (int i = 0; i < metaData.getFieldCount(); i++) {
            fieldNames.add(metaData.getName(i));
        }

        // 处理每一行
        for (int i = 0; i < stbTable.getNumRows(); i++) {
            stbTable.setRow(i);
            Map<String, Object> row = new LinkedHashMap<>();

            // 处理所有字段
            for (String fieldName : fieldNames) {
                Object value = getFieldValue(stbTable, fieldName);
                row.put(fieldName, value);

                // 添加中文描述
                row.put(fieldName + "_DESC", getFieldDescription(fieldName));
            }

            // 添加额外计算字段
            addCalculatedFields(row);
            result.add(row);
        }

        return result;
    }

    /**
     * 获取字段值（根据类型处理）
     */
    private Object getFieldValue(JCoTable table, String fieldName) {
        try {
            // JCO 3.x中获取字段值的方式
            int fieldType = table.getMetaData().getType(fieldName);
            switch (fieldType) {
                case JCoMetaData.TYPE_CHAR:
                case JCoMetaData.TYPE_STRING:
                case JCoMetaData.TYPE_NUM:
                    return table.getString(fieldName);
                case JCoMetaData.TYPE_INT:
                case JCoMetaData.TYPE_INT2:
                case JCoMetaData.TYPE_INT1:
                case JCoMetaData.TYPE_INT4:
                    return table.getInt(fieldName);
                case JCoMetaData.TYPE_INT8:
                    return table.getLong(fieldName);
                case JCoMetaData.TYPE_FLOAT:
                    return table.getFloat(fieldName);
                case JCoMetaData.TYPE_DOUBLE:
                    return table.getDouble(fieldName);
                case JCoMetaData.TYPE_BCD:
                case JCoMetaData.TYPE_DECF16:
                case JCoMetaData.TYPE_DECF34:
                    return table.getBigDecimal(fieldName);
                case JCoMetaData.TYPE_DATE:
                    return table.getDate(fieldName);
                case JCoMetaData.TYPE_TIME:
                    return table.getTime(fieldName);
                case JCoMetaData.TYPE_BYTE:
                case JCoMetaData.TYPE_XSTRING:
                    return table.getByteArray(fieldName);
                case JCoMetaData.TYPE_STRUCTURE:
                    return table.getStructure(fieldName);
                case JCoMetaData.TYPE_TABLE:
                    return table.getTable(fieldName);
                default:
                    return table.getValue(fieldName);
            }
        } catch (Exception e) {
            log.warn("获取字段值失败: {}", fieldName, e);
            return null;
        }
    }

    /**
     * 获取字段中文描述
     */
    private String getFieldDescription(String fieldName) {
        Map<String, String> fieldDescriptions = new HashMap<>();

        // BOM关键字段描述
        fieldDescriptions.put("STUFE", "BOM层次");
        fieldDescriptions.put("IDNRK", "组件物料号");
        fieldDescriptions.put("OJTXB", "组件物料描述");
        fieldDescriptions.put("OJTXP", "项目描述");
        fieldDescriptions.put("MENGE", "组件数量");
        fieldDescriptions.put("MEINS", "基本计量单位");
        fieldDescriptions.put("POSNR", "BOM项目号");
        fieldDescriptions.put("POSTP", "BOM项目类型");
        fieldDescriptions.put("WERKS", "工厂");
        fieldDescriptions.put("BMTYP", "BOM类型");
        fieldDescriptions.put("BMENG", "基本数量");
        fieldDescriptions.put("BMEIN", "基本计量单位");
        fieldDescriptions.put("AUFPL", "工艺路线号");
        fieldDescriptions.put("APLZL", "工艺路线计数器");
        fieldDescriptions.put("VWEGX", "路径");
        fieldDescriptions.put("WEGXX", "路线");
        fieldDescriptions.put("MTART", "物料类型");
        fieldDescriptions.put("MATKL", "物料组");
        fieldDescriptions.put("SPRAS", "语言代码");
        fieldDescriptions.put("MAKTX", "物料描述");
        fieldDescriptions.put("LGORT", "库存地点");
        fieldDescriptions.put("CHARG", "批次");
        fieldDescriptions.put("SOBES", "特殊采购类型");
        fieldDescriptions.put("SANFE", "总需求数量");
        fieldDescriptions.put("SAFES", "废品率");
        fieldDescriptions.put("RGEKZ", "反冲标识");
        fieldDescriptions.put("LABOR", "作业");
        fieldDescriptions.put("ANZZL", "数量单位");
        fieldDescriptions.put("POTX1", "项目文本第一行");
        fieldDescriptions.put("POTX2", "项目文本第二行");
        fieldDescriptions.put("AUSCH", "报废率");
        fieldDescriptions.put("AUSSS", "废品率");
        fieldDescriptions.put("EWAHR", "有效因子");
        fieldDescriptions.put("BWAHR", "基本数量有效因子");
        fieldDescriptions.put("SERNR", "BOM序列号");
        fieldDescriptions.put("SANIN", "独立需求");
        fieldDescriptions.put("SERNP", "BOM替代项目");
        fieldDescriptions.put("SERKZ", "序列化标识");
        fieldDescriptions.put("STLAL", "替代BOM");
        fieldDescriptions.put("STLAN", "BOM用途");
        fieldDescriptions.put("STLNR", "BOM编号");
        fieldDescriptions.put("STLTY", "BOM类别");
        fieldDescriptions.put("ALPGR", "替代项目组");
        fieldDescriptions.put("ALPST", "替代策略");
        fieldDescriptions.put("KNOBJ", "配置对象");
        fieldDescriptions.put("KMPME", "组件单位");
        fieldDescriptions.put("KMPMG", "组件数量");
        fieldDescriptions.put("NETAU", "净更改标识");
        fieldDescriptions.put("NTGEW", "净重");
        fieldDescriptions.put("GEWEI", "重量单位");
        fieldDescriptions.put("BRGEW", "毛重");
        fieldDescriptions.put("VOLUM", "体积");
        fieldDescriptions.put("VOLEH", "体积单位");
        fieldDescriptions.put("ANDAT", "创建日期");
        fieldDescriptions.put("ANNAM", "创建者");
        fieldDescriptions.put("AEDAT", "最后修改日期");
        fieldDescriptions.put("AENAM", "最后修改者");
        fieldDescriptions.put("DATUV", "有效起始日");
        fieldDescriptions.put("TECHV", "技术生效日期");
        fieldDescriptions.put("AENNR", "更改号");
        fieldDescriptions.put("REVLV", "版本级别");

        return fieldDescriptions.getOrDefault(fieldName, fieldName);
    }

    /**
     * 添加计算字段
     */
    private void addCalculatedFields(Map<String, Object> row) {
        try {
            // 计算总数量（考虑基本数量）
            String mengeStr = (String) row.get("MENGE");
            String bmengStr = (String) row.get("BMENG");

            if (mengeStr != null && bmengStr != null) {
                try {
                    double menge = Double.parseDouble(mengeStr);
                    double bmeng = Double.parseDouble(bmengStr);
                    if (bmeng > 0) {
                        double totalQty = menge / bmeng;
                        row.put("TOTAL_QTY", String.format("%.4f", totalQty));
                        row.put("TOTAL_QTY_DESC", "总数量（考虑基本数量）");
                    }
                } catch (NumberFormatException e) {
                    // 忽略转换错误
                }
            }

            // 判断是否为虚拟件
            String postp = (String) row.get("POSTP");
            if ("L".equals(postp)) {
                row.put("IS_VIRTUAL", "N");
                row.put("IS_VIRTUAL_DESC", "库存项目");
            } else if ("N".equals(postp)) {
                row.put("IS_VIRTUAL", "Y");
                row.put("IS_VIRTUAL_DESC", "非库存项目（虚拟件）");
            }

            // 生成唯一标识
            String idnrk = (String) row.get("IDNRK");
            String posnr = (String) row.get("POSNR");
            if (idnrk != null && posnr != null) {
                row.put("UNIQUE_KEY", idnrk + "_" + posnr);
            }

        } catch (Exception e) {
            log.warn("添加计算字段失败", e);
        }
    }

    /**
     * JCoTable转List<Map>
     */
    private List<Map<String, Object>> convertJCoTableToList(JCoTable table) {
        List<Map<String, Object>> result = new ArrayList<>();

        if (table == null || table.isEmpty()) {
            return result;
        }

        JCoRecordMetaData metaData = table.getMetaData();

        for (int i = 0; i < table.getNumRows(); i++) {
            table.setRow(i);
            Map<String, Object> row = new LinkedHashMap<>();

            // 遍历所有字段
            for (int j = 0; j < metaData.getFieldCount(); j++) {
                String fieldName = metaData.getName(j);
                row.put(fieldName, table.getValue(fieldName));
            }

            result.add(row);
        }

        return result;
    }

    /**
     * 参数校验
     */
    private void validateBomParameters(String materialNumber, String plant) {
        if (!StringUtils.hasText(materialNumber)) {
            throw new IllegalArgumentException("物料号不能为空");
        }
        if (!StringUtils.hasText(plant)) {
            throw new IllegalArgumentException("工厂不能为空");
        }

        // 物料号格式校验（示例）
        if (materialNumber.length() < 3) {
            throw new IllegalArgumentException("物料号格式不正确");
        }
    }

    /**
     * 从自定义函数获取BOM表头
     */
    private Map<String, Object> getBomHeaderFromCustom(String materialNumber, String plant, String bomUsage) {
        Map<String, Object> header = new HashMap<>();
        header.put("MATERIAL", materialNumber);
        header.put("PLANT", plant);
        header.put("BOMUSAGE", bomUsage);
        header.put("VALID_FROM", DEFAULT_DATUV);

        // 尝试从BOM组件中推断表头信息
        try {
            List<Map<String, Object>> bomComponents = getSingleLevelBom(materialNumber, plant);
            if (!bomComponents.isEmpty()) {
                Map<String, Object> firstComponent = bomComponents.get(0);
                header.put("BOM_CATEGORY", firstComponent.get("BMTYP"));
                header.put("BASE_QUANTITY", firstComponent.get("BMENG"));
                header.put("BASE_UOM", firstComponent.get("BMEIN"));
            }
        } catch (Exception e) {
            log.warn("推断BOM表头信息失败", e);
        }

        return header;
    }

    /**
     * 遍历JCoTable中的所有字段（替代老版本的遍历方式）
     */
    private List<String> getAllFieldNamesFromTable(JCoTable table) {
        List<String> fieldNames = new ArrayList<>();

        if (table != null) {
            JCoRecordMetaData metaData = table.getMetaData();
            for (int i = 0; i < metaData.getFieldCount(); i++) {
                fieldNames.add(metaData.getName(i));
            }
        }

        return fieldNames;
    }

    // ==================== 常量定义 ====================

    /**
     * BOM项目类型常量
     */
    public static class BomItemType {
        public static final String STOCK_ITEM = "L";      // 库存项目
        public static final String NON_STOCK_ITEM = "N";  // 非库存项目
        public static final String TEXT_ITEM = "T";       // 文本项目
        public static final String DOCUMENT_ITEM = "D";   // 文档项目
        public static final String CLASS_ITEM = "C";      // 类项目
    }

    /**
     * BOM用途常量
     */
    public static class BomUsage {
        public static final String PRODUCTION = "1";      // 生产
        public static final String ENGINEERING = "2";     // 工程
        public static final String PLANT_MAINTENANCE = "3"; // 工厂维护
        public static final String COSTING = "4";         // 成本核算
    }

    /**
     * BOM类别常量
     */
    public static class BomCategory {
        public static final String MATERIAL_BOM = "M";    // 物料BOM
        public static final String EQUIPMENT_BOM = "E";   // 设备BOM
        public static final String FUNCTIONAL_BOM = "F";  // 功能BOM
        public static final String ORDER_BOM = "O";       // 订单BOM
    }
}