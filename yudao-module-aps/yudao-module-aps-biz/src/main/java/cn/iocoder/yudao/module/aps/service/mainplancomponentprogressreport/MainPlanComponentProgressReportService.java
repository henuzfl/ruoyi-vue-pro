package cn.iocoder.yudao.module.aps.service.mainplancomponentprogressreport;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.aps.controller.admin.mainplancomponentprogressreport.vo.MainPlanComponentProgressReportPageReqVO;
import cn.iocoder.yudao.module.aps.controller.admin.mainplancomponentprogressreport.vo.MainPlanComponentProgressReportRespVO;

public interface MainPlanComponentProgressReportService {

    /**
     * 分页查询组件物料需求报表
     */
    PageResult<MainPlanComponentProgressReportRespVO> getComponentProgressReportPage(MainPlanComponentProgressReportPageReqVO pageReqVO);
}