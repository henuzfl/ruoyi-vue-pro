package cn.iocoder.yudao.module.buyer.controller.admin.materialplanmrp;

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

import cn.iocoder.yudao.module.buyer.controller.admin.materialplanmrp.vo.*;
import cn.iocoder.yudao.module.buyer.dal.dataobject.materialplanmrp.MaterialPlanMrpDO;
import cn.iocoder.yudao.module.buyer.service.materialplanmrp.MaterialPlanMrpService;

@Tag(name = "管理后台 - 买家需求预测")
@RestController
@RequestMapping("/buyer/material-plan-mrp")
@Validated
public class MaterialPlanMrpController {

    @Resource
    private MaterialPlanMrpService materialPlanMrpService;

    @GetMapping("/page")
    @Operation(summary = "获得买家需求预测分页")
    @PreAuthorize("@ss.hasPermission('buyer:material-plan-mrp:query')")
    public CommonResult<PageResult<MaterialPlanMrpRespVO>> getMaterialPlanMrpPage(@Valid MaterialPlanMrpPageReqVO pageReqVO) {
        PageResult<MaterialPlanMrpDO> pageResult = materialPlanMrpService.getMaterialPlanMrpPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, MaterialPlanMrpRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出买家需求预测 Excel")
    @PreAuthorize("@ss.hasPermission('buyer:material-plan-mrp:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportMaterialPlanMrpExcel(@Valid MaterialPlanMrpPageReqVO pageReqVO,
                                           HttpServletResponse response) throws IOException {
        List<MaterialPlanMrpDO> list = materialPlanMrpService.getMaterialPlanMrpExport(pageReqVO);
        // 导出 Excel
        ExcelUtils.write(response, "买家需求预测.xls", "数据", MaterialPlanMrpRespVO.class,
                BeanUtils.toBean(list, MaterialPlanMrpRespVO.class));
    }
}