package cn.iocoder.yudao.module.wm.controller.admin.materialkittingtool;

import cn.iocoder.yudao.module.wm.controller.admin.kittingmaster.vo.KittingMasterRespVO;
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
import java.net.URLEncoder;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
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

import cn.iocoder.yudao.module.wm.controller.admin.materialkittingtool.vo.*;
import cn.iocoder.yudao.module.wm.dal.dataobject.materialkittingtool.MaterialKittingToolDO;
import cn.iocoder.yudao.module.wm.service.materialkittingtool.MaterialKittingToolService;

@Tag(name = "管理后台 - 齐套工具")
@RestController
@RequestMapping("/wm/material-kitting-tool")
@Validated
@Slf4j
public class MaterialKittingToolController {

    @Resource
    private MaterialKittingToolService materialKittingToolService;


    @GetMapping("/get")
    @Operation(summary = "获得齐套工具")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('wm:material-kitting-tool:query')")
    public CommonResult<MaterialKittingToolRespVO> getMaterialKittingTool(@RequestParam("id") BigDecimal id) {
        MaterialKittingToolDO materialKittingTool = materialKittingToolService.getMaterialKittingTool(id);
        return success(BeanUtils.toBean(materialKittingTool, MaterialKittingToolRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得齐套工具分页")
    @PreAuthorize("@ss.hasPermission('wm:material-kitting-tool:query')")
    public CommonResult<PageResult<MaterialKittingToolRespVO>> getMaterialKittingToolPage(@Valid MaterialKittingToolPageReqVO pageReqVO) {
        log.info("=== 明细齐套 ===");
        PageResult<MaterialKittingToolDO> pageResult = materialKittingToolService.selectMaterialKittingToolByParams(pageReqVO);

        // 将 DO 转换为 RespVO
        PageResult<MaterialKittingToolRespVO> voPageResult = BeanUtils.toBean(pageResult, MaterialKittingToolRespVO.class);

        return success(voPageResult);
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出齐套工具 Excel")
    @PreAuthorize("@ss.hasPermission('wm:material-kitting-tool:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportMaterialKittingToolExcel(@Valid MaterialKittingToolPageReqVO pageReqVO,
                                               HttpServletResponse response) throws IOException {
        long startTime = System.currentTimeMillis();

        try {
            log.info("开始导出齐套工具数据，查询参数: {}", pageReqVO);

            // 1. 设置不分页（获取所有数据）
            // 使用 Integer.MAX_VALUE 作为不分页的标志
            pageReqVO.setPageNo(1);
            pageReqVO.setPageSize(Integer.MAX_VALUE);

            // 2. 调用查询方法 - 修改这里
            // 方案一：如果服务有专门导出方法，返回List
            List<MaterialKittingToolDO> list = materialKittingToolService.selectMasterKittingToolForExport(pageReqVO);

            // 方案二：如果服务只有分页方法，可以这样调用
            // PageResult<MaterialKittingToolDO> pageResult = materialKittingToolService.selectMaterialKittingToolByParams(pageReqVO);
            // List<MaterialKittingToolDO> list = pageResult.getList();

            // 3. 检查数据是否为空
            if (list == null || list.isEmpty()) {
                log.warn("导出数据为空，查询条件: {}", pageReqVO);
                // 导出空文件 - 修正为使用正确的VO类
                ExcelUtils.write(response, "订单齐套工具.xls", "数据",
                        MaterialKittingToolRespVO.class, Collections.emptyList());
                return;
            }

            // 4. 验证数据量，防止内存溢出
            if (list.size() > 50000) {
                throw new RuntimeException("数据量过大，最多支持导出50000条数据，请缩小查询范围");
            }

            log.info("成功查询到 {} 条数据，开始导出", list.size());

            // 5. 数据转换 - 修正为使用正确的VO类
            List<MaterialKittingToolRespVO> exportList = BeanUtils.toBean(list, MaterialKittingToolRespVO.class);

            // 6. 设置响应头，支持中文文件名
            String fileName = generateFileName(pageReqVO);
            setResponseHeaders(response, fileName);

            // 7. 导出Excel - 使用正确的参数顺序和VO类
            ExcelUtils.write(response, fileName, "数据",
                    MaterialKittingToolRespVO.class, exportList);

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

    @GetMapping("/calculatepage")
    @Operation(summary = "获得齐套工具分页")
    @PreAuthorize("@ss.hasPermission('wm:material-kitting-tool:calculatequery')")
    public CommonResult<PageResult<MaterialKittingToolRespVO>> getCalculateKittingToolPage(@Valid MaterialKittingToolPageReqVO pageReqVO) {
        log.info("=== 明细齐套 ===");
        PageResult<MaterialKittingToolDO> pageResult = materialKittingToolService.selectCalculateKittingToolByParams(pageReqVO);

        // 将 DO 转换为 RespVO
        PageResult<MaterialKittingToolRespVO> voPageResult = BeanUtils.toBean(pageResult, MaterialKittingToolRespVO.class);

        return success(voPageResult);
    }

    @GetMapping("/exportcalculate-excel")
    @Operation(summary = "导出齐套工具 Excel")
    @PreAuthorize("@ss.hasPermission('wm:material-kitting-tool:calculateexport')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportCalculateKittingToolExcel(@Valid MaterialKittingToolPageReqVO pageReqVO,
                                               HttpServletResponse response) throws IOException {
        long startTime = System.currentTimeMillis();

        try {
            log.info("开始导出齐套工具数据，查询参数: {}", pageReqVO);

            // 1. 设置不分页（获取所有数据）
            // 使用 Integer.MAX_VALUE 作为不分页的标志
            pageReqVO.setPageNo(1);
            pageReqVO.setPageSize(Integer.MAX_VALUE);

            // 2. 调用查询方法 - 修改这里
            // 方案一：如果服务有专门导出方法，返回List
            List<MaterialKittingToolDO> list = materialKittingToolService.selectCalculateKittingToolForExport(pageReqVO);

            // 方案二：如果服务只有分页方法，可以这样调用
            // PageResult<MaterialKittingToolDO> pageResult = materialKittingToolService.selectMaterialKittingToolByParams(pageReqVO);
            // List<MaterialKittingToolDO> list = pageResult.getList();

            // 3. 检查数据是否为空
            if (list == null || list.isEmpty()) {
                log.warn("导出数据为空，查询条件: {}", pageReqVO);
                // 导出空文件 - 修正为使用正确的VO类
                ExcelUtils.write(response, "订单齐套工具.xls", "数据",
                        MaterialKittingToolRespVO.class, Collections.emptyList());
                return;
            }

            // 4. 验证数据量，防止内存溢出
            if (list.size() > 50000) {
                throw new RuntimeException("数据量过大，最多支持导出50000条数据，请缩小查询范围");
            }

            log.info("成功查询到 {} 条数据，开始导出", list.size());

            // 5. 数据转换 - 修正为使用正确的VO类
            List<MaterialKittingToolRespVO> exportList = BeanUtils.toBean(list, MaterialKittingToolRespVO.class);

            // 6. 设置响应头，支持中文文件名
            String fileName = generateFileName(pageReqVO);
            setResponseHeaders(response, fileName);

            // 7. 导出Excel - 使用正确的参数顺序和VO类
            ExcelUtils.write(response, fileName, "数据",
                    MaterialKittingToolRespVO.class, exportList);

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
    private String generateFileName(MaterialKittingToolPageReqVO pageReqVO) {
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
//    public void exportMaterialKittingToolExcel(@Valid MaterialKittingToolPageReqVO pageReqVO,
//              HttpServletResponse response) throws IOException {
//        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
//        List<MaterialKittingToolDO> list = materialKittingToolService.selectMaterialKittingToolByParams(pageReqVO).getList();
//        // 导出 Excel
//        ExcelUtils.write(response, "齐套工具.xls", "数据", MaterialKittingToolRespVO.class,
//                        BeanUtils.toBean(list, MaterialKittingToolRespVO.class));
//    }

}