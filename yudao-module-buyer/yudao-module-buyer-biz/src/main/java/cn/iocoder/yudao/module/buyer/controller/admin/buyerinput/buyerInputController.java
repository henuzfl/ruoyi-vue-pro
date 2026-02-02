package cn.iocoder.yudao.module.buyer.controller.admin.buyerinput;

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

import cn.iocoder.yudao.module.buyer.controller.admin.buyerinput.vo.*;
import cn.iocoder.yudao.module.buyer.dal.dataobject.buyerinput.buyerInputDO;
import cn.iocoder.yudao.module.buyer.service.buyerinput.buyerInputService;

@Tag(name = "管理后台 - 需求输入")
@RestController
@RequestMapping("/buyer/buyer-input")
@Validated
public class buyerInputController {

    @Resource
    private buyerInputService buyerInputService;

    @PostMapping("/create")
    @Operation(summary = "创建需求输入")
    @PreAuthorize("@ss.hasPermission('buyer:buyer-input:create')")
    public CommonResult<Long> createbuyerInput(@Valid @RequestBody buyerInputSaveReqVO createReqVO) {
        return success(buyerInputService.createbuyerInput(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新需求输入")
    @PreAuthorize("@ss.hasPermission('buyer:buyer-input:update')")
    public CommonResult<Boolean> updatebuyerInput(@Valid @RequestBody buyerInputSaveReqVO updateReqVO) {
        buyerInputService.updatebuyerInput(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除需求输入")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('buyer:buyer-input:delete')")
    public CommonResult<Boolean> deletebuyerInput(@RequestParam("id") Long id) {
        buyerInputService.deletebuyerInput(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得需求输入")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('buyer:buyer-input:query')")
    public CommonResult<buyerInputRespVO> getbuyerInput(@RequestParam("id") Long id) {
        buyerInputDO buyerInput = buyerInputService.getbuyerInput(id);
        return success(BeanUtils.toBean(buyerInput, buyerInputRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得需求输入分页")
    @PreAuthorize("@ss.hasPermission('buyer:buyer-input:query')")
    public CommonResult<PageResult<buyerInputRespVO>> getbuyerInputPage(@Valid buyerInputPageReqVO pageReqVO) {
        PageResult<buyerInputDO> pageResult = buyerInputService.getbuyerInputPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, buyerInputRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出需求输入 Excel")
    @PreAuthorize("@ss.hasPermission('buyer:buyer-input:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportbuyerInputExcel(@Valid buyerInputPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<buyerInputDO> list = buyerInputService.getbuyerInputPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "需求输入.xls", "数据", buyerInputRespVO.class,
                        BeanUtils.toBean(list, buyerInputRespVO.class));
    }

}