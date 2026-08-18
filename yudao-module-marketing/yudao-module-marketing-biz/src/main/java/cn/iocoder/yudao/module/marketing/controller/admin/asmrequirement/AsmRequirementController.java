package cn.iocoder.yudao.module.marketing.controller.admin.asmrequirement;

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

import cn.iocoder.yudao.module.marketing.controller.admin.asmrequirement.vo.*;
import cn.iocoder.yudao.module.marketing.dal.dataobject.asmrequirement.AsmRequirementDO;
import cn.iocoder.yudao.module.marketing.service.asmrequirement.AsmRequirementService;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "管理后台 - 营销总成需求")
@RestController
@RequestMapping("/marketing/asm-requirement")
@Validated
public class AsmRequirementController {

    @Resource
    private AsmRequirementService asmRequirementService;

    @PostMapping("/create")
    @Operation(summary = "创建营销总成需求")
    @PreAuthorize("@ss.hasPermission('marketing:asm-requirement:create')")
    public CommonResult<Long> createAsmRequirement(@Valid @RequestBody AsmRequirementSaveReqVO createReqVO) {
        return success(asmRequirementService.createAsmRequirement(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新营销总成需求")
    @PreAuthorize("@ss.hasPermission('marketing:asm-requirement:update')")
    public CommonResult<Boolean> updateAsmRequirement(@Valid @RequestBody AsmRequirementSaveReqVO updateReqVO) {
        asmRequirementService.updateAsmRequirement(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除营销总成需求")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('marketing:asm-requirement:delete')")
    public CommonResult<Boolean> deleteAsmRequirement(@RequestParam("id") Long id) {
        asmRequirementService.deleteAsmRequirement(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得营销总成需求")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('marketing:asm-requirement:query')")
    public CommonResult<AsmRequirementRespVO> getAsmRequirement(@RequestParam("id") Long id) {
        AsmRequirementDO asmRequirement = asmRequirementService.getAsmRequirement(id);
        return success(BeanUtils.toBean(asmRequirement, AsmRequirementRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得营销总成需求分页")
    @PreAuthorize("@ss.hasPermission('marketing:asm-requirement:query')")
    public CommonResult<PageResult<AsmRequirementRespVO>> getAsmRequirementPage(@Valid AsmRequirementPageReqVO pageReqVO) {
        PageResult<AsmRequirementDO> pageResult = asmRequirementService.getAsmRequirementPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, AsmRequirementRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出营销总成需求 Excel")
    @PreAuthorize("@ss.hasPermission('marketing:asm-requirement:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportAsmRequirementExcel(@Valid AsmRequirementPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<AsmRequirementDO> list = asmRequirementService.getAsmRequirementPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "营销总成需求.xls", "数据", AsmRequirementRespVO.class,
                        BeanUtils.toBean(list, AsmRequirementRespVO.class));
    }

    @PostMapping("/import")
    @Operation(summary = "导入营销总成需求")
    @PreAuthorize("@ss.hasPermission('marketing:asm-requirement:import')")
    @ApiAccessLog(operateType = IMPORT)
    public CommonResult<Boolean> importExcel(@RequestParam("file") MultipartFile file) throws IOException {
        asmRequirementService.importExcel(file);
        return success(true);
    }

}