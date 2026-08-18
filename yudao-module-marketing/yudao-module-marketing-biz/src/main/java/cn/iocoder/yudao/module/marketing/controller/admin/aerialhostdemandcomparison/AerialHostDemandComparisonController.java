package cn.iocoder.yudao.module.marketing.controller.admin.aerialhostdemandcomparison;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.apilog.core.annotation.ApiAccessLog;
import cn.iocoder.yudao.module.marketing.controller.admin.aerialhostdemandcomparison.vo.AerialHostDemandComparisonPageReqVO;
import cn.iocoder.yudao.module.marketing.controller.admin.aerialhostdemandcomparison.vo.AerialHostDemandComparisonRespVO;
import cn.iocoder.yudao.module.marketing.controller.admin.aerialhostdemandcomparison.vo.AerialHostDemandComparisonWeekRespVO;
import cn.iocoder.yudao.module.marketing.service.aerialhostdemandcomparison.AerialHostDemandComparisonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.framework.apilog.core.enums.OperateTypeEnum.EXPORT;

@Tag(name = "管理后台 - 主机需求对比分析")
@RestController
@RequestMapping("/marketing/aerial-host-demand-comparison")
@Validated
public class AerialHostDemandComparisonController {

    @Resource
    private AerialHostDemandComparisonService service;

    @GetMapping("/page")
    @Operation(summary = "获得主机需求对比分页")
    @PreAuthorize("@ss.hasPermission('marketing:aerial-host-demand-comparison:query')")
    public CommonResult<PageResult<AerialHostDemandComparisonRespVO>> getComparisonPage(@Valid AerialHostDemandComparisonPageReqVO pageReqVO) {
        return success(service.getComparisonPage(pageReqVO));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出主机需求对比 Excel")
    @PreAuthorize("@ss.hasPermission('marketing:host-requirement-comparison:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportExcel(@Valid AerialHostDemandComparisonPageReqVO pageReqVO,
                            HttpServletResponse response) throws IOException {
        List<AerialHostDemandComparisonRespVO> list = service.getAllForExport(pageReqVO);
        List<List<String>> head = new ArrayList<>();
        head.add(Collections.singletonList("物料编码"));
        head.add(Collections.singletonList("特力编码"));
        head.add(Collections.singletonList("物料描述"));
        head.add(Collections.singletonList("产品型号"));
        head.add(Collections.singletonList("上线计划日期"));
        head.add(Collections.singletonList("当前批次数量"));
        head.add(Collections.singletonList("对比批次数量"));
        head.add(Collections.singletonList("差异"));

        List<List<Object>> data = new ArrayList<>();
        for (AerialHostDemandComparisonRespVO vo : list) {
            List<Object> row = new ArrayList<>();
            row.add(vo.getMaterialCode());
            row.add(vo.getTeliCode());
            row.add(vo.getMaterialDesc());
            row.add(vo.getProductModel());
            row.add(vo.getOnlinePlan());
            row.add(vo.getCurrentQty());
            row.add(vo.getCompareQty());
            row.add(vo.getDiffQty());
            data.add(row);
        }

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("高机主机需求对比.xlsx", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-Disposition", "attachment;filename*=utf-8''" + fileName);
        com.alibaba.excel.EasyExcel.write(response.getOutputStream()).head(head).sheet("数据").doWrite(data);
    }

    @GetMapping("/available-dates")
    @Operation(summary = "获取可选的导入日期列表")
    @PreAuthorize("@ss.hasPermission('marketing:aerial-host-demand-comparison:query')")
    public CommonResult<List<String>> getAvailableDates(@RequestParam(required = false) String plate) {
        return success(service.getAvailableImportDates(plate));
    }




    @GetMapping("/day-comparison")
    @Operation(summary = "获取日级别主机需求对比数据（全量）")
    @PreAuthorize("@ss.hasPermission('marketing:aerial-host-demand-comparison:query')")
    public CommonResult<List<AerialHostDemandComparisonRespVO>> getDayComparison(
            @Valid AerialHostDemandComparisonPageReqVO reqVO) {
        return success(service.getDayComparisonData(reqVO));
    }

    @GetMapping("/week-comparison")
    @Operation(summary = "获取周级别主机需求对比数据（全量）")
    @PreAuthorize("@ss.hasPermission('marketing:aerial-host-demand-comparison:query')")
    public CommonResult<List<AerialHostDemandComparisonWeekRespVO>> getWeekComparison(
            @Valid AerialHostDemandComparisonPageReqVO reqVO) {
        return success(service.getWeekComparisonData(reqVO));
    }

    @GetMapping("/available-plates")
    @Operation(summary = "获取可用的板块列表（从日计划表去重）")
    @PreAuthorize("@ss.hasPermission('marketing:aerial-host-demand-comparison:query')")
    public CommonResult<List<String>> getAvailablePlates() {
        return success(service.getAvailablePlates());
    }
}