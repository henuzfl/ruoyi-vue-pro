package cn.iocoder.yudao.module.buyer.controller.admin.hostconfig;

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

import cn.iocoder.yudao.module.buyer.controller.admin.hostconfig.vo.*;
import cn.iocoder.yudao.module.buyer.dal.dataobject.hostconfig.HostConfigDO;
import cn.iocoder.yudao.module.buyer.service.hostconfig.HostConfigService;

@Tag(name = "管理后台 - 主机配置")
@RestController
@RequestMapping("/buyer/host-config")
@Validated
public class HostConfigController {

    @Resource
    private HostConfigService hostConfigService;

    @PostMapping("/create")
    @Operation(summary = "创建主机配置")
    @PreAuthorize("@ss.hasPermission('buyer:host-config:create')")
    public CommonResult<BigDecimal> createHostConfig(@Valid @RequestBody HostConfigSaveReqVO createReqVO) {
        return success(hostConfigService.createHostConfig(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新主机配置")
    @PreAuthorize("@ss.hasPermission('buyer:host-config:update')")
    public CommonResult<Boolean> updateHostConfig(@Valid @RequestBody HostConfigSaveReqVO updateReqVO) {
        hostConfigService.updateHostConfig(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除主机配置")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('buyer:host-config:delete')")
    public CommonResult<Boolean> deleteHostConfig(@RequestParam("id") BigDecimal id) {
        hostConfigService.deleteHostConfig(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得主机配置")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('buyer:host-config:query')")
    public CommonResult<HostConfigRespVO> getHostConfig(@RequestParam("id") BigDecimal id) {
        HostConfigDO hostConfig = hostConfigService.getHostConfig(id);
        return success(BeanUtils.toBean(hostConfig, HostConfigRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得主机配置分页")
    @PreAuthorize("@ss.hasPermission('buyer:host-config:query')")
    public CommonResult<PageResult<HostConfigRespVO>> getHostConfigPage(@Valid HostConfigPageReqVO pageReqVO) {
        PageResult<HostConfigDO> pageResult = hostConfigService.getHostConfigPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, HostConfigRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出主机配置 Excel")
    @PreAuthorize("@ss.hasPermission('buyer:host-config:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportHostConfigExcel(@Valid HostConfigPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<HostConfigDO> list = hostConfigService.getHostConfigPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "主机配置.xls", "数据", HostConfigRespVO.class,
                        BeanUtils.toBean(list, HostConfigRespVO.class));
    }

}