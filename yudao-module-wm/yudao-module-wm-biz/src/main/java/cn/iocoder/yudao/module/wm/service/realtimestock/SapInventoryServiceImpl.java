package cn.iocoder.yudao.module.wm.service.realtimestock;

import cn.iocoder.yudao.module.buyer.dal.dataobject.buyertimestock.buyerTimeStockDO;
import cn.iocoder.yudao.module.buyer.service.buyertimestock.buyerTimeStockService;
import cn.iocoder.yudao.module.wm.controller.admin.realtimestock.vo.SapStockSyncReqVO;
import cn.iocoder.yudao.module.wm.controller.admin.realtimestock.vo.InventoryResultVO;
import cn.iocoder.yudao.module.wm.controller.admin.realtimestock.vo.SyncResultVO;
import cn.iocoder.yudao.module.wm.util.SapInventoryUtils;
import cn.iocoder.yudao.module.buyer.service.buyertimestock.buyerTimeStockService;
import com.alibaba.druid.util.StringUtils;
import com.baomidou.dynamic.datasource.annotation.DS;
import org.springframework.beans.factory.annotation.Value;   // ✅ 正确导入
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * SAP库存查询服务实现
 */
@Service
@DS("oracle") // 指定使用 Oracle 数据源
@Slf4j
public class SapInventoryServiceImpl implements SapInventoryService {

    @Autowired
    private SapInventoryUtils sapInventoryUtils;

    @Autowired
    private buyerTimeStockService buyerTimeStockService;

    @Value("${sap.default-plant:6400}")  // 默认6400，可通过配置文件覆盖
    private String defaultPlant;

    @Override
    public List<InventoryResultVO> searchInventory(SapStockSyncReqVO queryVO) {
        // 转换查询参数
        Map<String, Object> params = convertToSapParams(queryVO);

        // 调用SAP函数获取数据
        List<Map<String, Object>> sapData = sapInventoryUtils.searchInventory(params);

        // 过滤掉库存为0的记录
        sapData = filterZeroStock(sapData);

        // 转换为VO
        return convertToResultVO(sapData);
    }

    @Override
    public List<Map<String, Object>> searchInventoryRawData(SapStockSyncReqVO queryVO) {
        // 转换查询参数
        Map<String, Object> params = convertToSapParams(queryVO);

        // 直接调用SAP函数获取原始数据
        List<Map<String, Object>> sapData = sapInventoryUtils.searchInventory(params);

        // 过滤掉库存为0的记录
        return filterZeroStock(sapData);
    }

    @Override
    public Map<String, Object> getMaterialInventorySummary(String materialNumber, String plant) {
        Map<String, Object> summary = sapInventoryUtils.getMaterialInventorySummary(materialNumber, plant);

        // 过滤掉库存为0的记录
        Object inventoryListObj = summary.get("inventoryList");
        if (inventoryListObj instanceof List) {
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> inventoryList = (List<Map<String, Object>>) inventoryListObj;
            List<Map<String, Object>> filteredList = filterZeroStock(inventoryList);
            summary.put("inventoryList", filteredList);
        }

        return summary;
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public SyncResultVO syncAllStock() {
        log.info("[syncAllStock] 开始全量同步SAP库存");

        SyncResultVO result = new SyncResultVO();
        result.setAllSuccess(false);

        try {
            // 1. 构造空条件VO（不传入物料号，由SAP函数返回全量数据）
            SapStockSyncReqVO queryVO = new SapStockSyncReqVO();
            queryVO.setPlant(defaultPlant);   // 👈 写死为6400，或从配置读取

            // 2. 调用SAP获取原始数据（假设底层支持无物料号查询）
            List<Map<String, Object>> rawData = this.searchInventoryRawData(queryVO);
            log.info("[syncAllStock] SAP返回全量库存数据，共 {} 条", rawData.size());

            if (rawData.isEmpty()) {
                result.setAllSuccess(true);
                result.setTotal(0);
                result.setSuccessCount(0);
                result.setFailCount(0);
                return result;
            }

            // 3. 转换为DO并批量入库
            List<buyerTimeStockDO> stockDOList = convertRawDataToStockDOList(rawData, null);
            String batchResult = buyerTimeStockService.batchInsertOrUpdateStock(stockDOList);

            // 4. 构建成功结果
            result.setAllSuccess(true);
            result.setTotal(stockDOList.size());
            result.setSuccessCount(stockDOList.size());
            result.setFailCount(0);

            log.info("[syncAllStock] 全量同步完成，结果: {}", batchResult);

        } catch (Exception e) {
            log.error("[syncAllStock] 全量同步失败", e);
            result.setAllSuccess(false);
            result.setFailCount(1);
            // 添加全局失败记录
            SyncResultVO.FailureDetail failure = new SyncResultVO.FailureDetail();
            failure.setMaterialNumber("ALL");
            failure.setErrorMessage(e.getMessage());
            result.setFailures(Collections.singletonList(failure));
        }

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SyncResultVO syncSingleStock(SapStockSyncReqVO queryVO) {
        // 参数校验：物料号必填
        if (queryVO == null || StringUtils.isEmpty(queryVO.getMaterialNumber())) {
            throw new IllegalArgumentException("物料号不能为空");
        }
        if (StringUtils.isEmpty(queryVO.getPlant())) {
            queryVO.setPlant(defaultPlant);
            log.debug("[syncSingleStock] 未指定工厂，使用默认工厂: {}", defaultPlant);
        }
        log.info("[syncSingleStock] 开始同步单个物料: {}", queryVO.getMaterialNumber());

        SyncResultVO result = new SyncResultVO();
        result.setAllSuccess(false);

        try {
            // 1. 查询SAP（复用已有方法）
            List<Map<String, Object>> rawData = this.searchInventoryRawData(queryVO);
            log.info("[syncSingleStock] 物料 {} 返回 {} 条库存记录",
                    queryVO.getMaterialNumber(), rawData.size());

            // 2. 转换为DO
            List<buyerTimeStockDO> stockDOList = convertRawDataToStockDOList(
                    rawData, queryVO.getMaterialNumber());

            // 3. 批量插入/更新（即使只有一条也走批量，便于统一）
            if (!stockDOList.isEmpty()) {
                buyerTimeStockService.batchInsertOrUpdateStock(stockDOList);
            }

            // 4. 构建结果
            result.setAllSuccess(true);
            result.setTotal(stockDOList.size());
            result.setSuccessCount(stockDOList.size());
            result.setFailCount(0);

            log.info("[syncSingleStock] 物料 {} 同步完成", queryVO.getMaterialNumber());

        } catch (Exception e) {
            log.error("[syncSingleStock] 物料 {} 同步失败", queryVO.getMaterialNumber(), e);
            result.setAllSuccess(false);
            result.setFailCount(1);

            SyncResultVO.FailureDetail failure = new SyncResultVO.FailureDetail();
            failure.setMaterialNumber(queryVO.getMaterialNumber());
            failure.setErrorMessage(e.getMessage());
            result.setFailures(Collections.singletonList(failure));
        }

        return result;
    }

    @Override
    public boolean checkMaterialHasStock(String materialNumber, String plant) {
        SapStockSyncReqVO queryVO = new SapStockSyncReqVO();
        queryVO.setMaterialNumber(materialNumber);
        queryVO.setPlant(plant);

        List<Map<String, Object>> sapData = searchInventoryRawData(queryVO);
        return !sapData.isEmpty();
    }

    /**
     * 过滤掉库存为0的记录
     */
    private List<Map<String, Object>> filterZeroStock(List<Map<String, Object>> sapData) {
        if (CollectionUtils.isEmpty(sapData)) {
            return Collections.emptyList();
        }

        return sapData.stream()
                .filter(item -> {
                    Object stockQty = item.get("LABST");
                    if (stockQty == null) {
                        return false;
                    }
                    try {
                        double stock = Double.parseDouble(stockQty.toString());
                        return stock > 0;
                    } catch (NumberFormatException e) {
                        return false;
                    }
                })
                .collect(Collectors.toList());
    }

    /**
     * 将查询VO转换为SAP函数参数
     */
    private Map<String, Object> convertToSapParams(SapStockSyncReqVO queryVO) {
        Map<String, Object> params = new HashMap<>();

        if (queryVO == null) {
            return params;
        }

        if (queryVO.getMaterialNumber() != null) {
            params.put("MATNR", queryVO.getMaterialNumber());
        }
        if (queryVO.getPlant() != null) {
            params.put("WERKS", queryVO.getPlant());
        }
        if (queryVO.getStorageLocation() != null) {
            params.put("LGORT", queryVO.getStorageLocation());
        }
        if (queryVO.getSpecialStock() != null) {
            params.put("SOBKZ", queryVO.getSpecialStock());
        }
        if (queryVO.getSupplierCode() != null) {
            params.put("LIFNR", queryVO.getSupplierCode());
        }
        if (queryVO.getSupplierName() != null) {
            params.put("NAME1", queryVO.getSupplierName());
        }
        // 默认启用 SAP 端零库存过滤，除非显式传入了其他值
        if (queryVO != null && queryVO.getBusinessField1() != null) {
            params.put("BFIELD1", queryVO.getBusinessField1());
        } else {
            params.put("BFIELD1", "X"); // 默认过滤
        }
        if (queryVO.getBusinessField2() != null) {
            params.put("BFIELD2", queryVO.getBusinessField2());
        }
        if (queryVO.getBusinessField3() != null) {
            params.put("BFIELD3", queryVO.getBusinessField3());
        }
        if (queryVO.getBusinessField4() != null) {
            params.put("BFIELD4", queryVO.getBusinessField4());
        }
        if (queryVO.getBusinessField5() != null) {
            params.put("BFIELD5", queryVO.getBusinessField5());
        }

        return params;
    }
    /**
     * 将SAP原始数据转换为本地DO列表
     * @param rawDataList SAP返回的原始Map列表
     * @param mainMaterialNo 主物料号（可为null，仅用于备注）
     */
    private List<buyerTimeStockDO> convertRawDataToStockDOList(
            List<Map<String, Object>> rawDataList, String mainMaterialNo) {

        List<buyerTimeStockDO> result = new ArrayList<>();

        for (int i = 0; i < rawDataList.size(); i++) {
            Map<String, Object> item = rawDataList.get(i);
            buyerTimeStockDO stock = new buyerTimeStockDO();

            stock.setMaterialNo(getStringValue(item, "MATNR"));
            stock.setMaterialDesc(getStringValue(item, "MAKTX"));
            stock.setStockLocation(getStringValue(item, "LGORT"));

            // 库存数量转换
            Object stockQty = item.get("LABST");
            BigDecimal qty = convertToBigDecimal(stockQty);
            stock.setStockQuantity(qty);
            stock.setAvailableQuantity(qty);

            stock.setStatus(0);
            if (mainMaterialNo != null) {
                stock.setRemark("从SAP同步，物料号：" + mainMaterialNo + "，序号：" + (i + 1));
            } else {
                stock.setRemark("全量同步，序号：" + (i + 1));
            }

            result.add(stock);
        }

        log.debug("转换完成，共 {} 条", result.size());
        return result;
    }

    private BigDecimal convertToBigDecimal(Object value) {
        if (value == null) {
            return BigDecimal.ZERO;
        }
        try {
            return new BigDecimal(value.toString());
        } catch (NumberFormatException e) {
            log.warn("库存数量格式错误: {}", value);
            return BigDecimal.ZERO;
        }
    }
    /**
     * 将SAP返回的数据转换为结果VO
     */
    private List<InventoryResultVO> convertToResultVO(List<Map<String, Object>> sapData) {
        if (CollectionUtils.isEmpty(sapData)) {
            return Collections.emptyList();
        }

        return sapData.stream().map(item -> {
            InventoryResultVO vo = new InventoryResultVO();

            vo.setMaterialNumber(getStringValue(item, "MATNR"));
            vo.setMaterialDescription(getStringValue(item, "MAKTX"));
            vo.setPlant(getStringValue(item, "WERKS"));
            vo.setStorageLocation(getStringValue(item, "LGORT"));
            vo.setStorageLocationDesc(getStringValue(item, "LGOBE"));
            vo.setStockQuantity(getStringValue(item, "LABST"));
            vo.setUnit(getStringValue(item, "MEINS"));
            vo.setUnitDescription(getStringValue(item, "MSEHT"));
            vo.setSpecialStock(getStringValue(item, "SOBKZ"));
            vo.setSupplierCode(getStringValue(item, "LIFNR"));
            vo.setSupplierName(getStringValue(item, "NAME1"));
            vo.setStockTypeDesc(getStringValue(item, "STOCK_TYPE_DESC"));
            vo.setStockStatusDesc(getStringValue(item, "STOCK_STATUS_DESC"));

            return vo;
        }).collect(Collectors.toList());
    }

    /**
     * 安全获取字符串值
     */
    private String getStringValue(Map<String, Object> map, String key) {
        Object value = map.get(key);
        return value != null ? value.toString() : "";
    }
}