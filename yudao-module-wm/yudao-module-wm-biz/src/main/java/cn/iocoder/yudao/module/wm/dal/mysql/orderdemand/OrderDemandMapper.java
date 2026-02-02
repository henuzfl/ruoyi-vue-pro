package cn.iocoder.yudao.module.wm.dal.mysql.orderdemand;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.wm.dal.dataobject.orderdemand.OrderDemandDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.wm.controller.admin.orderdemand.vo.*;

/**
 * 订单追溯需求 Mapper
 *
 * @author 柳文
 */
@Mapper
public interface OrderDemandMapper extends BaseMapperX<OrderDemandDO> {

    default PageResult<OrderDemandDO> selectPage(OrderDemandPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<OrderDemandDO>()
                .eqIfPresent(OrderDemandDO::getOrderNo, reqVO.getOrderNo())
                .eqIfPresent(OrderDemandDO::getTraceDemandNo, reqVO.getTraceDemandNo())
                .eqIfPresent(OrderDemandDO::getMaterialNo, reqVO.getMaterialNo())
                .eqIfPresent(OrderDemandDO::getMaterialDescription, reqVO.getMaterialDescription())
                .eqIfPresent(OrderDemandDO::getDemandQuantity, reqVO.getDemandQuantity())
                .eqIfPresent(OrderDemandDO::getOutboundAccumulated, reqVO.getOutboundAccumulated())
                .eqIfPresent(OrderDemandDO::getOpenQuantity, reqVO.getOpenQuantity())
                .eqIfPresent(OrderDemandDO::getStatus, reqVO.getStatus())
                .eqIfPresent(OrderDemandDO::getRemark, reqVO.getRemark())
                .betweenIfPresent(OrderDemandDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(OrderDemandDO::getId));
    }

}