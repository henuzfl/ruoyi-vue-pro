package cn.iocoder.yudao.module.wm.service.bom;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.wm.controller.admin.distributiontask.vo.DistributionTaskPageReqVO;
import cn.iocoder.yudao.module.wm.controller.admin.distributiontask.vo.DistributionTaskSaveReqVO;
import cn.iocoder.yudao.module.wm.dal.dataobject.distributiontask.DistributionTaskDO;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * BOM服务接口
 */
public interface BomService {

    /**
     * 从SAP获取BOM信息
     * @param conditions 查询条件
     * @return BOM组件列表
     */
    List<Map<String, Object>> getBomFromSap(Map<String, Object> conditions);

    /**
     * 根据物料号获取BOM
     * @param materialNumber 物料号
     * @param plant 工厂
     * @return BOM组件列表
     */
    List<Map<String, Object>> getBomByMaterial(String materialNumber, String plant);
}