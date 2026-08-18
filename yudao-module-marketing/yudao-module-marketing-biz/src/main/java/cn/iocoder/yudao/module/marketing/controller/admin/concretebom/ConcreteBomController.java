package cn.iocoder.yudao.module.marketing.controller.admin.concretebom;

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

import cn.iocoder.yudao.module.marketing.controller.admin.concretebom.vo.*;
import cn.iocoder.yudao.module.marketing.dal.dataobject.concretebom.ConcreteBomDO;
import cn.iocoder.yudao.module.marketing.service.concretebom.ConcreteBomService;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "管理后台 - 混凝土BOM")
@RestController
@RequestMapping("/marketing/concrete-bom")
@Validated
public class ConcreteBomController {

    @Resource
    private ConcreteBomService concreteBomService;

    @PostMapping("/create")
    @Operation(summary = "创建混凝土BOM")
    @PreAuthorize("@ss.hasPermission('marketing:concrete-bom:create')")
    public CommonResult<Long> createConcreteBom(@Valid @RequestBody ConcreteBomSaveReqVO createReqVO) {
        return success(concreteBomService.createConcreteBom(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新混凝土BOM")
    @PreAuthorize("@ss.hasPermission('marketing:concrete-bom:update')")
    public CommonResult<Boolean> updateConcreteBom(@Valid @RequestBody ConcreteBomSaveReqVO updateReqVO) {
        concreteBomService.updateConcreteBom(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除混凝土BOM")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('marketing:concrete-bom:delete')")
    public CommonResult<Boolean> deleteConcreteBom(@RequestParam("id") Long id) {
        concreteBomService.deleteConcreteBom(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得混凝土BOM")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('marketing:concrete-bom:query')")
    public CommonResult<ConcreteBomRespVO> getConcreteBom(@RequestParam("id") Long id) {
        ConcreteBomDO concreteBom = concreteBomService.getConcreteBom(id);
        return success(BeanUtils.toBean(concreteBom, ConcreteBomRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得混凝土BOM分页")
    @PreAuthorize("@ss.hasPermission('marketing:concrete-bom:query')")
    public CommonResult<PageResult<ConcreteBomRespVO>> getConcreteBomPage(@Valid ConcreteBomPageReqVO pageReqVO) {
        PageResult<ConcreteBomDO> pageResult = concreteBomService.getConcreteBomPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, ConcreteBomRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出混凝土BOM Excel")
    @PreAuthorize("@ss.hasPermission('marketing:concrete-bom:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportConcreteBomExcel(@Valid ConcreteBomPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ConcreteBomDO> list = concreteBomService.getConcreteBomPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "混凝土BOM.xls", "数据", ConcreteBomRespVO.class,
                        BeanUtils.toBean(list, ConcreteBomRespVO.class));
    }

    @PostMapping("/import")
    @Operation(summary = "导入混凝土BOM")
    @PreAuthorize("@ss.hasPermission('marketing:concrete-bom:import')")
    @ApiAccessLog(operateType = IMPORT)
    public CommonResult<Boolean> importExcel(
            @RequestParam("file") MultipartFile file,
            @RequestParam("importTime") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate importTime) throws IOException {
        concreteBomService.importExcel(file, importTime);
        return success(true);
    }


    @GetMapping("/compare")
    @Operation(summary = "混凝土BOM差异对比")
    @PreAuthorize("@ss.hasPermission('marketing:concrete-bom:compare')")
    public CommonResult<List<ConcreteBomCompareRespVO>> compareDifference() {
        return success(concreteBomService.compareDifference());
    }

}