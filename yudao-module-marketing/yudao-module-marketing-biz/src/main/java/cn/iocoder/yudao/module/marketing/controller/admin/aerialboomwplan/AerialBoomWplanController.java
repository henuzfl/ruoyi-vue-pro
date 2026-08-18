package cn.iocoder.yudao.module.marketing.controller.admin.aerialboomwplan;

import cn.iocoder.yudao.module.marketing.service.aerialboomwplan.AerialBoomWplanImportListener;
import com.alibaba.excel.annotation.format.DateTimeFormat;
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
import java.time.LocalDate;
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

import cn.iocoder.yudao.module.marketing.controller.admin.aerialboomwplan.vo.*;
import cn.iocoder.yudao.module.marketing.dal.dataobject.aerialboomwplan.AerialBoomWplanDO;
import cn.iocoder.yudao.module.marketing.service.aerialboomwplan.AerialBoomWplanService;
import org.springframework.web.multipart.MultipartFile;
import java.util.regex.Pattern;

@Tag(name = "管理后台 - 高机臂式周计划")
@RestController
@RequestMapping("/marketing/aerial-boom-wplan")
@Validated
@Slf4j
public class AerialBoomWplanController {

    @Resource
    private AerialBoomWplanService aerialBoomWplanService;

    @PostMapping("/create")
    @Operation(summary = "创建高机臂式周计划")
    @PreAuthorize("@ss.hasPermission('marketing:aerial-boom-wplan:create')")
    public CommonResult<Long> createAerialBoomWplan(@Valid @RequestBody AerialBoomWplanSaveReqVO createReqVO) {
        return success(aerialBoomWplanService.createAerialBoomWplan(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新高机臂式周计划")
    @PreAuthorize("@ss.hasPermission('marketing:aerial-boom-wplan:update')")
    public CommonResult<Boolean> updateAerialBoomWplan(@Valid @RequestBody AerialBoomWplanSaveReqVO updateReqVO) {
        aerialBoomWplanService.updateAerialBoomWplan(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除高机臂式周计划")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('marketing:aerial-boom-wplan:delete')")
    public CommonResult<Boolean> deleteAerialBoomWplan(@RequestParam("id") Long id) {
        aerialBoomWplanService.deleteAerialBoomWplan(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得高机臂式周计划")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('marketing:aerial-boom-wplan:query')")
    public CommonResult<AerialBoomWplanRespVO> getAerialBoomWplan(@RequestParam("id") Long id) {
        AerialBoomWplanDO aerialBoomWplan = aerialBoomWplanService.getAerialBoomWplan(id);
        return success(BeanUtils.toBean(aerialBoomWplan, AerialBoomWplanRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得高机臂式周计划分页")
    @PreAuthorize("@ss.hasPermission('marketing:aerial-boom-wplan:query')")
    public CommonResult<PageResult<AerialBoomWplanRespVO>> getAerialBoomWplanPage(@Valid AerialBoomWplanPageReqVO pageReqVO) {
        PageResult<AerialBoomWplanDO> pageResult = aerialBoomWplanService.getAerialBoomWplanPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, AerialBoomWplanRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出高机臂式周计划 Excel")
    @PreAuthorize("@ss.hasPermission('marketing:aerial-boom-wplan:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportAerialBoomWplanExcel(@Valid AerialBoomWplanPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<AerialBoomWplanDO> list = aerialBoomWplanService.getAerialBoomWplanPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "高机臂式周计划.xls", "数据", AerialBoomWplanRespVO.class,
                        BeanUtils.toBean(list, AerialBoomWplanRespVO.class));
    }

    @PostMapping("/import")
    @Operation(summary = "导入高机臂式周计划")
    @PreAuthorize("@ss.hasPermission('marketing:aerial-boom-wplan:import')")
    @ApiAccessLog(operateType = IMPORT)
    public CommonResult<Boolean> importExcel(
            @RequestParam("file") MultipartFile file,
            @RequestParam("importTime") @org.springframework.format.annotation.DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate importTime) throws IOException {

        log.info("===== 接收到导入请求 =====");
        log.info("文件名称: {}", file.getOriginalFilename());
        log.info("文件大小: {} bytes", file.getSize());
        log.info("导入批次时间: {}", importTime);

        // 可选：打印请求头，检查 token 等
        // ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        // if (attributes != null) {
        //     HttpServletRequest request = attributes.getRequest();
        //     log.info("Authorization: {}", request.getHeader("Authorization"));
        // }

        aerialBoomWplanService.importExcel(file, importTime);
        log.info("导入处理完成");
        return success(true);
    }


}