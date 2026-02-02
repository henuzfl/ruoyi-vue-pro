package cn.iocoder.yudao.module.wm.controller.admin.kittingmaster;

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
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<KittingMasterDO> list = kittingMasterService.selectKittingMasterByParams(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "订单齐套工具.xls", "数据", KittingMasterRespVO.class,
                        BeanUtils.toBean(list, KittingMasterRespVO.class));
    }

}