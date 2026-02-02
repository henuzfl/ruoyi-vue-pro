package cn.iocoder.yudao.module.wm.controller.admin.materialkittingtool;

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

import cn.iocoder.yudao.module.wm.controller.admin.materialkittingtool.vo.*;
import cn.iocoder.yudao.module.wm.dal.dataobject.materialkittingtool.MaterialKittingToolDO;
import cn.iocoder.yudao.module.wm.service.materialkittingtool.MaterialKittingToolService;

@Tag(name = "管理后台 - 齐套工具")
@RestController
@RequestMapping("/wm/material-kitting-tool")
@Validated
public class MaterialKittingToolController {

    @Resource
    private MaterialKittingToolService materialKittingToolService;


    @GetMapping("/get")
    @Operation(summary = "获得齐套工具")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('wm:material-kitting-tool:query')")
    public CommonResult<MaterialKittingToolRespVO> getMaterialKittingTool(@RequestParam("id") BigDecimal id) {
        MaterialKittingToolDO materialKittingTool = materialKittingToolService.getMaterialKittingTool(id);
        return success(BeanUtils.toBean(materialKittingTool, MaterialKittingToolRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得齐套工具分页")
    @PreAuthorize("@ss.hasPermission('wm:material-kitting-tool:query')")
    public CommonResult<PageResult<MaterialKittingToolRespVO>> getMaterialKittingToolPage(@Valid MaterialKittingToolPageReqVO pageReqVO) {
        PageResult<MaterialKittingToolDO> pageResult = materialKittingToolService.selectMaterialKittingToolByParams(pageReqVO);

        // 将 DO 转换为 RespVO
        PageResult<MaterialKittingToolRespVO> voPageResult = BeanUtils.toBean(pageResult, MaterialKittingToolRespVO.class);

        return success(voPageResult);
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出齐套工具 Excel")
    @PreAuthorize("@ss.hasPermission('wm:material-kitting-tool:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportMaterialKittingToolExcel(@Valid MaterialKittingToolPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<MaterialKittingToolDO> list = materialKittingToolService.selectMaterialKittingToolByParams(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "齐套工具.xls", "数据", MaterialKittingToolRespVO.class,
                        BeanUtils.toBean(list, MaterialKittingToolRespVO.class));
    }

}