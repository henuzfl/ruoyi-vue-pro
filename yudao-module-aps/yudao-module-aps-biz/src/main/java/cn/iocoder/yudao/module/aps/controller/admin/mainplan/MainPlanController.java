package cn.iocoder.yudao.module.aps.controller.admin.mainplan;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.metadata.data.ReadCellData;
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
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.math.BigDecimal;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;

import cn.iocoder.yudao.framework.apilog.core.annotation.ApiAccessLog;
import static cn.iocoder.yudao.framework.apilog.core.enums.OperateTypeEnum.*;

import cn.iocoder.yudao.module.aps.controller.admin.mainplan.vo.*;
import cn.iocoder.yudao.module.aps.dal.dataobject.mainplan.MainPlanDO;
import cn.iocoder.yudao.module.aps.service.mainplan.MainPlanService;
import org.springframework.web.multipart.MultipartFile;
import com.alibaba.excel.metadata.Cell; // 注意包路径
import com.alibaba.excel.metadata.data.ReadCellData; // 确保导入


@Tag(name = "管理后台 - 主计划")
@RestController
@RequestMapping("/aps/main-plan")
@Validated
@Slf4j
public class MainPlanController {

    @Resource
    private MainPlanService mainPlanService;

    @PostMapping("/create")
    @Operation(summary = "创建主计划")
    @PreAuthorize("@ss.hasPermission('aps:main-plan:create')")
    public CommonResult<BigDecimal> createMainPlan(@Valid @RequestBody MainPlanSaveReqVO createReqVO) {
        return success(mainPlanService.createMainPlan(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新主计划")
    @PreAuthorize("@ss.hasPermission('aps:main-plan:update')")
    public CommonResult<Boolean> updateMainPlan(@Valid @RequestBody MainPlanSaveReqVO updateReqVO) {
        mainPlanService.updateMainPlan(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除主计划")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('aps:main-plan:delete')")
    public CommonResult<Boolean> deleteMainPlan(@RequestParam("id") BigDecimal id) {
        mainPlanService.deleteMainPlan(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得主计划")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('aps:main-plan:query')")
    public CommonResult<MainPlanRespVO> getMainPlan(@RequestParam("id") BigDecimal id) {
        MainPlanDO mainPlan = mainPlanService.getMainPlan(id);
        return success(BeanUtils.toBean(mainPlan, MainPlanRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得主计划分页")
    @PreAuthorize("@ss.hasPermission('aps:main-plan:query')")
    public CommonResult<PageResult<MainPlanRespVO>> getMainPlanPage(@Valid MainPlanPageReqVO pageReqVO) {
        PageResult<MainPlanDO> pageResult = mainPlanService.getMainPlanPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, MainPlanRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出主计划 Excel")
    @PreAuthorize("@ss.hasPermission('aps:main-plan:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportMainPlanExcel(@Valid MainPlanPageReqVO pageReqVO,
                                    HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<MainPlanDO> list = mainPlanService.getMainPlanPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "主计划.xls", "数据", MainPlanRespVO.class,
                BeanUtils.toBean(list, MainPlanRespVO.class));
    }

    @PostMapping("/import-excel")
    @Operation(summary = "导入主计划 Excel")
    @PreAuthorize("@ss.hasPermission('aps:main-plan:import')")
    @ApiAccessLog(operateType = IMPORT)
    public CommonResult<Integer> importMainPlan(@RequestParam("file") MultipartFile file) throws IOException {
        // 使用同步读取，获取原始数据（键为列索引，值为单元格对象）
        List<Map<Integer, Object>> dataList = EasyExcel.read(file.getInputStream()).sheet().doReadSync();
        log.info("原始数据行数：{}", dataList.size());

        List<MainPlanImportReqVO> importList = new ArrayList<>();

        for (Map<Integer, Object> row : dataList) {
            // 打印原始行数据（可选，用于调试）
            log.debug("原始行数据：{}", row);

            // 手动构建 VO
            MainPlanImportReqVO vo = new MainPlanImportReqVO();
            // vo.setProductionOrderNo(getCellStringValue(row.get(0)));
            String orderNo = getCellStringValue(row.get(0));
            if (orderNo != null && !orderNo.isEmpty()) {
                orderNo = String.format("%012d", new java.math.BigDecimal(orderNo).longValue());
            }
            vo.setProductionOrderNo(orderNo);
            // vo.setProductionOrderNo(getCellStringValue(row.get(0)));
            vo.setAssemblyMaterialNo(getCellStringValue(row.get(1)));
            vo.setMainMaterialDesc(getCellStringValue(row.get(2)));
            vo.setScheduledDate(parseLocalDateTime(row.get(3))); // 使用增强版解析
            vo.setScheduledQuantity(parseBigDecimal(row.get(4)));
            vo.setProductionWorkshop(getCellStringValue(row.get(5)));
            vo.setCompletedQuantity(parseBigDecimal(row.get(6)));
            log.info("构建的1 vo: CompletedQuantity={}",vo.getCompletedQuantity());
            log.info("构建的2 vo: CompletedQuantity={}",row.get(6));

            // 校验日期（可根据业务决定是否允许空）
            if (vo.getScheduledDate() == null) {
                throw new IllegalArgumentException("第 " + (importList.size() + 1) + " 行排产时间格式错误或为空，请检查 Excel 文件");
            }

            importList.add(vo);
            log.info("构建的 vo: productionOrderNo={}, assemblyMaterialNo={}, scheduledDate={}",
                    vo.getProductionOrderNo(), vo.getAssemblyMaterialNo(), vo.getScheduledDate());
        }

        int count = mainPlanService.importMainPlan(importList);
        return success(count);
    }

    // 辅助方法：将单元格对象转为字符串（保留原有）
    private String getCellStringValue(Object cell) {
        if (cell == null) return null;
        if (cell instanceof String) return (String) cell;
        if (cell instanceof Number) return String.valueOf(cell);
        if (cell instanceof Date) return cell.toString(); // 备用
        return cell.toString();
    }

    // 辅助方法：解析数字
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
    //
    private LocalDateTime parseLocalDateTime(Object cell) {
        if (cell == null) return null;

        // 1. 如果已经是 LocalDateTime，直接返回
        if (cell instanceof LocalDateTime) {
            return (LocalDateTime) cell;
        }

        // 2. 如果是 java.util.Date，转换为 LocalDateTime
        if (cell instanceof Date) {
            return ((Date) cell).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        }

        // 3. 如果是字符串，尝试多种格式解析
        if (cell instanceof String) {
            String str = (String) cell;
            // 定义支持的日期时间格式（按优先级排序）
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
                    // 尝试解析为 LocalDateTime（如果格式包含时间）
                    if (formatter.toString().contains("HH")) {
                        return LocalDateTime.parse(str, formatter);
                    } else {
                        // 否则解析为 LocalDate，并转为当天的开始时间
                        LocalDate date = LocalDate.parse(str, formatter);
                        return date.atStartOfDay();
                    }
                } catch (DateTimeParseException e) {
                    // 忽略，继续尝试下一个格式
                }
            }
            log.warn("无法解析日期字符串: {}", str);
        }

        // 4. 其他类型无法处理，返回 null
        return null;
    }

    @DeleteMapping("/clear-all")
    @Operation(summary = "清空主计划")
    @PreAuthorize("@ss.hasPermission('aps:main-plan:delete')")
    public CommonResult<Boolean> clearAllMainPlan() {
        mainPlanService.clearAllMainPlan();
        return success(true);
    }

}