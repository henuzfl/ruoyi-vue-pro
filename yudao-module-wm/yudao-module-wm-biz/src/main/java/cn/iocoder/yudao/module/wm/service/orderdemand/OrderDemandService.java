package cn.iocoder.yudao.module.wm.service.orderdemand;

import java.math.BigDecimal;
import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.wm.controller.admin.orderdemand.vo.*;
import cn.iocoder.yudao.module.wm.dal.dataobject.orderdemand.OrderDemandDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 订单追溯需求 Service 接口
 *
 * @author 柳文
 */
public interface OrderDemandService {

    /**
     * 创建订单追溯需求
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    BigDecimal createOrderDemand(@Valid OrderDemandSaveReqVO createReqVO);

    /**
     * 更新订单追溯需求
     *
     * @param updateReqVO 更新信息
     */
    void updateOrderDemand(@Valid OrderDemandSaveReqVO updateReqVO);

    /**
     * 删除订单追溯需求
     *
     * @param id 编号
     */
    void deleteOrderDemand(BigDecimal id);

    /**
     * 获得订单追溯需求
     *
     * @param id 编号
     * @return 订单追溯需求
     */
    OrderDemandDO getOrderDemand(BigDecimal id);

    /**
     * 获得订单追溯需求分页
     *
     * @param pageReqVO 分页查询
     * @return 订单追溯需求分页
     */
    PageResult<OrderDemandDO> getOrderDemandPage(OrderDemandPageReqVO pageReqVO);

}