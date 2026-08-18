package cn.iocoder.yudao.module.marketing.service.scissorliftdplan;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.marketing.controller.admin.scissorliftdplan.vo.*;
import cn.iocoder.yudao.module.marketing.dal.dataobject.scissorliftdplan.ScissorLiftDplanDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.marketing.dal.mysql.scissorliftdplan.ScissorLiftDplanMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.marketing.enums.ErrorCodeConstants.*;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;

/**
 * 高机剪叉日计划 Service 实现类
 *
 * @author 柳文
 */
@Service
@DS("oracle") // 指定使用 Oracle 数据源
@Validated
public class ScissorLiftDplanServiceImpl implements ScissorLiftDplanService {

    @Resource
    private ScissorLiftDplanMapper scissorLiftDplanMapper;

    @Override
    public Long createScissorLiftDplan(ScissorLiftDplanSaveReqVO createReqVO) {
        ScissorLiftDplanDO entity = BeanUtils.toBean(createReqVO, ScissorLiftDplanDO.class);
        if (entity.getId() == null) {
            entity.setId(IdWorker.getId());
        }
        scissorLiftDplanMapper.insert(entity);
        return entity.getId();
    }

    @Override
    public void updateScissorLiftDplan(ScissorLiftDplanSaveReqVO updateReqVO) {
        // 校验存在
        validateScissorLiftDplanExists(updateReqVO.getId());
        // 更新
        ScissorLiftDplanDO updateObj = BeanUtils.toBean(updateReqVO, ScissorLiftDplanDO.class);
        scissorLiftDplanMapper.updateById(updateObj);
    }

    @Override
    public void deleteScissorLiftDplan(Long id) {
        // 校验存在
        validateScissorLiftDplanExists(id);
        // 删除
        scissorLiftDplanMapper.deleteById(id);
    }

    private void validateScissorLiftDplanExists(Long id) {
        if (scissorLiftDplanMapper.selectById(id) == null) {
            throw exception(SCISSOR_LIFT_DPLAN_NOT_EXISTS);
        }
    }

    @Override
    public ScissorLiftDplanDO getScissorLiftDplan(Long id) {
        return scissorLiftDplanMapper.selectById(id);
    }

    @Override
    public PageResult<ScissorLiftDplanDO> getScissorLiftDplanPage(ScissorLiftDplanPageReqVO pageReqVO) {
        return scissorLiftDplanMapper.selectPage(pageReqVO);
    }

}