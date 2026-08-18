package cn.iocoder.yudao.module.aps.controller.admin.assempart;

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

import cn.iocoder.yudao.module.aps.controller.admin.assempart.vo.*;
import cn.iocoder.yudao.module.aps.dal.dataobject.assempart.AssemPartDO;
import cn.iocoder.yudao.module.aps.service.assempart.AssemPartService;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "管理后台 - 总成与子件关联表管理")
@RestController
@RequestMapping("/aps/assem-part")
@Validated
public class AssemPartController {

    @Resource
    private AssemPartService assemPartService;

    @PostMapping("/create")
    @Operation(summary = "创建总成与子件关联表管理")
    @PreAuthorize("@ss.hasPermission('aps:assem-part:create')")
    public CommonResult<Long> createAssemPart(@Valid @RequestBody AssemPartSaveReqVO createReqVO) {
        return success(assemPartService.createAssemPart(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新总成与子件关联表管理")
    @PreAuthorize("@ss.hasPermission('aps:assem-part:update')")
    public CommonResult<Boolean> updateAssemPart(@Valid @RequestBody AssemPartSaveReqVO updateReqVO) {
        assemPartService.updateAssemPart(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除总成与子件关联表管理")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('aps:assem-part:delete')")
    public CommonResult<Boolean> deleteAssemPart(@RequestParam("id") Long id) {
        assemPartService.deleteAssemPart(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得总成与子件关联表管理")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('aps:assem-part:query')")
    public CommonResult<AssemPartRespVO> getAssemPart(@RequestParam("id") Long id) {
        AssemPartDO assemPart = assemPartService.getAssemPart(id);
        return success(BeanUtils.toBean(assemPart, AssemPartRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得总成与子件关联表管理分页")
    @PreAuthorize("@ss.hasPermission('aps:assem-part:query')")
    public CommonResult<PageResult<AssemPartRespVO>> getAssemPartPage(@Valid AssemPartPageReqVO pageReqVO) {
        PageResult<AssemPartDO> pageResult = assemPartService.getAssemPartPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, AssemPartRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出总成与子件关联表管理 Excel")
    @PreAuthorize("@ss.hasPermission('aps:assem-part:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportAssemPartExcel(@Valid AssemPartPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<AssemPartDO> list = assemPartService.getAssemPartPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "总成与子件关联表管理.xls", "数据", AssemPartRespVO.class,
                        BeanUtils.toBean(list, AssemPartRespVO.class));
    }

    @PostMapping("/import-excel")
    @Operation(summary = "导入总成与子件关联数据")
    @PreAuthorize("@ss.hasPermission('aps:assem-part:import')")
    @ApiAccessLog(operateType = IMPORT)
    public CommonResult<Integer> importAssemPart(@RequestParam("file") MultipartFile file) throws IOException {
        int count = assemPartService.importAssemPart(file);
        return success(count);
    }

}