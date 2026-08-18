package cn.iocoder.yudao.module.wm.controller.admin.sapordersync;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.wm.service.sapordersync.SapOrderSyncService;
import cn.iocoder.yudao.module.wm.controller.admin.sapordersync.vo.OrderFromSapVO;
import cn.iocoder.yudao.module.wm.controller.admin.sapordersync.vo.SapOrderQueryReqVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 订单表 - SAP订单信息")
@RestController
@RequestMapping("/wm/sap-order")
@Validated
public class SapOrderSyncController {

    @Resource
    private SapOrderSyncService orderService;

    @PostMapping("/sync-from-sap")
    @Operation(summary = "从 SAP 同步生产订单")
    @PreAuthorize("@ss.hasPermission('wm:sap-order:sync')")
    public CommonResult<Integer> syncOrderFromSap(@Valid @RequestBody SapOrderQueryReqVO reqVO) {
        int count = orderService.syncOrderFromSap(reqVO);
        return success(count);
    }

    @PostMapping("/search-from-sap")
    @Operation(summary = "查询 SAP 订单（不保存）")
    @PreAuthorize("@ss.hasPermission('wm:sap-order:query')")
    public CommonResult<List<OrderFromSapVO>> searchOrderFromSap(@Valid @RequestBody SapOrderQueryReqVO reqVO) {
        List<OrderFromSapVO> list = orderService.searchOrderFromSap(reqVO);
        return success(list);
    }

}