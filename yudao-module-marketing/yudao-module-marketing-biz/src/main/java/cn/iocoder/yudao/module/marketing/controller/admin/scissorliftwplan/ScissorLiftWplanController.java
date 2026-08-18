package cn.iocoder.yudao.module.marketing.controller.admin.scissorliftwplan;

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

import cn.iocoder.yudao.module.marketing.controller.admin.scissorliftwplan.vo.*;
import cn.iocoder.yudao.module.marketing.dal.dataobject.scissorliftwplan.ScissorLiftWplanDO;
import cn.iocoder.yudao.module.marketing.service.scissorliftwplan.ScissorLiftWplanService;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "管理后台 - 高机剪叉周计划")
@RestController
@RequestMapping("/marketing/scissor-lift-wplan")
@Validated
public class ScissorLiftWplanController {

    @Resource
    private ScissorLiftWplanService scissorLiftWplanService;

    @PostMapping("/create")
    @Operation(summary = "创建高机剪叉周计划")
    @PreAuthorize("@ss.hasPermission('marketing:scissor-lift-wplan:create')")
    public CommonResult<Long> createScissorLiftWplan(@Valid @RequestBody ScissorLiftWplanSaveReqVO createReqVO) {
        return success(scissorLiftWplanService.createScissorLiftWplan(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新高机剪叉周计划")
    @PreAuthorize("@ss.hasPermission('marketing:scissor-lift-wplan:update')")
    public CommonResult<Boolean> updateScissorLiftWplan(@Valid @RequestBody ScissorLiftWplanSaveReqVO updateReqVO) {
        scissorLiftWplanService.updateScissorLiftWplan(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除高机剪叉周计划")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('marketing:scissor-lift-wplan:delete')")
    public CommonResult<Boolean> deleteScissorLiftWplan(@RequestParam("id") Long id) {
        scissorLiftWplanService.deleteScissorLiftWplan(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得高机剪叉周计划")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('marketing:scissor-lift-wplan:query')")
    public CommonResult<ScissorLiftWplanRespVO> getScissorLiftWplan(@RequestParam("id") Long id) {
        ScissorLiftWplanDO scissorLiftWplan = scissorLiftWplanService.getScissorLiftWplan(id);
        return success(BeanUtils.toBean(scissorLiftWplan, ScissorLiftWplanRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得高机剪叉周计划分页")
    @PreAuthorize("@ss.hasPermission('marketing:scissor-lift-wplan:query')")
    public CommonResult<PageResult<ScissorLiftWplanRespVO>> getScissorLiftWplanPage(@Valid ScissorLiftWplanPageReqVO pageReqVO) {
        PageResult<ScissorLiftWplanDO> pageResult = scissorLiftWplanService.getScissorLiftWplanPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, ScissorLiftWplanRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出高机剪叉周计划 Excel")
    @PreAuthorize("@ss.hasPermission('marketing:scissor-lift-wplan:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportScissorLiftWplanExcel(@Valid ScissorLiftWplanPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ScissorLiftWplanDO> list = scissorLiftWplanService.getScissorLiftWplanPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "高机剪叉周计划.xls", "数据", ScissorLiftWplanRespVO.class,
                        BeanUtils.toBean(list, ScissorLiftWplanRespVO.class));
    }

    @PostMapping("/import")
    @Operation(summary = "导入高机剪叉周计划")
    @PreAuthorize("@ss.hasPermission('marketing:scissor-lift-wplan:import')")
    @ApiAccessLog(operateType = IMPORT)
    public CommonResult<Boolean> importExcel(
            @RequestParam("file") MultipartFile file,
            @RequestParam("importTime") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate importTime) throws IOException {
        scissorLiftWplanService.importExcel(file, importTime);
        return success(true);
    }

}