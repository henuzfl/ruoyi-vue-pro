package cn.iocoder.yudao.module.buyer.controller.admin.deliver;

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

import cn.iocoder.yudao.module.buyer.controller.admin.deliver.vo.*;
import cn.iocoder.yudao.module.buyer.dal.dataobject.deliver.DeliverDO;
import cn.iocoder.yudao.module.buyer.service.deliver.DeliverService;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "管理后台 - 配送与采购报表")
@RestController
@RequestMapping("/buyer/deliver")
@Validated
public class DeliverController {

    @Resource
    private DeliverService deliverService;

    @PostMapping("/create")
    @Operation(summary = "创建配送与采购报表")
    @PreAuthorize("@ss.hasPermission('buyer:deliver:create')")
    public CommonResult<Long> createDeliver(@Valid @RequestBody DeliverSaveReqVO createReqVO) {
        return success(deliverService.createDeliver(createReqVO));  // 返回 Long
    }

    @PutMapping("/update")
    @Operation(summary = "更新配送与采购报表")
    @PreAuthorize("@ss.hasPermission('buyer:deliver:update')")
    public CommonResult<Boolean> updateDeliver(@Valid @RequestBody DeliverSaveReqVO updateReqVO) {
        deliverService.updateDeliver(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除配送与采购报表")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('buyer:deliver:delete')")
    public CommonResult<Boolean> deleteDeliver(@RequestParam("id") Long id) {
        deliverService.deleteDeliver(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得配送与采购报表")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('buyer:deliver:query')")
    public CommonResult<DeliverRespVO> getDeliver(@RequestParam("id") Long id) {
        DeliverDO deliver = deliverService.getDeliver(id);
        return success(BeanUtils.toBean(deliver, DeliverRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得配送与采购报表分页")
    @PreAuthorize("@ss.hasPermission('buyer:deliver:query')")
    public CommonResult<PageResult<DeliverRespVO>> getDeliverPage(@Valid DeliverPageReqVO pageReqVO) {
        PageResult<DeliverDO> pageResult = deliverService.getDeliverPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, DeliverRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出配送与采购报表 Excel")
    @PreAuthorize("@ss.hasPermission('buyer:deliver:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportDeliverExcel(@Valid DeliverPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<DeliverDO> list = deliverService.getDeliverPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "配送与采购报表.xls", "数据", DeliverRespVO.class,
                        BeanUtils.toBean(list, DeliverRespVO.class));
    }

    // 添加导入接口
    @PostMapping("/import-excel")
    @Operation(summary = "导入配送与采购报表数据")
    @PreAuthorize("@ss.hasPermission('buyer:deliver:import')")
    @ApiAccessLog(operateType = IMPORT)
    public CommonResult<Integer> importDeliver(@RequestParam("file") MultipartFile file) throws IOException {
        int count = deliverService.importDeliver(file);
        return success(count);
    }

}