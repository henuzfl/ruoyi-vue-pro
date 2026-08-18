package cn.iocoder.yudao.module.marketing.service.aerialboomdplan;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.marketing.controller.admin.aerialboomdplan.vo.*;
import cn.iocoder.yudao.module.marketing.dal.dataobject.aerialboomdplan.AerialBoomDplanDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.marketing.dal.mysql.aerialboomdplan.AerialBoomDplanMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.marketing.enums.ErrorCodeConstants.*;

/**
 * 高机臂式日计划 Service 实现类
 *
 * @author 柳文
 */
@Service
@DS("oracle") // 指定使用 Oracle 数据源
@Validated
public class AerialBoomDplanServiceImpl
        extends ServiceImpl<AerialBoomDplanMapper, AerialBoomDplanDO>
        implements AerialBoomDplanService {

    @Resource
    private AerialBoomDplanMapper aerialBoomDplanMapper;

    @Override
    public Long createAerialBoomDplan(AerialBoomDplanSaveReqVO createReqVO) {
        // 插入
        AerialBoomDplanDO aerialBoomDplan = BeanUtils.toBean(createReqVO, AerialBoomDplanDO.class);
        aerialBoomDplanMapper.insert(aerialBoomDplan);
        // 返回
        return aerialBoomDplan.getId();
    }

    @Override
    public void updateAerialBoomDplan(AerialBoomDplanSaveReqVO updateReqVO) {
        // 校验存在
        validateAerialBoomDplanExists(updateReqVO.getId());
        // 更新
        AerialBoomDplanDO updateObj = BeanUtils.toBean(updateReqVO, AerialBoomDplanDO.class);
        aerialBoomDplanMapper.updateById(updateObj);
    }

    @Override
    public void deleteAerialBoomDplan(Long id) {
        // 校验存在
        validateAerialBoomDplanExists(id);
        // 删除
        aerialBoomDplanMapper.deleteById(id);
    }

    private void validateAerialBoomDplanExists(Long id) {
        if (aerialBoomDplanMapper.selectById(id) == null) {
            throw exception(AERIAL_BOOM_DPLAN_NOT_EXISTS);
        }
    }

    @Override
    public AerialBoomDplanDO getAerialBoomDplan(Long id) {
        return aerialBoomDplanMapper.selectById(id);
    }

    @Override
    public PageResult<AerialBoomDplanDO> getAerialBoomDplanPage(AerialBoomDplanPageReqVO pageReqVO) {
        return aerialBoomDplanMapper.selectPage(pageReqVO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveBatch(List<AerialBoomDplanDO> list) {
        if (list == null || list.isEmpty()) return;
        // 使用继承自 ServiceImpl 的 saveBatch，每批 100 条
        saveBatch(list, 100);
    }

    @Override
    public void insertAerialBoomDplan(AerialBoomDplanDO entity) {
        if (entity != null) {
            getBaseMapper().insert(entity);  // 逐条使用 MyBatis-Plus 的 insert，序列自动生效
        }
    }

}