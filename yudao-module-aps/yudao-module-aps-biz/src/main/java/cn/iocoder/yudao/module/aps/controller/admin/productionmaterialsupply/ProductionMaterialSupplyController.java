package cn.iocoder.yudao.module.aps.controller.admin.productionmaterialsupply;

import cn.iocoder.yudao.framework.apilog.core.annotation.ApiAccessLog;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.module.aps.controller.admin.productionmaterialsupply.vo.ProductionMaterialSupplyPageReqVO;
import cn.iocoder.yudao.module.aps.controller.admin.productionmaterialsupply.vo.ProductionMaterialSupplyRespVO;
import cn.iocoder.yudao.module.aps.service.productionmaterialsupply.ProductionMaterialSupplyService;
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

import static cn.iocoder.yudao.framework.apilog.core.enums.OperateTypeEnum.EXPORT;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 生产订单物料供需")
@RestController
@RequestMapping("/aps/production-material-supply")
@Validated
public class ProductionMaterialSupplyController {

    @Resource
    private ProductionMaterialSupplyService service;

    @GetMapping("/page")
    @Operation(summary = "获取生产订单物料供需分页")
    @PreAuthorize("@ss.hasPermission('aps:production-material-supply:query')")
    public CommonResult<PageResult<ProductionMaterialSupplyRespVO>> getPage(
            @Valid ProductionMaterialSupplyPageReqVO pageReqVO) {
        return success(service.getPage(pageReqVO));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出生产订单物料供需 Excel")
    @PreAuthorize("@ss.hasPermission('aps:production-material-supply:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportExcel(@Valid ProductionMaterialSupplyPageReqVO pageReqVO,
                            HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        PageResult<ProductionMaterialSupplyRespVO> result = service.getPage(pageReqVO);
        ExcelUtils.write(response, "生产订单物料供需.xls", "数据",
                ProductionMaterialSupplyRespVO.class, result.getList());
    }
}
