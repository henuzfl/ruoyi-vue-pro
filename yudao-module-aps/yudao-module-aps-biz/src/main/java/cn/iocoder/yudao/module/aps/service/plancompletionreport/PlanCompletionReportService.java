package cn.iocoder.yudao.module.aps.service.plancompletionreport;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.aps.controller.admin.plancompletionreport.vo.PlanCompletionReportPageReqVO;
import cn.iocoder.yudao.module.aps.controller.admin.plancompletionreport.vo.PlanCompletionReportRespVO;

public interface PlanCompletionReportService {

    /**
     * 分页查询计划完成情况报表
     */
    PageResult<PlanCompletionReportRespVO> getReportPage(PlanCompletionReportPageReqVO pageReqVO);
}