package cn.iocoder.yudao.module.aps.controller.admin.mainplanprogressreport;

import cn.iocoder.yudao.module.aps.controller.admin.mainplanprogressreport.vo.*;
import cn.iocoder.yudao.module.aps.service.mainplanprogressreport.MainPlanProgressReportService;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.metadata.data.ReadCellData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;

import javax.validation.constraints.*;
import javax.validation.*;
import javax.servlet.http.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.math.BigDecimal;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;

import cn.iocoder.yudao.framework.apilog.core.annotation.ApiAccessLog;
import static cn.iocoder.yudao.framework.apilog.core.enums.OperateTypeEnum.*;

import cn.iocoder.yudao.module.aps.controller.admin.mainplan.vo.*;
import cn.iocoder.yudao.module.aps.dal.dataobject.mainplan.MainPlanDO;
import cn.iocoder.yudao.module.aps.service.mainplan.MainPlanService;
import org.springframework.web.multipart.MultipartFile;
import com.alibaba.excel.metadata.Cell; // 注意包路径
import com.alibaba.excel.metadata.data.ReadCellData; // 确保导入


@Tag(name = "管理后台 - 主计划进度跟踪")
@RestController
@RequestMapping("/aps/main-plan-progress-report")
@Validated
@Slf4j
public class MainPlanProgressReportController {

    @Resource
    private MainPlanProgressReportService mainPlanProgressReportService;

//    @GetMapping("/query")
//    @Operation(summary = "获取主计划进度报表")
//    @PreAuthorize("@ss.hasPermission('aps:main-plan-progress-report:query')")
//    public CommonResult<List<MainPlanProgressReportRespVO>> getProgressReport() {
//        List<MainPlanProgressReportRespVO> list = mainPlanProgressReportService.getProgressReport();
//        return success(list);
//    }


    @GetMapping("/overview")
    @Operation(summary = "获取概览卡片数据")
    public CommonResult<DashboardOverviewRespVO> getOverview(
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate,
            @RequestParam(value = "workshop", required = false) String workshop,
            @RequestParam(value = "supplier", required = false) String supplier) {
        return success(mainPlanProgressReportService.getOverview(startDate, endDate, workshop, supplier));
    }

    @GetMapping("/workshop-stats")
    @Operation(summary = "车间完成情况")
    public CommonResult<List<DashboardWorkshopRespVO>> getWorkshopStats(
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate,
            @RequestParam(value = "workshop", required = false) String workshop,
            @RequestParam(value = "supplier", required = false) String supplier) {
        return success(mainPlanProgressReportService.getWorkshopStats(startDate, endDate, workshop, supplier));
    }

    @GetMapping("/supplier-stats")
    @Operation(summary = "供应商交付风险")
    public CommonResult<List<DashboardSupplierRespVO>> getSupplierStats(
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate,
            @RequestParam(value = "workshop", required = false) String workshop,
            @RequestParam(value = "supplier", required = false) String supplier) {
        return success(mainPlanProgressReportService.getSupplierStats(startDate, endDate, workshop, supplier));
    }

    @GetMapping("/material-shortage")
    @Operation(summary = "物料短缺 Top5")
    public CommonResult<List<DashboardMaterialShortageRespVO>> getMaterialShortage(
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate,
            @RequestParam(value = "workshop", required = false) String workshop,
            @RequestParam(value = "supplier", required = false) String supplier) {
        return success(mainPlanProgressReportService.getMaterialShortage(startDate, endDate, workshop, supplier));
    }

    @GetMapping("/order-page")
    @Operation(summary = "订单执行明细分页")
    public CommonResult<PageResult<DashboardOrderRespVO>> getOrderPage(DashboardOrderPageReqVO reqVO) {
        return success(mainPlanProgressReportService.getOrderPage(reqVO));
    }

    @GetMapping("/order-shortages")
    @Operation(summary = "订单缺料零件详情")
    public CommonResult<List<OrderShortageRespVO>> getOrderShortages(@RequestParam("orderNo") String orderNo) {
        return success(mainPlanProgressReportService.getOrderShortages(orderNo));
    }

    @GetMapping("/component-purchases")
    @Operation(summary = "零件采购订单列表")
    public CommonResult<List<ComponentPurchaseRespVO>> getComponentPurchases(@RequestParam("componentCode") String componentCode,
                                                                             @RequestParam("orderNo") String orderNo) {
        return success(mainPlanProgressReportService.getComponentPurchases(componentCode,orderNo));
    }

    @GetMapping("/query")
    @Operation(summary = "获取主计划进度报表（全量）")
    @PreAuthorize("@ss.hasPermission('aps:main-plan-progress-report:query')")
    public CommonResult<List<MainPlanProgressReportRespVO>> getProgressReport() {
        List<MainPlanProgressReportRespVO> list = mainPlanProgressReportService.getProgressReport();
        return success(list);
    }

    @GetMapping("/page")
    @Operation(summary = "获取主计划进度报表分页")
    @PreAuthorize("@ss.hasPermission('aps:main-plan-progress-report:query')")
    public CommonResult<PageResult<MainPlanProgressReportRespVO>> getProgressReportPage(@Valid MainPlanProgressReportPageReqVO pageReqVO) {
        PageResult<MainPlanProgressReportRespVO> pageResult = mainPlanProgressReportService.getProgressReportPage(pageReqVO);
        return success(pageResult);
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出主计划进度报表")
    @PreAuthorize("@ss.hasPermission('aps:main-plan-progress-report:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportMainPlanProgressReport(HttpServletResponse response) throws IOException {
        List<MainPlanProgressReportRespVO> list = mainPlanProgressReportService.getProgressReport();
        ExcelUtils.write(response, "主计划进度报表.xls", "数据", MainPlanProgressReportRespVO.class, list);
    }

}