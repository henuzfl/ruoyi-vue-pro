package cn.iocoder.yudao;

import com.sap.conn.jco.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import cn.iocoder.yudao.module.wm.util.SapRfcUtils;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
public class SapBusinessTableTest {

    @Autowired
    private SapRfcUtils sapRfcUtils;

    @Test
    public void testCommonBusinessTables() {
        System.out.println("=== 测试 SAP 业务常用表 ===");

        // 1. 先测试连接
        System.out.println("1. 测试连接状态...");
        boolean connected = sapRfcUtils.testConnection();
        System.out.println("连接状态: " + (connected ? "✅ 成功" : "❌ 失败"));

        // 2. 测试常见的业务表
        System.out.println("\n2. 测试常见业务表:");

        // 物料主数据表
        testTable("MARAV", "物料主数据（工厂级）");
        testTable("MARA", "物料主数据（基本视图）");
        testTable("MAKT", "物料描述");
        testTable("MARC", "物料工厂数据");
        testTable("MBEW", "物料评估数据");

        // 库存表
        testTable("MARD", "物料库存地点库存");
        testTable("MSKU", "特殊库存供应商");
        testTable("MCHB", "物料批次库存");
        testTable("MKOL", "特殊库存销售订单");

        // BOM表
        testTable("MAST", "物料BOM链接");
        testTable("STKO", "BOM表头");
        testTable("STPO", "BOM项目");
        testTable("STPU", "BOM子项目");
        testTable("STAS", "BOM项目选择");

        // 采购相关表
        testTable("EKKO", "采购订单抬头");
        testTable("EKPO", "采购订单项目");
        testTable("LFA1", "供应商主数据（一般数据）");
        testTable("LFB1", "供应商主数据（公司代码）");
        testTable("LFM1", "供应商主数据（采购组织）");

        // 销售相关表
        testTable("VBAK", "销售订单抬头");
        testTable("VBAP", "销售订单项目");
        testTable("KNA1", "客户主数据");
        testTable("KNVV", "客户主数据销售数据");

        System.out.println("\n3. 测试自定义函数:");
        testCustomFunctions();
    }

    private void testTable(String tableName, String description) {
        System.out.print("测试表: " + tableName + " (" + description + ")... ");

        try {
            Map<String, Object> params = new HashMap<>();
            params.put("QUERY_TABLE", tableName);
            params.put("DELIMITER", "|");
            params.put("ROWCOUNT", "5");  // 只读取5条记录测试

            Map<String, Object> result = sapRfcUtils.executeFunction("RFC_READ_TABLE", params);

            if (result.containsKey("ERROR")) {
                System.out.println("❌ 失败: " + result.get("ERROR_MESSAGE"));
            } else {
                System.out.println("✅ 成功");

                // 显示字段信息
                Object fieldsObj = result.get("FIELDS");
                Object dataObj = result.get("DATA");

                if (fieldsObj instanceof java.util.List) {
                    java.util.List<?> fields = (java.util.List<?>) fieldsObj;
                    System.out.println("   字段数: " + fields.size());
                }

                if (dataObj instanceof java.util.List) {
                    java.util.List<?> data = (java.util.List<?>) dataObj;
                    System.out.println("   记录数: " + data.size());
                }
            }
        } catch (Exception e) {
            System.out.println("❌ 异常: " + e.getMessage());
        }
    }

    private void testCustomFunctions() {
        System.out.println("\n4. 测试你的自定义函数:");

        // 测试你的采购订单函数
        testPurchaseOrderFunction();

        // 测试其他可能的函数
        testOtherFunctions();
    }

    private void testPurchaseOrderFunction() {
        System.out.print("测试采购订单函数 ZFGTLMM0002_GET_PURCH... ");

        try {
            Map<String, Object> params = new HashMap<>();
            params.put("WERKS", "6400");  // 工厂

            Map<String, Object> result = sapRfcUtils.executeFunction("ZFGTLMM0002_GET_PURCH", params);

            if (result.containsKey("ERROR")) {
                System.out.println("❌ 失败: " + result.get("ERROR_MESSAGE"));
            } else {
                System.out.println("✅ 成功");

                // 显示结果结构
                System.out.println("返回参数:");
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

    private void testOtherFunctions() {
        // 测试其他可能的函数
        String[] otherFunctions = {
                "ZMM_GET_PURCHASE_ORDER",
                "Z_PO_GET_DETAIL",
                "BAPI_PO_GETDETAIL",
                "BAPI_PO_GETITEMS",
                "RFC_GET_TABLE_ENTRIES"
        };

        for (String func : otherFunctions) {
            System.out.print("测试函数: " + func + "... ");

            try {
                Map<String, Object> result = sapRfcUtils.executeFunction(func, new HashMap<>());
                if (result.containsKey("ERROR")) {
                    System.out.println("❌ (需要参数)");
                } else {
                    System.out.println("✅ 可用");
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