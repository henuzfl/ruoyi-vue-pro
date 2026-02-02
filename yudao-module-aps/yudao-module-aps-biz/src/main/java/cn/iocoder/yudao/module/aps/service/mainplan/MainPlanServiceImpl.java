package cn.iocoder.yudao.module.aps.service.mainplan;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import cn.iocoder.yudao.module.aps.controller.admin.mainplan.vo.*;
import cn.iocoder.yudao.module.aps.dal.dataobject.mainplan.MainPlanDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.aps.dal.mysql.mainplan.MainPlanMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.aps.enums.ErrorCodeConstants.*;

/**
 * 主计划 Service 实现类
 *
 * @author 柳文
 */
@Service
@DS("oracle") // 指定使用 Oracle 数据源
@Validated
public class MainPlanServiceImpl implements MainPlanService {

    @Resource
    private MainPlanMapper mainPlanMapper;

    @Override
    public BigDecimal createMainPlan(MainPlanSaveReqVO createReqVO) {
        // 插入
        MainPlanDO mainPlan = BeanUtils.toBean(createReqVO, MainPlanDO.class);
        mainPlanMapper.insert(mainPlan);
        // 返回
        return mainPlan.getId();
    }

    @Override
    public void updateMainPlan(MainPlanSaveReqVO updateReqVO) {
        // 校验存在
        validateMainPlanExists(updateReqVO.getId());
        // 更新
        MainPlanDO updateObj = BeanUtils.toBean(updateReqVO, MainPlanDO.class);
        mainPlanMapper.updateById(updateObj);
    }

    @Override
    public void deleteMainPlan(BigDecimal id) {
        // 校验存在
        validateMainPlanExists(id);
        // 删除
        mainPlanMapper.deleteById(id);
    }

    private void validateMainPlanExists(BigDecimal id) {
        if (mainPlanMapper.selectById(id) == null) {
            throw exception(MAIN_PLAN_NOT_EXISTS);
        }
    }

    @Override
    public MainPlanDO getMainPlan(BigDecimal id) {
        return mainPlanMapper.selectById(id);
    }

    @Override
    public PageResult<MainPlanDO> getMainPlanPage(MainPlanPageReqVO pageReqVO) {
        return mainPlanMapper.selectPage(pageReqVO);
    }

}