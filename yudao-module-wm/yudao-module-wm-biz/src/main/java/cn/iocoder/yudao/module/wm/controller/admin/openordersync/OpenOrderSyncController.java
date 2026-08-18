package cn.iocoder.yudao.module.wm.controller.admin.openordersync;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.wm.controller.admin.openordersync.vo.SapPurchaseParamDTO;
import cn.iocoder.yudao.module.wm.dal.dataobject.openordersync.SyncOpenOrderDO;
import cn.iocoder.yudao.module.wm.service.openordersync.OpenOrderSyncService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "管理后台 - 从 SAP 同步采购未清订单")
@RestController
@RequestMapping("/buyer/open-order/sync")
public class OpenOrderSyncController {

    @Autowired
    private OpenOrderSyncService syncService;

    @PostMapping("/from-sap")
    @Operation(summary = "从 SAP 同步采购未清订单")
    @PreAuthorize("@ss.hasPermission('buyer:open-order:sync')")
    public CommonResult<Integer> syncFromSap(@RequestBody SapPurchaseParamDTO param) {
        int count = syncService.syncPurchaseOrders(param);
        return CommonResult.success(count);
    }

    @PostMapping("/from-sap-table")
    @Operation(summary = "从 SAP 同步采购未清订单-直接接口显示")
    @PreAuthorize("@ss.hasPermission('buyer:open-order:sync')")
    public CommonResult<List<SyncOpenOrderDO>> syncFromSaptable(@RequestBody SapPurchaseParamDTO param) {
        return CommonResult.success(syncService.syncPurchaseOrdersSap(param));
    }
}