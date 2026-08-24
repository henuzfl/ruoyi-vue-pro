package cn.iocoder.yudao.module.buyer.controller.admin.overseasinventory;

import cn.iocoder.yudao.framework.apilog.core.annotation.ApiAccessLog;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.module.buyer.controller.admin.overseasinventory.vo.*;
import cn.iocoder.yudao.module.buyer.dal.dataobject.overseasinventory.OverseasInventoryDO;
import cn.iocoder.yudao.module.buyer.service.overseasinventory.OverseasInventoryService;
import com.alibaba.excel.EasyExcel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

import static cn.iocoder.yudao.framework.apilog.core.enums.OperateTypeEnum.EXPORT;
import static cn.iocoder.yudao.framework.apilog.core.enums.OperateTypeEnum.IMPORT;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 驻外库存")
@RestController
@RequestMapping("/buyer/overseas-inventory")
@Validated
public class OverseasInventoryController {

    private static final List<String> EXCEL_HEADERS = Arrays.asList(
            "仓库", "货主代码", "供应商代码", "供应商名称", "货品编码", "货品名称", "货品规格",
            "库存数量", "占用数量", "可用量", "冻结数量");

    @Resource
    private OverseasInventoryService overseasInventoryService;

    @PostMapping("/create")
    @Operation(summary = "创建驻外库存")
    @PreAuthorize("@ss.hasPermission('buyer:overseas-inventory:create')")
    public CommonResult<BigDecimal> create(@Valid @RequestBody OverseasInventorySaveReqVO reqVO) {
        return success(overseasInventoryService.createOverseasInventory(reqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新驻外库存")
    @PreAuthorize("@ss.hasPermission('buyer:overseas-inventory:update')")
    public CommonResult<Boolean> update(@Valid @RequestBody OverseasInventorySaveReqVO reqVO) {
        overseasInventoryService.updateOverseasInventory(reqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除驻外库存")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('buyer:overseas-inventory:delete')")
    public CommonResult<Boolean> delete(@RequestParam("id") BigDecimal id) {
        overseasInventoryService.deleteOverseasInventory(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得驻外库存")
    @PreAuthorize("@ss.hasPermission('buyer:overseas-inventory:query')")
    public CommonResult<OverseasInventoryRespVO> get(@RequestParam("id") BigDecimal id) {
        return success(BeanUtils.toBean(overseasInventoryService.getOverseasInventory(id),
                OverseasInventoryRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得驻外库存分页")
    @PreAuthorize("@ss.hasPermission('buyer:overseas-inventory:query')")
    public CommonResult<PageResult<OverseasInventoryRespVO>> page(@Valid OverseasInventoryPageReqVO reqVO) {
        return success(BeanUtils.toBean(overseasInventoryService.getOverseasInventoryPage(reqVO),
                OverseasInventoryRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出驻外库存 Excel")
    @PreAuthorize("@ss.hasPermission('buyer:overseas-inventory:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void export(@Valid OverseasInventoryPageReqVO reqVO, HttpServletResponse response) throws IOException {
        reqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<OverseasInventoryDO> list = overseasInventoryService.getOverseasInventoryPage(reqVO).getList();
        ExcelUtils.write(response, "驻外库存.xls", "数据", OverseasInventoryRespVO.class,
                BeanUtils.toBean(list, OverseasInventoryRespVO.class));
    }

    @GetMapping("/import-template")
    @Operation(summary = "下载驻外库存导入模板")
    @PreAuthorize("@ss.hasPermission('buyer:overseas-inventory:import')")
    @ApiAccessLog(operateType = EXPORT)
    public void importTemplate(HttpServletResponse response) throws IOException {
        ExcelUtils.write(response, "驻外库存导入模板.xlsx", "数据", OverseasInventoryImportReqVO.class,
                Collections.emptyList());
    }

    @PostMapping("/import-excel")
    @Operation(summary = "导入驻外库存 Excel")
    @PreAuthorize("@ss.hasPermission('buyer:overseas-inventory:import')")
    @ApiAccessLog(operateType = IMPORT)
    public CommonResult<Integer> importExcel(@RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("导入文件不能为空");
        }
        String fileName = file.getOriginalFilename();
        if (fileName == null || !(fileName.toLowerCase(Locale.ROOT).endsWith(".xls")
                || fileName.toLowerCase(Locale.ROOT).endsWith(".xlsx"))) {
            throw new IllegalArgumentException("仅支持 xls、xlsx 格式文件");
        }
        List<Map<Integer, Object>> rows = EasyExcel.read(file.getInputStream()).headRowNumber(0).sheet().doReadSync();
        if (rows.isEmpty()) {
            throw new IllegalArgumentException("导入文件没有内容");
        }
        validateHeaders(rows.get(0));
        List<OverseasInventoryImportReqVO> list = new ArrayList<>();
        for (int i = 1; i < rows.size(); i++) {
            Map<Integer, Object> row = rows.get(i);
            if (isBlankRow(row)) {
                continue;
            }
            int excelRow = i + 1;
            OverseasInventoryImportReqVO item = new OverseasInventoryImportReqVO();
            item.setWarehouse(cellString(row.get(0)));
            item.setOwnerCode(cellString(row.get(1)));
            item.setSupplierCode(cellString(row.get(2)));
            item.setSupplierName(cellString(row.get(3)));
            item.setItemCode(cellString(row.get(4)));
            item.setItemName(cellString(row.get(5)));
            item.setItemSpecification(cellString(row.get(6)));
            item.setInventoryQuantity(cellLong(row.get(7), excelRow, "库存数量"));
            item.setOccupiedQuantity(cellLong(row.get(8), excelRow, "占用数量"));
            item.setAvailableQuantity(cellLong(row.get(9), excelRow, "可用量"));
            item.setFrozenQuantity(cellLong(row.get(10), excelRow, "冻结数量"));
            list.add(item);
        }
        return success(overseasInventoryService.importOverseasInventory(list));
    }

    private void validateHeaders(Map<Integer, Object> row) {
        for (int i = 0; i < EXCEL_HEADERS.size(); i++) {
            String actual = cellString(row.get(i));
            if (!EXCEL_HEADERS.get(i).equals(actual)) {
                throw new IllegalArgumentException("Excel 第 " + (i + 1) + " 列表头应为“" + EXCEL_HEADERS.get(i) + "”");
            }
        }
    }

    private boolean isBlankRow(Map<Integer, Object> row) {
        return row == null || row.values().stream().allMatch(value -> cellString(value) == null);
    }

    private String cellString(Object value) {
        if (value == null) {
            return null;
        }
        String text = String.valueOf(value).trim();
        return text.isEmpty() ? null : text;
    }

    private Long cellLong(Object value, int row, String field) {
        String text = cellString(value);
        if (text == null) {
            return null;
        }
        try {
            return new BigDecimal(text).longValueExact();
        } catch (ArithmeticException | NumberFormatException ex) {
            throw new IllegalArgumentException("Excel 第 " + row + " 行“" + field + "”必须是整数");
        }
    }
}
