package cn.iocoder.yudao.module.buyer.controller.admin.vehicleplan;

import com.alibaba.excel.EasyExcel;
import com.google.common.net.HttpHeaders;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;
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
import java.net.URLEncoder;
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

import cn.iocoder.yudao.module.buyer.controller.admin.vehicleplan.vo.*;
import cn.iocoder.yudao.module.buyer.dal.dataobject.vehicleplan.VehiclePlanDO;
import cn.iocoder.yudao.module.buyer.service.vehicleplan.VehiclePlanService;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "管理后台 - 买家车辆营销计划表（主机厂计划）")
@RestController
@RequestMapping("/buyer/vehicle-plan")
@Validated
@Slf4j
public class VehiclePlanController {

    @Resource
    private VehiclePlanService vehiclePlanService;

    @PostMapping("/create")
    @Operation(summary = "创建买家车辆营销计划表（主机厂计划）")
    @PreAuthorize("@ss.hasPermission('buyer:vehicle-plan:create')")
    public CommonResult<BigDecimal> createVehiclePlan(@Valid @RequestBody VehiclePlanSaveReqVO createReqVO) {
        return success(vehiclePlanService.createVehiclePlan(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新买家车辆营销计划表（主机厂计划）")
    @PreAuthorize("@ss.hasPermission('buyer:vehicle-plan:update')")
    public CommonResult<Boolean> updateVehiclePlan(@Valid @RequestBody VehiclePlanSaveReqVO updateReqVO) {
        vehiclePlanService.updateVehiclePlan(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除买家车辆营销计划表（主机厂计划）")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('buyer:vehicle-plan:delete')")
    public CommonResult<Boolean> deleteVehiclePlan(@RequestParam("id") String id) {
        vehiclePlanService.deleteVehiclePlan(new String(id));
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得买家车辆营销计划表（主机厂计划）")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('buyer:vehicle-plan:query')")
    public CommonResult<VehiclePlanRespVO> getVehiclePlan(@RequestParam("id") BigDecimal id) {
        VehiclePlanDO vehiclePlan = vehiclePlanService.getVehiclePlan(id);
        return success(BeanUtils.toBean(vehiclePlan, VehiclePlanRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得买家车辆营销计划表（主机厂计划）分页")
    @PreAuthorize("@ss.hasPermission('buyer:vehicle-plan:query')")
    public CommonResult<PageResult<VehiclePlanRespVO>> getVehiclePlanPage(@Valid VehiclePlanPageReqVO pageReqVO) {
        PageResult<VehiclePlanDO> pageResult = vehiclePlanService.getVehiclePlanPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, VehiclePlanRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出买家车辆营销计划表（主机厂计划） Excel")
    @PreAuthorize("@ss.hasPermission('buyer:vehicle-plan:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportVehiclePlanExcel(@Valid VehiclePlanPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<VehiclePlanDO> list = vehiclePlanService.getVehiclePlanPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "买家车辆营销计划表（主机厂计划）.xls", "数据", VehiclePlanRespVO.class,
                        BeanUtils.toBean(list, VehiclePlanRespVO.class));
    }


    @GetMapping("/import-template")
    @Operation(summary = "下载主机计划表导入模版")
    @PreAuthorize("@ss.hasPermission('buyer:vehicle-plan:import')")
    @ApiAccessLog(operateType = EXPORT)
    public void downloadImportTemplate(HttpServletResponse response) throws IOException {
        // 设置响应头
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("主机计划导入模板.xlsx", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename*=utf-8''" + fileName);

        // 从 classpath 读取静态模版文件
        ClassPathResource resource = new ClassPathResource("templates/excel/主机计划导入模板.xlsx");
        if (!resource.exists()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "模版文件不存在");
            return;
        }
        FileCopyUtils.copy(resource.getInputStream(), response.getOutputStream());
    }


    @PostMapping("/import-excel")
    @Operation(summary = "导入买家车辆营销计划 Excel")
    @PreAuthorize("@ss.hasPermission('buyer:vehicle-plan:import')")
    @ApiAccessLog(operateType = IMPORT)
    public CommonResult<Integer> importVehiclePlan(@RequestParam("file") MultipartFile file) throws IOException {
        // 使用同步读取，获取原始数据（键为列索引，值为单元格对象）
        List<Map<Integer, Object>> dataList = EasyExcel.read(file.getInputStream()).sheet().doReadSync();
        log.info("原始数据行数：{}", dataList.size());

        // 自动跳过表头行（假设第一行第一个单元格为“序号”）
        int startRow = 0;
        if (!dataList.isEmpty()) {
            Object firstCell = dataList.get(0).get(0);
            if (firstCell != null && "序号".equals(firstCell.toString())) {
                startRow = 1;
                log.info("检测到表头行，从第2行开始处理");
            }
        }

        List<VehiclePlanImportReqVO> importList = new ArrayList<>();
        for (int i = startRow; i < dataList.size(); i++) {
            Map<Integer, Object> row = dataList.get(i);

            // 手动构建 VO，请根据实际 Excel 列顺序调整索引
            VehiclePlanImportReqVO vo = new VehiclePlanImportReqVO();
            vo.setImportDate(parseImportDate(row.get(0)));                  // 导入日期
            vo.setProductLine(getCellStringValue(row.get(1)));                // 产品线
            vo.setProductModel(getCellStringValue(row.get(2)));               // 产品机型
            vo.setVehicleCode(getCellStringValue(row.get(3)));                // 车型代码
            vo.setSeqNo2025(getCellStringValue(row.get(4)));                  // 2025顺序号
            vo.setSeqNo2026(getCellStringValue(row.get(5)));                  // 2026顺序号
            vo.setVin(getCellStringValue(row.get(6)));                        // VIN
            vo.setBareMachineOrderNo(getCellStringValue(row.get(7)));         // 裸机订单号
            vo.setDrivingUnitOrderNo(getCellStringValue(row.get(8)));         // 行驶单元订单号
            vo.setTradeType(getCellStringValue(row.get(9)));                  // 内外贸
            vo.setUnitQuantity(parseBigDecimal(row.get(10)));                 // 台份
            vo.setBlankingPlanDate(getCellStringValue(row.get(11)));          // 下料完工计划
            vo.setBoomLegPlanDate(getCellStringValue(row.get(12)));           // 吊臂板/中吨位支腿完工计划
            vo.setBoomTopBottomPlanDate(getCellStringValue(row.get(13)));     // 吊臂或主臂顶底完工计划
            vo.setTurntablePlanDate(getCellStringValue(row.get(14)));         // 转台结构件完工计划
            vo.setFramePlanDate(getCellStringValue(row.get(15)));             // 车架结构件完工计划
            vo.setChassisOnlinePlanDate(parseLocalDateTime(row.get(16)));     // 底盘上线计划
            vo.setFinishedProductPlanDate(parseLocalDateTime(row.get(17)));   // 成台完工计划

            // 跳过裸机订单号为空的行（包括合计行等）
//            if (vo.getBareMachineOrderNo() == null || vo.getBareMachineOrderNo().isEmpty()) {
//                log.debug("跳过裸机订单号为空的第 {} 行", i + 1);
//                continue;
//            }

            importList.add(vo);
        }

        int count = vehiclePlanService.importVehiclePlan(importList);
        return success(count);
    }

    // 辅助方法（可复用或单独提取到基类）
    // ---------- 增强辅助方法 ----------
    private String parseImportDate(Object cell) {
        if (cell == null) return null;
        if (cell instanceof String) return (String) cell;
        if (cell instanceof Number) return String.valueOf(cell);
        if (cell instanceof Date) {
            // 格式化为 yyyy-M，例如 2026-3
            return ((Date) cell).toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime()
                    .format(DateTimeFormatter.ofPattern("yyyy-M"));
        }
        if (cell instanceof LocalDateTime) {
            return ((LocalDateTime) cell).format(DateTimeFormatter.ofPattern("yyyy-M"));
        }
        if (cell instanceof LocalDate) {
            return ((LocalDate) cell).format(DateTimeFormatter.ofPattern("yyyy-M"));
        }
        return cell.toString();
    }

    private String getCellStringValue(Object cell) {
        if (cell == null) return null;
        if (cell instanceof String) return (String) cell;
        if (cell instanceof Number) return String.valueOf(cell);
        if (cell instanceof Date) return cell.toString();
        return cell.toString();
    }

    private Long parseLong(Object cell) {
        String str = getCellStringValue(cell);
        if (str == null || str.isEmpty()) return null;
        try {
            return Long.parseLong(str);
        } catch (NumberFormatException e) {
            log.debug("Long解析失败，忽略: {}", str);
            return null;
        }
    }

    private BigDecimal parseBigDecimal(Object cell) {
        String str = getCellStringValue(cell);
        if (str == null || str.isEmpty()) return null;
        // 快速过滤明显非数字的内容（如“合计”）
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
            // 处理常见空值符号
            if (str.isEmpty() || "/".equals(str) || "-".equals(str)) {
                return null;
            }
            // 支持多种格式
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