package cn.iocoder.yudao.module.aps.service.mainplanprogressreport;

import java.math.BigDecimal;
import java.util.*;
import javax.validation.*;

import cn.iocoder.yudao.module.aps.controller.admin.mainplanprogressreport.vo.*;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

/**
 * 主计划 Service 接口
 *
 * @author 柳文
 */
public interface MainPlanProgressReportService {

    /**
     * // 全量查询（用于导出）
     */
    List<MainPlanProgressReportRespVO> getProgressReport();

    // 分页查询
    PageResult<MainPlanProgressReportRespVO> getProgressReportPage(MainPlanProgressReportPageReqVO pageReqVO);

    DashboardOverviewRespVO getOverview(String startDate, String endDate, String workshop, String supplier);
    List<DashboardWorkshopRespVO> getWorkshopStats(String startDate, String endDate, String workshop, String supplier);
    List<DashboardSupplierRespVO> getSupplierStats(String startDate, String endDate, String workshop, String supplier);
    List<DashboardMaterialShortageRespVO> getMaterialShortage(String startDate, String endDate, String workshop, String supplier);
    PageResult<DashboardOrderRespVO> getOrderPage(DashboardOrderPageReqVO reqVO);
    List<OrderShortageRespVO> getOrderShortages(String orderNo);
    List<ComponentPurchaseRespVO> getComponentPurchases(String componentCode,String orderNo);
}