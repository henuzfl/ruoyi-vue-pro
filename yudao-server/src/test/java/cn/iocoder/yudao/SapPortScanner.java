package cn.iocoder.yudao;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class SapPortScanner {

    public static void main(String[] args) {
        String host = "172.30.1.52";
        int[] commonSapPorts = {
                3300, 3301, 3302, 3303,  // 常见网关端口
                3200, 3201, 3202, 3203,  // 备用网关端口
                3600, 3601,             // Message Server端口
                8000, 8001, 8002,       // HTTP端口
                44300, 44301,           // HTTPS端口
                8100, 8101              // 其他常见端口
        };

        System.out.println("=== 扫描 SAP 常见端口 ===");
        System.out.println("主机: " + host);
        System.out.println();

        for (int port : commonSapPorts) {
            testPort(host, port);
        }
    }

    private static void testPort(String host, int port) {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(host, port), 2000);
            System.out.println("✅ 端口 " + port + " 开放 (" + getPortDescription(port) + ")");
        } catch (IOException e) {
            // 不打印失败的，只打印成功的
        }
    }

    private static String getPortDescription(int port) {
        switch (port) {
            case 3300: case 3301: case 3302: case 3303:
                return "SAP 网关端口";
            case 3200: case 3201: case 3202: case 3203:
                return "SAP 内部网关";
            case 3600: case 3601:
                return "SAP Message Server";
            case 8000: case 8001: case 8002:
                return "SAP HTTP 服务";
            case 44300: case 44301:
                return "SAP HTTPS 服务";
            default:
                return "未知 SAP 端口";
        }
    }
}