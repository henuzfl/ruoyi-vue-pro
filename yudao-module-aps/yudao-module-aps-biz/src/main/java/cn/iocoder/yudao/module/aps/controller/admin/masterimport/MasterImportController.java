package cn.iocoder.yudao.module.aps.controller.admin.masterimport;

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

import cn.iocoder.yudao.module.aps.controller.admin.masterimport.vo.*;
import cn.iocoder.yudao.module.aps.dal.dataobject.masterimport.MasterImportDO;
import cn.iocoder.yudao.module.aps.service.masterimport.MasterImportService;

@Tag(name = "管理后台 - 物料主数据导入")
@RestController
@RequestMapping("/aps/master-import")
@Validated
public class MasterImportController {

    @Resource
    private MasterImportService masterImportService;

    @PostMapping("/create")
    @Operation(summary = "创建物料主数据导入")
    @PreAuthorize("@ss.hasPermission('aps:master-import:create')")
    public CommonResult<Long> createMasterImport(@Valid @RequestBody MasterImportSaveReqVO createReqVO) {
        return success(masterImportService.createMasterImport(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新物料主数据导入")
    @PreAuthorize("@ss.hasPermission('aps:master-import:update')")
    public CommonResult<Boolean> updateMasterImport(@Valid @RequestBody MasterImportSaveReqVO updateReqVO) {
        masterImportService.updateMasterImport(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除物料主数据导入")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('aps:master-import:delete')")
    public CommonResult<Boolean> deleteMasterImport(@RequestParam("id") Long id) {
        masterImportService.deleteMasterImport(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得物料主数据导入")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('aps:master-import:query')")
    public CommonResult<MasterImportRespVO> getMasterImport(@RequestParam("id") Long id) {
        MasterImportDO masterImport = masterImportService.getMasterImport(id);
        return success(BeanUtils.toBean(masterImport, MasterImportRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得物料主数据导入分页")
    @PreAuthorize("@ss.hasPermission('aps:master-import:query')")
    public CommonResult<PageResult<MasterImportRespVO>> getMasterImportPage(@Valid MasterImportPageReqVO pageReqVO) {
        PageResult<MasterImportDO> pageResult = masterImportService.getMasterImportPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, MasterImportRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出物料主数据导入 Excel")
    @PreAuthorize("@ss.hasPermission('aps:master-import:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportMasterImportExcel(@Valid MasterImportPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<MasterImportDO> list = masterImportService.getMasterImportPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "物料主数据导入.xls", "数据", MasterImportRespVO.class,
                        BeanUtils.toBean(list, MasterImportRespVO.class));
    }

}