package cn.iocoder.yudao.module.wm.controller.admin.distributiontask;

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

import cn.iocoder.yudao.module.wm.controller.admin.distributiontask.vo.*;
import cn.iocoder.yudao.module.wm.dal.dataobject.distributiontask.DistributionTaskDO;
import cn.iocoder.yudao.module.wm.service.distributiontask.DistributionTaskService;

@Tag(name = "管理后台 - 配送任务下发")
@RestController
@RequestMapping("/wm/distribution-task")
@Validated
public class DistributionTaskController {

    @Resource
    private DistributionTaskService distributionTaskService;

    @PostMapping("/create")
    @Operation(summary = "创建配送任务下发")
    @PreAuthorize("@ss.hasPermission('wm:distribution-task:create')")
    public CommonResult<BigDecimal> createDistributionTask(@Valid @RequestBody DistributionTaskSaveReqVO createReqVO) {
        return success(distributionTaskService.createDistributionTask(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新配送任务下发")
    @PreAuthorize("@ss.hasPermission('wm:distribution-task:update')")
    public CommonResult<Boolean> updateDistributionTask(@Valid @RequestBody DistributionTaskSaveReqVO updateReqVO) {
        distributionTaskService.updateDistributionTask(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除配送任务下发")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('wm:distribution-task:delete')")
    public CommonResult<Boolean> deleteDistributionTask(@RequestParam("id") BigDecimal id) {
        distributionTaskService.deleteDistributionTask(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得配送任务下发")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('wm:distribution-task:query')")
    public CommonResult<DistributionTaskRespVO> getDistributionTask(@RequestParam("id") BigDecimal id) {
        DistributionTaskDO distributionTask = distributionTaskService.getDistributionTask(id);
        return success(BeanUtils.toBean(distributionTask, DistributionTaskRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得配送任务下发分页")
    @PreAuthorize("@ss.hasPermission('wm:distribution-task:query')")
    public CommonResult<PageResult<DistributionTaskRespVO>> getDistributionTaskPage(@Valid DistributionTaskPageReqVO pageReqVO) {
        PageResult<DistributionTaskDO> pageResult = distributionTaskService.getDistributionTaskPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, DistributionTaskRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出配送任务下发 Excel")
    @PreAuthorize("@ss.hasPermission('wm:distribution-task:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportDistributionTaskExcel(@Valid DistributionTaskPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<DistributionTaskDO> list = distributionTaskService.getDistributionTaskPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "配送任务下发.xls", "数据", DistributionTaskRespVO.class,
                        BeanUtils.toBean(list, DistributionTaskRespVO.class));
    }

}