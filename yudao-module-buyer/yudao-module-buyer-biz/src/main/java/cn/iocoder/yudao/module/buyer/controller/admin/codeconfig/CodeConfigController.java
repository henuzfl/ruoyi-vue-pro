package cn.iocoder.yudao.module.buyer.controller.admin.codeconfig;

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

import cn.iocoder.yudao.module.buyer.controller.admin.codeconfig.vo.*;
import cn.iocoder.yudao.module.buyer.dal.dataobject.codeconfig.CodeConfigDO;
import cn.iocoder.yudao.module.buyer.service.codeconfig.CodeConfigService;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;
import org.springframework.http.HttpHeaders;
import java.net.URLEncoder;

@Tag(name = "管理后台 - 主机编码配置")
@RestController
@RequestMapping("/buyer/code-config")
@Validated
@Slf4j
public class CodeConfigController {

    @Resource
    private CodeConfigService codeConfigService;

    @PostMapping("/create")
    @Operation(summary = "创建主机编码配置")
    @PreAuthorize("@ss.hasPermission('buyer:code-config:create')")
    public CommonResult<BigDecimal> createCodeConfig(@Valid @RequestBody CodeConfigSaveReqVO createReqVO) {
        return success(codeConfigService.createCodeConfig(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新主机编码配置")
    @PreAuthorize("@ss.hasPermission('buyer:code-config:update')")
    public CommonResult<Boolean> updateCodeConfig(@Valid @RequestBody CodeConfigSaveReqVO updateReqVO) {
        codeConfigService.updateCodeConfig(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除主机编码配置")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('buyer:code-config:delete')")
    public CommonResult<Boolean> deleteCodeConfig(@RequestParam("id") BigDecimal id) {
        codeConfigService.deleteCodeConfig(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得主机编码配置")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('buyer:code-config:query')")
    public CommonResult<CodeConfigRespVO> getCodeConfig(@RequestParam("id") BigDecimal id) {
        CodeConfigDO codeConfig = codeConfigService.getCodeConfig(id);
        return success(BeanUtils.toBean(codeConfig, CodeConfigRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得主机编码配置分页")
    @PreAuthorize("@ss.hasPermission('buyer:code-config:query')")
    public CommonResult<PageResult<CodeConfigRespVO>> getCodeConfigPage(@Valid CodeConfigPageReqVO pageReqVO) {
        PageResult<CodeConfigDO> pageResult = codeConfigService.getCodeConfigPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, CodeConfigRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出主机编码配置 Excel")
    @PreAuthorize("@ss.hasPermission('buyer:code-config:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportCodeConfigExcel(@Valid CodeConfigPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<CodeConfigDO> list = codeConfigService.getCodeConfigPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "主机编码配置.xls", "数据", CodeConfigRespVO.class,
                        BeanUtils.toBean(list, CodeConfigRespVO.class));
    }

    @GetMapping("/import-template")
    @Operation(summary = "下载主机编码配置导入模版")
    @PreAuthorize("@ss.hasPermission('buyer:code-config:import')")
    @ApiAccessLog(operateType = EXPORT)
    public void downloadImportTemplate(HttpServletResponse response) throws IOException {
        // 设置响应头
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("主机编码对照导入模板.xlsx", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename*=utf-8''" + fileName);

        // 从 classpath 读取静态模版文件
        ClassPathResource resource = new ClassPathResource("templates/excel/主机编码对照导入模板.xlsx");
        if (!resource.exists()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "模版文件不存在");
            return;
        }
        FileCopyUtils.copy(resource.getInputStream(), response.getOutputStream());
    }

    @PostMapping("/import-excel")
    @Operation(summary = "导入主机编码配置 Excel")
    @PreAuthorize("@ss.hasPermission('buyer:code-config:import')")
    @ApiAccessLog(operateType = IMPORT)
    public CommonResult<Integer> importCodeConfig(@RequestParam("file") MultipartFile file) throws IOException {
        // 使用同步读取，获取原始数据（键为列索引，值为单元格对象）
        List<Map<Integer, Object>> dataList = EasyExcel.read(file.getInputStream()).sheet().doReadSync();
        log.info("原始数据行数：{}", dataList.size());

        // 自动跳过表头行（假设第一行第一个单元格为“名称”）
        int startRow = 0;
        if (!dataList.isEmpty()) {
            Object firstCell = dataList.get(0).get(0);
            if (firstCell != null && "名称".equals(firstCell.toString())) {
                startRow = 1;
                log.info("检测到表头行，从第2行开始处理");
            }
        }

        List<CodeConfigImportReqVO> importList = new ArrayList<>();
        for (int i = startRow; i < dataList.size(); i++) {
            Map<Integer, Object> row = dataList.get(i);

            // 手动构建 VO，请根据实际 Excel 列顺序调整索引
            CodeConfigImportReqVO vo = new CodeConfigImportReqVO();
            vo.setName(getCellStringValue(row.get(0)));          // 名称
            vo.setHostCode(getCellStringValue(row.get(1)));      // 主机编码
            vo.setTeliCode(getCellStringValue(row.get(2)));      // 特力编码

//            if (vo.getName() == null || vo.getName().isEmpty()) {
//                log.debug("跳过名称为空的第 {} 行", i + 1);
//                continue;
//            }

            importList.add(vo);
        }

        int count = codeConfigService.importCodeConfig(importList);
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