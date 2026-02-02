package cn.iocoder.yudao.module.aps.controller.admin.mainplan;

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

import cn.iocoder.yudao.module.aps.controller.admin.mainplan.vo.*;
import cn.iocoder.yudao.module.aps.dal.dataobject.mainplan.MainPlanDO;
import cn.iocoder.yudao.module.aps.service.mainplan.MainPlanService;

@Tag(name = "管理后台 - 主计划")
@RestController
@RequestMapping("/aps/main-plan")
@Validated
public class MainPlanController {

    @Resource
    private MainPlanService mainPlanService;

    @PostMapping("/create")
    @Operation(summary = "创建主计划")
    @PreAuthorize("@ss.hasPermission('aps:main-plan:create')")
    public CommonResult<BigDecimal> createMainPlan(@Valid @RequestBody MainPlanSaveReqVO createReqVO) {
        return success(mainPlanService.createMainPlan(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新主计划")
    @PreAuthorize("@ss.hasPermission('aps:main-plan:update')")
    public CommonResult<Boolean> updateMainPlan(@Valid @RequestBody MainPlanSaveReqVO updateReqVO) {
        mainPlanService.updateMainPlan(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除主计划")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('aps:main-plan:delete')")
    public CommonResult<Boolean> deleteMainPlan(@RequestParam("id") BigDecimal id) {
        mainPlanService.deleteMainPlan(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得主计划")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('aps:main-plan:query')")
    public CommonResult<MainPlanRespVO> getMainPlan(@RequestParam("id") BigDecimal id) {
        MainPlanDO mainPlan = mainPlanService.getMainPlan(id);
        return success(BeanUtils.toBean(mainPlan, MainPlanRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得主计划分页")
    @PreAuthorize("@ss.hasPermission('aps:main-plan:query')")
    public CommonResult<PageResult<MainPlanRespVO>> getMainPlanPage(@Valid MainPlanPageReqVO pageReqVO) {
        PageResult<MainPlanDO> pageResult = mainPlanService.getMainPlanPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, MainPlanRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出主计划 Excel")
    @PreAuthorize("@ss.hasPermission('aps:main-plan:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportMainPlanExcel(@Valid MainPlanPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<MainPlanDO> list = mainPlanService.getMainPlanPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "主计划.xls", "数据", MainPlanRespVO.class,
                        BeanUtils.toBean(list, MainPlanRespVO.class));
    }

}