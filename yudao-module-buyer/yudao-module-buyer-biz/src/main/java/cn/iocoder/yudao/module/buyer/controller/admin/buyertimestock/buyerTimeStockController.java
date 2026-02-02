package cn.iocoder.yudao.module.buyer.controller.admin.buyertimestock;

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

import cn.iocoder.yudao.module.buyer.controller.admin.buyertimestock.vo.*;
import cn.iocoder.yudao.module.buyer.dal.dataobject.buyertimestock.buyerTimeStockDO;
import cn.iocoder.yudao.module.buyer.service.buyertimestock.buyerTimeStockService;

@Tag(name = "管理后台 - 实时库存")
@RestController
@RequestMapping("/buyer/buyer-time-stock")
@Validated
public class buyerTimeStockController {

    @Resource
    private buyerTimeStockService buyerTimeStockService;

    @PostMapping("/create")
    @Operation(summary = "创建实时库存")
    @PreAuthorize("@ss.hasPermission('buyer:buyer-time-stock:create')")
    public CommonResult<Long> createbuyerTimeStock(@Valid @RequestBody buyerTimeStockSaveReqVO createReqVO) {
        return success(buyerTimeStockService.createbuyerTimeStock(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新实时库存")
    @PreAuthorize("@ss.hasPermission('buyer:buyer-time-stock:update')")
    public CommonResult<Boolean> updatebuyerTimeStock(@Valid @RequestBody buyerTimeStockSaveReqVO updateReqVO) {
        buyerTimeStockService.updatebuyerTimeStock(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除实时库存")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('buyer:buyer-time-stock:delete')")
    public CommonResult<Boolean> deletebuyerTimeStock(@RequestParam("id") Long id) {
        buyerTimeStockService.deletebuyerTimeStock(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得实时库存")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('buyer:buyer-time-stock:query')")
    public CommonResult<buyerTimeStockRespVO> getbuyerTimeStock(@RequestParam("id") Long id) {
        buyerTimeStockDO buyerTimeStock = buyerTimeStockService.getbuyerTimeStock(id);
        return success(BeanUtils.toBean(buyerTimeStock, buyerTimeStockRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得实时库存分页")
    @PreAuthorize("@ss.hasPermission('buyer:buyer-time-stock:query')")
    public CommonResult<PageResult<buyerTimeStockRespVO>> getbuyerTimeStockPage(@Valid buyerTimeStockPageReqVO pageReqVO) {
        PageResult<buyerTimeStockDO> pageResult = buyerTimeStockService.getbuyerTimeStockPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, buyerTimeStockRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出实时库存 Excel")
    @PreAuthorize("@ss.hasPermission('buyer:buyer-time-stock:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportbuyerTimeStockExcel(@Valid buyerTimeStockPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<buyerTimeStockDO> list = buyerTimeStockService.getbuyerTimeStockPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "实时库存.xls", "数据", buyerTimeStockRespVO.class,
                        BeanUtils.toBean(list, buyerTimeStockRespVO.class));
    }

}