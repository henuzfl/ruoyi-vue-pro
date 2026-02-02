package cn.iocoder.yudao.module.wm.service.bom;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.wm.controller.admin.distributiontask.vo.DistributionTaskPageReqVO;
import cn.iocoder.yudao.module.wm.controller.admin.distributiontask.vo.DistributionTaskSaveReqVO;
import cn.iocoder.yudao.module.wm.dal.dataobject.distributiontask.DistributionTaskDO;

import javax.validation.Valid;
import java.math.BigDecimal;

/**
 * 配送任务下发 Service 接口
 *
 * @author 柳文
 */
public interface BomService {

    /**
     * 创建配送任务下发
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    BigDecimal createDistributionTask(@Valid DistributionTaskSaveReqVO createReqVO);

    /**
     * 更新配送任务下发
     *
     * @param updateReqVO 更新信息
     */
    void updateDistributionTask(@Valid DistributionTaskSaveReqVO updateReqVO);

    /**
     * 删除配送任务下发
     *
     * @param id 编号
     */
    void deleteDistributionTask(BigDecimal id);

    /**
     * 获得配送任务下发
     *
     * @param id 编号
     * @return 配送任务下发
     */
    DistributionTaskDO getDistributionTask(BigDecimal id);

    /**
     * 获得配送任务下发分页
     *
     * @param pageReqVO 分页查询
     * @return 配送任务下发分页
     */
    PageResult<DistributionTaskDO> getDistributionTaskPage(DistributionTaskPageReqVO pageReqVO);

}