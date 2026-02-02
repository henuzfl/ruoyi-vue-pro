package cn.iocoder.yudao.module.aps.controller.admin.routeimport;

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

import cn.iocoder.yudao.module.aps.controller.admin.routeimport.vo.*;
import cn.iocoder.yudao.module.aps.dal.dataobject.routeimport.RouteImportDO;
import cn.iocoder.yudao.module.aps.service.routeimport.RouteImportService;

@Tag(name = "管理后台 - 工艺路线导入")
@RestController
@RequestMapping("/aps/route-import")
@Validated
public class RouteImportController {

    @Resource
    private RouteImportService routeImportService;

    @PostMapping("/create")
    @Operation(summary = "创建工艺路线导入")
    @PreAuthorize("@ss.hasPermission('aps:route-import:create')")
    public CommonResult<Long> createRouteImport(@Valid @RequestBody RouteImportSaveReqVO createReqVO) {
        return success(routeImportService.createRouteImport(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新工艺路线导入")
    @PreAuthorize("@ss.hasPermission('aps:route-import:update')")
    public CommonResult<Boolean> updateRouteImport(@Valid @RequestBody RouteImportSaveReqVO updateReqVO) {
        routeImportService.updateRouteImport(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除工艺路线导入")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('aps:route-import:delete')")
    public CommonResult<Boolean> deleteRouteImport(@RequestParam("id") Long id) {
        routeImportService.deleteRouteImport(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得工艺路线导入")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('aps:route-import:query')")
    public CommonResult<RouteImportRespVO> getRouteImport(@RequestParam("id") Long id) {
        RouteImportDO routeImport = routeImportService.getRouteImport(id);
        return success(BeanUtils.toBean(routeImport, RouteImportRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得工艺路线导入分页")
    @PreAuthorize("@ss.hasPermission('aps:route-import:query')")
    public CommonResult<PageResult<RouteImportRespVO>> getRouteImportPage(@Valid RouteImportPageReqVO pageReqVO) {
        PageResult<RouteImportDO> pageResult = routeImportService.getRouteImportPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, RouteImportRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出工艺路线导入 Excel")
    @PreAuthorize("@ss.hasPermission('aps:route-import:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportRouteImportExcel(@Valid RouteImportPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<RouteImportDO> list = routeImportService.getRouteImportPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "工艺路线导入.xls", "数据", RouteImportRespVO.class,
                        BeanUtils.toBean(list, RouteImportRespVO.class));
    }

}