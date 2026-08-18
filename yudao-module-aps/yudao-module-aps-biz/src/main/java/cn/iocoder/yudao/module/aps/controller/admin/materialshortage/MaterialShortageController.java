package cn.iocoder.yudao.module.aps.controller.admin.materialshortage;

import cn.iocoder.yudao.framework.apilog.core.annotation.ApiAccessLog;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.module.aps.controller.admin.materialshortage.vo.*;
import cn.iocoder.yudao.module.aps.service.materialshortage.MaterialShortageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.framework.apilog.core.enums.OperateTypeEnum.EXPORT;

@Tag(name = "管理后台 - APS 物料缺口汇总")
@RestController
@RequestMapping("/aps/material-shortage")
@Validated
public class MaterialShortageController {

    @Resource
    private MaterialShortageService materialShortageService;

    @GetMapping("/summary-page")
    @Operation(summary = "获取物料缺口汇总分页")
    @PreAuthorize("@ss.hasPermission('aps:material-shortage:query')")
    public CommonResult<PageResult<MaterialShortageSummaryRespVO>> getShortageSummaryPage(
            @Valid MaterialShortagePageReqVO pageReqVO) {
        return success(materialShortageService.getShortageSummaryPage(pageReqVO));
    }

    @GetMapping("/details")
    @Operation(summary = "获取某个成品的缺口明细")
    @Parameter(name = "mainMaterialNo", description = "成品物料编码", required = true)
    @PreAuthorize("@ss.hasPermission('aps:material-shortage:query')")
    public CommonResult<List<MaterialShortageDetailRespVO>> getShortageDetails(
            @RequestParam("mainMaterialNo") String mainMaterialNo) {
        return success(materialShortageService.getShortageDetails(mainMaterialNo));
    }

    @PostMapping("/refresh")
    @Operation(summary = "刷新缺口数据")
    @PreAuthorize("@ss.hasPermission('aps:material-shortage:refresh')")
    public CommonResult<Boolean> refreshShortageData() {
        materialShortageService.refreshShortageData();
        return success(true);
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出物料缺口汇总Excel")
    @PreAuthorize("@ss.hasPermission('aps:material-shortage:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportShortageExcel(@Valid MaterialShortagePageReqVO pageReqVO,
                                    HttpServletResponse response) throws IOException {
        List<MaterialShortageSummaryRespVO> list = materialShortageService.exportShortageSummary(pageReqVO);
        ExcelUtils.write(response, "物料缺口汇总.xls", "缺口汇总",
                MaterialShortageSummaryRespVO.class, list);
    }

    @GetMapping("/component-summary-page")
    @Operation(summary = "获取组件缺口汇总分页")
    @PreAuthorize("@ss.hasPermission('aps:component-shortage:query')")
    public CommonResult<PageResult<MaterialShortageComponentSummaryRespVO>> getComponentShortagePage(
            @Valid MaterialShortageComponentPageReqVO pageReqVO) {
        return success(materialShortageService.getComponentShortagePage(pageReqVO));
    }

    @GetMapping("/export-component-excel")
    @Operation(summary = "导出组件缺口汇总Excel")
    @PreAuthorize("@ss.hasPermission('aps:component-shortage:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportComponentShortageExcel(@Valid MaterialShortageComponentPageReqVO pageReqVO,
                                             HttpServletResponse response) throws IOException {
        List<MaterialShortageComponentSummaryRespVO> list = materialShortageService.exportComponentShortage(pageReqVO);
        ExcelUtils.write(response, "组件缺口汇总.xls", "组件缺口汇总",
                MaterialShortageComponentSummaryRespVO.class, list);
    }

}