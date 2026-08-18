package cn.iocoder.yudao.module.aps.service.order;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.aps.controller.admin.order.vo.*;
import cn.iocoder.yudao.module.aps.dal.dataobject.order.OrderDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 订单表 - SAP订单信息 Service 接口
 *
 * @author 柳文
 */
public interface OrderService {

    /**
     * 创建订单表 - SAP订单信息
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createOrder(@Valid OrderSaveReqVO createReqVO);

    /**
     * 更新订单表 - SAP订单信息
     *
     * @param updateReqVO 更新信息
     */
    void updateOrder(@Valid OrderSaveReqVO updateReqVO);

    /**
     * 删除订单表 - SAP订单信息
     *
     * @param id 编号
     */
    void deleteOrder(Long id);

    /**
     * 获得订单表 - SAP订单信息
     *
     * @param id 编号
     * @return 订单表 - SAP订单信息
     */
    OrderDO getOrder(Long id);

    /**
     * 获得订单表 - SAP订单信息分页
     *
     * @param pageReqVO 分页查询
     * @return 订单表 - SAP订单信息分页
     */
    PageResult<OrderDO> getOrderPage(OrderPageReqVO pageReqVO);


}