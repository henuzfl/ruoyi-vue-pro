package cn.iocoder.yudao.module.infra.service;

import org.junit.jupiter.api.Test;
import java.sql.*;

// 移除 @SpringBootTest，因为这是纯 JDBC 测试
public class TestOracleCharset {

    @Test
    public void testOracleConnection() {
        try {
            // 1. 测试字符集类是否可加载
            Class.forName("oracle.i18n.util.LocaleMapper");
            System.out.println("✅ orai18n.jar 加载成功！");

            // 2. 测试Oracle驱动
            Class.forName("oracle.jdbc.OracleDriver");
            System.out.println("✅ Oracle驱动加载成功！");

            // 3. 测试数据库连接
            Connection conn = DriverManager.getConnection(
                    "jdbc:oracle:thin:@127.0.0.1:1521:orcl1", "system", "admin");
            System.out.println("✅ Oracle数据库连接成功！");

            // 4. 测试读取表信息（模拟代码生成功能）
            DatabaseMetaData meta = conn.getMetaData();
            ResultSet tables = meta.getTables(null, "SYSTEM", "%", new String[]{"TABLE"});

            int count = 0;
            while (tables.next() && count < 5) {
                System.out.println("表: " + tables.getString("TABLE_NAME"));
                count++;
            }

            System.out.println("✅ 字符集测试成功！读取了 " + count + " 个表");

            // 5. 清理资源
            tables.close();
            conn.close();

        } catch (ClassNotFoundException e) {
            System.out.println("❌ 错误: orai18n.jar 未在classpath中找到: " + e.getMessage());
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("❌ 数据库错误: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("❌ 其他错误: " + e.getMessage());
            e.printStackTrace();
        }
    }
}