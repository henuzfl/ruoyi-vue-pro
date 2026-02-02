package cn.iocoder.yudao.module.aps.service.mainplan;

import java.math.BigDecimal;
import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.aps.controller.admin.mainplan.vo.*;
import cn.iocoder.yudao.module.aps.dal.dataobject.mainplan.MainPlanDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 主计划 Service 接口
 *
 * @author 柳文
 */
public interface MainPlanService {

    /**
     * 创建主计划
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    BigDecimal createMainPlan(@Valid MainPlanSaveReqVO createReqVO);

    /**
     * 更新主计划
     *
     * @param updateReqVO 更新信息
     */
    void updateMainPlan(@Valid MainPlanSaveReqVO updateReqVO);

    /**
     * 删除主计划
     *
     * @param id 编号
     */
    void deleteMainPlan(BigDecimal id);

    /**
     * 获得主计划
     *
     * @param id 编号
     * @return 主计划
     */
    MainPlanDO getMainPlan(BigDecimal id);

    /**
     * 获得主计划分页
     *
     * @param pageReqVO 分页查询
     * @return 主计划分页
     */
    PageResult<MainPlanDO> getMainPlanPage(MainPlanPageReqVO pageReqVO);

}