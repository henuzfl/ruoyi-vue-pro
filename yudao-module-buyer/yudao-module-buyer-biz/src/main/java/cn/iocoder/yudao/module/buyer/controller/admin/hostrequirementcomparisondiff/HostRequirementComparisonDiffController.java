package cn.iocoder.yudao.module.buyer.controller.admin.hostrequirementcomparisondiff;

import cn.iocoder.yudao.framework.apilog.core.annotation.ApiAccessLog;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.module.buyer.controller.admin.hostrequirementcomparison.vo.HostRequirementComparisonPageReqVO;
import cn.iocoder.yudao.module.buyer.controller.admin.hostrequirementcomparison.vo.HostRequirementComparisonRespVO;
import cn.iocoder.yudao.module.buyer.controller.admin.hostrequirementcomparisondiff.vo.HostRequirementComparisonDiffReqVO;
import cn.iocoder.yudao.module.buyer.controller.admin.hostrequirementcomparisondiff.vo.HostRequirementComparisonDiffRespVO;
import cn.iocoder.yudao.module.buyer.controller.admin.hostrequirementcomparisondiff.vo.HostRequirementComparisonDiffVO;
import cn.iocoder.yudao.module.buyer.service.hostrequirementcomparison.HostRequirementComparisonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

import static cn.iocoder.yudao.framework.apilog.core.enums.OperateTypeEnum.EXPORT;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 主机需求对比宽表")
@RestController
@RequestMapping("/buyer/host-requirement-comparison-diff")
@Validated
public class HostRequirementComparisonDiffController {

    @Resource
    private HostRequirementComparisonService hostRequirementComparisonService;


    @GetMapping("/page")
    @Operation(summary = "获得主机需求对比差异分页")
    @PreAuthorize("@ss.hasPermission('buyer:host-requirement-comparison:query')")
    public CommonResult<PageResult<HostRequirementComparisonDiffVO>> getComparisonDiff(@Valid HostRequirementComparisonDiffReqVO diffReqVO) {
        PageResult<HostRequirementComparisonDiffVO> pageResult = hostRequirementComparisonService.getComparisonDiff(diffReqVO);
        return success(pageResult);
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出主机需求对比差异 Excel")
    @PreAuthorize("@ss.hasPermission('buyer:host-requirement-comparison:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportExcel(@Valid HostRequirementComparisonDiffReqVO reqVO,
                            HttpServletResponse response) throws IOException {
        List<HostRequirementComparisonDiffVO> list = hostRequirementComparisonService.getAllDiffForExport(reqVO);
        ExcelUtils.write(response, "主机需求差异对比.xls", "差异数据", HostRequirementComparisonDiffVO.class, list);
    }

    @GetMapping("/available-dates")
    @Operation(summary = "获取可选的导入日期列表")
    @PreAuthorize("@ss.hasPermission('buyer:host-requirement-comparison:query')")
    public CommonResult<List<String>> getAvailableDates() {
        List<String> dates = hostRequirementComparisonService.getAvailableImportDates();
        return success(dates);
    }
}