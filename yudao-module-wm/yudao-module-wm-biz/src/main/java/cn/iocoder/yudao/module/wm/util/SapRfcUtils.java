package cn.iocoder.yudao.module.wm.util;

import cn.iocoder.yudao.module.wm.service.sapjcoclient.ISapConfigService;
import com.sap.conn.jco.*;
import com.sap.conn.jco.ext.DestinationDataProvider;
import com.sap.conn.jco.ext.DestinationDataEventListener;
import com.sap.conn.jco.ext.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * SAP JCO 工具类 (基于数据库配置) - JCO 3.0.19 版本
 */
@Component
public class SapRfcUtils {

    private static final Logger log = LoggerFactory.getLogger(SapRfcUtils.class);
    private static final String DESTINATION_NAME = "SAP_RUOYI_DEST";
    private static volatile boolean providerRegistered = false;
    private static volatile boolean initialized = false;  // 新增：懒加载标志

    @Autowired
    private ISapConfigService sapConfigService;

    // 懒加载初始化方法（加锁保证线程安全）
    private synchronized void lazyInit() {
        if (initialized) {
            return;
        }
        log.info("SapRfcUtils 懒加载初始化开始...");
        try {
            // 1. 创建自定义Provider
            CustomDestinationDataProvider provider = new CustomDestinationDataProvider();

            // 2. 获取连接属性（此时才去读取数据库配置）
            Properties logonProps = getLogonProps();

            // 3. 添加目的地配置
            provider.addDestination(DESTINATION_NAME, logonProps);

            // 4. 注册到JCO环境（仅一次）
            if (!providerRegistered) {
                Environment.registerDestinationDataProvider(provider);
                providerRegistered = true;
            }

            // 5. 测试连接 - 直接使用已注册的 destination
            try {
                JCoDestination destination = JCoDestinationManager.getDestination(DESTINATION_NAME);
                destination.ping();
                log.info("SAP连接测试成功");
            } catch (JCoException e) {
                //log.warn("SAP连接测试失败: {}", e.getMessage());
                // 可以选择抛出异常或记录错误，视业务需求
                // 打印完整的错误细节
                log.error("SAP连接测试失败 - 错误码: {} 错误组: {} 消息: {}",
                        e.getKey(), e.getGroup(), e.getMessage());
                // 可选：将异常栈打印出来
                log.error("详细异常栈: ", e);
            }

            initialized = true;
            log.info("SAP JCO 懒加载初始化完成");
        } catch (Exception e) {
            log.error("SAP JCO 懒加载初始化失败", e);
            throw new RuntimeException("SAP JCO 初始化失败: " + e.getMessage(), e);
        }
    }

    /* ========== 第一部分：自定义DestinationDataProvider实现 ========== */

    /**
     * 自定义的DestinationDataProvider实现
     */
    private static class CustomDestinationDataProvider implements DestinationDataProvider {
        private final HashMap<String, Properties> destinations = new HashMap<>();
        private DestinationDataEventListener eventListener;

        @Override
        public Properties getDestinationProperties(String destinationName) {
            return destinations.get(destinationName);
        }

        @Override
        public void setDestinationDataEventListener(DestinationDataEventListener eventListener) {
            this.eventListener = eventListener;
        }

        @Override
        public boolean supportsEvents() {
            return true;
        }

        public void addDestination(String destName, Properties properties) {
            destinations.put(destName, properties);
            if (eventListener != null) {
                eventListener.updated(destName);
            }
        }
    }

    /* ========== 第二部分：连接管理 ========== */

    /**
     * 获取连接属性_正式环境
     */
    public Properties getLogonProps() {
        Properties props = new Properties();
        // ✅ 使用Message Server方式（端口3600）
        props.setProperty("jco.client.mshost", sapConfigService.getBaseCodeByType("_JCO_CLIENT_ASHOST_")); // 172.30.1.52
        props.setProperty("jco.client.msserv", "3600");  // 端口3600

        // ✅ 必须提供系统ID和服务器组
        props.setProperty("jco.client.r3name", "EP1");     // 需要确认：SAP系统ID
        props.setProperty("jco.client.group", "PRD");   // 需要确认：服务器组名称

        // 登录信息
        props.setProperty("jco.client.client", sapConfigService.getBaseCodeByType("_JCO_CLIENT_CLIENT_")); // 800
        props.setProperty("jco.client.user", sapConfigService.getBaseCodeByType("_JCO_CLIENT_USER_"));
        props.setProperty("jco.client.passwd", sapConfigService.getBaseCodeByType("_JCO_CLIENT_PASSWD_"));
        props.setProperty("jco.client.lang", "ZH");

        // 连接池配置
        props.setProperty("jco.destination.pool_capacity", "10");
        props.setProperty("jco.destination.max_get_client_time", "10000");

        return props;
    }
    /**
     * 获取连接属性_qas环境
     */
//    public Properties getLogonProps() {
//        Properties props = new Properties();
//
//        String ashost = sapConfigService.getBaseCodeByType("_JCO_CLIENT_QAS_ASHOST_");
//        String client = sapConfigService.getBaseCodeByType("_JCO_CLIENT_QAS_CLIENT_");
//        String user = sapConfigService.getBaseCodeByType("_JCO_CLIENT_QAS_USER_");
//        String rawPassword = sapConfigService.getBaseCodeByType("_JCO_CLIENT_QAS_PASSWD_");
//
//
//        props.setProperty("jco.client.ashost", ashost);
//        props.setProperty("jco.client.sysnr",  "00");
//        props.setProperty("jco.client.client", client);
//        props.setProperty("jco.client.user",   user);
//        props.setProperty("jco.client.passwd", rawPassword);
//        props.setProperty("jco.client.lang",   "ZH");
//
//        // 可选：强制设置代码页
//        // props.setProperty("jco.client.codepage", "1100");
//
//        props.setProperty("jco.destination.pool_capacity", "10");
//        props.setProperty("jco.destination.max_get_client_time", "10000");
//
//        return props;
//    }
    /**
     * 获取JCO目的地
     */
    public JCoDestination getDestination() throws JCoException {
        if (!initialized) {
            lazyInit();  // 第一次调用时初始化
        }
        return JCoDestinationManager.getDestination(DESTINATION_NAME);
    }

    /**
     * 测试连接
     */
//    public boolean testConnection() {
//        try {
//            JCoDestination destination = getDestination();
//            destination.ping();
//            log.info("SAP连接测试成功");
//            return true;
//        } catch (JCoException e) {
//            log.error("SAP连接测试失败: {}", e.getMessage());
//            return false;
//        }
//    }
    // 测试连接方法 - 不再调用 getDestination()
    public boolean testConnection() {
        if (!initialized) {
            log.warn("SAP连接尚未初始化，无法测试连接");
            return false;
        }
        try {
            JCoDestination destination = JCoDestinationManager.getDestination(DESTINATION_NAME);
            destination.ping();
            log.info("SAP连接测试成功");
            return true;
        } catch (JCoException e) {
            log.error("SAP连接测试失败: {}", e.getMessage());
            return false;
        }
    }

    /* ========== 第三部分：核心RFC调用 ========== */

    /**
     * 通用的SAP RFC函数执行方法
     */
    public Map<String, Object> executeFunction(String functionName, Map<String, Object> importParams) {
        try {
            // 1. 获取目的地
            JCoDestination destination = getDestination();

            // 2. 获取函数对象
            JCoFunction function = destination.getRepository().getFunction(functionName);
            if (function == null) {
                throw new RuntimeException("SAP中未找到函数: " + functionName);
            }

            // 3. 设置输入参数
            if (importParams != null && !importParams.isEmpty()) {
                JCoParameterList input = function.getImportParameterList();
                for (Map.Entry<String, Object> entry : importParams.entrySet()) {
                    setJCoParameter(input, entry.getKey(), entry.getValue());
                }
            }

            // 4. 执行函数
            function.execute(destination);
            log.debug("成功执行SAP函数: {}", functionName);

            // 5. 处理返回结果
            Map<String, Object> result = new HashMap<>();
            processFunctionResult(function, result);

            return result;

        } catch (JCoException e) {
            log.error("执行SAP函数 {} 失败", functionName, e);
            Map<String, Object> errorResult = new HashMap<>();
            errorResult.put("ERROR", true);
            errorResult.put("ERROR_MESSAGE", e.getMessage());
            errorResult.put("ERROR_KEY", e.getKey());
            throw new RuntimeException("SAP函数调用失败: " + e.getMessage(), e);
        }
    }

    /**
     * 采购订单查询的便捷封装方法
     */
    public Map<String, Object> callPurchaseOrderFunction(String vendorNo, Date startDate, Date endDate,
                                                         Map<String, Object> otherConditions) {
        try {
            Map<String, Object> importParams = new HashMap<>();
            importParams.put("WERKS", "6400");

            // 供应商号补零
            if (vendorNo != null && !vendorNo.isEmpty()) {
                try {
                    if (vendorNo.length() < 10) {
                        vendorNo = String.format("%010d", Integer.parseInt(vendorNo));
                    }
                } catch (NumberFormatException e) {
                    log.warn("供应商号非纯数字，跳过补零: {}", vendorNo);
                }
            }
            importParams.put("LIFNR", vendorNo);

            // 日期格式化
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            if (startDate != null) importParams.put("BEDAT_S", sdf.format(startDate));
            if (endDate != null) importParams.put("BEDAT_E", sdf.format(endDate));

            // 其他条件
            if (otherConditions != null) {
                importParams.putAll(otherConditions);
            }

            return this.executeFunction("ZFGTLMM0002_GET_PURCH", importParams);
        } catch (Exception e) {
            log.error("准备采购订单查询参数失败", e);
            throw new RuntimeException("构建SAP调用参数失败", e);
        }
    }

    /* ========== 第四部分：辅助方法 ========== */

    /**
     * 处理函数返回结果
     */
    /**
     * 处理函数返回结果 - 使用JCoMetaData接口
     */
    private void processFunctionResult(JCoFunction function, Map<String, Object> result) {
        // 1. 处理EXPORT参数
        JCoParameterList exportParams = function.getExportParameterList();
        if (exportParams != null) {
            JCoMetaData metaData = exportParams.getMetaData();
            for (int i = 0; i < metaData.getFieldCount(); i++) {
                String fieldName = metaData.getName(i);
                result.put(fieldName, exportParams.getValue(fieldName));
            }
        }

        // 2. 处理TABLE参数
        JCoParameterList tableParams = function.getTableParameterList();
        if (tableParams != null) {
            JCoMetaData tableMetaData = tableParams.getMetaData();
            for (int i = 0; i < tableMetaData.getFieldCount(); i++) {
                String tableName = tableMetaData.getName(i);
                JCoTable table = tableParams.getTable(tableName);
                List<Map<String, Object>> tableData = convertJCoTableToList(table);
                result.put(tableName, tableData);
            }
        }
    }

    /**
     * 将JCoTable转换为List<Map> - 使用JCoMetaData
     */
    public List<Map<String, Object>> convertJCoTableToList(JCoTable table) {
        List<Map<String, Object>> resultList = new ArrayList<>();

        if (table == null || table.isEmpty()) {
            return resultList;
        }

        // 保存当前行位置
        int currentRow = table.getRow();

        try {
            // 获取表格元数据 - 直接使用JCoMetaData
            JCoMetaData metaData = table.getRecordMetaData();

            // 遍历表格每一行
            for (int row = 0; row < table.getNumRows(); row++) {
                table.setRow(row);
                Map<String, Object> rowData = new HashMap<>();

                // 遍历每一列
                for (int col = 0; col < metaData.getFieldCount(); col++) {
                    String columnName = metaData.getName(col);
                    rowData.put(columnName, table.getValue(columnName));
                }

                resultList.add(rowData);
            }
        } finally {
            // 恢复原来的行位置
            table.setRow(currentRow);
        }

        return resultList;
    }

    /**
     * 智能设置JCO参数值
     */
    private void setJCoParameter(JCoParameterList paramList, String key, Object value) {
        if (value == null || paramList == null) return;

        try {
            if (value instanceof String) {
                paramList.setValue(key, (String) value);
            } else if (value instanceof Integer) {
                paramList.setValue(key, (Integer) value);
            } else if (value instanceof Double) {
                paramList.setValue(key, (Double) value);
            } else if (value instanceof Date) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                paramList.setValue(key, sdf.format((Date) value));
            } else if (value instanceof BigDecimal) {
                paramList.setValue(key, ((BigDecimal) value).doubleValue());
            } else if (value instanceof Boolean) {
                paramList.setValue(key, (Boolean) value ? "X" : "");
            } else {
                paramList.setValue(key, value.toString());
            }
        } catch (Exception e) {
            log.warn("设置SAP参数 {} 失败，值: {}", key, value, e);
            paramList.setValue(key, String.valueOf(value));
        }
    }

    /* ========== 第五部分：初始化和销毁 ========== */

    /**
     * 初始化时注册自定义的DestinationDataProvider
     */
    /*
     @PostConstruct
     */
    public void init() {
        log.info("SapRfcUtils (JCO 3.0) 初始化...");

        synchronized (SapRfcUtils.class) {
            if (providerRegistered) {
                log.info("DestinationDataProvider 已注册，跳过初始化");
                return;
            }

            try {
                // 创建自定义Provider
                CustomDestinationDataProvider provider = new CustomDestinationDataProvider();

                // 获取连接属性
                Properties logonProps = getLogonProps();

                // 添加目的地配置
                provider.addDestination(DESTINATION_NAME, logonProps);

                // 注册到JCO环境
                Environment.registerDestinationDataProvider(provider);
                providerRegistered = true;

                log.info("SAP JCO 3.0 目的地注册成功，目标主机: {}",
                        logonProps.getProperty("jco.client.ashost"));

                // 测试连接
                if (testConnection()) {
                    log.info("SAP JCO 3.0 初始化完成");
                } else {
                    log.warn("SAP连接测试失败，但工具类已初始化");
                }

            } catch (Exception e) {
                log.error("SAP JCO 3.0 初始化失败", e);
                throw new RuntimeException("SAP JCO初始化失败: " + e.getMessage(), e);
            }
        }
    }




    /**
     * 销毁时清理资源
     */
    @PreDestroy
    public void destroy() {
        log.info("清理SAP JCO资源...");
        // JCO 3.0会自动管理资源
    }

    /* ========== 第六部分：便捷方法 ========== */

    /**
     * 快速测试RFC函数 - 用于调试
     */
    public Map<String, Object> testFunction(String functionName) {
        Map<String, Object> importParams = new HashMap<>();
        importParams.put("REQUTEXT", "Test from RuoYi");
        return executeFunction(functionName, importParams);
    }

    /**
     * 获取SAP系统信息
     */
    public Map<String, Object> getSystemInfo() {
        Map<String, Object> info = new HashMap<>();
        try {
            JCoDestination destination = getDestination();

            // 方法1：尝试从属性中获取信息
            Properties props = getLogonProps();
            info.put("host", props.getProperty("jco.client.ashost"));
            info.put("client", props.getProperty("jco.client.client"));
            info.put("systemNumber", props.getProperty("jco.client.sysnr"));
            info.put("user", props.getProperty("jco.client.user"));
            info.put("language", props.getProperty("jco.client.lang"));

            // 方法2：尝试从destination获取附加信息
            try {
                // 尝试获取其他可用属性
                info.put("destinationName", destination.getDestinationName());

                // 尝试调用ping方法确认连接状态
                destination.ping();
                info.put("connectionStatus", "ACTIVE");
            } catch (Exception e) {
                info.put("connectionStatus", "UNKNOWN");
            }

            // 方法3：添加连接测试结果
            info.put("testConnection", testConnection());

        } catch (JCoException e) {
            log.error("获取SAP系统信息失败", e);
            info.put("ERROR", true);
            info.put("ERROR_MESSAGE", e.getMessage());
            info.put("ERROR_KEY", e.getKey());
        }
        return info;
    }
}