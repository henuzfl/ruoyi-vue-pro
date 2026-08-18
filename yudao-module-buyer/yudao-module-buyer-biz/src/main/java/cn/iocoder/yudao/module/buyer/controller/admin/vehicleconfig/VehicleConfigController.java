package cn.iocoder.yudao.module.buyer.controller.admin.vehicleconfig;

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
import java.text.SimpleDateFormat;
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

import cn.iocoder.yudao.module.buyer.controller.admin.vehicleconfig.vo.*;
import cn.iocoder.yudao.module.buyer.dal.dataobject.vehicleconfig.VehicleConfigDO;
import cn.iocoder.yudao.module.buyer.service.vehicleconfig.VehicleConfigService;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;
import java.net.URLEncoder;

@Tag(name = "管理后台 - 主机车型配置")
@RestController
@RequestMapping("/buyer/vehicle-config")
@Validated
@Slf4j
public class VehicleConfigController {

    @Resource
    private VehicleConfigService vehicleConfigService;

    @PostMapping("/create")
    @Operation(summary = "创建主机车型配置")
    @PreAuthorize("@ss.hasPermission('buyer:vehicle-config:create')")
    public CommonResult<BigDecimal> createVehicleConfig(@Valid @RequestBody VehicleConfigSaveReqVO createReqVO) {
        return success(vehicleConfigService.createVehicleConfig(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新主机车型配置")
    @PreAuthorize("@ss.hasPermission('buyer:vehicle-config:update')")
    public CommonResult<Boolean> updateVehicleConfig(@Valid @RequestBody VehicleConfigSaveReqVO updateReqVO) {
        vehicleConfigService.updateVehicleConfig(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除主机车型配置")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('buyer:vehicle-config:delete')")
    public CommonResult<Boolean> deleteVehicleConfig(@RequestParam("id") String id) {
        vehicleConfigService.deleteVehicleConfig(new BigDecimal(id));
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得主机车型配置")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('buyer:vehicle-config:query')")
    public CommonResult<VehicleConfigRespVO> getVehicleConfig(@RequestParam("id") BigDecimal id) {
        VehicleConfigDO vehicleConfig = vehicleConfigService.getVehicleConfig(id);
        return success(BeanUtils.toBean(vehicleConfig, VehicleConfigRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得主机车型配置分页")
    @PreAuthorize("@ss.hasPermission('buyer:vehicle-config:query')")
    public CommonResult<PageResult<VehicleConfigRespVO>> getVehicleConfigPage(@Valid VehicleConfigPageReqVO pageReqVO) {
        PageResult<VehicleConfigDO> pageResult = vehicleConfigService.getVehicleConfigPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, VehicleConfigRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出主机车型配置 Excel")
    @PreAuthorize("@ss.hasPermission('buyer:vehicle-config:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportVehicleConfigExcel(@Valid VehicleConfigPageReqVO pageReqVO,
                                         HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<VehicleConfigDO> list = vehicleConfigService.getVehicleConfigPage(pageReqVO).getList();

        // 手动转换并格式化日期
        List<VehicleConfigRespVO> voList = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-DD");
        for (VehicleConfigDO doObj : list) {
            VehicleConfigRespVO vo = BeanUtils.toBean(doObj, VehicleConfigRespVO.class);
            if (doObj.getImportDate() != null) {
                vo.setImportDate(sdf.format(doObj.getImportDate()));
            }
            voList.add(vo);
        }

        // 导出 Excel
        ExcelUtils.write(response, "主机车型配置.xls", "数据", VehicleConfigRespVO.class, voList);
    }

    @GetMapping("/import-template")
    @Operation(summary = "下载主机车型配置导入模版")
    @PreAuthorize("@ss.hasPermission('buyer:vehicle-config:import')")
    @ApiAccessLog(operateType = EXPORT)
    public void downloadImportTemplate(HttpServletResponse response) throws IOException {
        // 设置响应头
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("主机车型配置导入模版.xlsx", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename*=utf-8''" + fileName);

        // 从 classpath 读取静态模版文件
        ClassPathResource resource = new ClassPathResource("templates/excel/主机车型配置导入模版.xlsx");
        if (!resource.exists()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "模版文件不存在");
            return;
        }
        // 复制文件内容到输出流
        FileCopyUtils.copy(resource.getInputStream(), response.getOutputStream());
    }

    /**
     * 导入
     * @param file
     * @return
     * @throws IOException
     */
    @PostMapping("/import-excel")
    @Operation(summary = "导入主机车型配置 Excel")
    @PreAuthorize("@ss.hasPermission('buyer:vehicle-config:import')")
    @ApiAccessLog(operateType = IMPORT)
    public CommonResult<Integer> importVehicleConfig(@RequestParam("file") MultipartFile file) throws IOException {
        List<Map<Integer, Object>> dataList = EasyExcel.read(file.getInputStream()).sheet().doReadSync();
        log.info("原始数据行数：{}", dataList.size());

        // 跳过表头行（假设第一行第一个单元格为“订单”）
        int startRow = 0;
        if (!dataList.isEmpty()) {
            Object firstCell = dataList.get(0).get(0);
            if (firstCell != null && "订单".equals(firstCell.toString())) {
                startRow = 1;
                log.info("检测到表头行，从第2行开始处理");
            }
        }

        List<VehicleConfigImportReqVO> importList = new ArrayList<>();
        for (int i = startRow; i < dataList.size(); i++) {
            Map<Integer, Object> row = dataList.get(i);

            VehicleConfigImportReqVO vo = new VehicleConfigImportReqVO();
            vo.setImportDate(parseImportDate(row.get(0)));
            vo.setOrderNo(getCellStringValue(row.get(1)));               // 订单号
            vo.setVehicleModel(getCellStringValue(row.get(2)));          // 车型
            vo.setSeqNo2025(getCellStringValue(row.get(3)));             // 2025顺序号
            vo.setSeqNo2026(getCellStringValue(row.get(4)));             // 2026顺序号
            vo.setRequiredArrivalTime(getCellStringValue(row.get(5)));   // 要求到货时间
            vo.setMaterialDesc(getCellStringValue(row.get(6)));          // 物料描述
            vo.setQuota2(getCellStringValue(row.get(7)));                         // 配额
             vo.setMaterialNo(getCellStringValue(row.get(8)));            // 物料号
            vo.setFactory(getCellStringValue(row.get(9)));               // 工厂
            vo.setRequiredQuantity(parseBigDecimal(row.get(10)));        // 需求数量
            vo.setDeliveredQuantity(parseBigDecimal(row.get(11)));       // 已交货数量

            if (vo.getVehicleModel() == null || vo.getVehicleModel().isEmpty() ||
                    vo.getSeqNo2026() == null || vo.getSeqNo2026().isEmpty()) {
                log.debug("跳过车型或顺序号为空的第 {} 行", i + 1);
                continue;
            }

            importList.add(vo);
        }

        int count = vehicleConfigService.importVehicleConfig(importList);
        return success(count);
    }

    private String parseImportDate(Object cell) {
        if (cell == null) return null;
        if (cell instanceof Date) {
            // 转为 yyyy-M-d 格式
            return new java.text.SimpleDateFormat("yyyy-M-d").format((Date) cell);
        }
        if (cell instanceof LocalDateTime) {
            return ((LocalDateTime) cell).format(DateTimeFormatter.ofPattern("yyyy-M-d"));
        }
        if (cell instanceof String) {
            String str = ((String) cell).trim();
            if (str.isEmpty() || "/".equals(str) || "-".equals(str)) {
                return null;
            }
            // 尝试解析常见格式，并统一输出 yyyy-M-d
            DateTimeFormatter[] formatters = {
                    DateTimeFormatter.ofPattern("yyyy-M-d"),
                    DateTimeFormatter.ofPattern("yyyy/MM/dd"),
                    DateTimeFormatter.ofPattern("yyyy-MM-dd"),
                    DateTimeFormatter.ofPattern("yyyy/M/d")
            };
            for (DateTimeFormatter formatter : formatters) {
                try {
                    LocalDate date = LocalDate.parse(str, formatter);
                    return date.format(DateTimeFormatter.ofPattern("yyyy-M-d"));
                } catch (DateTimeParseException ignored) {
                }
            }
            // 如果无法解析，当作普通字符串返回（但后续会报错）
            return str;
        }
        return cell.toString().trim();
    }



    private Long parseLong(Object cell) {
        String str = getCellStringValue(cell);
        if (str == null || str.isEmpty()) return null;
        try {
            return Long.parseLong(str);
        } catch (NumberFormatException e) {
            log.warn("Long解析失败: {}", str, e);
            return null;
        }
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