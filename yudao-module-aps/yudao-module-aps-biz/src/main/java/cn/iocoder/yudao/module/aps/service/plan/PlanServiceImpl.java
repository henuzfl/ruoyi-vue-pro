package cn.iocoder.yudao.module.aps.service.plan;

import com.baomidou.dynamic.datasource.annotation.DS;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.jdbc.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.aps.controller.admin.plan.vo.*;
import cn.iocoder.yudao.module.aps.dal.dataobject.plan.PlanDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.aps.dal.mysql.plan.PlanMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.system.enums.ErrorCodeConstants.*;

/**
 * 设备调度 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@DS("oracle")
@Validated
@Slf4j
public class PlanServiceImpl implements PlanService {

    @Resource
    private PlanMapper planMapper;

    @Override
    public Short createPlan(PlanSaveReqVO createReqVO) {
        // 插入
        PlanDO plan = BeanUtils.toBean(createReqVO, PlanDO.class);
        planMapper.insert(plan);
        // 返回
        return plan.getId();
    }

    @Override
    public void updatePlan(PlanSaveReqVO updateReqVO) {
        // 校验存在
        validatePlanExists(updateReqVO.getId());
        // 更新
        PlanDO updateObj = BeanUtils.toBean(updateReqVO, PlanDO.class);
        planMapper.updateById(updateObj);
    }

    @Override
    public void deletePlan(Short id) {
        // 校验存在
        validatePlanExists(id);
        // 删除
        planMapper.deleteById(id);
    }

    private void validatePlanExists(Short id) {
        if (planMapper.selectById(id) == null) {
            throw exception(PLAN_NOT_EXISTS);
        }
    }

    @Override
    public PlanDO getPlan(Short id) {
        return planMapper.selectById(id);
    }

    @Override
    public PageResult<PlanDO> getPlanPage(PlanPageReqVO pageReqVO) {
        return planMapper.selectPage(pageReqVO);
    }


    @Override
    @Transactional
    public void callUpdateStockProcedure() {
        try {
            log.info("开始调用存储过程 SYSTEM.generate_schedule_v1()");
            planMapper.callUpdateStockProcedure("SYSTEM");
            log.info("存储过程调用成功");
        } catch (Exception e) {
            log.error("存储过程调用失败", e);
            // 打印详细的错误信息
            if (e.getCause() != null) {
                log.error("根本原因: {}", e.getCause().getMessage());
            }
            throw new RuntimeException("调度计算失败: " + e.getMessage(), e);
        }
    }
}