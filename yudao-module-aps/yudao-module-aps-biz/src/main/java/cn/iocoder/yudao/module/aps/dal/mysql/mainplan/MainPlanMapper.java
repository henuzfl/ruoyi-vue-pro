package cn.iocoder.yudao.module.aps.dal.mysql.mainplan;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.aps.dal.dataobject.mainplan.MainPlanDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.aps.controller.admin.mainplan.vo.*;

/**
 * 主计划 Mapper
 *
 * @author 柳文
 */
@Mapper
public interface MainPlanMapper extends BaseMapperX<MainPlanDO> {

    default PageResult<MainPlanDO> selectPage(MainPlanPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<MainPlanDO>()
                .eqIfPresent(MainPlanDO::getProductionOrderNo, reqVO.getProductionOrderNo())
                .eqIfPresent(MainPlanDO::getAssemblyMaterialNo, reqVO.getAssemblyMaterialNo())
                .eqIfPresent(MainPlanDO::getMainMaterialDesc, reqVO.getMainMaterialDesc())
                .betweenIfPresent(MainPlanDO::getScheduledDate, reqVO.getScheduledDate())
                .eqIfPresent(MainPlanDO::getScheduledQuantity, reqVO.getScheduledQuantity())
                .eqIfPresent(MainPlanDO::getProductionWorkshop, reqVO.getProductionWorkshop())
                .betweenIfPresent(MainPlanDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(MainPlanDO::getId));
    }

}