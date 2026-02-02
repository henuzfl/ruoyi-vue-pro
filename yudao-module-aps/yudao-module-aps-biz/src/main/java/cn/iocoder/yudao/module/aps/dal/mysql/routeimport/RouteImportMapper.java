package cn.iocoder.yudao.module.aps.dal.mysql.routeimport;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.aps.dal.dataobject.routeimport.RouteImportDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.aps.controller.admin.routeimport.vo.*;

/**
 * 工艺路线导入 Mapper
 *
 * @author 柳文
 */
@Mapper
public interface RouteImportMapper extends BaseMapperX<RouteImportDO> {

    default PageResult<RouteImportDO> selectPage(RouteImportPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<RouteImportDO>()
                .eqIfPresent(RouteImportDO::getMaterialNo, reqVO.getMaterialNo())
                .eqIfPresent(RouteImportDO::getMaterialDesc, reqVO.getMaterialDesc())
                .eqIfPresent(RouteImportDO::getProcessRoute, reqVO.getProcessRoute())
                .eqIfPresent(RouteImportDO::getGroupNo, reqVO.getGroupNo())
                .eqIfPresent(RouteImportDO::getPlant, reqVO.getPlant())
                .betweenIfPresent(RouteImportDO::getValidFromDate, reqVO.getValidFromDate())
                .eqIfPresent(RouteImportDO::getWorkCenter, reqVO.getWorkCenter())
                .eqIfPresent(RouteImportDO::getSequenceNo, reqVO.getSequenceNo())
                .eqIfPresent(RouteImportDO::getOperationSeq, reqVO.getOperationSeq())
                .eqIfPresent(RouteImportDO::getOperationText, reqVO.getOperationText())
                .eqIfPresent(RouteImportDO::getControlCode, reqVO.getControlCode())
                .eqIfPresent(RouteImportDO::getLaborHours, reqVO.getLaborHours())
                .eqIfPresent(RouteImportDO::getLaborHoursUnit, reqVO.getLaborHoursUnit())
                .eqIfPresent(RouteImportDO::getFixedCost, reqVO.getFixedCost())
                .eqIfPresent(RouteImportDO::getFixedCostUnit, reqVO.getFixedCostUnit())
                .eqIfPresent(RouteImportDO::getVariableCost, reqVO.getVariableCost())
                .eqIfPresent(RouteImportDO::getVariableCostUnit, reqVO.getVariableCostUnit())
                .eqIfPresent(RouteImportDO::getProductionCycle, reqVO.getProductionCycle())
                .eqIfPresent(RouteImportDO::getProductionCycleUnit, reqVO.getProductionCycleUnit())
                .eqIfPresent(RouteImportDO::getChangeNo, reqVO.getChangeNo())
                .eqIfPresent(RouteImportDO::getDeleteFlag, reqVO.getDeleteFlag())
                .eqIfPresent(RouteImportDO::getProductionScheduler, reqVO.getProductionScheduler())
                .eqIfPresent(RouteImportDO::getProcurementType, reqVO.getProcurementType())
                .betweenIfPresent(RouteImportDO::getImportDate, reqVO.getImportDate())
                .betweenIfPresent(RouteImportDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(RouteImportDO::getId));
    }

}