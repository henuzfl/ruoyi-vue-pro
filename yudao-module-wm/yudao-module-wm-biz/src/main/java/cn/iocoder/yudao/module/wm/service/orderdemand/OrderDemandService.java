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

    /**
     * 批量导入订单追溯需求
     * @param importVOList 导入数据列表
     * @return 成功导入条数
     */
    int importOrderDemand(List<OrderDemandImportReqVO> importVOList);

    // 在接口中添加以下方法声明

    /**
     * 从 SAP 同步订单需求数据（生产订单预留）
     * @param reqVO 查询参数
     * @return 同步成功的记录数
     */
    int syncOrderDemandFromSap(SapResbQueryReqVO reqVO);

    /**
     * 仅查询 SAP 预留数据（不保存）
     * @param reqVO 查询参数
     * @return 预留数据列表
     */
    List<OrderDemandFromSapVO> searchResbFromSap(SapResbQueryReqVO reqVO);

}