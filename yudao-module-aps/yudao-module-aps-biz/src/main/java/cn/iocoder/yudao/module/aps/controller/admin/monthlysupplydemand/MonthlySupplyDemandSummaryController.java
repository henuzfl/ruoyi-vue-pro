package cn.iocoder.yudao.module.aps.controller.admin.monthlysupplydemand;

import cn.iocoder.yudao.framework.apilog.core.annotation.ApiAccessLog;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.module.aps.controller.admin.monthlysupplydemand.vo.MonthlySupplyDemandSummaryPageReqVO;
import cn.iocoder.yudao.module.aps.controller.admin.monthlysupplydemand.vo.MonthlySupplyDemandSummaryRespVO;
import cn.iocoder.yudao.module.aps.service.monthlysupplydemand.MonthlySupplyDemandSummaryService;
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

/**
 * 管理后台 - 月度供需总览表 Controller
 *
 * @author 柳文
 */
@Tag(name = "管理后台 - 月度供需总览")
@RestController
@RequestMapping("/aps/monthly-supply-demand-summary")
@Validated
public class MonthlySupplyDemandSummaryController {

    @Resource
    private MonthlySupplyDemandSummaryService service;

    @GetMapping("/page")
    @Operation(summary = "获得月度供需总览表分页")
    @PreAuthorize("@ss.hasPermission('aps:monthly-supply-demand-summary:query')")
    public CommonResult<PageResult<MonthlySupplyDemandSummaryRespVO>> getPage(@Valid MonthlySupplyDemandSummaryPageReqVO pageReqVO) {
        return success(service.getPage(pageReqVO));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出月度供需总览表 Excel")
    @PreAuthorize("@ss.hasPermission('aps:monthly-supply-demand-summary:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportExcel(@Valid MonthlySupplyDemandSummaryPageReqVO pageReqVO,
                            HttpServletResponse response) throws IOException {
        // 不分页，导出全部
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<MonthlySupplyDemandSummaryRespVO> list = service.getPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "月度供需总览表.xls", "数据", MonthlySupplyDemandSummaryRespVO.class, list);
    }
}