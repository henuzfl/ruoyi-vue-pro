package cn.iocoder.yudao.module.marketing.controller.admin.aerialboombom;

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

import cn.iocoder.yudao.module.marketing.controller.admin.aerialboombom.vo.*;
import cn.iocoder.yudao.module.marketing.dal.dataobject.aerialboombom.AerialBoomBomDO;
import cn.iocoder.yudao.module.marketing.service.aerialboombom.AerialBoomBomService;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "管理后台 - 高机臂式/剪叉BOM物料清单")
@RestController
@RequestMapping("/marketing/aerial-boom-bom")
@Validated
public class AerialBoomBomController {

    @Resource
    private AerialBoomBomService aerialBoomBomService;

    @PostMapping("/create")
    @Operation(summary = "创建高机臂式/剪叉BOM物料清单")
    @PreAuthorize("@ss.hasPermission('marketing:aerial-boom-bom:create')")
    public CommonResult<Long> createAerialBoomBom(@Valid @RequestBody AerialBoomBomSaveReqVO createReqVO) {
        return success(aerialBoomBomService.createAerialBoomBom(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新高机臂式/剪叉BOM物料清单")
    @PreAuthorize("@ss.hasPermission('marketing:aerial-boom-bom:update')")
    public CommonResult<Boolean> updateAerialBoomBom(@Valid @RequestBody AerialBoomBomSaveReqVO updateReqVO) {
        aerialBoomBomService.updateAerialBoomBom(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除高机臂式/剪叉BOM物料清单")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('marketing:aerial-boom-bom:delete')")
    public CommonResult<Boolean> deleteAerialBoomBom(@RequestParam("id") Long id) {
        aerialBoomBomService.deleteAerialBoomBom(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得高机臂式/剪叉BOM物料清单")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('marketing:aerial-boom-bom:query')")
    public CommonResult<AerialBoomBomRespVO> getAerialBoomBom(@RequestParam("id") Long id) {
        AerialBoomBomDO aerialBoomBom = aerialBoomBomService.getAerialBoomBom(id);
        return success(BeanUtils.toBean(aerialBoomBom, AerialBoomBomRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得高机臂式/剪叉BOM物料清单分页")
    @PreAuthorize("@ss.hasPermission('marketing:aerial-boom-bom:query')")
    public CommonResult<PageResult<AerialBoomBomRespVO>> getAerialBoomBomPage(@Valid AerialBoomBomPageReqVO pageReqVO) {
        PageResult<AerialBoomBomDO> pageResult = aerialBoomBomService.getAerialBoomBomPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, AerialBoomBomRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出高机臂式/剪叉BOM物料清单 Excel")
    @PreAuthorize("@ss.hasPermission('marketing:aerial-boom-bom:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportAerialBoomBomExcel(@Valid AerialBoomBomPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<AerialBoomBomDO> list = aerialBoomBomService.getAerialBoomBomPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "高机臂式/剪叉BOM物料清单.xls", "数据", AerialBoomBomRespVO.class,
                        BeanUtils.toBean(list, AerialBoomBomRespVO.class));
    }

    @PostMapping("/import")
    @Operation(summary = "导入高机臂式/剪叉BOM物料清单")
    @PreAuthorize("@ss.hasPermission('marketing:aerial-boom-bom:import')")
    @ApiAccessLog(operateType = IMPORT)
    public CommonResult<Boolean> importExcel(
            @RequestParam("file") MultipartFile file,
            @RequestParam("importTime") @org.springframework.format.annotation.DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate importTime) throws IOException {
        aerialBoomBomService.importBomExcel(file, importTime);
        return success(true);
    }

}