package cn.iocoder.yudao;

import com.sap.conn.jco.*;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import cn.iocoder.yudao.module.wm.util.SapRfcUtils;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
public class SapFunctionTest {

    @Autowired
    private SapRfcUtils sapRfcUtils;

    @Test
    public void testBasicSapFunctions() {
        System.out.println("=== 测试 SAP 基本功能 ===");

        // 1. 测试连接状态
        System.out.println("1. 测试连接状态...");
        boolean connected = sapRfcUtils.testConnection();
        System.out.println("连接状态: " + (connected ? "✅ 成功" : "❌ 失败"));

        // 2. 测试系统信息
        System.out.println("\n2. 获取系统信息...");
        try {
            Map<String, Object> systemInfo = sapRfcUtils.getSystemInfo();
            System.out.println("系统信息:");
            systemInfo.forEach((key, value) -> {
                if (!key.contains("PASS") && !key.contains("PWD")) {
                    System.out.println("  " + key + ": " + value);
                }
            });
        } catch (Exception e) {
            System.out.println("获取系统信息失败: " + e.getMessage());
        }

        // 3. 测试简单的 RFC 函数
        System.out.println("\n3. 测试 RFC 函数...");
        testRfcFunction("RFC_PING", "Ping 测试");
        testRfcFunction("RFC_READ_TABLE", "读取表测试");

        // 4. 测试采购订单查询
        System.out.println("\n4. 测试采购订单查询...");
        testPurchaseOrderQuery();

        System.out.println("\n🎉 SAP 功能测试完成！");
    }

    private void testRfcFunction(String functionName, String description) {
        System.out.print("测试 " + description + " (" + functionName + ")... ");

        try {
            Map<String, Object> result = sapRfcUtils.executeFunction(functionName, new HashMap<>());

            if (result.containsKey("ERROR")) {
                System.out.println("❌ 失败: " + result.get("ERROR_MESSAGE"));
            } else {
                System.out.println("✅ 成功");

                // 显示部分结果
                if (!result.isEmpty()) {
                    System.out.println("  返回字段: " + result.keySet());
                }
            }
        } catch (Exception e) {
            System.out.println("❌ 异常: " + e.getMessage());
        }
    }

    private void testPurchaseOrderQuery() {
        try {
            System.out.print("测试采购订单查询 ZFGTLMM0002_GET_PURCH... ");

            // 准备测试参数
            Map<String, Object> params = new HashMap<>();
            params.put("WERKS", "6400");  // 工厂代码
            // params.put("LIFNR", "0000100001");  // 供应商号（如果需要）

            Map<String, Object> result = sapRfcUtils.executeFunction(
                    "ZFGTLMM0002_GET_PURCH", params);

            if (result.containsKey("ERROR")) {
                System.out.println("❌ 失败: " + result.get("ERROR_MESSAGE"));
            } else {
                System.out.println("✅ 成功");

                // 显示结果结构
                System.out.println("返回结构:");
                result.forEach((key, value) -> {
                    if (value instanceof java.util.List) {
                        java.util.List<?> list = (java.util.List<?>) value;
                        System.out.println("  " + key + ": " + list.size() + " 条记录");
                        if (!list.isEmpty() && list.get(0) instanceof Map) {
                            Map<?, ?> firstRow = (Map<?, ?>) list.get(0);
                            System.out.println("    字段: " + firstRow.keySet());
                        }
                    } else {
                        System.out.println("  " + key + ": " + value);
                    }
                });
            }
        } catch (Exception e) {
            System.out.println("❌ 异常: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Test
    public void testAdvancedFunctions() {
        System.out.println("=== 测试高级 SAP 功能 ===");

        // 测试常用的 BAPI 函数
        String[] bapiFunctions = {
                "BAPI_USER_GET_DETAIL",      // 获取用户详情
                "BAPI_MATERIAL_GET_LIST",    // 获取物料列表
                "BAPI_PO_GETDETAIL",         // 获取采购订单详情
                "BAPI_VENDOR_GETDETAIL"      // 获取供应商详情
        };

        for (String bapi : bapiFunctions) {
            System.out.print("测试 " + bapi + "... ");

            try {
                Map<String, Object> result = sapRfcUtils.executeFunction(bapi, new HashMap<>());
                if (result.containsKey("ERROR")) {
                    System.out.println("❌ (函数存在但参数不足)");
                } else {
                    System.out.println("✅ 函数可用");
                }
            } catch (Exception e) {
                if (e.getMessage().contains("not found")) {
                    System.out.println("❌ 函数不存在");
                } else {
                    System.out.println("❌ 错误: " + e.getMessage());
                }
            }
        }
    }
}