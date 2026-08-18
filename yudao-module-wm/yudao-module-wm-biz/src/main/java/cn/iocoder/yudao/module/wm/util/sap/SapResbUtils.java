package cn.iocoder.yudao.module.wm.util.sap;

import cn.iocoder.yudao.module.wm.util.SapRfcUtils;
import com.sap.conn.jco.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.*;

@Component
@Slf4j
public class SapResbUtils {

    @Autowired
    private SapRfcUtils sapRfcUtils;

    private static final String SAP_DATE_FORMAT = "yyyyMMdd";
    private static final String DEFAULT_PLANT = "6400";

    /**
     * 查询生产订单预留数据
     * @param plant 工厂（必填）
     * @param aufnr 生产订单号（可选）
     * @param rsnum 预留号（可选）
     * @param dateRange 日期范围（可选，格式如 "20230101-20231231"）
     * @return 预留数据列表
     */
    public List<Map<String, Object>> searchResb(String plant, String aufnr, String rsnum, String dateRange) {
        log.info("调用 SAP RFC: ZFM_TL_SEARCH_RESB, 工厂: {}, 订单: {}, 预留: {}, 日期范围: {}",
                plant, aufnr, rsnum, dateRange);
        JCoDestination destination;
        JCoFunction function;

        try {
            destination = sapRfcUtils.getDestination();
            function = destination.getRepository().getFunction("ZFM_TL_SEARCH_RESB");
            if (function == null) {
                throw new RuntimeException("SAP 中未找到函数: ZFM_TL_SEARCH_RESB");
            }

            String targetPlant = StringUtils.hasText(plant) ? plant : DEFAULT_PLANT;

            // 1. 设置导入参数
            JCoParameterList importParams = function.getImportParameterList();
            importParams.setValue("IN_WERKS", targetPlant);
            if (StringUtils.hasText(aufnr)) {
                importParams.setValue("IN_AUFNR", aufnr);
            }
            if (StringUtils.hasText(rsnum)) {
                importParams.setValue("IN_RSNUM", rsnum);
            }
            if (StringUtils.hasText(dateRange)) {
                // 假设 IN_DATE 是单个日期字符串，若实际为 RANGE 结构需调整
                importParams.setValue("IN_DATE", dateRange);
            }

            // 2. 执行 RFC
            function.execute(destination);

            // 3. 获取返回表 ET_RESB
            JCoTable etResb = function.getTableParameterList().getTable("ET_RESB");
            int rowCount = etResb.getNumRows();
            log.info("【SAP查询】返回预留数据行数: {}", rowCount);

            // 4. 获取导出消息
            String ztype = function.getExportParameterList().getString("ZTYPE");
            String message = function.getExportParameterList().getString("MESSAGE");
            if ("E".equalsIgnoreCase(ztype)) {
                throw new RuntimeException("SAP 返回错误: " + message);
            }
            log.debug("SAP 返回消息: {} - {}", ztype, message);

            return sapRfcUtils.convertJCoTableToList(etResb);

        } catch (JCoException e) {
            log.error("执行 SAP RFC ZFM_TL_SEARCH_RESB 失败", e);
            throw new RuntimeException("SAP 预留查询异常: " + e.getMessage(), e);
        }
    }

    /**
     * 便捷方法：根据生产订单号查询
     */
    public List<Map<String, Object>> searchByAufnr(String plant, String aufnr) {
        return searchResb(plant, aufnr, null, null);
    }
}