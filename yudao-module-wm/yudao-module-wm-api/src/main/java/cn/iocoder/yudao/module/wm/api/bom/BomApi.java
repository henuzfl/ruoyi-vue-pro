package cn.iocoder.yudao.module.wm.api.bom;

import cn.iocoder.yudao.module.wm.api.bom.dto.BomRespDTO;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * BOM API 接口
 *
 * @author 芋道源码
 */
public interface BomApi {

    /**
     * 根据物料号和工厂获取 BOM 列表
     *
     * @param materialNumber 物料号
     * @param plant 工厂
     * @return BOM 组件列表
     */
    List<BomRespDTO> getBomByMaterial(String materialNumber, String plant);

    /**
     * 获得 BOM 信息（根据 SAP 的组合键）
     *
     * @param bomId BOM 标识（格式：工厂:物料号）
     * @return BOM 信息
     */
    BomRespDTO getBom(String bomId);

    /**
     * 获得 BOM 信息数组
     *
     * @param bomIds BOM 标识数组
     * @return BOM 信息数组
     */
    List<BomRespDTO> getBomList(Collection<String> bomIds);

    /**
     * 校验 BOM 们是否有效
     *
     * @param bomIds BOM 标识数组
     */
    void validateBomList(Collection<String> bomIds);
}