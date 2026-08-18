package cn.iocoder.yudao.module.aps.dal.mysql.order;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.aps.dal.dataobject.order.OrderDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.aps.controller.admin.order.vo.*;

/**
 * 订单表 - SAP订单信息 Mapper
 *
 * @author 柳文
 */
@Mapper
public interface OrderMapper extends BaseMapperX<OrderDO> {

    default PageResult<OrderDO> selectPage(OrderPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<OrderDO>()
                .eqIfPresent(OrderDO::getAssemblyMaterialNo, reqVO.getAssemblyMaterialNo())
                .eqIfPresent(OrderDO::getMainMaterialDesc, reqVO.getMainMaterialDesc())
                .eqIfPresent(OrderDO::getComponentOrderType, reqVO.getComponentOrderType())
                .eqIfPresent(OrderDO::getScheduledQuantity, reqVO.getScheduledQuantity())
                .eqIfPresent(OrderDO::getDeliveredQuantity, reqVO.getDeliveredQuantity())
                .betweenIfPresent(OrderDO::getCreationDate, reqVO.getCreationDate())
                .eqIfPresent(OrderDO::getCreatedBy, reqVO.getCreatedBy())
                .eqIfPresent(OrderDO::getSystemStatus, reqVO.getSystemStatus())
                .betweenIfPresent(OrderDO::getScheduledDate, reqVO.getScheduledDate())
                .betweenIfPresent(OrderDO::getActualStartTime, reqVO.getActualStartTime())
                .betweenIfPresent(OrderDO::getBasicEndDate, reqVO.getBasicEndDate())
                .eqIfPresent(OrderDO::getPlant, reqVO.getPlant())
                .eqIfPresent(OrderDO::getMrpController, reqVO.getMrpController())
                .eqIfPresent(OrderDO::getProductionWorkshop, reqVO.getProductionWorkshop())
                .eqIfPresent(OrderDO::getUnitOfMeasure, reqVO.getUnitOfMeasure())
                .eqIfPresent(OrderDO::getProductionVersion, reqVO.getProductionVersion())
                .betweenIfPresent(OrderDO::getActualEndDate, reqVO.getActualEndDate())
                .betweenIfPresent(OrderDO::getProcessStartDate, reqVO.getProcessStartDate())
                .betweenIfPresent(OrderDO::getSubmitDate, reqVO.getSubmitDate())
                .eqIfPresent(OrderDO::getProcessReleased, reqVO.getProcessReleased())
                .eqIfPresent(OrderDO::getCentralProc, reqVO.getCentralProc())
                .betweenIfPresent(OrderDO::getChangeDate, reqVO.getChangeDate())
                .eqIfPresent(OrderDO::getLastChangedBy, reqVO.getLastChangedBy())
                .eqIfPresent(OrderDO::getOrderCategory, reqVO.getOrderCategory())
                .eqIfPresent(OrderDO::getSalesOrder, reqVO.getSalesOrder())
                .eqIfPresent(OrderDO::getDescription, reqVO.getDescription())
                .eqIfPresent(OrderDO::getConfirmedQuantity, reqVO.getConfirmedQuantity())
                .betweenIfPresent(OrderDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(OrderDO::getId));
    }

}