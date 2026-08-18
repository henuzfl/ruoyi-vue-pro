package cn.iocoder.yudao.module.buyer.service.openorder;

import java.math.BigDecimal;
import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.buyer.controller.admin.openorder.vo.*;
import cn.iocoder.yudao.module.buyer.dal.dataobject.openorder.OpenOrderDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 采购未清订单 Service 接口
 *
 * @author 柳文
 */
public interface OpenOrderService {

    /**
     * 创建采购未清订单
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    BigDecimal createOpenOrder(@Valid OpenOrderSaveReqVO createReqVO);

    /**
     * 更新采购未清订单
     *
     * @param updateReqVO 更新信息
     */
    void updateOpenOrder(@Valid OpenOrderSaveReqVO updateReqVO);

    /**
     * 删除采购未清订单
     *
     * @param id 编号
     */
    void deleteOpenOrder(BigDecimal id);

    /**
     * 获得采购未清订单
     *
     * @param id 编号
     * @return 采购未清订单
     */
    OpenOrderDO getOpenOrder(BigDecimal id);

    /**
     * 获得采购未清订单分页
     *
     * @param pageReqVO 分页查询
     * @return 采购未清订单分页
     */
    PageResult<OpenOrderDO> getOpenOrderPage(OpenOrderPageReqVO pageReqVO);

}