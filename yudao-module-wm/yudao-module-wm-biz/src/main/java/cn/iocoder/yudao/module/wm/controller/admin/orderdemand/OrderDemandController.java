package cn.iocoder.yudao.module.wm.controller.admin.orderdemand;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import lombok.extern.slf4j.Slf4j;
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

import cn.iocoder.yudao.module.wm.controller.admin.orderdemand.vo.*;
import cn.iocoder.yudao.module.wm.dal.dataobject.orderdemand.OrderDemandDO;
import cn.iocoder.yudao.module.wm.service.orderdemand.OrderDemandService;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "管理后台 - 订单追溯需求")
@RestController
@RequestMapping("/wm/order-demand")
@Validated
@Slf4j
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
    @PostMapping("/import-excel")
    @Operation(summary = "导入订单追溯需求 Excel")
    @PreAuthorize("@ss.hasPermission('wm:order-demand:import')")  // 需要配置权限
    @ApiAccessLog(operateType = IMPORT)
    public CommonResult<Integer> importOrderDemand(@RequestParam("file") MultipartFile file) throws IOException {
        // 同步读取 Excel 数据（键为列索引，值为单元格对象）
        List<Map<Integer, Object>> dataList = EasyExcel.read(file.getInputStream()).sheet().doReadSync();
        log.info("原始数据行数：{}", dataList.size());

        List<OrderDemandImportReqVO> importList = new ArrayList<>();
        for (Map<Integer, Object> row : dataList) {
            OrderDemandImportReqVO vo = new OrderDemandImportReqVO();
            vo.setOrderNo(getCellStringValue(row.get(2)));          // C列
            vo.setTraceDemandNo(getCellStringValue(row.get(0)));    // A列
            vo.setMaterialNo(getCellStringValue(row.get(4)));       // E列
            vo.setMaterialDescription(getCellStringValue(row.get(5))); // F列
            BigDecimal demandQty = parseBigDecimal(row.get(6));
            vo.setDemandQuantity(demandQty != null ? demandQty : BigDecimal.ZERO);      // G列
            vo.setOutboundAccumulated(parseBigDecimal(row.get(7))); // H列
            vo.setOpenQuantity(parseBigDecimal(row.get(8)));        // I列
            vo.setStatus(0);
            vo.setRemark(null);

            // 校验必填字段
            if (vo.getOrderNo() == null || vo.getMaterialNo() == null) {
                throw new IllegalArgumentException("第 " + (importList.size() + 1) + " 行订单号或物料号为空");
            }
            if (vo.getDemandQuantity() == null) {
                throw new IllegalArgumentException("第 " + (importList.size() + 1) + " 行需求量为空");
            }

            importList.add(vo);
        }
        int count = orderDemandService.importOrderDemand(importList);
        return success(count);
    }

    @PostMapping("/sync-from-sap")
    @Operation(summary = "从 SAP 同步订单需求数据")
    @PreAuthorize("@ss.hasPermission('wm:order-demand:sync')")
    public CommonResult<Integer> syncOrderDemandFromSap(@Valid @RequestBody SapResbQueryReqVO reqVO) {
        int count = orderDemandService.syncOrderDemandFromSap(reqVO);
        return success(count);
    }

    @PostMapping("/search-from-sap")
    @Operation(summary = "查询 SAP 预留数据（不保存）")
    @PreAuthorize("@ss.hasPermission('wm:order-demand:query')")
    public CommonResult<List<OrderDemandFromSapVO>> searchResbFromSap(@Valid @RequestBody SapResbQueryReqVO reqVO) {
        List<OrderDemandFromSapVO> list = orderDemandService.searchResbFromSap(reqVO);
        return success(list);
    }

    // 辅助方法（可提取到基类）
    private String getCellStringValue(Object cell) {
        if (cell == null) return null;
        if (cell instanceof String) return (String) cell;
        if (cell instanceof Number) return String.valueOf(cell);
        if (cell instanceof Date) return cell.toString();
        return cell.toString();
    }


    private Integer parseInteger(Object cell) {
        String str = getCellStringValue(cell);
        if (str == null || str.isEmpty()) return null;
        try {
            return Integer.valueOf(str);
        } catch (NumberFormatException e) {
            log.warn("整数解析失败: {}", str);
            return null;
        }
    }

    private BigDecimal parseBigDecimal(Object cell) {
        String str = getCellStringValue(cell);
        if (str == null || str.trim().isEmpty()) {
            return null;
        }
        // 去除千分位逗号
        str = str.replace(",", "");
        try {
            return new BigDecimal(str);
        } catch (NumberFormatException e) {
            log.warn("数字解析失败: {}", str);
            throw new IllegalArgumentException("数字格式错误: " + str);
        }
    }

}