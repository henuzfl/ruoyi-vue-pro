package cn.iocoder.yudao.module.buyer.controller.admin.hostrequirementcomparison;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.framework.apilog.core.annotation.ApiAccessLog;
import cn.iocoder.yudao.module.buyer.controller.admin.hostrequirementcomparison.vo.HostRequirementComparisonPageReqVO;
import cn.iocoder.yudao.module.buyer.controller.admin.hostrequirementcomparison.vo.HostRequirementComparisonRespVO;
import cn.iocoder.yudao.module.buyer.service.hostrequirementcomparison.HostRequirementComparisonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.framework.apilog.core.enums.OperateTypeEnum.EXPORT;
import com.alibaba.excel.EasyExcel;
import org.springframework.http.HttpHeaders;
import java.net.URLEncoder;

@Tag(name = "管理后台 - 主机需求对比宽表")
@RestController
@RequestMapping("/buyer/host-requirement-comparison")
@Validated
@Slf4j
public class HostRequirementComparisonController {

    @Resource
    private HostRequirementComparisonService hostRequirementComparisonService;

    @GetMapping("/page")
    @Operation(summary = "获得主机需求对比分页")
    @PreAuthorize("@ss.hasPermission('buyer:host-requirement-comparison:query')")
    public CommonResult<PageResult<HostRequirementComparisonRespVO>> getComparisonPage(@Valid HostRequirementComparisonPageReqVO pageReqVO) {
        PageResult<HostRequirementComparisonRespVO> pageResult = hostRequirementComparisonService.getComparisonPage(pageReqVO);
        return success(pageResult);
    }


//    public void exportExcel(@Valid HostRequirementComparisonPageReqVO pageReqVO,
//                            HttpServletResponse response) throws IOException {
//        // 直接调用不分页的导出方法
//        List<HostRequirementComparisonRespVO> list = hostRequirementComparisonService.getAllForExport(pageReqVO);
//        ExcelUtils.write(response, "主机需求对比.xls", "数据", HostRequirementComparisonRespVO.class, list);
//    }
    @GetMapping("/export-excel")
    @Operation(summary = "导出主机需求对比 Excel")
    @PreAuthorize("@ss.hasPermission('buyer:host-requirement-comparison:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportExcel(@Valid HostRequirementComparisonPageReqVO pageReqVO,
                            HttpServletResponse response) throws IOException {
        // 1. 获取导出的数据列表
        List<HostRequirementComparisonRespVO> list = hostRequirementComparisonService.getAllForExport(pageReqVO);

        // 2. 确定使用的日期
        String currentDate = pageReqVO.getCurrentDate();
        String compareDate = pageReqVO.getCompareDate();
        if (currentDate == null || compareDate == null) {
            List<String> dates = hostRequirementComparisonService.getAvailableImportDates();
            if (dates.size() >= 2) {
                currentDate = dates.get(0);
                compareDate = dates.get(1);
            } else if (dates.size() == 1) {
                currentDate = dates.get(0);
                compareDate = dates.get(0);
            } else {
                // 无可用日期，返回空 Excel
                response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                response.setCharacterEncoding("utf-8");
                String fileName = URLEncoder.encode("主机需求对比.xlsx", "UTF-8").replaceAll("\\+", "%20");
                response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename*=utf-8''" + fileName);
                EasyExcel.write(response.getOutputStream()).sheet("数据").doWrite(Collections.emptyList());
                return;
            }
        }

        // 3. 构建动态表头
        List<List<String>> head = new ArrayList<>();
        head.add(Collections.singletonList(currentDate + "版本"));
        head.add(Collections.singletonList("车型"));
        head.add(Collections.singletonList("出产顺序"));
        head.add(Collections.singletonList("中联订单号"));
        head.add(Collections.singletonList("底盘订单号"));
        head.add(Collections.singletonList("配额" + compareDate));
        head.add(Collections.singletonList("配额" + currentDate));
        head.add(Collections.singletonList("台套"));
        head.add(Collections.singletonList(compareDate + "版本"));
        head.add(Collections.singletonList("分解油缸"));
        head.add(Collections.singletonList("主机图号"));
        head.add(Collections.singletonList("特力图号"));
        head.add(Collections.singletonList("配置"));
        head.add(Collections.singletonList("需配数量"));
        head.add(Collections.singletonList("匹配类型"));

        // 4. 构建数据行
        List<List<Object>> data = new ArrayList<>();
        if (list != null && !list.isEmpty()) {
            if (!list.isEmpty()) {
                log.info("样例数据 - chassisOnlinePlanDate: {}, versionDate: {}",
                        list.get(0).getChassisOnlinePlanDate(),
                        list.get(0).getVersionDate());
            }
            for (HostRequirementComparisonRespVO vo : list) {
                List<Object> row = new ArrayList<>();
                row.add(parseToLocalDateOrText(vo.getChassisOnlinePlanDate()));  // 第1列：当前版本日期
                row.add(vo.getProductModel());
                row.add(vo.getProductionOrder());
                row.add(vo.getBareMachineOrderNo());
                row.add(vo.getDrivingUnitOrderNo());
                row.add(vo.getQuota1());
                row.add(vo.getQuota2());
                row.add(vo.getUnitQuantity());
                row.add(parseToLocalDateOrText(vo.getVersionDate()));           // 第9列：对比版本日期
                row.add(vo.getCylinderName());
                row.add(vo.getMaterialNo());
                row.add(vo.getTeliCode());
                row.add(vo.getConfig());
                row.add(vo.getRequiredQuantity());
                row.add(vo.getFallbackMatched() == null ? "" :
                        (vo.getFallbackMatched() == 0 ? "正常匹配" :
                                (vo.getFallbackMatched() == 1 ? "后备匹配" : "无配置")));
                data.add(row);
            }
        }

        // 5. 输出 Excel
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = URLEncoder.encode("主机需求对比.xlsx", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename*=utf-8''" + fileName);
        EasyExcel.write(response.getOutputStream())
                .head(head)
                .sheet("数据")
                .doWrite(data);
    }

    @GetMapping("/available-dates")
    @Operation(summary = "获取可选的导入日期列表")
    @PreAuthorize("@ss.hasPermission('buyer:host-requirement-comparison:query')")
    public CommonResult<List<String>> getAvailableDates() {
        List<String> dates = hostRequirementComparisonService.getAvailableImportDates();
        return success(dates);
    }


    private LocalDate parseToLocalDate(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return null;
        }
        dateStr = dateStr.trim();
        // 定义常见格式列表（按顺序尝试）
        List<DateTimeFormatter> formatters = Arrays.asList(
                DateTimeFormatter.ISO_LOCAL_DATE,                           // yyyy-MM-dd
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),        // 带时间
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S"),      // 带一个毫秒
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SS"),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"),
                DateTimeFormatter.ofPattern("yyyy/MM/dd"),                 // yyyy/MM/dd
                DateTimeFormatter.ofPattern("yyyy/M/d"),                   // yyyy/M/d
                DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"),        // yyyy/MM/dd 带时间
                DateTimeFormatter.ofPattern("yyyy/M/d HH:mm:ss"),
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"),      // ISO 带 T
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS")
        );
        for (DateTimeFormatter fmt : formatters) {
            try {
                return LocalDate.parse(dateStr, fmt);
            } catch (DateTimeParseException ignored) {
            }
        }
        // 兜底：尝试截取前10个字符，并替换斜杠为横线，再尝试解析
        try {
            String str = dateStr.length() >= 10 ? dateStr.substring(0, 10) : dateStr;
            str = str.replace('/', '-');
            return LocalDate.parse(str);
        } catch (Exception e) {
            return null;
        }
    }

    private Object parseToLocalDateOrText(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return null;
        }
        dateStr = dateStr.trim();
        // 定义常见日期格式列表（按顺序尝试）
        List<DateTimeFormatter> formatters = Arrays.asList(
                DateTimeFormatter.ISO_LOCAL_DATE,                           // yyyy-MM-dd
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),        // 带时间
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S"),      // 带一个毫秒
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SS"),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS"),
                DateTimeFormatter.ofPattern("yyyy/MM/dd"),                 // yyyy/MM/dd
                DateTimeFormatter.ofPattern("yyyy/M/d"),                   // yyyy/M/d
                DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"),        // yyyy/MM/dd 带时间
                DateTimeFormatter.ofPattern("yyyy/M/d HH:mm:ss"),
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"),      // ISO 带 T
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS")
        );
        for (DateTimeFormatter fmt : formatters) {
            try {
                return LocalDate.parse(dateStr, fmt);   // 解析成功 → 返回 LocalDate (Excel 日期)
            } catch (DateTimeParseException ignored) {
            }
        }

//        try {
//            // 匹配常见的月/日格式，并补充当前年份
//            String[] parts = dateStr.split("[/-]");
//            if (parts.length == 2) {
//                int month = Integer.parseInt(parts[0]);
//                int day = Integer.parseInt(parts[1]);
//                int year = LocalDate.now().getYear();
//                return LocalDate.of(year, month, day);
//            }
//        } catch (Exception e) {
//            // 仍失败则返回原始字符串
//        }

        // 兜底：截取前10个字符并替换斜杠，再尝试一次
        try {
            String str = dateStr.length() >= 10 ? dateStr.substring(0, 10) : dateStr;
            str = str.replace('/', '-');
            return LocalDate.parse(str);
        } catch (Exception e) {
            // 实在无法解析为日期，返回原始字符串（保证不丢失数据）
            return dateStr;
        }
    }
}