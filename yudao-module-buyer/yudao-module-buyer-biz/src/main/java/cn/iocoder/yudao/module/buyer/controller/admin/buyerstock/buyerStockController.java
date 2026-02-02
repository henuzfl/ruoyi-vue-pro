package cn.iocoder.yudao.module.buyer.controller.admin.buyerstock;

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

import cn.iocoder.yudao.module.buyer.controller.admin.buyerstock.vo.*;
import cn.iocoder.yudao.module.buyer.dal.dataobject.buyerstock.buyerStockDO;
import cn.iocoder.yudao.module.buyer.service.buyerstock.buyerStockService;

@Tag(name = "管理后台 - 供应商库存")
@RestController
@RequestMapping("/buyer/buyer-stock")
@Validated
public class buyerStockController {

    @Resource
    private buyerStockService buyerStockService;

    @PostMapping("/create")
    @Operation(summary = "创建供应商库存")
    @PreAuthorize("@ss.hasPermission('buyer:buyer-stock:create')")
    public CommonResult<Long> createbuyerStock(@Valid @RequestBody buyerStockSaveReqVO createReqVO) {
        return success(buyerStockService.createbuyerStock(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新供应商库存")
    @PreAuthorize("@ss.hasPermission('buyer:buyer-stock:update')")
    public CommonResult<Boolean> updatebuyerStock(@Valid @RequestBody buyerStockSaveReqVO updateReqVO) {
        buyerStockService.updatebuyerStock(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除供应商库存")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('buyer:buyer-stock:delete')")
    public CommonResult<Boolean> deletebuyerStock(@RequestParam("id") Long id) {
        buyerStockService.deletebuyerStock(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得供应商库存")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('buyer:buyer-stock:query')")
    public CommonResult<buyerStockRespVO> getbuyerStock(@RequestParam("id") Long id) {
        buyerStockDO buyerStock = buyerStockService.getbuyerStock(id);
        return success(BeanUtils.toBean(buyerStock, buyerStockRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得供应商库存分页")
    @PreAuthorize("@ss.hasPermission('buyer:buyer-stock:query')")
    public CommonResult<PageResult<buyerStockRespVO>> getbuyerStockPage(@Valid buyerStockPageReqVO pageReqVO) {
        PageResult<buyerStockDO> pageResult = buyerStockService.getbuyerStockPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, buyerStockRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出供应商库存 Excel")
    @PreAuthorize("@ss.hasPermission('buyer:buyer-stock:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportbuyerStockExcel(@Valid buyerStockPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<buyerStockDO> list = buyerStockService.getbuyerStockPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "供应商库存.xls", "数据", buyerStockRespVO.class,
                        BeanUtils.toBean(list, buyerStockRespVO.class));
    }

}