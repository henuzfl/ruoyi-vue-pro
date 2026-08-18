package cn.iocoder.yudao.module.aps.controller.admin.plancompletionreport;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.apilog.core.annotation.ApiAccessLog;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.module.aps.controller.admin.plancompletionreport.vo.PlanCompletionReportPageReqVO;
import cn.iocoder.yudao.module.aps.controller.admin.plancompletionreport.vo.PlanCompletionReportRespVO;
import cn.iocoder.yudao.module.aps.service.plancompletionreport.PlanCompletionReportService;
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

@Tag(name = "管理后台 - 计划完成情况报表")
@RestController
@RequestMapping("/aps/plan-completion-report")
@Validated
public class PlanCompletionReportController {

    @Resource
    private PlanCompletionReportService planCompletionReportService;

    @GetMapping("/page")
    @Operation(summary = "获得计划完成情况报表分页")
    @PreAuthorize("@ss.hasPermission('aps:plan-completion-report:query')")
    public CommonResult<PageResult<PlanCompletionReportRespVO>> getReportPage(@Valid PlanCompletionReportPageReqVO pageReqVO) {
        PageResult<PlanCompletionReportRespVO> pageResult = planCompletionReportService.getReportPage(pageReqVO);
        return success(pageResult);
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出计划完成情况报表 Excel")
    @PreAuthorize("@ss.hasPermission('aps:plan-completion-report:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportExcel(@Valid PlanCompletionReportPageReqVO pageReqVO,
                            HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE); // 不分页，导出所有
        PageResult<PlanCompletionReportRespVO> pageResult = planCompletionReportService.getReportPage(pageReqVO);
        List<PlanCompletionReportRespVO> list = pageResult.getList();

        // 导出 Excel
        ExcelUtils.write(response, "计划完成情况报表.xls", "数据", PlanCompletionReportRespVO.class, list);
    }
}