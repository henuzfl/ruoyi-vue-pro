package cn.iocoder.yudao.module.aps.service.mainplan;

import java.math.BigDecimal;
import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.aps.controller.admin.mainplan.vo.*;
import cn.iocoder.yudao.module.aps.dal.dataobject.mainplan.MainPlanDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.apache.ibatis.annotations.Param;

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

    /**
     * 获取所有去重的总成物料号
     */
    List<String> getDistinctAssemblyMaterialNo();

    /**
     * 获取所有去重的总成一级子件物料号
     */
    List<String> getDistinctComponentMaterialNo();

    /**
     * 批量导入主计划
     * @param importVOList 导入数据列表
     * @return 成功导入条数
     */
    int importMainPlan(List<MainPlanImportReqVO> importVOList);

    void deleteMainPlan(BigDecimal id);

    void clearAllMainPlan();

}