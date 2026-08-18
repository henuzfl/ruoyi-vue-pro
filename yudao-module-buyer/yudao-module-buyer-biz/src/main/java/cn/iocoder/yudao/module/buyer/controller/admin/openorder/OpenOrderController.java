package cn.iocoder.yudao.module.buyer.controller.admin.openorder;

import cn.iocoder.yudao.module.buyer.controller.admin.openorder.vo.OpenOrderPageReqVO;
import cn.iocoder.yudao.module.buyer.controller.admin.openorder.vo.OpenOrderRespVO;
import cn.iocoder.yudao.module.buyer.controller.admin.openorder.vo.OpenOrderSaveReqVO;
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

import cn.iocoder.yudao.module.buyer.dal.dataobject.openorder.OpenOrderDO;
import cn.iocoder.yudao.module.buyer.service.openorder.OpenOrderService;

@Tag(name = "管理后台 - 采购未清订单")
@RestController
@RequestMapping("/buyer/open-order")
@Validated
public class OpenOrderController {

    @Resource
    private OpenOrderService openOrderService;

    @PostMapping("/create")
    @Operation(summary = "创建采购未清订单")
    @PreAuthorize("@ss.hasPermission('buyer:open-order:create')")
    public CommonResult<BigDecimal> createOpenOrder(@Valid @RequestBody OpenOrderSaveReqVO createReqVO) {
        return success(openOrderService.createOpenOrder(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新采购未清订单")
    @PreAuthorize("@ss.hasPermission('buyer:open-order:update')")
    public CommonResult<Boolean> updateOpenOrder(@Valid @RequestBody OpenOrderSaveReqVO updateReqVO) {
        openOrderService.updateOpenOrder(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除采购未清订单")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('buyer:open-order:delete')")
    public CommonResult<Boolean> deleteOpenOrder(@RequestParam("id") BigDecimal id) {
        openOrderService.deleteOpenOrder(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得采购未清订单")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('buyer:open-order:query')")
    public CommonResult<OpenOrderRespVO> getOpenOrder(@RequestParam("id") BigDecimal id) {
        OpenOrderDO openOrder = openOrderService.getOpenOrder(id);
        return success(BeanUtils.toBean(openOrder, OpenOrderRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得采购未清订单分页")
    @PreAuthorize("@ss.hasPermission('buyer:open-order:query')")
    public CommonResult<PageResult<OpenOrderRespVO>> getOpenOrderPage(@Valid OpenOrderPageReqVO pageReqVO) {
        PageResult<OpenOrderDO> pageResult = openOrderService.getOpenOrderPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, OpenOrderRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出采购未清订单 Excel")
    @PreAuthorize("@ss.hasPermission('buyer:open-order:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportOpenOrderExcel(@Valid OpenOrderPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<OpenOrderDO> list = openOrderService.getOpenOrderPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "采购未清订单.xls", "数据", OpenOrderRespVO.class,
                        BeanUtils.toBean(list, OpenOrderRespVO.class));
    }

}