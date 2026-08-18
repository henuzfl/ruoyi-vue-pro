package cn.iocoder.yudao.module.wm.service.realtimestock;

import cn.iocoder.yudao.module.wm.controller.admin.realtimestock.vo.SapStockSyncReqVO;
import cn.iocoder.yudao.module.wm.controller.admin.realtimestock.vo.InventoryResultVO;
import cn.iocoder.yudao.module.wm.controller.admin.realtimestock.vo.SyncResultVO;

import java.util.List;
import java.util.Map;

/**
 * SAP库存查询服务接口
 */
public interface SapInventoryService {

    /**
     * 根据条件查询SAP库存
     * @param queryVO 查询条件
     * @return 库存列表
     */
    List<InventoryResultVO> searchInventory(SapStockSyncReqVO queryVO);

    /**
     * 获取物料库存汇总
     * @param materialNumber 物料号
     * @param plant 工厂
     * @return 库存汇总信息
     */
    Map<String, Object> getMaterialInventorySummary(String materialNumber, String plant);

    /**
     * 检查物料是否有库存
     * @param materialNumber 物料号
     * @param plant 工厂
     * @return 是否有库存
     */
    boolean checkMaterialHasStock(String materialNumber, String plant);

    /**
     * 根据条件查询SAP库存（返回原始Map数据）
     * @param queryVO 查询条件
     * @return 原始库存数据列表
     */
    List<Map<String, Object>> searchInventoryRawData(SapStockSyncReqVO queryVO);
    /**
     * 全量同步：从 SAP 拉取所有库存数据，覆盖本地表
     * @return 同步结果（成功/失败数量等）
     */
    SyncResultVO syncAllStock();

    /**
     * 单个物料同步：根据物料号等条件同步指定物料库存
     * @param queryVO 查询条件（物料号必填）
     * @return 同步结果
     */
    SyncResultVO syncSingleStock(SapStockSyncReqVO queryVO);
}