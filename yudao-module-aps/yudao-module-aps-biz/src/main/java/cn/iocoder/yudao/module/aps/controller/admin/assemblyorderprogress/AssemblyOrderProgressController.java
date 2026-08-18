package cn.iocoder.yudao.module.aps.controller.admin.assemblyorderprogress;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.module.aps.controller.admin.assemblyorderprogress.vo.*;
import cn.iocoder.yudao.module.aps.service.assemblyorderprogress.AssemblyOrderProgressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 总成订单进度追踪")
@RestController
@RequestMapping("/aps/assembly-order-progress")
@Validated
@Slf4j
public class AssemblyOrderProgressController {

    @Resource
    private AssemblyOrderProgressService assemblyOrderProgressService;

    @GetMapping("/page")
    @Operation(summary = "获取总成进度分页")
    @PreAuthorize("@ss.hasPermission('aps:assembly-order-progress:query')")
    public CommonResult<PageResult<AssemblyOrderProgressRespVO>> getPage(@Valid AssemblyOrderProgressPageReqVO reqVO) {
        return success(assemblyOrderProgressService.getPage(reqVO));
    }

    @GetMapping("/shortages")
    @Operation(summary = "获取总成下缺料零件列表")
    @PreAuthorize("@ss.hasPermission('aps:assembly-order-progress:query')")
    public CommonResult<List<AssemblyOrderShortageRespVO>> getShortages(
            @RequestParam("materialCode") String materialCode,
            @RequestParam(value = "scheduleTime", required = false) String scheduleTime) {
        return success(assemblyOrderProgressService.getShortages(materialCode, scheduleTime));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出总成进度")
    @PreAuthorize("@ss.hasPermission('aps:assembly-order-progress:export')")
    public void exportExcel(@Valid AssemblyOrderProgressPageReqVO reqVO,
                            HttpServletResponse response) throws IOException {
        List<AssemblyOrderProgressRespVO> list = assemblyOrderProgressService.getExportList(reqVO);
        ExcelUtils.write(response, "总成订单进度追踪.xls", "数据", AssemblyOrderProgressRespVO.class, list);
    }
}