package cn.iocoder.yudao.module.marketing.controller.admin.scissorliftdplan;

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

import cn.iocoder.yudao.module.marketing.controller.admin.scissorliftdplan.vo.*;
import cn.iocoder.yudao.module.marketing.dal.dataobject.scissorliftdplan.ScissorLiftDplanDO;
import cn.iocoder.yudao.module.marketing.service.scissorliftdplan.ScissorLiftDplanService;

@Tag(name = "管理后台 - 高机剪叉日计划")
@RestController
@RequestMapping("/marketing/scissor-lift-dplan")
@Validated
public class ScissorLiftDplanController {

    @Resource
    private ScissorLiftDplanService scissorLiftDplanService;

    @PostMapping("/create")
    @Operation(summary = "创建高机剪叉日计划")
    @PreAuthorize("@ss.hasPermission('marketing:scissor-lift-dplan:create')")
    public CommonResult<Long> createScissorLiftDplan(@Valid @RequestBody ScissorLiftDplanSaveReqVO createReqVO) {
        return success(scissorLiftDplanService.createScissorLiftDplan(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新高机剪叉日计划")
    @PreAuthorize("@ss.hasPermission('marketing:scissor-lift-dplan:update')")
    public CommonResult<Boolean> updateScissorLiftDplan(@Valid @RequestBody ScissorLiftDplanSaveReqVO updateReqVO) {
        scissorLiftDplanService.updateScissorLiftDplan(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除高机剪叉日计划")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('marketing:scissor-lift-dplan:delete')")
    public CommonResult<Boolean> deleteScissorLiftDplan(@RequestParam("id") Long id) {
        scissorLiftDplanService.deleteScissorLiftDplan(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得高机剪叉日计划")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('marketing:scissor-lift-dplan:query')")
    public CommonResult<ScissorLiftDplanRespVO> getScissorLiftDplan(@RequestParam("id") Long id) {
        ScissorLiftDplanDO scissorLiftDplan = scissorLiftDplanService.getScissorLiftDplan(id);
        return success(BeanUtils.toBean(scissorLiftDplan, ScissorLiftDplanRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得高机剪叉日计划分页")
    @PreAuthorize("@ss.hasPermission('marketing:scissor-lift-dplan:query')")
    public CommonResult<PageResult<ScissorLiftDplanRespVO>> getScissorLiftDplanPage(@Valid ScissorLiftDplanPageReqVO pageReqVO) {
        PageResult<ScissorLiftDplanDO> pageResult = scissorLiftDplanService.getScissorLiftDplanPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, ScissorLiftDplanRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出高机剪叉日计划 Excel")
    @PreAuthorize("@ss.hasPermission('marketing:scissor-lift-dplan:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportScissorLiftDplanExcel(@Valid ScissorLiftDplanPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ScissorLiftDplanDO> list = scissorLiftDplanService.getScissorLiftDplanPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "高机剪叉日计划.xls", "数据", ScissorLiftDplanRespVO.class,
                        BeanUtils.toBean(list, ScissorLiftDplanRespVO.class));
    }

}