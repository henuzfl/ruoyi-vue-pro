package cn.iocoder.yudao.module.marketing.controller.admin.aerialboomdplan;

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

import cn.iocoder.yudao.module.marketing.controller.admin.aerialboomdplan.vo.*;
import cn.iocoder.yudao.module.marketing.dal.dataobject.aerialboomdplan.AerialBoomDplanDO;
import cn.iocoder.yudao.module.marketing.service.aerialboomdplan.AerialBoomDplanService;

@Tag(name = "管理后台 - 高机臂式日计划")
@RestController
@RequestMapping("/marketing/aerial-boom-dplan")
@Validated
public class AerialBoomDplanController {

    @Resource
    private AerialBoomDplanService aerialBoomDplanService;

    @PostMapping("/create")
    @Operation(summary = "创建高机臂式日计划")
    @PreAuthorize("@ss.hasPermission('marketing:aerial-boom-dplan:create')")
    public CommonResult<Long> createAerialBoomDplan(@Valid @RequestBody AerialBoomDplanSaveReqVO createReqVO) {
        return success(aerialBoomDplanService.createAerialBoomDplan(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新高机臂式日计划")
    @PreAuthorize("@ss.hasPermission('marketing:aerial-boom-dplan:update')")
    public CommonResult<Boolean> updateAerialBoomDplan(@Valid @RequestBody AerialBoomDplanSaveReqVO updateReqVO) {
        aerialBoomDplanService.updateAerialBoomDplan(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除高机臂式日计划")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('marketing:aerial-boom-dplan:delete')")
    public CommonResult<Boolean> deleteAerialBoomDplan(@RequestParam("id") Long id) {
        aerialBoomDplanService.deleteAerialBoomDplan(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得高机臂式日计划")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('marketing:aerial-boom-dplan:query')")
    public CommonResult<AerialBoomDplanRespVO> getAerialBoomDplan(@RequestParam("id") Long id) {
        AerialBoomDplanDO aerialBoomDplan = aerialBoomDplanService.getAerialBoomDplan(id);
        return success(BeanUtils.toBean(aerialBoomDplan, AerialBoomDplanRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得高机臂式日计划分页")
    @PreAuthorize("@ss.hasPermission('marketing:aerial-boom-dplan:query')")
    public CommonResult<PageResult<AerialBoomDplanRespVO>> getAerialBoomDplanPage(@Valid AerialBoomDplanPageReqVO pageReqVO) {
        PageResult<AerialBoomDplanDO> pageResult = aerialBoomDplanService.getAerialBoomDplanPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, AerialBoomDplanRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出高机臂式日计划 Excel")
    @PreAuthorize("@ss.hasPermission('marketing:aerial-boom-dplan:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportAerialBoomDplanExcel(@Valid AerialBoomDplanPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<AerialBoomDplanDO> list = aerialBoomDplanService.getAerialBoomDplanPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "高机臂式日计划.xls", "数据", AerialBoomDplanRespVO.class,
                        BeanUtils.toBean(list, AerialBoomDplanRespVO.class));
    }

}