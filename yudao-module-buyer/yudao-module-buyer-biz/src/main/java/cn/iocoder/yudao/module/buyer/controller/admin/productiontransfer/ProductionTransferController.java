package cn.iocoder.yudao.module.buyer.controller.admin.productiontransfer;

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
import java.time.LocalDateTime;
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

import cn.iocoder.yudao.module.buyer.controller.admin.productiontransfer.vo.*;
import cn.iocoder.yudao.module.buyer.dal.dataobject.productiontransfer.ProductionTransferDO;
import cn.iocoder.yudao.module.buyer.service.productiontransfer.ProductionTransferService;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import com.alibaba.excel.EasyExcel;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "管理后台 - MES转序单信息")
@RestController
@RequestMapping("/buyer/production-transfer")
@Validated
@Slf4j
public class ProductionTransferController {

    @Resource
    private ProductionTransferService productionTransferService;

    @PostMapping("/create")
    @Operation(summary = "创建MES转序单信息")
    @PreAuthorize("@ss.hasPermission('buyer:production-transfer:create')")
    public CommonResult<BigDecimal> createProductionTransfer(@Valid @RequestBody ProductionTransferSaveReqVO createReqVO) {
        return success(productionTransferService.createProductionTransfer(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新MES转序单信息")
    @PreAuthorize("@ss.hasPermission('buyer:production-transfer:update')")
    public CommonResult<Boolean> updateProductionTransfer(@Valid @RequestBody ProductionTransferSaveReqVO updateReqVO) {
        productionTransferService.updateProductionTransfer(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除MES转序单信息")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('buyer:production-transfer:delete')")
    public CommonResult<Boolean> deleteProductionTransfer(@RequestParam("id") BigDecimal id) {
        productionTransferService.deleteProductionTransfer(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得MES转序单信息")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('buyer:production-transfer:query')")
    public CommonResult<ProductionTransferRespVO> getProductionTransfer(@RequestParam("id") BigDecimal id) {
        ProductionTransferDO productionTransfer = productionTransferService.getProductionTransfer(id);
        return success(BeanUtils.toBean(productionTransfer, ProductionTransferRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得MES转序单信息分页")
    @PreAuthorize("@ss.hasPermission('buyer:production-transfer:query')")
    public CommonResult<PageResult<ProductionTransferRespVO>> getProductionTransferPage(@Valid ProductionTransferPageReqVO pageReqVO) {
        PageResult<ProductionTransferDO> pageResult = productionTransferService.getProductionTransferPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, ProductionTransferRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出MES转序单信息 Excel")
    @PreAuthorize("@ss.hasPermission('buyer:production-transfer:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportProductionTransferExcel(@Valid ProductionTransferPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ProductionTransferDO> list = productionTransferService.getProductionTransferPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "MES转序单信息.xls", "数据", ProductionTransferRespVO.class,
                        BeanUtils.toBean(list, ProductionTransferRespVO.class));
    }

    @PostMapping("/import-excel")
    @Operation(summary = "导入MES转序单信息 Excel")
    @PreAuthorize("@ss.hasPermission('buyer:production-transfer:import')")
    @ApiAccessLog(operateType = IMPORT)
    public CommonResult<Integer> importProductionTransfer(@RequestParam("file") MultipartFile file) throws IOException {
        // 使用同步读取，获取原始数据（键为列索引，值为单元格对象）
        List<Map<Integer, Object>> dataList = EasyExcel.read(file.getInputStream()).sheet().doReadSync();
        log.info("原始数据行数：{}", dataList.size());

        // 自动跳过表头行（假设第一行第一个单元格为“序号”或“订单号”等）
        int startRow = 0;
        if (!dataList.isEmpty()) {
            Object firstCell = dataList.get(0).get(0);
            if (firstCell != null && ("序号".equals(firstCell.toString()) || "订单号".equals(firstCell.toString()))) {
                startRow = 1;
                log.info("检测到表头行，从第2行开始处理");
            }
        }

        List<ProductionTransferImportReqVO> importList = new ArrayList<>();
        for (int i = startRow; i < dataList.size(); i++) {
            Map<Integer, Object> row = dataList.get(i);

            // 根据Excel列顺序手动构建VO（请根据实际Excel调整索引）
            ProductionTransferImportReqVO vo = new ProductionTransferImportReqVO();
            vo.setOrderNo(getCellStringValue(row.get(0)));                 // 订单号
            vo.setMaterialCode(getCellStringValue(row.get(1)));            // 物料编码
            vo.setMaterialDesc(getCellStringValue(row.get(2)));            // 物料描述
            vo.setProductionScheduler(getCellStringValue(row.get(3)));     // 生产调度员
            vo.setTransferInitiator(getCellStringValue(row.get(4)));       // 转序发起人
            vo.setInitiatorDate(parseLocalDateTime(row.get(5)));           // 发起日期
            vo.setQuantity(parseBigDecimal(row.get(6)));                   // 数量
            vo.setTransferNo(getCellStringValue(row.get(7)));              // 转序单号
            vo.setBatchNo(getCellStringValue(row.get(8)));                 // 计划批次
            vo.setSigner(getCellStringValue(row.get(9)));                  // 签收人
            vo.setSignTime(parseLocalDateTime(row.get(10)));               // 签收时间

            // 跳过关键字段为空的行（可根据业务调整，例如转序单号必填）
            if (vo.getTransferNo() == null || vo.getTransferNo().isEmpty()) {
                log.debug("跳过转序单号为空的第 {} 行", i + 1);
                continue;
            }

            importList.add(vo);
        }

        int count = productionTransferService.importProductionTransfer(importList);
        return success(count);
    }

    @PostMapping("/sync-from-mes")
    @Operation(summary = "从MES同步转序单数据")
    @PreAuthorize("@ss.hasPermission('buyer:production-transfer:sync')")
    @ApiAccessLog(operateType = IMPORT)  // 可复用导入的操作类型
    public CommonResult<Integer> syncFromMes(@Valid @RequestBody MesSyncReqVO syncReqVO) {
        int count = productionTransferService.syncFromMes(syncReqVO);
        return success(count);
    }

    // ---------- 辅助方法（可直接复用或提取到基类）----------
    private String getCellStringValue(Object cell) {
        if (cell == null) return null;
        if (cell instanceof String) return (String) cell;
        if (cell instanceof Number) return String.valueOf(cell);
        if (cell instanceof Date) return cell.toString();
        return cell.toString();
    }

    private BigDecimal parseBigDecimal(Object cell) {
        String str = getCellStringValue(cell);
        if (str == null || str.isEmpty()) return null;
        // 过滤非数字（如“合计”）
        if (!str.matches("-?\\d+(\\.\\d+)?")) {
            log.debug("非数字内容，忽略: {}", str);
            return null;
        }
        try {
            return new BigDecimal(str);
        } catch (NumberFormatException e) {
            log.debug("数字解析失败，忽略: {}", str);
            return null;
        }
    }

    private LocalDateTime parseLocalDateTime(Object cell) {
        if (cell == null) return null;
        if (cell instanceof LocalDateTime) return (LocalDateTime) cell;
        if (cell instanceof Date) {
            return ((Date) cell).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        }
        if (cell instanceof String) {
            String str = ((String) cell).trim();
            if (str.isEmpty() || "/".equals(str) || "-".equals(str)) {
                return null;
            }
            // 支持多种日期格式
            DateTimeFormatter[] formatters = {
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),
                    DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd"),
                    DateTimeFormatter.ofPattern("yyyy/MM/dd"),
                    DateTimeFormatter.ofPattern("yyyy-M-d"),
                    DateTimeFormatter.ofPattern("yyyy/M/d")
            };
            for (DateTimeFormatter formatter : formatters) {
                try {
                    if (formatter.toString().contains("HH")) {
                        return LocalDateTime.parse(str, formatter);
                    } else {
                        return LocalDate.parse(str, formatter).atStartOfDay();
                    }
                } catch (DateTimeParseException ignored) {}
            }
            log.warn("无法解析日期字符串: {}", str);
        }
        return null;
    }

}