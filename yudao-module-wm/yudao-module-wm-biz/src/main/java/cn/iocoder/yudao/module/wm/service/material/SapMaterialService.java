package cn.iocoder.yudao.module.wm.service.material;

import cn.iocoder.yudao.module.wm.controller.admin.material.vo.MaterialResultVO;
import cn.iocoder.yudao.module.wm.controller.admin.material.vo.SapMaterialQueryReqVO;

import java.time.LocalDate;
import java.util.List;

/**
 * SAP物料主数据查询服务接口
 * 调用 RFC: ZFM_JT_QMS_I001_RFC
 */
public interface SapMaterialService {

    /**
     * 批量查询物料信息（自动分批处理）
     * @param reqVO 查询条件（物料号列表、工厂）
     * @return 物料信息列表
     */
    List<MaterialResultVO> searchMaterials(SapMaterialQueryReqVO reqVO);

    /**
     * 查询单个物料信息
     * @param matnr 物料号
     * @param plant 工厂（为空则使用默认工厂）
     * @return 物料信息，不存在返回 null
     */
    MaterialResultVO getMaterial(String matnr, String plant);

    /**
     * 查询单个物料信息（使用默认工厂）
     */
    default MaterialResultVO getMaterial(String matnr) {
        return getMaterial(matnr, null);
    }

    /**
     * 检查物料是否存在
     */
    boolean checkMaterialExists(String matnr, String plant);

    /**
     * 从主计划同步 SAP 物料到物料主数据导入表
     * @param materialNos 物料号列表（从主计划获取）
     * @return 成功同步的物料数
     */
    int syncMaterialsFromMainPlan(List<String> materialNos);

    /**
     * 同步单个物料到导入表
     * @param matnr 物料号
     * @param plant 工厂
     * @return true-成功，false-失败
     */
    boolean syncSingleMaterial(String matnr, String plant);

    /**
     * 按日期范围同步物料主数据到导入表
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 同步成功的物料数
     */
    int syncMaterialsByDate(LocalDate startDate, LocalDate endDate);
}