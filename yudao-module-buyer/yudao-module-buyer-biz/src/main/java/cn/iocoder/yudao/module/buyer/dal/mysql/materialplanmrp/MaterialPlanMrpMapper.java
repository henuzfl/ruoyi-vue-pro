package cn.iocoder.yudao.module.buyer.dal.mysql.materialplanmrp;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.buyer.dal.dataobject.materialplanmrp.MaterialPlanMrpDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.buyer.controller.admin.materialplanmrp.vo.MaterialPlanMrpPageReqVO;

/**
 * 买家需求预测 Mapper
 *
 * @author 柳文
 */
@Mapper
public interface MaterialPlanMrpMapper extends BaseMapperX<MaterialPlanMrpDO> {

    default PageResult<MaterialPlanMrpDO> selectPage(MaterialPlanMrpPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<MaterialPlanMrpDO>()
                .eqIfPresent(MaterialPlanMrpDO::getCustomerName, reqVO.getCustomerName())
                .eqIfPresent(MaterialPlanMrpDO::getProductLine, reqVO.getProductLine())
                .eqIfPresent(MaterialPlanMrpDO::getTonnageSegment, reqVO.getTonnageSegment())
                .eqIfPresent(MaterialPlanMrpDO::getVehicleModel, reqVO.getVehicleModel())
                .eqIfPresent(MaterialPlanMrpDO::getCylinderCode, reqVO.getCylinderCode())
                .eqIfPresent(MaterialPlanMrpDO::getComponentMaterialNo, reqVO.getComponentMaterialNo())
                .eqIfPresent(MaterialPlanMrpDO::getComponentDesc, reqVO.getComponentDesc())
                .eqIfPresent(MaterialPlanMrpDO::getPurchasingGroup, reqVO.getPurchasingGroup())
                .eqIfPresent(MaterialPlanMrpDO::getMaterialType, reqVO.getMaterialType())
                .eqIfPresent(MaterialPlanMrpDO::getMaterialSupplyDesc, reqVO.getMaterialSupplyDesc())
                .betweenIfPresent(MaterialPlanMrpDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(MaterialPlanMrpDO::getCreateTime));
    }

    default List<MaterialPlanMrpDO> selectList(MaterialPlanMrpPageReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<MaterialPlanMrpDO>()
                .eqIfPresent(MaterialPlanMrpDO::getCustomerName, reqVO.getCustomerName())
                .eqIfPresent(MaterialPlanMrpDO::getProductLine, reqVO.getProductLine())
                .eqIfPresent(MaterialPlanMrpDO::getTonnageSegment, reqVO.getTonnageSegment())
                .eqIfPresent(MaterialPlanMrpDO::getVehicleModel, reqVO.getVehicleModel())
                .eqIfPresent(MaterialPlanMrpDO::getCylinderCode, reqVO.getCylinderCode())
                .eqIfPresent(MaterialPlanMrpDO::getComponentMaterialNo, reqVO.getComponentMaterialNo())
                .eqIfPresent(MaterialPlanMrpDO::getComponentDesc, reqVO.getComponentDesc())
                .eqIfPresent(MaterialPlanMrpDO::getPurchasingGroup, reqVO.getPurchasingGroup())
                .eqIfPresent(MaterialPlanMrpDO::getMaterialType, reqVO.getMaterialType())
                .eqIfPresent(MaterialPlanMrpDO::getMaterialSupplyDesc, reqVO.getMaterialSupplyDesc())
                .betweenIfPresent(MaterialPlanMrpDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(MaterialPlanMrpDO::getCreateTime));
    }
}