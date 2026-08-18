package cn.iocoder.yudao.module.aps.controller.admin.masterimport;

import com.alibaba.excel.EasyExcel;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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

import cn.iocoder.yudao.module.aps.controller.admin.masterimport.vo.*;
import cn.iocoder.yudao.module.aps.dal.dataobject.masterimport.MasterImportDO;
import cn.iocoder.yudao.module.aps.service.masterimport.MasterImportService;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "管理后台 - 物料主数据导入")
@RestController
@RequestMapping("/aps/master-import")
@Validated
@Slf4j
public class MasterImportController {

    @Resource
    private MasterImportService masterImportService;

    @PostMapping("/create")
    @Operation(summary = "创建物料主数据导入")
    @PreAuthorize("@ss.hasPermission('aps:master-import:create')")
    public CommonResult<Long> createMasterImport(@Valid @RequestBody MasterImportSaveReqVO createReqVO) {
        return success(masterImportService.createMasterImport(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新物料主数据导入")
    @PreAuthorize("@ss.hasPermission('aps:master-import:update')")
    public CommonResult<Boolean> updateMasterImport(@Valid @RequestBody MasterImportSaveReqVO updateReqVO) {
        masterImportService.updateMasterImport(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除物料主数据导入")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('aps:master-import:delete')")
    public CommonResult<Boolean> deleteMasterImport(@RequestParam("id") Long id) {
        masterImportService.deleteMasterImport(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得物料主数据导入")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('aps:master-import:query')")
    public CommonResult<MasterImportRespVO> getMasterImport(@RequestParam("id") Long id) {
        MasterImportDO masterImport = masterImportService.getMasterImport(id);
        return success(BeanUtils.toBean(masterImport, MasterImportRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得物料主数据导入分页")
    @PreAuthorize("@ss.hasPermission('aps:master-import:query')")
    public CommonResult<PageResult<MasterImportRespVO>> getMasterImportPage(@Valid MasterImportPageReqVO pageReqVO) {
        PageResult<MasterImportDO> pageResult = masterImportService.getMasterImportPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, MasterImportRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出物料主数据导入 Excel")
    @PreAuthorize("@ss.hasPermission('aps:master-import:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportMasterImportExcel(@Valid MasterImportPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<MasterImportDO> list = masterImportService.getMasterImportPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "物料主数据导入.xls", "数据", MasterImportRespVO.class,
                        BeanUtils.toBean(list, MasterImportRespVO.class));
    }

    /**
     * 导入物料
     * @param file
     * @return
     * @throws IOException
     */

    @PostMapping("/import-excel")
    @Operation(summary = "导入物料主数据 Excel")
    @PreAuthorize("@ss.hasPermission('aps:master-import:import')")  // 建议单独配置权限
    @ApiAccessLog(operateType = IMPORT)
    public CommonResult<Integer> importMasterImport(@RequestParam("file") MultipartFile file) throws IOException {
        // 使用同步读取，获取原始数据（键为列索引，值为单元格对象）
        List<Map<Integer, Object>> dataList = EasyExcel.read(file.getInputStream()).sheet().doReadSync();
        log.info("原始数据行数：{}", dataList.size());

        List<MasterImportImportReqVO> importList = new ArrayList<>();

        for (Map<Integer, Object> row : dataList) {
            // 手动构建 VO，请根据实际 Excel 列顺序调整索引
            MasterImportImportReqVO vo = new MasterImportImportReqVO();
            vo.setMaterialNo(getCellStringValue(row.get(0)));
            vo.setMaterialDesc(getCellStringValue(row.get(1)));
            vo.setMaterialType(getCellStringValue(row.get(2)));
            vo.setGrossWeight(parseBigDecimal(row.get(3)));
            vo.setNetWeight(parseBigDecimal(row.get(4)));
            vo.setBaseUom(getCellStringValue(row.get(5)));
            vo.setValuationClass(getCellStringValue(row.get(6)));
            vo.setPriceControl(getCellStringValue(row.get(7)));
            vo.setNoCostEstimation(getCellStringValue(row.get(8)));
            vo.setQsCostEstimate(getCellStringValue(row.get(9)));
            vo.setSizeDimension(getCellStringValue(row.get(10)));
            vo.setProcurementType(getCellStringValue(row.get(11)));
            vo.setProductionStorageLocation(getCellStringValue(row.get(12)));
            vo.setProductionScheduler(getCellStringValue(row.get(13)));
            vo.setDistributionFlag(getCellStringValue(row.get(14)));
            vo.setMaterialCategory(getCellStringValue(row.get(15)));
            vo.setExternalProcurementStorage(getCellStringValue(row.get(16)));
            vo.setPlannedDeliveryTime(parseLocalDateTime(row.get(17))); // 日期列
            vo.setPurchasingGroup(getCellStringValue(row.get(18)));


            // 可根据需要添加空值校验
            if (vo.getMaterialNo() == null || vo.getMaterialNo().isEmpty()) {
                throw new IllegalArgumentException("第 " + (importList.size() + 1) + " 行物料号为空");
            }

            importList.add(vo);
        }

        int count = masterImportService.importMasterImport(importList);
        return success(count);
    }

    // 辅助方法（可复用或单独提取到基类）
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
        try {
            return new BigDecimal(str);
        } catch (NumberFormatException e) {
            log.warn("数字解析失败: {}", str, e);
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
            String str = (String) cell;
            // 支持多种格式，参考 MainPlanController
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