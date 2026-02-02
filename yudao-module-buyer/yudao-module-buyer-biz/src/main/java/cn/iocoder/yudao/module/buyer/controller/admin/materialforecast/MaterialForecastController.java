package cn.iocoder.yudao.module.buyer.controller.admin.materialforecast;

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

import cn.iocoder.yudao.module.buyer.controller.admin.materialforecast.vo.*;
import cn.iocoder.yudao.module.buyer.dal.dataobject.materialforecast.MaterialForecastDO;
import cn.iocoder.yudao.module.buyer.service.materialforecast.MaterialForecastService;

@Tag(name = "管理后台 - 营销材料备料预测")
@RestController
@RequestMapping("/buyer/material-forecast")
@Validated
public class MaterialForecastController {

    @Resource
    private MaterialForecastService materialForecastService;

    @PostMapping("/create")
    @Operation(summary = "创建营销材料备料预测")
    @PreAuthorize("@ss.hasPermission('buyer:material-forecast:create')")
    public CommonResult<BigDecimal> createMaterialForecast(@Valid @RequestBody MaterialForecastSaveReqVO createReqVO) {
        return success(materialForecastService.createMaterialForecast(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新营销材料备料预测")
    @PreAuthorize("@ss.hasPermission('buyer:material-forecast:update')")
    public CommonResult<Boolean> updateMaterialForecast(@Valid @RequestBody MaterialForecastSaveReqVO updateReqVO) {
        materialForecastService.updateMaterialForecast(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除营销材料备料预测")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('buyer:material-forecast:delete')")
    public CommonResult<Boolean> deleteMaterialForecast(@RequestParam("id") BigDecimal id) {
        materialForecastService.deleteMaterialForecast(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得营销材料备料预测")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('buyer:material-forecast:query')")
    public CommonResult<MaterialForecastRespVO> getMaterialForecast(@RequestParam("id") BigDecimal id) {
        MaterialForecastDO materialForecast = materialForecastService.getMaterialForecast(id);
        return success(BeanUtils.toBean(materialForecast, MaterialForecastRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得营销材料备料预测分页")
    @PreAuthorize("@ss.hasPermission('buyer:material-forecast:query')")
    public CommonResult<PageResult<MaterialForecastRespVO>> getMaterialForecastPage(@Valid MaterialForecastPageReqVO pageReqVO) {
        PageResult<MaterialForecastDO> pageResult = materialForecastService.getMaterialForecastPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, MaterialForecastRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出营销材料备料预测 Excel")
    @PreAuthorize("@ss.hasPermission('buyer:material-forecast:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportMaterialForecastExcel(@Valid MaterialForecastPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<MaterialForecastDO> list = materialForecastService.getMaterialForecastPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "营销材料备料预测.xls", "数据", MaterialForecastRespVO.class,
                        BeanUtils.toBean(list, MaterialForecastRespVO.class));
    }

}