package cn.iocoder.yudao.module.aps.service.ordercomponentprogressreport;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.aps.controller.admin.ordercomponentprogressreport.vo.OrderComponentProgressReportPageReqVO;
import cn.iocoder.yudao.module.aps.controller.admin.ordercomponentprogressreport.vo.OrderComponentProgressReportRespVO;

public interface OrderComponentProgressReportService {

    /**
     * 分页查询订单组件需求进度报表
     */
    PageResult<OrderComponentProgressReportRespVO> getComponentProgressReportPage(OrderComponentProgressReportPageReqVO pageReqVO);
}
