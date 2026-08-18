package cn.iocoder.yudao.module.wm.controller.admin.material;

import cn.iocoder.yudao.framework.apilog.core.annotation.ApiAccessLog;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.module.aps.service.mainplan.MainPlanService;
import cn.iocoder.yudao.module.aps.service.mainplan.MainPlanService;
import cn.iocoder.yudao.module.wm.controller.admin.material.vo.*;
import cn.iocoder.yudao.module.wm.controller.admin.material.vo.SapMaterialQueryReqVO;
import cn.iocoder.yudao.module.wm.service.material.SapMaterialService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static cn.iocoder.yudao.framework.apilog.core.enums.OperateTypeEnum.EXPORT;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - SAP物料主数据")
@RestController
@RequestMapping("/wm/sap-material")
@Validated
@Slf4j
public class SapMaterialController {

    @Resource
    private SapMaterialService sapMaterialService;

    @Resource
    private MainPlanService mainPlanService;

    @PostMapping("/search")
    @Operation(summary = "批量查询物料信息")
    @PreAuthorize("@ss.hasPermission('wm:sap-material:query')")
    public CommonResult<List<MaterialResultVO>> searchMaterials(@Valid @RequestBody SapMaterialQueryReqVO reqVO) {
        log.info("[searchMaterials] 批量查询，物料数: {}", reqVO.getMaterialNumbers().size());
        List<MaterialResultVO> list = sapMaterialService.searchMaterials(reqVO);
        return success(list);
    }

    @GetMapping("/get")
    @Operation(summary = "查询单个物料信息")
    @PreAuthorize("@ss.hasPermission('wm:sap-material:query')")
    public CommonResult<MaterialResultVO> getMaterial(@RequestParam("matnr") String matnr,
                                                      @RequestParam(value = "plant", required = false) String plant) {
        MaterialResultVO material = sapMaterialService.getMaterial(matnr, plant);
        return success(material);
    }

    @PostMapping("/sync-from-mainplan")
    @Operation(summary = "从主计划同步SAP物料到物料主数据导入表")
    @PreAuthorize("@ss.hasPermission('wm:sap-material:sync')")
    public CommonResult<String> syncMaterialsFromMainPlan() {
        // 1. 获取去重物料号
        List<String> materialNos = mainPlanService.getDistinctAssemblyMaterialNo();
        if (materialNos.isEmpty()) {
            return success("主计划表中无物料数据");
        }
        log.info("待同步物料数：{}", materialNos.size());

        // 2. 同步
        int syncCount = sapMaterialService.syncMaterialsFromMainPlan(materialNos);

        return success(String.format("同步完成，成功处理 %d 个物料", syncCount));
    }

    @PostMapping("/sync-single")
    @Operation(summary = "单个物料同步到导入表")
    @PreAuthorize("@ss.hasPermission('wm:sap-material:sync')")
    public CommonResult<String> syncSingleMaterial(@RequestParam("matnr") String matnr,
                                                   @RequestParam(value = "plant", required = false) String plant) {
        // 调用 Service 同步单个物料
        boolean success = sapMaterialService.syncSingleMaterial(matnr, plant);
        if (success) {
            return success("同步成功，物料号：" + matnr);
        } else {
            return success("物料未找到或同步失败：" + matnr);
        }
    }

    // ========== 私有方法 ==========

    private String generateFileName(SapMaterialQueryReqVO reqVO) {
        StringBuilder sb = new StringBuilder("SAP物料信息");
        if (reqVO.getPlant() != null) {
            sb.append("_").append(reqVO.getPlant());
        }
        sb.append("_").append(new SimpleDateFormat("yyyyMMdd").format(new Date()));
        sb.append(".xls");
        return sb.toString();
    }

    private void setResponseHeaders(HttpServletResponse response, String fileName) throws IOException {
        String encodedFileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + encodedFileName);
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
    }

    private void handleExportException(HttpServletResponse response, Exception e, String defaultMsg) {
        try {
            response.reset();
            response.setContentType("application/json;charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            String errorMsg = defaultMsg;
            if (e.getMessage() != null && e.getMessage().contains("数据量过大")) {
                errorMsg = e.getMessage();
            }
            response.getWriter().write("{\"code\": 500, \"msg\": \"" + errorMsg + "\"}");
        } catch (IOException ioException) {
            log.error("处理导出异常时发生错误", ioException);
        }
    }
}