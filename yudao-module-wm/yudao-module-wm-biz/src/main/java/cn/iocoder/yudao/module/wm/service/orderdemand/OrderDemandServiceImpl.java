package cn.iocoder.yudao.module.wm.service.orderdemand;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.wm.controller.admin.orderdemand.vo.*;
import cn.iocoder.yudao.module.wm.dal.dataobject.orderdemand.OrderDemandDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.wm.dal.mysql.orderdemand.OrderDemandMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.wm.enums.ErrorCodeConstants.*;

/**
 * 订单追溯需求 Service 实现类
 *
 * @author 柳文
 */
@Service
@Validated
public class OrderDemandServiceImpl implements OrderDemandService {

    @Resource
    private OrderDemandMapper orderDemandMapper;

    @Override
    public BigDecimal createOrderDemand(OrderDemandSaveReqVO createReqVO) {
        // 插入
        OrderDemandDO orderDemand = BeanUtils.toBean(createReqVO, OrderDemandDO.class);
        orderDemandMapper.insert(orderDemand);
        // 返回
        return orderDemand.getId();
    }

    @Override
    public void updateOrderDemand(OrderDemandSaveReqVO updateReqVO) {
        // 校验存在
        validateOrderDemandExists(updateReqVO.getId());
        // 更新
        OrderDemandDO updateObj = BeanUtils.toBean(updateReqVO, OrderDemandDO.class);
        orderDemandMapper.updateById(updateObj);
    }

    @Override
    public void deleteOrderDemand(BigDecimal id) {
        // 校验存在
        validateOrderDemandExists(id);
        // 删除
        orderDemandMapper.deleteById(id);
    }

    private void validateOrderDemandExists(BigDecimal id) {
        if (orderDemandMapper.selectById(id) == null) {
            throw exception(ORDER_DEMAND_NOT_EXISTS);
        }
    }

    @Override
    public OrderDemandDO getOrderDemand(BigDecimal id) {
        return orderDemandMapper.selectById(id);
    }

    @Override
    public PageResult<OrderDemandDO> getOrderDemandPage(OrderDemandPageReqVO pageReqVO) {
        return orderDemandMapper.selectPage(pageReqVO);
    }

}