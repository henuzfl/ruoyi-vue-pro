package cn.iocoder.yudao.module.aps.service.plan;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.aps.controller.admin.plan.vo.*;
import cn.iocoder.yudao.module.aps.dal.dataobject.plan.PlanDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;

/**
 * 设备调度 Service 接口
 *
 * @author 芋道源码
 */
public interface PlanService {

    /**
     * 创建设备调度
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Short createPlan(@Valid PlanSaveReqVO createReqVO);

    /**
     * 更新设备调度
     *
     * @param updateReqVO 更新信息
     */
    void updatePlan(@Valid PlanSaveReqVO updateReqVO);

    /**
     * 删除设备调度
     *
     * @param id 编号
     */
    void deletePlan(Short id);

    /**
     * 获得设备调度
     *
     * @param id 编号
     * @return 设备调度
     */
    PlanDO getPlan(Short id);

    /**
     * 获得设备调度分页
     *
     * @param pageReqVO 分页查询
     * @return 设备调度分页
     */
    PageResult<PlanDO> getPlanPage(PlanPageReqVO pageReqVO);

    /**
     * 执行计划
     *
     * @param
     * @return 执行计划
     */
    @InterceptorIgnore(tenantLine = "true")
    void callUpdateStockProcedure();
}