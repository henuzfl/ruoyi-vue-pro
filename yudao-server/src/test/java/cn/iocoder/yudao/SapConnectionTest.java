package cn.iocoder.yudao.module.wm.service.purchaseorder;

import cn.iocoder.yudao.module.wm.util.SapRfcUtils;
import com.sap.mw.jco.JCO;
import org.junit.jupiter.api.Test;  // ✅ 使用JUnit5
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.annotation.Resource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * SAP连接测试类
 */
// 关键注解1：启用Spring测试上下文
@SpringBootTest(classes = cn.iocoder.yudao.module.wm.WmApplication.class)
// 关键注解2：加载Spring Boot应用上下文
@TestPropertySource(locations = "classpath:application-test.yml")
@ComponentScan(basePackages = "cn.iocoder.yudao.module.wm")  // ✅ 确保扫描到SapRfcUtils
public class SapConnectionTest {

    private static final Logger log = LoggerFactory.getLogger(SapConnectionTest.class);

    // 关键注解3：自动注入要测试的工具类
    @Autowired
    private SapRfcUtils sapRfcUtils;

    /**
     * 测试SAP JCO连接是否正常
     */
    @Test
    public void testSapConnection() {
        log.info("开始测试SAP连接...");

        // ✅ 先检查sapRfcUtils是否注入成功
        assertNotNull(sapRfcUtils, "SapRfcUtils未正确注入，检查@ComponentScan配置");

        JCO.Client client = null;

        try {
            // 1. 获取SAP客户端连接
            client = sapRfcUtils.getClient();

            // 2. 断言验证（如果连接失败，这里会直接抛出断言错误，测试失败）
            assertNotNull(client, "SAP客户端连接对象不应为null");
            assertTrue(client.isAlive(), "SAP连接应处于活动状态");

            // 3. 打印连接成功信息
            log.info("✅ SAP连接测试成功！");
            log.info("连接状态: {}", client.isAlive() ? "活动" : "非活动");
            log.info("连接属性: {}", client.getAttributes());

        } catch (Exception e) {
            // 4. 连接失败，记录详细错误
            log.error("❌ SAP连接测试失败！", e);
            fail("SAP连接测试失败: " + e.getMessage());

        } finally {
            // 5. 确保断开连接
            if (client != null && client.isAlive()) {
                try {
                    client.disconnect();
                    log.info("SAP连接已安全断开");
                } catch (Exception e) {
                    log.warn("断开SAP连接时发生警告", e);
                }
            }
        }
    }

    /**
     * 测试SAP配置是否正确加载（不实际建立连接）
     */
    @Test
    public void testSapConfigLoading() {
        log.info("测试SAP配置加载...");
        // ✅ 先检查sapRfcUtils是否注入成功
        assertNotNull(sapRfcUtils, "SapRfcUtils未正确注入");
        try {
            // 测试配置加载，不建立实际连接
            String ashost = sapRfcUtils.getLogonProps().getProperty("jco.client.ashost");
            String user = sapRfcUtils.getLogonProps().getProperty("jco.client.user");

            assertNotNull(ashost, "SAP主机地址不应为null");
            assertNotNull(user, "SAP用户名不应为null");

            log.info("✅ SAP配置加载成功");
            log.info("SAP主机: {}", ashost);
            log.info("SAP用户: {}", user);

        } catch (Exception e) {
            log.error("❌ SAP配置加载失败", e);
            fail("SAP配置加载失败: " + e.getMessage());
        }
    }
}