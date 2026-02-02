package cn.iocoder.yudao.module.wm.service.sapjcoclient;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.wm.controller.admin.orderdemand.vo.OrderDemandPageReqVO;
import cn.iocoder.yudao.module.wm.controller.admin.orderdemand.vo.OrderDemandSaveReqVO;
import cn.iocoder.yudao.module.wm.dal.dataobject.orderdemand.OrderDemandDO;
import java.util.Date;
import java.util.Map;

import javax.validation.Valid;
import java.math.BigDecimal;

/**
 * 订单追溯需求 Service 接口
 *
 * @author 柳文
 */
public interface IPurchaseOrderService {

    /**
     * 查询SAP采购订单
     * 方法签名应尽量清晰，避免直接使用Map传参[citation:6]
     */
    Map<String, Object> findSapPurchaseOrder(String vendorNo, Date startDate, Date endDate, Map<String, Object> otherConditions);
}