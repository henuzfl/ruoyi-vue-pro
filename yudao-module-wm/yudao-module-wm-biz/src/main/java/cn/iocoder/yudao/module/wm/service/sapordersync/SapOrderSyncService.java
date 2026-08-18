package cn.iocoder.yudao.module.wm.service.sapordersync;

import cn.iocoder.yudao.module.wm.controller.admin.sapordersync.vo.OrderFromSapVO;
import cn.iocoder.yudao.module.wm.controller.admin.sapordersync.vo.SapOrderQueryReqVO;

import java.util.List;
import java.util.Map;

/**
 * 订单表 - SAP订单信息 Service 接口
 *
 * @author 柳文
 */
public interface SapOrderSyncService {

    /**
     * 从 SAP 同步生产订单数据
     * @param reqVO 查询参数
     * @return 同步成功的记录数
     */
    int syncOrderFromSap(SapOrderQueryReqVO reqVO);

    /**
     * 仅查询 SAP 订单数据（不保存）
     * @param reqVO 查询参数
     * @return 订单数据列表
     */
    List<OrderFromSapVO> searchOrderFromSap(SapOrderQueryReqVO reqVO);

    /**
     * 从 SAP 同步生产订单数据（批量，直接传入 SAP 原始数据）
     *
     * @param rawData SAP 返回的原始数据列表
     * @return 同步成功的记录数
     */
    int syncOrdersFromSapRawData(List<Map<String, Object>> rawData);

}