package cn.iocoder.yudao.module.wm.service.sapordersync;

import cn.iocoder.yudao.module.aps.dal.dataobject.order.OrderDO;
import cn.iocoder.yudao.module.wm.dal.mysql.sapordersync.SapOrderSyncMapper;
import com.baomidou.dynamic.datasource.annotation.DS;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;

import cn.iocoder.yudao.module.wm.util.sap.SapOrderUtils;
import cn.iocoder.yudao.module.wm.controller.admin.sapordersync.vo.SapOrderQueryReqVO;
import cn.iocoder.yudao.module.wm.controller.admin.sapordersync.vo.OrderFromSapVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;

/**
 * 订单表 - SAP订单信息 Service 实现类
 *
 * @author 柳文
 */
@Service
@DS("oracle") // 指定使用 Oracle 数据源
@Validated
@Slf4j
public class SapOrderSyncServiceImpl implements SapOrderSyncService {

    @Resource
    private SapOrderSyncMapper sapOrderSyncMapper;

    @Autowired
    private SapOrderUtils sapOrderUtils;

    @Value("${sap.default-plant:6400}")
    private String defaultPlant;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int syncOrderFromSap(SapOrderQueryReqVO reqVO) {
        if (reqVO == null) reqVO = new SapOrderQueryReqVO();
        if (!StringUtils.hasText(reqVO.getPlant())) {
            reqVO.setPlant(defaultPlant);
        }

        // 构造日期参数（SAP RFC 需要字符串，格式 yyyyMMdd 或 yyyyMMdd-yyyyMMdd）
        String dateParam = null;
        if (reqVO.getStartDate() != null) {
            String start = reqVO.getStartDate().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
            if (reqVO.getEndDate() != null && !reqVO.getEndDate().equals(reqVO.getStartDate())) {
                String end = reqVO.getEndDate().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
                dateParam = start + "-" + end;
            } else {
                dateParam = start;
            }
        }

        // 1. 调用 SAP
        List<Map<String, Object>> rawData = sapOrderUtils.searchOrders(
                reqVO.getPlant(), reqVO.getAufnr(), dateParam);

        if (CollectionUtils.isEmpty(rawData)) {
            log.info("SAP 未返回订单数据");
            return 0;
        }

        // 2. 转换为 OrderDO
        List<OrderDO> orderList = convertSapToOrderDO(rawData);
        if (orderList.isEmpty()) return 0;

        for (OrderDO order : orderList) {
            order.setId(String.valueOf(IdWorker.getId())); // id 字段类型为 String
        }

        // 3. 提取订单号列表用于删除
        List<String> aufnrList = orderList.stream()
                .map(OrderDO::getProductionOrderNo)
                .distinct()
                .collect(Collectors.toList());

        // 4. 分批删除旧数据（Oracle IN 列表最多 1000 项）
        int deleted = 0;
        if (!aufnrList.isEmpty()) {
            int batchSize = 30;
            for (int i = 0; i < aufnrList.size(); i += batchSize) {
                int end = Math.min(i + batchSize, aufnrList.size());
                List<String> subList = aufnrList.subList(i, end);
                deleted += sapOrderSyncMapper.deleteByProductionOrderNos(subList);
            }
        }
        log.info("同步前删除旧订单 {} 条", deleted);
        // 设置审计字段
        // 5. 批量插入新数据（分批，每批 500 条）
        LocalDateTime now = LocalDateTime.now();
        for (OrderDO order : orderList) {
            order.setCreateTime(now);
            order.setUpdateTime(now);
        }
        int batchSize = 30;
        int totalInserted = 0;
        for (int i = 0; i < orderList.size(); i += batchSize) {
            int end = Math.min(i + batchSize, orderList.size());
            List<OrderDO> subList = orderList.subList(i, end);
            sapOrderSyncMapper.batchInsert(subList);
            totalInserted += subList.size();
        }
        log.info("从 SAP 同步订单完成，共插入 {} 条", totalInserted);
        return totalInserted;
    }

    /**
     * 批量同步：直接从 SAP 原始数据插入
     * 只调用一次 SAP，在内存中处理去重，然后批量删除+插入
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int syncOrdersFromSapRawData(List<Map<String, Object>> rawData) {
        if (CollectionUtils.isEmpty(rawData)) {
            log.info("SAP 返回数据为空，无需同步");
            return 0;
        }

        log.info("开始批量同步 SAP 订单，原始数据行数: {}", rawData.size());

        // 1. 转换为 OrderDO
        List<OrderDO> orderList = convertSapToOrderDO(rawData);
        if (orderList.isEmpty()) {
            log.info("转换后无有效订单数据");
            return 0;
        }

        // 2. 按订单号去重（保留第一条）
        Map<String, OrderDO> orderMap = orderList.stream()
                .collect(Collectors.toMap(
                        OrderDO::getProductionOrderNo,
                        order -> order,
                        (existing, replacement) -> existing  // 如果有重复，保留第一个
                ));

        List<OrderDO> distinctOrders = new ArrayList<>(orderMap.values());
        log.info("去重后订单数量: {} (原始: {})", distinctOrders.size(), orderList.size());

        // 3. 提取所有订单号
        List<String> aufnrList = distinctOrders.stream()
                .map(OrderDO::getProductionOrderNo)
                .filter(StringUtils::hasText)
                .distinct()
                .collect(Collectors.toList());

        if (aufnrList.isEmpty()) {
            log.info("无有效订单号");
            return 0;
        }

        // 4. 设置 ID 和审计字段
        LocalDateTime now = LocalDateTime.now();
        for (OrderDO order : distinctOrders) {
            order.setId(String.valueOf(IdWorker.getId()));
            order.setCreateTime(now);
            order.setUpdateTime(now);
        }

        // 5. 分批删除旧数据（Oracle IN 列表最多 1000 项）
        int deleted = 0;
        int deleteBatchSize = 30;  // 与您的原代码保持一致
        for (int i = 0; i < aufnrList.size(); i += deleteBatchSize) {
            int end = Math.min(i + deleteBatchSize, aufnrList.size());
            List<String> subList = aufnrList.subList(i, end);
            deleted += sapOrderSyncMapper.deleteByProductionOrderNos(subList);
        }
        log.info("删除旧订单 {} 条", deleted);

        // 6. 批量插入新数据（分批，每批 30 条）
        int insertBatchSize = 30;
        int totalInserted = 0;
        for (int i = 0; i < distinctOrders.size(); i += insertBatchSize) {
            int end = Math.min(i + insertBatchSize, distinctOrders.size());
            List<OrderDO> subList = distinctOrders.subList(i, end);
            sapOrderSyncMapper.batchInsert(subList);
            totalInserted += subList.size();
        }

        log.info("批量同步完成，插入 {} 条订单记录", totalInserted);
        return totalInserted;
    }

    @Override
    public List<OrderFromSapVO> searchOrderFromSap(SapOrderQueryReqVO reqVO) {
        if (reqVO == null) reqVO = new SapOrderQueryReqVO();
        if (!StringUtils.hasText(reqVO.getPlant())) {
            reqVO.setPlant(defaultPlant);
        }
        List<Map<String, Object>> rawData = sapOrderUtils.searchOrders(
                reqVO.getPlant(), reqVO.getAufnr(), null);
        return convertMapToOrderFromSapVO(rawData);
    }

// ========== 私有转换方法 ==========

    private List<OrderDO> convertSapToOrderDO(List<Map<String, Object>> rawData) {
        List<OrderDO> list = new ArrayList<>();
        if (CollectionUtils.isEmpty(rawData)) return list;

        for (Map<String, Object> item : rawData) {
            OrderDO order = new OrderDO();
            order.setProductionOrderNo(getString(item, "AUFNR"));
            order.setAssemblyMaterialNo(getString(item, "MATNR"));
            order.setMainMaterialDesc(getString(item, "MAKTX"));
            order.setComponentOrderType(getString(item, "AUART")); // 订单类型
            order.setScheduledQuantity(toBigDecimal(item.get("PSMNG")));
            order.setDeliveredQuantity(toBigDecimal(item.get("WEMNG")));
            order.setCreationDate(toLocalDateTime(item.get("ERDAT")));
            order.setCreatedBy(getString(item, "ERNAM"));
            // 优先使用 ZASTNR 状态描述，其次 ASTNR 状态码
            String statusDesc = getString(item, "ZASTNR");
            order.setSystemStatus(StringUtils.hasText(statusDesc) ? statusDesc : getString(item, "ASTNR"));
            // 排产开始 GSTRS 作为计划开始日期，若无则用 GSTRP
            LocalDateTime scheduledStart = toLocalDateTime(item.get("GSTRS"));
            if (scheduledStart == null) {
                scheduledStart = toLocalDateTime(item.get("GSTRP"));
            }
            order.setScheduledDate(scheduledStart);
            order.setActualStartTime(toLocalDateTime(item.get("GSTRI")));
            order.setBasicEndDate(toLocalDateTime(item.get("GLTRS")));
            order.setPlant(getString(item, "WERKS"));
            order.setMrpController(getString(item, "DISPO"));
            order.setProductionWorkshop(getString(item, "FEVOR"));
            order.setUnitOfMeasure(getString(item, "MEINS"));
            order.setProductionVersion(getString(item, "VERID"));
            order.setActualEndDate(toLocalDateTime(item.get("GETRI")));
            // 若有 GETRI (确认完成日期) 且 GLTRI 为空，可使用 GETRI
            if (order.getActualEndDate() == null) {
                order.setActualEndDate(toLocalDateTime(item.get("GLTRI")));
            }
            order.setProcessStartDate(toLocalDateTime(item.get("GSTRI"))); // 近似
            order.setSubmitDate(toLocalDateTime(item.get("FTMPS"))); // 计划下达日期
            order.setProcessReleased(getString(item, "FTRMS")); // 计划下达日期字符
            order.setCentralProc(null);
            order.setChangeDate(toLocalDateTime(item.get("AEDAT")));
            order.setLastChangedBy(getString(item, "AENAM"));
            order.setOrderCategory(getString(item, "AUTYP"));
            order.setSalesOrder(getString(item, "KDAUF"));
            order.setDescription(null);
            order.setConfirmedQuantity(BigDecimal.ZERO); // 无对应字段

            list.add(order);
        }
        return list;
    }

    private List<OrderFromSapVO> convertMapToOrderFromSapVO(List<Map<String, Object>> rawData) {
        List<OrderFromSapVO> list = new ArrayList<>();
        if (CollectionUtils.isEmpty(rawData)) return list;

        for (Map<String, Object> item : rawData) {
            OrderFromSapVO vo = new OrderFromSapVO();
            vo.setAufnr(getString(item, "AUFNR"));
            vo.setMatnr(getString(item, "MATNR"));
            vo.setMaktx(getString(item, "MAKTX"));
            vo.setPsmng(toBigDecimal(item.get("PSMNG")));
            vo.setWemng(toBigDecimal(item.get("WEMNG")));
            vo.setAutyp(getString(item, "AUTYP"));
            vo.setAuart(getString(item, "AUART"));
            vo.setErdat(getString(item, "ERDAT"));
            vo.setErnam(getString(item, "ERNAM"));
            vo.setAstnr(getString(item, "ASTNR"));
            vo.setZastnr(getString(item, "ZASTNR"));
            vo.setFtrmp(getString(item, "FTRMP"));
            vo.setGstri(getString(item, "GSTRI"));
            vo.setGltrs(getString(item, "GLTRS"));
            vo.setWerks(getString(item, "WERKS"));
            vo.setDispo(getString(item, "DISPO"));
            vo.setFevor(getString(item, "FEVOR"));
            vo.setMeins(getString(item, "MEINS"));
            vo.setVerid(getString(item, "VERID"));
            vo.setLgort(getString(item, "LGORT"));
            vo.setGltri(getString(item, "GLTRI"));
            vo.setGltrp(getString(item, "GLTRP"));
            vo.setGstrp(getString(item, "GSTRP"));
            vo.setGstrs(getString(item, "GSTRS"));
            vo.setFtrms(getString(item, "FTRMS"));
            vo.setElikz(getString(item, "ELIKZ"));
            vo.setDgltp(getString(item, "DGLTP"));
            vo.setDglts(getString(item, "DGLTS"));
            vo.setAblad(getString(item, "ABLAD"));
            vo.setWempf(getString(item, "WEMPF"));
            vo.setLoekz(getString(item, "LOEKZ"));
            vo.setAenam(getString(item, "AENAM"));
            vo.setAedat(getString(item, "AEDAT"));
            vo.setKdauf(getString(item, "KDAUF"));
            vo.setGetri(getString(item, "GETRI"));
            vo.setObjnr(getString(item, "OBJNR"));
            list.add(vo);
        }
        return list;
    }

    // 辅助方法
    private String getString(Map<String, Object> map, String key) {
        Object val = map.get(key);
        return val == null ? "" : val.toString().trim();
    }

    private BigDecimal toBigDecimal(Object val) {
        if (val == null) return BigDecimal.ZERO;
        try {
            return new BigDecimal(val.toString());
        } catch (NumberFormatException e) {
            return BigDecimal.ZERO;
        }
    }

    private LocalDateTime toLocalDateTime(Object val) {
        if (val == null) return null;
        // 根据 SAP 返回格式处理，假设为 yyyyMMdd 字符串或 Date
        if (val instanceof java.util.Date) {
            return ((java.util.Date) val).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        }
        String str = val.toString();
        if (str.length() == 8) {
            try {
                LocalDate date = LocalDate.parse(str, DateTimeFormatter.ofPattern("yyyyMMdd"));
                return date.atStartOfDay();
            } catch (Exception e) {
                log.warn("日期解析失败: {}", str);
            }
        }
        return null;
    }

}