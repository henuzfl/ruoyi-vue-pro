package cn.iocoder.yudao.module.wm.util.sap;

import com.sap.conn.jco.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import cn.iocoder.yudao.module.wm.util.SapRfcUtils;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class SapOrderUtils {

    @Autowired
    private SapRfcUtils sapRfcUtils;

    private static final String DEFAULT_PLANT = "6400";

    /**
     * 查询 SAP 生产订单
     * @param plant   工厂（必填）
     * @param aufnr   订单号（可选）
     * @param dateRange 日期范围（格式待定，暂忽略）
     * @return 订单数据列表
     */
    public List<Map<String, Object>> searchOrders(String plant, String aufnr, String dateRange) {
        log.info("调用 SAP RFC: ZFM_TL_SEARCH_ORDER, 工厂: {}, 订单: {}", plant, aufnr);
        JCoDestination destination;
        JCoFunction function;

        try {
            destination = sapRfcUtils.getDestination();
            function = destination.getRepository().getFunction("ZFM_TL_SEARCH_ORDER");
            if (function == null) {
                throw new RuntimeException("SAP 中未找到函数: ZFM_TL_SEARCH_ORDER");
            }

            String targetPlant = StringUtils.hasText(plant) ? plant : DEFAULT_PLANT;

            JCoParameterList importParams = function.getImportParameterList();
            importParams.setValue("IN_WERKS", targetPlant);
            if (StringUtils.hasText(aufnr)) {
                importParams.setValue("IN_AUFNR", aufnr);
            }

            // 设置日期范围表参数（导入参数中的表类型）
            if (StringUtils.hasText(dateRange)) {
                JCoTable inDateTable = importParams.getTable("IN_DATE");  // 关键修改
                inDateTable.clear();
                inDateTable.appendRow();
                inDateTable.setValue("SIGN", "I");
                if (dateRange.contains("-")) {
                    String[] parts = dateRange.split("-");
                    inDateTable.setValue("OPTION", "BT");
                    inDateTable.setValue("LOW", parts[0]);
                    inDateTable.setValue("HIGH", parts[1]);
                } else {
                    inDateTable.setValue("OPTION", "EQ");
                    inDateTable.setValue("LOW", dateRange);
                }
            }
            // 日期参数暂略

            function.execute(destination);

            JCoTable etAfpo = function.getTableParameterList().getTable("ET_AFPO");
            log.info("【SAP查询】返回订单行数: {}", etAfpo.getNumRows());

            String ztype = function.getExportParameterList().getString("ZTYPE");
            String message = function.getExportParameterList().getString("MESSAGE");
            if ("E".equalsIgnoreCase(ztype)) {
                throw new RuntimeException("SAP 返回错误: " + message);
            }

            return sapRfcUtils.convertJCoTableToList(etAfpo);

        } catch (JCoException e) {
            log.error("执行 SAP RFC ZFM_TL_SEARCH_ORDER 失败", e);
            throw new RuntimeException("SAP 订单查询异常: " + e.getMessage(), e);
        }
    }

    public List<Map<String, Object>> searchByAufnr(String plant, String aufnr) {
        return searchOrders(plant, aufnr, null);
    }

    /**
     * 仅获取指定日期范围内有更新的订单号列表（不获取完整数据）
     */
    public List<String> getOrderNosByDate(String plant, String dateRange) {
        log.info("查询 SAP 更新订单号，工厂: {}, 日期范围: {}", plant, dateRange);
        // 复用现有查询方法
        List<Map<String, Object>> rawData = searchOrders(plant, null, dateRange);
        if (CollectionUtils.isEmpty(rawData)) {
            return Collections.emptyList();
        }
        return rawData.stream()
                .map(map -> map.get("AUFNR"))
                .filter(Objects::nonNull)
                .map(Object::toString)
                .distinct()
                .collect(Collectors.toList());
    }
}