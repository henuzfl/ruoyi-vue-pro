package cn.iocoder.yudao.module.wm.controller.admin.realtimestock;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.buyer.dal.dataobject.buyertimestock.buyerTimeStockDO;
import cn.iocoder.yudao.module.buyer.service.buyertimestock.buyerTimeStockService;
import cn.iocoder.yudao.module.wm.controller.admin.realtimestock.vo.SapStockBatchSyncReqVO;
import cn.iocoder.yudao.module.wm.controller.admin.realtimestock.vo.SapStockSyncReqVO;
import cn.iocoder.yudao.module.wm.controller.admin.realtimestock.vo.InventoryResultVO;
import cn.iocoder.yudao.module.wm.controller.admin.realtimestock.vo.SyncResultVO;
import cn.iocoder.yudao.module.wm.service.realtimestock.SapInventoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - SAP库存查询")
@RestController
@RequestMapping("/wm/sap-inventory")
@Validated
@Slf4j
public class SapInventoryController {

    @Autowired
    private SapInventoryService sapInventoryService;

    @Resource
    private buyerTimeStockService pbuyerTimeStockService;

    @PostMapping("/search")
    @Operation(summary = "查询SAP库存")
    public CommonResult<List<InventoryResultVO>> searchInventory(@Valid @RequestBody SapStockSyncReqVO queryDTO) {
        log.info("[searchInventory] 开始查询SAP库存，请求参数: materialNumber={}, plant={}, storageLocation={}, overwrite={}",
                queryDTO.getMaterialNumber(), queryDTO.getPlant(), queryDTO.getStorageLocation(),
                queryDTO.getOverwrite());

        try {
            List<InventoryResultVO> result = sapInventoryService.searchInventory(queryDTO);
            log.info("[searchInventory] SAP库存查询完成，返回结果数量: {}", result != null ? result.size() : 0);
            return success(result);
        } catch (Exception e) {
            log.error("[searchInventory] SAP库存查询异常", e);
            throw e; // 重新抛出异常，让全局异常处理器处理
        }
    }

    @GetMapping("/summary")
    @Operation(summary = "获取物料库存汇总")
    public CommonResult<Map<String, Object>> getMaterialInventorySummary(
            @RequestParam("materialNumber") String materialNumber,
            @RequestParam("plant") String plant) {
        log.info("[getMaterialInventorySummary] 获取物料库存汇总，参数: materialNumber={}, plant={}",
                materialNumber, plant);

        try {
            Map<String, Object> summary = sapInventoryService.getMaterialInventorySummary(materialNumber, plant);
            log.info("[getMaterialInventorySummary] 物料库存汇总获取完成，返回结果: {}", summary);
            return success(summary);
        } catch (Exception e) {
            log.error("[getMaterialInventorySummary] 获取物料库存汇总异常", e);
            throw e;
        }
    }

    @GetMapping("/check-stock")
    @Operation(summary = "检查物料是否有库存")
    public CommonResult<Boolean> checkMaterialHasStock(
            @RequestParam("materialNumber") String materialNumber,
            @RequestParam("plant") String plant) {
        log.info("[checkMaterialHasStock] 检查物料是否有库存，参数: materialNumber={}, plant={}",
                materialNumber, plant);

        try {
            boolean hasStock = sapInventoryService.checkMaterialHasStock(materialNumber, plant);
            log.info("[checkMaterialHasStock] 库存检查完成，结果: {}", hasStock);
            return success(hasStock);
        } catch (Exception e) {
            log.error("[checkMaterialHasStock] 检查物料库存异常", e);
            throw e;
        }
    }

    @GetMapping("/simple-search")
    @Operation(summary = "简单条件查询库存")
    public CommonResult<List<InventoryResultVO>> searchInventorySimple(
            @RequestParam(value = "materialNumber", required = false) String materialNumber,
            @RequestParam(value = "plant", required = false) String plant,
            @RequestParam(value = "storageLocation", required = false) String storageLocation) {

        log.info("[searchInventorySimple] 简单条件查询库存，参数: materialNumber={}, plant={}, storageLocation={}",
                materialNumber, plant, storageLocation);

        SapStockSyncReqVO queryDTO = new SapStockSyncReqVO();
        queryDTO.setMaterialNumber(materialNumber);
        queryDTO.setPlant(plant);
        queryDTO.setStorageLocation(storageLocation);

        try {
            List<InventoryResultVO> result = sapInventoryService.searchInventory(queryDTO);
            log.info("[searchInventorySimple] 简单条件查询完成，返回结果数量: {}", result != null ? result.size() : 0);
            return success(result);
        } catch (Exception e) {
            log.error("[searchInventorySimple] 简单条件查询异常", e);
            throw e;
        }
    }


    /**
     * 批量同步多个物料的库存数据(问题)
     */
    @PostMapping("/batch-sync")
    @Operation(summary = "批量同步多个物料的库存数据")
    public CommonResult<String> batchSyncStock(@Valid @RequestBody SapStockBatchSyncReqVO batchSyncReqVO) {
        log.info("[batchSyncStock] 批量同步多个物料的库存数据开始");

        List<SapStockSyncReqVO> syncTasks = batchSyncReqVO.getSyncTasks();
        if (syncTasks == null || syncTasks.isEmpty()) {
            return success("同步任务列表为空");
        }

        // 收集所有数据
        List<buyerTimeStockDO> allStockList = new ArrayList<>();
        List<String> errorMessages = new ArrayList<>();

        for (int i = 0; i < syncTasks.size(); i++) {
            SapStockSyncReqVO queryVO = syncTasks.get(i);
            log.info("[batchSyncStock] 处理第{}个物料: {}", i + 1, queryVO.getMaterialNumber());

            try {
                // 获取SAP数据
                List<Map<String, Object>> sapStockList = sapInventoryService.searchInventoryRawData(queryVO);

                if (sapStockList != null && !sapStockList.isEmpty()) {
                    // 转换数据
                    List<buyerTimeStockDO> stockList = convertSapDataToStockDOList(sapStockList, queryVO.getMaterialNumber());
                    allStockList.addAll(stockList);
                    log.info("[batchSyncStock] 第{}个物料处理成功，数据量: {}",
                            i + 1, stockList.size());
                } else {
                    errorMessages.add(String.format("物料[%s]: SAP无库存数据", queryVO.getMaterialNumber()));
                    log.warn("[batchSyncStock] 第{}个物料无库存数据", i + 1);
                }

            } catch (Exception e) {
                errorMessages.add(String.format("物料[%s]: 处理失败 - %s",
                        queryVO.getMaterialNumber(), e.getMessage()));
                log.error("[batchSyncStock] 处理第{}个物料失败", i + 1, e);
            }
        }

        if (allStockList.isEmpty()) {
            String errorResult = "所有物料均无数据，同步失败\n" + String.join("\n", errorMessages);
            return success(errorResult);
        }

        // 调用Service层批量插入
        try {
            String result = pbuyerTimeStockService.batchInsertOrUpdateStock(allStockList);

            // 添加错误信息
            if (!errorMessages.isEmpty()) {
                result += "\n\n其他错误:\n" + String.join("\n", errorMessages);
            }

            log.info("[batchSyncStock] 批量同步完成: {}", result);
            return success(result);

        } catch (Exception e) {
            log.error("[batchSyncStock] 批量同步失败", e);
            return CommonResult.error(500, "批量同步失败: " + e.getMessage());
        }
    }
    /**
     * 同步所有库存数据
     */
    @PostMapping("/sync-all")
    @Operation(summary = "全量同步SAP库存")
    @PreAuthorize("@ss.hasPermission('wm:inventory:sync')")
    public CommonResult<SyncResultVO> syncAllStock() {
        log.info("[syncAllStock] 接收到全量同步请求");
        SyncResultVO result = sapInventoryService.syncAllStock();
        return success(result);
    }
    /**
     * 根据物料同步库存
     */
    @PostMapping("/sync-single")
    @Operation(summary = "单个物料同步")
    public CommonResult<SyncResultVO> syncSingleStock(@Valid @RequestBody SapStockSyncReqVO queryVO) {
        log.info("[syncSingleStock] 接收到单个物料同步请求: {}", queryVO.getMaterialNumber());
        SyncResultVO result = sapInventoryService.syncSingleStock(queryVO);
        return success(result);
    }

    // ==================== 私有方法 ====================

    /**
     * 将SAP数据转换为buyerTimeStockDO列表
     */
    private List<buyerTimeStockDO>
    convertSapDataToStockDOList(List<Map<String, Object>> sapDataList, String mainMaterialNo) {
        log.debug("[convertSapDataToStockDOList] 开始转换SAP数据，数据量: {}, 主物料号: {}",
                sapDataList.size(), mainMaterialNo);

        List<buyerTimeStockDO> result =
                new ArrayList<>();

        for (int i = 0; i < sapDataList.size(); i++) {
            Map<String, Object> sapData = sapDataList.get(i);

            if (log.isDebugEnabled()) {
                log.debug("[convertSapDataToStockDOList] 处理第{}条数据: MATNR={}, MAKTX={}, LGORT={}, LABST={}",
                        i + 1,
                        sapData.get("MATNR"),
                        sapData.get("MAKTX"),
                        sapData.get("LGORT"),
                        sapData.get("LABST"));
            }

            buyerTimeStockDO stock =
                    new buyerTimeStockDO();

            // 映射字段
            stock.setMaterialNo(getStringValue(sapData, "MATNR"));
            stock.setMaterialDesc(getStringValue(sapData, "MAKTX"));
            stock.setStockLocation(getStringValue(sapData, "LGORT"));

            // 处理库存数量
            Object stockQty = sapData.get("LABST");
            if (stockQty != null) {
                try {
                    BigDecimal stockQuantity = new BigDecimal(stockQty.toString());
                    stock.setStockQuantity(stockQuantity);
                    stock.setAvailableQuantity(stockQuantity);
                    log.debug("[convertSapDataToStockDOList] 第{}条数据库存数量: {}", i + 1, stockQuantity);
                } catch (NumberFormatException e) {
                    log.warn("[convertSapDataToStockDOList] 第{}条数据库存数量格式错误: {}", i + 1, stockQty);
                    stock.setStockQuantity(BigDecimal.ZERO);
                    stock.setAvailableQuantity(BigDecimal.ZERO);
                }
            } else {
                log.debug("[convertSapDataToStockDOList] 第{}条数据库存数量为空", i + 1);
                stock.setStockQuantity(BigDecimal.ZERO);
                stock.setAvailableQuantity(BigDecimal.ZERO);
            }

            // 设置状态和备注
            stock.setStatus(0);
            stock.setRemark("从SAP同步，批次号：" + mainMaterialNo + "，序号：" + (i + 1));

            result.add(stock);
        }

        log.info("[convertSapDataToStockDOList] SAP数据转换完成，转换数量: {}", result.size());
        return result;
    }

    /**
     * 安全获取字符串值
     */
    private String getStringValue(Map<String, Object> map, String key) {
        Object value = map.get(key);
        String result = value != null ? value.toString() : "";
        log.trace("[getStringValue] key={}, value={}, result={}", key, value, result);
        return result;
    }
}