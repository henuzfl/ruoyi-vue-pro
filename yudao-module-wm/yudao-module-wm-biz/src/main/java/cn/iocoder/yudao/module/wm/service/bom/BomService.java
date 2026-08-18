package cn.iocoder.yudao.module.wm.service.bom;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.wm.controller.admin.bom.vo.BomPageReqVO;
import cn.iocoder.yudao.module.wm.controller.admin.bom.vo.BomSaveReqVO;
import cn.iocoder.yudao.module.wm.controller.admin.bom.vo.SyncBomReqVO;
import cn.iocoder.yudao.module.wm.dal.dataobject.bom.BomDO;

import java.util.List;
import java.util.Map;

/**
 * BOM服务接口
 */
public interface BomService {

    /**
     * 从SAP获取BOM信息（兼容旧接口）
     * @param conditions 查询条件
     * @return BOM组件列表
     */
    List<Map<String, Object>> getBomFromSap(Map<String, Object> conditions);

    /**
     * 从SAP获取BOM信息（新版）
     * @param materialNumber 物料号
     * @param plant 工厂
     * @param date 日期（可选）
     * @return BOM组件列表
     */
    List<Map<String, Object>> getBomFromSap(String materialNumber, String plant, String date);

    /**
     * 根据物料号获取BOM
     * @param materialNumber 物料号
     * @param plant 工厂
     * @return BOM组件列表
     */
    List<Map<String, Object>> getBomByMaterial(String materialNumber, String plant);

    /**
     * 获取简化的BOM数据
     * @param materialNumber 物料号
     * @param plant 工厂
     * @return 简化的BOM组件列表
     */
    List<Map<String, Object>> getSimpleBom(String materialNumber, String plant);

    /**
     * 获取分层的BOM结构
     * @param materialNumber 物料号
     * @param plant 工厂
     * @return 按层级分组的BOM结构
     */
    Map<String, List<Map<String, Object>>> getGroupedBom(String materialNumber, String plant);

    /**
     * 创建BOM（本地数据库）
     */
    Long createBom(BomSaveReqVO createReqVO);

    /**
     * 更新BOM（本地数据库）
     */
    void updateBom(BomSaveReqVO updateReqVO);

    /**
     * 删除BOM（本地数据库）
     */
    void deleteBom(Long id);

    /**
     * 获得BOM（本地数据库）
     */
    BomDO getBom(Long id);

    /**
     * 获得BOM分页（本地数据库）
     */
    PageResult<BomDO> getBomPage(BomPageReqVO pageReqVO);

    /**
     * 从SAP同步BOM数据到本地
     */
    void syncBomFromSap(SyncBomReqVO syncReqVO);

    /**
     * 根据APS主计划的物料号，同步BOM到导入表
     * @return 同步结果描述
     */
    void syncBomForMaterial(String materialNumber, String plant);
}