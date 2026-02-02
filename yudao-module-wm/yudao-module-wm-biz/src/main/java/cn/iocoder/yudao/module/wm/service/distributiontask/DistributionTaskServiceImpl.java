package cn.iocoder.yudao.module.wm.service.distributiontask;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.wm.controller.admin.distributiontask.vo.*;
import cn.iocoder.yudao.module.wm.dal.dataobject.distributiontask.DistributionTaskDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.wm.dal.mysql.distributiontask.DistributionTaskMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.wm.enums.ErrorCodeConstants.*;

/**
 * 配送任务下发 Service 实现类
 *
 * @author 柳文
 */
@Service
@Validated
public class DistributionTaskServiceImpl implements DistributionTaskService {

    @Resource
    private DistributionTaskMapper distributionTaskMapper;

    @Override
    public BigDecimal createDistributionTask(DistributionTaskSaveReqVO createReqVO) {
        // 插入
        DistributionTaskDO distributionTask = BeanUtils.toBean(createReqVO, DistributionTaskDO.class);
        distributionTaskMapper.insert(distributionTask);
        // 返回
        return distributionTask.getId();
    }

    @Override
    public void updateDistributionTask(DistributionTaskSaveReqVO updateReqVO) {
        // 校验存在
        validateDistributionTaskExists(updateReqVO.getId());
        // 更新
        DistributionTaskDO updateObj = BeanUtils.toBean(updateReqVO, DistributionTaskDO.class);
        distributionTaskMapper.updateById(updateObj);
    }

    @Override
    public void deleteDistributionTask(BigDecimal id) {
        // 校验存在
        validateDistributionTaskExists(id);
        // 删除
        distributionTaskMapper.deleteById(id);
    }

    private void validateDistributionTaskExists(BigDecimal id) {
        if (distributionTaskMapper.selectById(id) == null) {
            throw exception(DISTRIBUTION_TASK_NOT_EXISTS);
        }
    }

    @Override
    public DistributionTaskDO getDistributionTask(BigDecimal id) {
        return distributionTaskMapper.selectById(id);
    }

    @Override
    public PageResult<DistributionTaskDO> getDistributionTaskPage(DistributionTaskPageReqVO pageReqVO) {
        return distributionTaskMapper.selectPage(pageReqVO);
    }

}