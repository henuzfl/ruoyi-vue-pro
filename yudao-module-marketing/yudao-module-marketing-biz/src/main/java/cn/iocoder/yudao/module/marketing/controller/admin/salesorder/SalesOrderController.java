package cn.iocoder.yudao.module.marketing.controller.admin.salesorder;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.module.marketing.controller.admin.salesorder.vo.SalesOrderPageReqVO;
import cn.iocoder.yudao.module.marketing.controller.admin.salesorder.vo.SalesOrderRespVO;
import cn.iocoder.yudao.module.marketing.controller.admin.salesorder.vo.SalesOrderSaveReqVO;
import cn.iocoder.yudao.module.marketing.dal.dataobject.salesorder.SalesOrderDO;
import cn.iocoder.yudao.module.marketing.service.salesorder.SalesOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 销售订单")
@RestController
@RequestMapping("/marketing/sales-order")
@Validated
@Slf4j
public class SalesOrderController {

    @Resource
    private SalesOrderService salesOrderService;

    @PostMapping("/create")
    @Operation(summary = "创建销售订单")
    @PreAuthorize("@ss.hasPermission('marketing:sales-order:create')")
    public CommonResult<Long> create(@Valid @RequestBody SalesOrderSaveReqVO reqVO) {
        return success(salesOrderService.createSalesOrder(reqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新销售订单")
    @PreAuthorize("@ss.hasPermission('marketing:sales-order:update')")
    public CommonResult<Boolean> update(@Valid @RequestBody SalesOrderSaveReqVO reqVO) {
        salesOrderService.updateSalesOrder(reqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除销售订单")
    @PreAuthorize("@ss.hasPermission('marketing:sales-order:delete')")
    public CommonResult<Boolean> delete(@RequestParam Long id) {
        salesOrderService.deleteSalesOrder(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获取销售订单详情")
    @PreAuthorize("@ss.hasPermission('marketing:sales-order:query')")
    public CommonResult<SalesOrderRespVO> get(@RequestParam Long id) {
        SalesOrderDO entity = salesOrderService.getSalesOrder(id);
        return success(BeanUtils.toBean(entity, SalesOrderRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "分页查询")
    @PreAuthorize("@ss.hasPermission('marketing:sales-order:query')")
    public CommonResult<PageResult<SalesOrderRespVO>> page(@Valid SalesOrderPageReqVO reqVO) {
        PageResult<SalesOrderDO> pageResult = salesOrderService.getSalesOrderPage(reqVO);
        return success(BeanUtils.toBean(pageResult, SalesOrderRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出 Excel")
    @PreAuthorize("@ss.hasPermission('marketing:sales-order:export')")
    public void exportExcel(@Valid SalesOrderPageReqVO reqVO, HttpServletResponse response) throws IOException {
        List<SalesOrderDO> list = salesOrderService.getExportList(reqVO);
        ExcelUtils.write(response, "销售订单.xls", "数据", SalesOrderRespVO.class,
                BeanUtils.toBean(list, SalesOrderRespVO.class));
    }

    @PostMapping("/import")
    @Operation(summary = "导入 Excel")
    @PreAuthorize("@ss.hasPermission('marketing:sales-order:import')")
    public CommonResult<Boolean> importExcel(@RequestParam("file") MultipartFile file) throws IOException {
        salesOrderService.importExcel(file);
        return success(true);
    }
}