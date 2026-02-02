package cn.iocoder.yudao.module.aps.controller.admin.plan;

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

import cn.iocoder.yudao.module.aps.controller.admin.plan.vo.*;
import cn.iocoder.yudao.module.aps.dal.dataobject.plan.PlanDO;
import cn.iocoder.yudao.module.aps.service.plan.PlanService;

@Tag(name = "管理后台 - 设备调度")
@RestController
@RequestMapping("/aps/plan")
@Validated
public class PlanController {

    @Resource
    private PlanService planService;

    @PostMapping("/create")
    @Operation(summary = "创建设备调度")
    @PreAuthorize("@ss.hasPermission('aps:plan:create')")
    public CommonResult<Short> createPlan(@Valid @RequestBody PlanSaveReqVO createReqVO) {
        return success(planService.createPlan(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新设备调度")
    @PreAuthorize("@ss.hasPermission('aps:plan:update')")
    public CommonResult<Boolean> updatePlan(@Valid @RequestBody PlanSaveReqVO updateReqVO) {
        planService.updatePlan(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除设备调度")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('aps:plan:delete')")
    public CommonResult<Boolean> deletePlan(@RequestParam("id") Short id) {
        planService.deletePlan(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得设备调度")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('aps:plan:query')")
    public CommonResult<PlanRespVO> getPlan(@RequestParam("id") Short id) {
        PlanDO plan = planService.getPlan(id);
        return success(BeanUtils.toBean(plan, PlanRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得设备调度分页")
    @PreAuthorize("@ss.hasPermission('aps:plan:query')")
    public CommonResult<PageResult<PlanRespVO>> getPlanPage(@Valid PlanPageReqVO pageReqVO) {
        PageResult<PlanDO> pageResult = planService.getPlanPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, PlanRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出设备调度 Excel")
    @PreAuthorize("@ss.hasPermission('aps:plan:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportPlanExcel(@Valid PlanPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<PlanDO> list = planService.getPlanPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "设备调度.xls", "数据", PlanRespVO.class,
                        BeanUtils.toBean(list, PlanRespVO.class));
    }

    @PostMapping("/calculate")
    @Operation(summary = "执行工序计划存储过程")
    @PreAuthorize("@ss.hasPermission('aps:plan:calculate')")
    public CommonResult<Boolean> calculateStock() {
        try {
            planService.callUpdateStockProcedure();
            return success(true);
        } catch (Exception e) {
            // 这里应该记录日志，并根据业务需求返回适当的错误信息
            return CommonResult.error(500, "调度计算失败: " + e.getMessage());
        }
    }

}