package cn.iocoder.yudao.module.aps.controller.admin.materialprogresstrack;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.aps.controller.admin.materialprogresstrack.vo.MaterialChildrenRespVO;
import cn.iocoder.yudao.module.aps.controller.admin.materialprogresstrack.vo.MaterialSummaryRespVO;
import cn.iocoder.yudao.module.aps.service.materialprogresstrack.MaterialProgressTrackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 物料进度跟踪套表")
@RestController
@RequestMapping("/aps/material-progresstrack")
@Validated
public class MaterialProgressTrackController {

    @Resource
    private MaterialProgressTrackService materialProgressTrackService;

    @GetMapping("/material-summary")
    @Operation(summary = "主表分页查询")
    public CommonResult<PageResult<MaterialSummaryRespVO>> getMaterialSummary(
            @RequestParam(value = "startDate", required = false) String startDate,
            @RequestParam(value = "endDate", required = false) String endDate,
            @RequestParam(value = "workshop", required = false) String workshop,
            @RequestParam(value = "materialCode", required = false) String materialCode,
            @RequestParam(value = "materialDesc", required = false) String materialDesc,
            @RequestParam(value = "onlyAbnormal", required = false) Boolean onlyAbnormal,
            @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {
        return success(materialProgressTrackService.getMaterialSummary(
                startDate, endDate, workshop, materialCode, materialDesc, onlyAbnormal, pageNo, pageSize));
    }

    @GetMapping("/material-children")
    @Operation(summary = "子件明细及供应商")
    public CommonResult<List<MaterialChildrenRespVO>> getMaterialChildren(
            @RequestParam("materialCode") String materialCode,
            @RequestParam("workshop") String workshop,
            @RequestParam("demandMonth") String demandMonth) {
        return success(materialProgressTrackService.getMaterialChildren(materialCode, workshop, demandMonth));
    }
}