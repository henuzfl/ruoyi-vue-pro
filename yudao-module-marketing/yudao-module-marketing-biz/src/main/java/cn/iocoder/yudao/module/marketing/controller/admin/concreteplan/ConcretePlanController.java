package cn.iocoder.yudao.module.marketing.controller.admin.concreteplan;

import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;

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

import cn.iocoder.yudao.module.marketing.controller.admin.concreteplan.vo.*;
import cn.iocoder.yudao.module.marketing.dal.dataobject.concreteplan.ConcretePlanDO;
import cn.iocoder.yudao.module.marketing.service.concreteplan.ConcretePlanService;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "管理后台 - 混凝土计划需求")
@RestController
@RequestMapping("/marketing/concrete-plan")
@Validated
public class ConcretePlanController {

    @Resource
    private ConcretePlanService concretePlanService;

    @PostMapping("/create")
    @Operation(summary = "创建混凝土计划需求")
    @PreAuthorize("@ss.hasPermission('marketing:concrete-plan:create')")
    public CommonResult<Long> createConcretePlan(@Valid @RequestBody ConcretePlanSaveReqVO createReqVO) {
        return success(concretePlanService.createConcretePlan(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新混凝土计划需求")
    @PreAuthorize("@ss.hasPermission('marketing:concrete-plan:update')")
    public CommonResult<Boolean> updateConcretePlan(@Valid @RequestBody ConcretePlanSaveReqVO updateReqVO) {
        concretePlanService.updateConcretePlan(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除混凝土计划需求")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('marketing:concrete-plan:delete')")
    public CommonResult<Boolean> deleteConcretePlan(@RequestParam("id") Long id) {
        concretePlanService.deleteConcretePlan(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得混凝土计划需求")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('marketing:concrete-plan:query')")
    public CommonResult<ConcretePlanRespVO> getConcretePlan(@RequestParam("id") Long id) {
        ConcretePlanDO concretePlan = concretePlanService.getConcretePlan(id);
        return success(BeanUtils.toBean(concretePlan, ConcretePlanRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得混凝土计划需求分页")
    @PreAuthorize("@ss.hasPermission('marketing:concrete-plan:query')")
    public CommonResult<PageResult<ConcretePlanRespVO>> getConcretePlanPage(@Valid ConcretePlanPageReqVO pageReqVO) {
        PageResult<ConcretePlanDO> pageResult = concretePlanService.getConcretePlanPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, ConcretePlanRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出混凝土计划需求 Excel")
    @PreAuthorize("@ss.hasPermission('marketing:concrete-plan:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportConcretePlanExcel(@Valid ConcretePlanPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ConcretePlanDO> list = concretePlanService.getConcretePlanPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "混凝土计划需求.xls", "数据", ConcretePlanRespVO.class,
                        BeanUtils.toBean(list, ConcretePlanRespVO.class));
    }

    @PostMapping("/import")
    @Operation(summary = "导入混凝土计划需求")
    @PreAuthorize("@ss.hasPermission('marketing:concrete-plan:import')")
    @ApiAccessLog(operateType = IMPORT)
    public CommonResult<Boolean> importExcel(
            @RequestParam("file") MultipartFile file,
            @RequestParam("importTime") @org.springframework.format.annotation.DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate importTime) throws IOException {
        concretePlanService.importExcel(file, importTime);
        return success(true);
    }

}