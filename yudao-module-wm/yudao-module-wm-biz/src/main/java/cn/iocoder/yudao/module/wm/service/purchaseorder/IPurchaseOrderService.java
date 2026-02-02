package cn.iocoder.yudao.module.wm.service.purchaseorder;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.wm.controller.admin.orderdemand.vo.OrderDemandPageReqVO;
import cn.iocoder.yudao.module.wm.controller.admin.orderdemand.vo.OrderDemandSaveReqVO;
import cn.iocoder.yudao.module.wm.dal.dataobject.orderdemand.OrderDemandDO;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
/**
 * 订单追溯需求 Service 接口
 *
 * @author 柳文
 */
public interface IPurchaseOrderService {
    Map<String, Object> findSapPurchaseOrder(String vendorNo, Date startDate,
                                             Date endDate, Map<String, Object> otherConditions);
}