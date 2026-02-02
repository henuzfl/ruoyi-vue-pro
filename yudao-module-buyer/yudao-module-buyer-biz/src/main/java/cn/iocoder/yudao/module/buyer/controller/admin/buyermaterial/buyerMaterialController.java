package cn.iocoder.yudao.module.buyer.controller.admin.buyermaterial;

import cn.iocoder.yudao.module.buyer.dal.dataobject.buyermaterial.buyerMaterialDO;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

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

import cn.iocoder.yudao.module.buyer.controller.admin.buyermaterial.vo.*;
import cn.iocoder.yudao.module.buyer.service.buyermaterial.buyerMaterialService;

@Tag(name = "管理后台 - 备件查询")
@RestController
@RequestMapping("/buyer/buyer-material")
@Validated
public class buyerMaterialController {

    @Resource
    private buyerMaterialService dataImportService;

    @GetMapping("/page")
    @Operation(summary = "获得备件查询导入分页")
    @PreAuthorize("@ss.hasPermission('buyer:material-import:query')")
    public CommonResult<PageResult<buyerMaterialPageReqVO>> getDataImportPage(@Valid buyerMaterialPageReqVO pageReqVO) {
        PageResult<buyerMaterialDO> pageResult = dataImportService.getDataImportPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, buyerMaterialPageReqVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出营销数据导入 Excel")
    @PreAuthorize("@ss.hasPermission('aps:data-import:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportDataImportExcel(@Valid buyerMaterialPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<buyerMaterialDO> list = dataImportService.getDataImportPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "备件数据导入.xls", "数据", buyerMaterialPageReqVO.class,
                        BeanUtils.toBean(list, buyerMaterialPageReqVO.class));
    }

}