package cn.iocoder.yudao;

import cn.iocoder.yudao.module.wm.util.SapRfcUtils;
import com.sap.conn.jco.*; // 关键修改：使用JCO 3.0导入
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.Map;
import java.util.Properties;
import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

/**
 * SAP连接测试类 - JCO 3.0版本
 */
@SpringBootTest
@ActiveProfiles("local")
public class SapConnectionTest {

    @Test
    public void testVmOptions() {
        System.out.println("=== VM选项测试 ===");

        // 方法1：检查断言是否启用（简单方法）
        boolean assertionsEnabled = false;
        assert assertionsEnabled = true; // 如果断言启用，这会改变assertionsEnabled的值
        System.out.println("断言是否启用: " + assertionsEnabled);

        // 方法2：检查系统属性
        System.out.println("java.library.path: " + System.getProperty("java.library.path"));
        System.out.println("java.version: " + System.getProperty("java.version"));
        System.out.println("user.dir: " + System.getProperty("user.dir"));
        System.out.println("sun.arch.data.model: " + System.getProperty("sun.arch.data.model"));

        // 检查DLL路径
        String libPath = System.getProperty("java.library.path");
        if (libPath != null) {
            String[] paths = libPath.split(File.pathSeparator);
            for (String path : paths) {
                System.out.println("检查路径: " + path);
                File dll = new File(path, "sapjco3.dll");
                if (dll.exists()) {
                    System.out.println("  ✅ sapjco3.dll存在，大小: " + dll.length() + "字节");
                } else {
                    System.out.println("  ❌ sapjco3.dll不存在");
                }
            }
        } else {
            System.out.println("java.library.path未设置");
        }

        // 检查当前工作目录
        System.out.println("当前目录: " + new File(".").getAbsolutePath());
    }

    @Test
    public void testSimpleJcoLoad() {
        System.out.println("=== 简单JCO加载测试 ===");

        try {
            // 直接加载原生库
            System.loadLibrary("sapjco3");
            System.out.println("✅ sapjco3.dll加载成功");

            // 尝试获取JCO版本
            Class<?> jcoClass = Class.forName("com.sap.conn.jco.JCo");
            java.lang.reflect.Method getVersion = jcoClass.getMethod("getVersion");
            String version = (String) getVersion.invoke(null);
            System.out.println("✅ JCO版本: " + version);

        } catch (Exception e) {
            System.err.println("❌ JCO加载失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
}