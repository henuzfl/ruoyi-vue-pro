package cn.iocoder.yudao.module.aps.controller.admin.bomimport;

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

import cn.iocoder.yudao.module.aps.controller.admin.bomimport.vo.*;
import cn.iocoder.yudao.module.aps.dal.dataobject.bomimport.BomImportDO;
import cn.iocoder.yudao.module.aps.service.bomimport.BomImportService;

@Tag(name = "管理后台 - 物料BOM导入")
@RestController
@RequestMapping("/aps/bom-import")
@Validated
public class BomImportController {

    @Resource
    private BomImportService bomImportService;

    @PostMapping("/create")
    @Operation(summary = "创建物料BOM导入")
    @PreAuthorize("@ss.hasPermission('aps:bom-import:create')")
    public CommonResult<Long> createBomImport(@Valid @RequestBody BomImportSaveReqVO createReqVO) {
        return success(bomImportService.createBomImport(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新物料BOM导入")
    @PreAuthorize("@ss.hasPermission('aps:bom-import:update')")
    public CommonResult<Boolean> updateBomImport(@Valid @RequestBody BomImportSaveReqVO updateReqVO) {
        bomImportService.updateBomImport(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除物料BOM导入")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('aps:bom-import:delete')")
    public CommonResult<Boolean> deleteBomImport(@RequestParam("id") Long id) {
        bomImportService.deleteBomImport(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得物料BOM导入")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('aps:bom-import:query')")
    public CommonResult<BomImportRespVO> getBomImport(@RequestParam("id") Long id) {
        BomImportDO bomImport = bomImportService.getBomImport(id);
        return success(BeanUtils.toBean(bomImport, BomImportRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得物料BOM导入分页")
    @PreAuthorize("@ss.hasPermission('aps:bom-import:query')")
    public CommonResult<PageResult<BomImportRespVO>> getBomImportPage(@Valid BomImportPageReqVO pageReqVO) {
        PageResult<BomImportDO> pageResult = bomImportService.getBomImportPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, BomImportRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出物料BOM导入 Excel")
    @PreAuthorize("@ss.hasPermission('aps:bom-import:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportBomImportExcel(@Valid BomImportPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<BomImportDO> list = bomImportService.getBomImportPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "物料BOM导入.xls", "数据", BomImportRespVO.class,
                        BeanUtils.toBean(list, BomImportRespVO.class));
    }

}