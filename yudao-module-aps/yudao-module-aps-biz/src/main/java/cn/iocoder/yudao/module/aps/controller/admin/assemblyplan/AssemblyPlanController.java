package cn.iocoder.yudao.module.aps.controller.admin.assemblyplan;

import org.springframework.format.annotation.DateTimeFormat;
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
import java.time.LocalDate;
import java.util.*;
import java.io.IOException;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;

import cn.iocoder.yudao.framework.apilog.core.annotation.ApiAccessLog;
import static cn.iocoder.yudao.framework.apilog.core.enums.OperateTypeEnum.*;

import cn.iocoder.yudao.module.aps.controller.admin.assemblyplan.vo.*;
import cn.iocoder.yudao.module.aps.dal.dataobject.assemblyplan.AssemblyPlanDO;
import cn.iocoder.yudao.module.aps.service.assemblyplan.AssemblyPlanService;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "管理后台 - 各车间开装计划")
@RestController
@RequestMapping("/aps/assembly-plan")
@Validated
public class AssemblyPlanController {

    @Resource
    private AssemblyPlanService assemblyPlanService;

    @PostMapping("/create")
    @Operation(summary = "创建各车间开装计划")
    @PreAuthorize("@ss.hasPermission('aps:assembly-plan:create')")
    public CommonResult<Long> createAssemblyPlan(@Valid @RequestBody AssemblyPlanSaveReqVO createReqVO) {
        return success(assemblyPlanService.createAssemblyPlan(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新各车间开装计划")
    @PreAuthorize("@ss.hasPermission('aps:assembly-plan:update')")
    public CommonResult<Boolean> updateAssemblyPlan(@Valid @RequestBody AssemblyPlanSaveReqVO updateReqVO) {
        assemblyPlanService.updateAssemblyPlan(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除各车间开装计划")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('aps:assembly-plan:delete')")
    public CommonResult<Boolean> deleteAssemblyPlan(@RequestParam("id") Long id) {
        assemblyPlanService.deleteAssemblyPlan(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得各车间开装计划")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('aps:assembly-plan:query')")
    public CommonResult<AssemblyPlanRespVO> getAssemblyPlan(@RequestParam("id") Long id) {
        AssemblyPlanDO assemblyPlan = assemblyPlanService.getAssemblyPlan(id);
        return success(BeanUtils.toBean(assemblyPlan, AssemblyPlanRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得各车间开装计划分页")
    @PreAuthorize("@ss.hasPermission('aps:assembly-plan:query')")
    public CommonResult<PageResult<AssemblyPlanRespVO>> getAssemblyPlanPage(@Valid AssemblyPlanPageReqVO pageReqVO) {
        PageResult<AssemblyPlanDO> pageResult = assemblyPlanService.getAssemblyPlanPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, AssemblyPlanRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出各车间开装计划 Excel")
    @PreAuthorize("@ss.hasPermission('aps:assembly-plan:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportAssemblyPlanExcel(@Valid AssemblyPlanPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<AssemblyPlanDO> list = assemblyPlanService.getAssemblyPlanPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "各车间开装计划.xls", "数据", AssemblyPlanRespVO.class,
                        BeanUtils.toBean(list, AssemblyPlanRespVO.class));
    }

    @PostMapping("/import")
    @Operation(summary = "导入各车间开装计划")
    @PreAuthorize("@ss.hasPermission('aps:assembly-plan:import')")
    @ApiAccessLog(operateType = IMPORT)
    public CommonResult<Boolean> importExcel(
            @RequestParam("file") MultipartFile file,
            @RequestParam("importTime") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate importTime) throws IOException {
        assemblyPlanService.importExcel(file, importTime);
        return success(true);
    }

}