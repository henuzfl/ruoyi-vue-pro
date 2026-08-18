package cn.iocoder.yudao.module.aps.controller.admin.mainplancomponentprogressreport;

import cn.iocoder.yudao.framework.apilog.core.annotation.ApiAccessLog;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.module.aps.controller.admin.mainplancomponentprogressreport.vo.MainPlanComponentProgressReportPageReqVO;
import cn.iocoder.yudao.module.aps.controller.admin.mainplancomponentprogressreport.vo.MainPlanComponentProgressReportRespVO;
import cn.iocoder.yudao.module.aps.service.mainplancomponentprogressreport.MainPlanComponentProgressReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

import static cn.iocoder.yudao.framework.apilog.core.enums.OperateTypeEnum.EXPORT;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 组件物料需求报表")
@RestController
@RequestMapping("/aps/main-plan-component-progress-report")
@Validated
public class MainPlanComponentProgressReportController {

    @Resource
    private MainPlanComponentProgressReportService service;

    @GetMapping("/page")
    @Operation(summary = "获取组件物料需求报表分页")
    @PreAuthorize("@ss.hasPermission('aps:main-plan-component-progress-report:query')")
    public CommonResult<PageResult<MainPlanComponentProgressReportRespVO>> getPage(@Valid MainPlanComponentProgressReportPageReqVO pageReqVO) {
        PageResult<MainPlanComponentProgressReportRespVO> pageResult = service.getComponentProgressReportPage(pageReqVO);
        return success(pageResult);
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出组件物料需求报表 Excel")
    @PreAuthorize("@ss.hasPermission('aps:main-plan-component-progress-report:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportExcel(@Valid MainPlanComponentProgressReportPageReqVO pageReqVO,
                            HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        PageResult<MainPlanComponentProgressReportRespVO> pageResult = service.getComponentProgressReportPage(pageReqVO);
        ExcelUtils.write(response, "组件物料需求报表.xls", "数据", MainPlanComponentProgressReportRespVO.class, pageResult.getList());
    }
}