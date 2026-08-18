package cn.iocoder.yudao.module.wm.controller.admin.kittingmaster;

import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.WriteTable;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.LocalDateTime;
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
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

import cn.iocoder.yudao.module.wm.controller.admin.kittingmaster.vo.*;
import cn.iocoder.yudao.module.wm.dal.dataobject.kittingmaster.KittingMasterDO;
import cn.iocoder.yudao.module.wm.service.kittingmaster.KittingMasterService;

@Tag(name = "管理后台 - 订单齐套工具")
@RestController
@RequestMapping("/wm/kitting-master")
@Validated
@Slf4j
public class KittingMasterController {

    @Resource
    private KittingMasterService kittingMasterService;


    @GetMapping("/get")
    @Operation(summary = "获得齐套工具")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('wm:kitting-master:query')")
    public CommonResult<KittingMasterRespVO> getKittingMaster(@RequestParam("id") BigDecimal id) {
        KittingMasterDO kittingMaster = kittingMasterService.getKittingMaster(id);
        return success(BeanUtils.toBean(kittingMaster, KittingMasterRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得齐套工具分页")
    @PreAuthorize("@ss.hasPermission('wm:kitting-master:query')")
    public CommonResult<PageResult<KittingMasterRespVO>> getKittingMasterPage(@Valid KittingMasterPageReqVO pageReqVO) {
        log.info("=== 齐套 ===");
        PageResult<KittingMasterDO> pageResult = kittingMasterService.selectKittingMasterByParams(pageReqVO);

        // 将 DO 转换为 RespVO
        PageResult<KittingMasterRespVO> voPageResult = BeanUtils.toBean(pageResult, KittingMasterRespVO.class);

        return success(voPageResult);
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出齐套工具 Excel")
    @PreAuthorize("@ss.hasPermission('wm:kitting-master:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportKittingMasterExcel(@Valid KittingMasterPageReqVO pageReqVO,
                                         HttpServletResponse response) throws IOException {
        long startTime = System.currentTimeMillis();

        try {
            log.info("开始导出齐套工具数据，查询参数: {}", pageReqVO);

            // 1. 设置不分页（获取所有数据）
            // 使用 Integer.MAX_VALUE 作为不分页的标志
            pageReqVO.setPageNo(1);
            pageReqVO.setPageSize(Integer.MAX_VALUE);

            // 2. 调用查询方法
            PageResult<KittingMasterDO> pageResult = kittingMasterService.selectKittingMasterByParams(pageReqVO);
            List<KittingMasterDO> list = pageResult.getList();

            // 3. 检查数据是否为空
            if (list == null || list.isEmpty()) {
                log.warn("导出数据为空，查询条件: {}", pageReqVO);
                // 导出空文件
                ExcelUtils.write(response, "订单齐套工具.xls", "数据",
                        KittingMasterRespVO.class, Collections.emptyList());
                return;
            }

            // 4. 验证数据量，防止内存溢出
            if (list.size() > 50000) {
                throw new RuntimeException("数据量过大，最多支持导出50000条数据，请缩小查询范围");
            }

            log.info("成功查询到 {} 条数据，开始导出", list.size());

            // 5. 数据转换
            List<KittingMasterRespVO> exportList = BeanUtils.toBean(list, KittingMasterRespVO.class);

            // 6. 设置响应头，支持中文文件名
            String fileName = generateFileName(pageReqVO);
            setResponseHeaders(response, fileName);

            // 7. 导出Excel - 使用正确的参数顺序
            // 若依框架 ExcelUtils.write 的常见签名:
            // write(HttpServletResponse response, String fileName, String sheetName, Class<T> head, Collection<T> data)
            ExcelUtils.write(response, fileName, "数据",
                    KittingMasterRespVO.class, exportList);

            long costTime = System.currentTimeMillis() - startTime;
            log.info("导出完成，数据量: {}，耗时: {}ms", exportList.size(), costTime);

        } catch (IllegalArgumentException e) {
            log.error("导出参数错误", e);
            handleExportException(response, e, "导出参数错误，请检查查询条件");
        } catch (Exception e) {
            log.error("导出Excel异常", e);
            handleExportException(response, e, "导出失败：" + e.getMessage());
        }
    }

    /**
     * 齐套存储过程
     */
    @PostMapping("/complecalculate")
    @Operation(summary = "执行齐套存储过程")
    @PreAuthorize("@ss.hasPermission('wm:kitting-maste:complecalculate')")
    public CommonResult<Boolean> calculateStock() {
        try {
            kittingMasterService.callUpdatecompProcedure();
            return success(true);
        } catch (Exception e) {
            // 这里应该记录日志，并根据业务需求返回适当的错误信息
            return CommonResult.error(500, "齐套计算失败: " + e.getMessage());
        }
    }
    @GetMapping("/calculatepage")
    @Operation(summary = "获得齐套存储过程工具查询数据")
    @PreAuthorize("@ss.hasPermission('wm:kitting-master:calculatequery')")
    public CommonResult<PageResult<KittingMasterRespVO>> getKittingcalculatePage(@Valid KittingMasterPageReqVO pageReqVO) {
        log.info("=== 齐套 ===");
        PageResult<KittingMasterDO> pageResult = kittingMasterService.selectKittingcalculateByParams(pageReqVO);

        // 将 DO 转换为 RespVO
        PageResult<KittingMasterRespVO> voPageResult = BeanUtils.toBean(pageResult, KittingMasterRespVO.class);

        return success(voPageResult);
    }

    @GetMapping("/exportcalculate-excel")
    @Operation(summary = "导出存储过程齐套工具 Excel")
    @PreAuthorize("@ss.hasPermission('wm:kitting-master:exportcalculate')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportKittingcalculateExcel(@Valid KittingMasterPageReqVO pageReqVO,
                                         HttpServletResponse response) throws IOException {
        long startTime = System.currentTimeMillis();
        log.info("=== 齐套执行计划导出 ===");
        try {
            log.info("开始导出齐套工具数据，查询参数: {}", pageReqVO);

            // 1. 设置不分页（获取所有数据）
            // 使用 Integer.MAX_VALUE 作为不分页的标志
            pageReqVO.setPageNo(1);
            pageReqVO.setPageSize(Integer.MAX_VALUE);

            // 2. 调用查询方法
            PageResult<KittingMasterDO> pageResult = kittingMasterService.selectKittingcalculateByParams(pageReqVO);
            List<KittingMasterDO> list = pageResult.getList();

            // 3. 检查数据是否为空
            if (list == null || list.isEmpty()) {
                log.warn("导出数据为空，查询条件: {}", pageReqVO);
                // 导出空文件
                ExcelUtils.write(response, "订单齐套工具.xls", "数据",
                        KittingMasterRespVO.class, Collections.emptyList());
                return;
            }

            // 4. 验证数据量，防止内存溢出
            if (list.size() > 50000) {
                throw new RuntimeException("数据量过大，最多支持导出50000条数据，请缩小查询范围");
            }

            log.info("成功查询到 {} 条数据，开始导出", list.size());

            // 5. 数据转换
            List<KittingMasterRespVO> exportList = BeanUtils.toBean(list, KittingMasterRespVO.class);

            // 6. 设置响应头，支持中文文件名
            String fileName = generateFileName(pageReqVO);
            setResponseHeaders(response, fileName);

            // 7. 导出Excel - 使用正确的参数顺序
            // 若依框架 ExcelUtils.write 的常见签名:
            // write(HttpServletResponse response, String fileName, String sheetName, Class<T> head, Collection<T> data)
            ExcelUtils.write(response, fileName, "数据",
                    KittingMasterRespVO.class, exportList);

            long costTime = System.currentTimeMillis() - startTime;
            log.info("导出完成，数据量: {}，耗时: {}ms", exportList.size(), costTime);

        } catch (IllegalArgumentException e) {
            log.error("导出参数错误", e);
            handleExportException(response, e, "导出参数错误，请检查查询条件");
        } catch (Exception e) {
            log.error("导出Excel异常", e);
            handleExportException(response, e, "导出失败：" + e.getMessage());
        }
    }

    /**
     * 生成文件名
     */
    private String generateFileName(KittingMasterPageReqVO pageReqVO) {
        StringBuilder fileName = new StringBuilder("订单齐套工具");

        // 根据查询条件添加日期信息
        if (pageReqVO.getScheduledDate() != null && pageReqVO.getScheduledDate().length >= 2) {
            // 假设 scheduledDate 是 Date 类型
            Date startDate = pageReqVO.getScheduledDate()[0];
            Date endDate = pageReqVO.getScheduledDate()[1];

            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
            if (startDate != null) {
                fileName.append("_").append(formatter.format(startDate));
            }
            if (endDate != null) {
                fileName.append("_").append(formatter.format(endDate));
            }
        }

        // 当前日期
        fileName.append("_").append(new SimpleDateFormat("yyyyMMdd").format(new Date()));
        fileName.append(".xls");

        return fileName.toString();
    }

    /**
     * 设置响应头
     */
    private void setResponseHeaders(HttpServletResponse response, String fileName) throws IOException {
        // 对文件名进行URL编码，支持中文
        String encodedFileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");

        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + encodedFileName);

        // 禁用缓存
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
    }

    /**
     * 处理导出异常
     */
    private void handleExportException(HttpServletResponse response, Exception e, String defaultMsg) {
        try {
            response.reset();
            response.setContentType("application/json;charset=UTF-8");
            response.setCharacterEncoding("UTF-8");

            String errorMsg = defaultMsg;

            // 根据异常类型提供更友好的错误信息
            if (e instanceof IllegalArgumentException && e.getMessage() != null) {
                if (e.getMessage().contains("subList")) {
                    errorMsg = "导出数据参数错误，请联系管理员";
                } else if (e.getMessage().contains("fromIndex") && e.getMessage().contains("toIndex")) {
                    errorMsg = "分页参数错误，请检查查询条件";
                }
            } else if (e instanceof SQLException) {
                errorMsg = "数据库查询异常，请稍后重试";
            } else if (e.getMessage() != null && e.getMessage().contains("数据量过大")) {
                errorMsg = e.getMessage();
            }

            response.getWriter().write("{\"code\": 500, \"msg\": \"" + errorMsg + "\"}");
        } catch (IOException ioException) {
            log.error("处理导出异常时发生错误", ioException);
        }
    }

}