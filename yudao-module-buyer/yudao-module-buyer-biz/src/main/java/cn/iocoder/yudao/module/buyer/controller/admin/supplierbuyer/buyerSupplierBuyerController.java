package cn.iocoder.yudao.module.buyer.controller.admin.supplierbuyer;

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

import cn.iocoder.yudao.module.buyer.controller.admin.supplierbuyer.vo.*;
import cn.iocoder.yudao.module.buyer.dal.dataobject.supplierbuyer.buyerSupplierBuyerDO;
import cn.iocoder.yudao.module.buyer.service.supplierbuyer.buyerSupplierBuyerService;

@Tag(name = "管理后台 - 物料供应商采购员对应")
@RestController
@RequestMapping("/buyer/buyer-supplier-buyer")
@Validated
public class buyerSupplierBuyerController {

    @Resource
    private buyerSupplierBuyerService buyerSupplierBuyerService;

    @PostMapping("/create")
    @Operation(summary = "创建物料供应商采购员对应")
    @PreAuthorize("@ss.hasPermission('buyer:buyer-supplier-buyer:create')")
    public CommonResult<Long> createbuyerSupplierBuyer(@Valid @RequestBody buyerSupplierBuyerSaveReqVO createReqVO) {
        return success(buyerSupplierBuyerService.createbuyerSupplierBuyer(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新物料供应商采购员对应")
    @PreAuthorize("@ss.hasPermission('buyer:buyer-supplier-buyer:update')")
    public CommonResult<Boolean> updatebuyerSupplierBuyer(@Valid @RequestBody buyerSupplierBuyerSaveReqVO updateReqVO) {
        buyerSupplierBuyerService.updatebuyerSupplierBuyer(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除物料供应商采购员对应")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('buyer:buyer-supplier-buyer:delete')")
    public CommonResult<Boolean> deletebuyerSupplierBuyer(@RequestParam("id") Long id) {
        buyerSupplierBuyerService.deletebuyerSupplierBuyer(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得物料供应商采购员对应")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('buyer:buyer-supplier-buyer:query')")
    public CommonResult<buyerSupplierBuyerRespVO> getbuyerSupplierBuyer(@RequestParam("id") Long id) {
        buyerSupplierBuyerDO buyerSupplierBuyer = buyerSupplierBuyerService.getbuyerSupplierBuyer(id);
        return success(BeanUtils.toBean(buyerSupplierBuyer, buyerSupplierBuyerRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得物料供应商采购员对应分页")
    @PreAuthorize("@ss.hasPermission('buyer:buyer-supplier-buyer:query')")
    public CommonResult<PageResult<buyerSupplierBuyerRespVO>> getbuyerSupplierBuyerPage(@Valid buyerSupplierBuyerPageReqVO pageReqVO) {
        PageResult<buyerSupplierBuyerDO> pageResult = buyerSupplierBuyerService.getbuyerSupplierBuyerPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, buyerSupplierBuyerRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出物料供应商采购员对应 Excel")
    @PreAuthorize("@ss.hasPermission('buyer:buyer-supplier-buyer:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportbuyerSupplierBuyerExcel(@Valid buyerSupplierBuyerPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<buyerSupplierBuyerDO> list = buyerSupplierBuyerService.getbuyerSupplierBuyerPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "物料供应商采购员对应.xls", "数据", buyerSupplierBuyerRespVO.class,
                        BeanUtils.toBean(list, buyerSupplierBuyerRespVO.class));
    }

}