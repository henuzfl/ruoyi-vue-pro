package cn.iocoder.yudao.module.wm.controller.admin.orderdemand;

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

import cn.iocoder.yudao.module.wm.controller.admin.orderdemand.vo.*;
import cn.iocoder.yudao.module.wm.dal.dataobject.orderdemand.OrderDemandDO;
import cn.iocoder.yudao.module.wm.service.orderdemand.OrderDemandService;

@Tag(name = "管理后台 - 订单追溯需求")
@RestController
@RequestMapping("/wm/order-demand")
@Validated
public class OrderDemandController {

    @Resource
    private OrderDemandService orderDemandService;

    @PostMapping("/create")
    @Operation(summary = "创建订单追溯需求")
    @PreAuthorize("@ss.hasPermission('wm:order-demand:create')")
    public CommonResult<BigDecimal> createOrderDemand(@Valid @RequestBody OrderDemandSaveReqVO createReqVO) {
        return success(orderDemandService.createOrderDemand(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新订单追溯需求")
    @PreAuthorize("@ss.hasPermission('wm:order-demand:update')")
    public CommonResult<Boolean> updateOrderDemand(@Valid @RequestBody OrderDemandSaveReqVO updateReqVO) {
        orderDemandService.updateOrderDemand(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除订单追溯需求")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('wm:order-demand:delete')")
    public CommonResult<Boolean> deleteOrderDemand(@RequestParam("id") BigDecimal id) {
        orderDemandService.deleteOrderDemand(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得订单追溯需求")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('wm:order-demand:query')")
    public CommonResult<OrderDemandRespVO> getOrderDemand(@RequestParam("id") BigDecimal id) {
        OrderDemandDO orderDemand = orderDemandService.getOrderDemand(id);
        return success(BeanUtils.toBean(orderDemand, OrderDemandRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得订单追溯需求分页")
    @PreAuthorize("@ss.hasPermission('wm:order-demand:query')")
    public CommonResult<PageResult<OrderDemandRespVO>> getOrderDemandPage(@Valid OrderDemandPageReqVO pageReqVO) {
        PageResult<OrderDemandDO> pageResult = orderDemandService.getOrderDemandPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, OrderDemandRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出订单追溯需求 Excel")
    @PreAuthorize("@ss.hasPermission('wm:order-demand:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportOrderDemandExcel(@Valid OrderDemandPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<OrderDemandDO> list = orderDemandService.getOrderDemandPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "订单追溯需求.xls", "数据", OrderDemandRespVO.class,
                        BeanUtils.toBean(list, OrderDemandRespVO.class));
    }

}