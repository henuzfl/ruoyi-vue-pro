package cn.iocoder.yudao.module.aps.controller.admin.dataimport;

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

import cn.iocoder.yudao.module.aps.controller.admin.dataimport.vo.*;
import cn.iocoder.yudao.module.aps.dal.dataobject.dataimport.DataImportDO;
import cn.iocoder.yudao.module.aps.service.dataimport.DataImportService;

@Tag(name = "管理后台 - 营销数据导入")
@RestController
@RequestMapping("/aps/data-import")
@Validated
public class DataImportController {

    @Resource
    private DataImportService dataImportService;

    @PostMapping("/create")
    @Operation(summary = "创建营销数据导入")
    @PreAuthorize("@ss.hasPermission('aps:data-import:create')")
    public CommonResult<Short> createDataImport(@Valid @RequestBody DataImportSaveReqVO createReqVO) {
        return success(dataImportService.createDataImport(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新营销数据导入")
    @PreAuthorize("@ss.hasPermission('aps:data-import:update')")
    public CommonResult<Boolean> updateDataImport(@Valid @RequestBody DataImportSaveReqVO updateReqVO) {
        dataImportService.updateDataImport(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除营销数据导入")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('aps:data-import:delete')")
    public CommonResult<Boolean> deleteDataImport(@RequestParam("id") Short id) {
        dataImportService.deleteDataImport(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得营销数据导入")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('aps:data-import:query')")
    public CommonResult<DataImportRespVO> getDataImport(@RequestParam("id") Short id) {
        DataImportDO dataImport = dataImportService.getDataImport(id);
        return success(BeanUtils.toBean(dataImport, DataImportRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得营销数据导入分页")
    @PreAuthorize("@ss.hasPermission('aps:data-import:query')")
    public CommonResult<PageResult<DataImportRespVO>> getDataImportPage(@Valid DataImportPageReqVO pageReqVO) {
        PageResult<DataImportDO> pageResult = dataImportService.getDataImportPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, DataImportRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出营销数据导入 Excel")
    @PreAuthorize("@ss.hasPermission('aps:data-import:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportDataImportExcel(@Valid DataImportPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<DataImportDO> list = dataImportService.getDataImportPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "营销数据导入.xls", "数据", DataImportRespVO.class,
                        BeanUtils.toBean(list, DataImportRespVO.class));
    }

}