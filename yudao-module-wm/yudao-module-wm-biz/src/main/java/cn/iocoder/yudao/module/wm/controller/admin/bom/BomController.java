package cn.iocoder.yudao.module.wm.controller.admin.bom;

import cn.iocoder.yudao.module.aps.dal.dataobject.bomimport.BomImportDO;
import cn.iocoder.yudao.module.aps.dal.dataobject.mainplan.MainPlanDO;
import cn.iocoder.yudao.module.aps.service.bomimport.BomImportService;
import cn.iocoder.yudao.module.aps.service.mainplan.MainPlanService;
import cn.iocoder.yudao.module.aps.service.plan.PlanService;
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

import cn.iocoder.yudao.module.wm.controller.admin.bom.vo.*;
import cn.iocoder.yudao.module.wm.dal.dataobject.bom.BomDO;
import cn.iocoder.yudao.module.wm.service.bom.BomService;

@Tag(name = "管理后台 - BOM管理")
@RestController
@RequestMapping("/wm/bom")
@Validated
@Slf4j
public class BomController {

    @Resource
    private BomService bomService;

    @Resource
    private BomImportService bomImportService; // 注入BOM导入服务

    @Resource
    private MainPlanService mainPlanService;   // 需要根据实际包名导入

    @PostMapping("/create")
    @Operation(summary = "创建BOM")
    @PreAuthorize("@ss.hasPermission('wm:bom:create')")
    public CommonResult<Long> createBom(@Valid @RequestBody BomSaveReqVO createReqVO) {
        return success(bomService.createBom(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新BOM")
    @PreAuthorize("@ss.hasPermission('wm:bom:update')")
    public CommonResult<Boolean> updateBom(@Valid @RequestBody BomSaveReqVO updateReqVO) {
        bomService.updateBom(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除BOM")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('wm:bom:delete')")
    public CommonResult<Boolean> deleteBom(@RequestParam("id") Long id) {
        bomService.deleteBom(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得BOM")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('wm:bom:query')")
    public CommonResult<BomRespVO> getBom(@RequestParam("id") Long id) {
        BomDO bom = bomService.getBom(id);
        return success(BeanUtils.toBean(bom, BomRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得BOM分页")
    @PreAuthorize("@ss.hasPermission('wm:bom:query')")
    public CommonResult<PageResult<BomRespVO>> getBomPage(@Valid BomPageReqVO pageReqVO) {
        PageResult<BomDO> pageResult = bomService.getBomPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, BomRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出BOM Excel")
    @PreAuthorize("@ss.hasPermission('wm:bom:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportBomExcel(@Valid BomPageReqVO pageReqVO,
                               HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<BomDO> list = bomService.getBomPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "BOM数据.xls", "数据", BomRespVO.class,
                BeanUtils.toBean(list, BomRespVO.class));
    }

    // ============ SAP BOM查询相关接口 ============

    @PostMapping("/get-from-sap")
    @Operation(summary = "从SAP获取BOM信息")
    @PreAuthorize("@ss.hasPermission('wm:bom:query')")
    public CommonResult<List<Map<String, Object>>> getBomFromSap(@RequestBody Map<String, Object> conditions) {
        List<Map<String, Object>> result = bomService.getBomFromSap(conditions);
        return success(result);
    }

    @GetMapping("/get-by-material")
    @Operation(summary = "根据物料号和工厂获取BOM")
    @PreAuthorize("@ss.hasPermission('wm:bom:query')")
    public CommonResult<List<Map<String, Object>>> getBomByMaterial(
            @RequestParam("materialNumber") String materialNumber,
            @RequestParam("plant") String plant) {
        List<Map<String, Object>> result = bomService.getBomByMaterial(materialNumber, plant);
        return success(result);
    }

    @GetMapping("/list-tree")
    @Operation(summary = "获取BOM树形结构")
    @PreAuthorize("@ss.hasPermission('wm:bom:query')")
    public CommonResult<List<Map<String, Object>>> getBomTree(
            @RequestParam(value = "materialNumber", required = false) String materialNumber,
            @RequestParam(value = "plant", required = false) String plant,
            @RequestParam(value = "topLevel", defaultValue = "true") Boolean topLevel) {
        // 这里可以调用BomService的方法获取树形结构的BOM
        // 需要根据你的业务逻辑实现
        return success(new ArrayList<>());
    }

    @PostMapping("/sync-from-sap")
    @Operation(summary = "从SAP同步BOM数据到本地")
    @PreAuthorize("@ss.hasPermission('wm:bom:sync')")
    public CommonResult<Boolean> syncBomFromSap(@RequestBody SyncBomReqVO syncReqVO) {
        // 创建同步请求VO
        bomService.syncBomFromSap(syncReqVO);
        return success(true);
    }
    // ============ BOM导入表相关接口 ============

    @PostMapping("/sync-and-import")
    @Operation(summary = "从SAP同步并导入BOM到导入表")
    @PreAuthorize("@ss.hasPermission('wm:bom:sync')")
    public CommonResult<String> syncAndImportBom(
            @RequestParam("materialNumber") String materialNumber,
            @RequestParam("plant") String plant) {

        // 1. 从SAP获取BOM数据
        List<Map<String, Object>> sapBomList = bomService.getBomFromSap(materialNumber, plant, null);

        if (sapBomList == null || sapBomList.isEmpty()) {
            return success("从SAP获取的BOM数据为空");
        }

        // 2. 导入到BOM导入表
        String importResult = bomImportService.importBomFromSapData(sapBomList, materialNumber, plant);

        return success(importResult);
    }

    @PostMapping("/sync-and-delete-import")
    @Operation(summary = "批量从APS主计划同步BOM并导入（忽略参数，全量同步）")
    @PreAuthorize("@ss.hasPermission('wm:bom:sync')")
    public CommonResult<String> syncDeleteAndImportBom(
            @RequestParam(value = "materialNumber", required = false) String materialNumber, // 废弃
            @RequestParam(value = "plant", required = false) String plant) {                // 废弃

        // 1. 获取去重的物料号列表
        List<String> distinctMaterialNoList = mainPlanService.getDistinctAssemblyMaterialNo();
        if (distinctMaterialNoList.isEmpty()) {
            return success("APS主计划表中无物料数据，无需同步");
        }
        // 打印总数量
        int totalCount = distinctMaterialNoList.size();
        log.info("开始批量同步BOM，共 {} 个物料", totalCount);

        List<String> successMaterials = new ArrayList<>();
        List<String> failMaterials = new ArrayList<>();
        Map<String, String> errorMap = new HashMap<>();

        // 2. 循环处理每个物料
        int currentIndex = 0;
        for (String materialNo : distinctMaterialNoList) {   // ✅ 直接遍历物料号
            currentIndex++;
            String plantCode = "6400";   // 临时固定工厂，可根据需要从配置读取
            // 打印进度
            log.info("正在处理物料 [{}/{}]：{}", currentIndex, totalCount, materialNo);

            try {
                // 2.1 删除该物料在BOM导入表中的历史数据
                bomImportService.clearBomImportData(materialNo, plantCode);

                // 2.2 从SAP获取BOM数据
                List<Map<String, Object>> sapBomList = bomService.getBomFromSap(materialNo, plantCode, null);
                if (sapBomList == null || sapBomList.isEmpty()) {
                    successMaterials.add(materialNo + " (SAP无数据)");
                    continue;
                }

                // 2.3 导入到BOM导入表
                bomImportService.importBomFromSapData(sapBomList, materialNo, plantCode);
                successMaterials.add(materialNo + "(" + plantCode + ")");
            } catch (Exception e) {
                failMaterials.add(materialNo + "(" + plantCode + ")");
                errorMap.put(materialNo + "@" + plantCode, e.getMessage());
            }
        }

        // 3. 获取去重的物料号列表
//        List<String> distinctComponentNoList = mainPlanService.getDistinctComponentMaterialNo();
//        if (distinctComponentNoList.isEmpty()) {
//            return success("APS主计划表子件中无物料数据，无需同步");
//        }
//
//
//        // 2. 循环处理每个物料
//        for (String materialNo : distinctComponentNoList) {   // ✅ 直接遍历物料号
//            String plantCode = "6400";   // 临时固定工厂，可根据需要从配置读取
//
//            try {
//                // 2.1 删除该物料在BOM导入表中的历史数据
//                bomImportService.clearBomImportData(materialNo, plantCode);
//
//                // 2.2 从SAP获取BOM数据
//                List<Map<String, Object>> sapBomList = bomService.getBomFromSap(materialNo, plantCode, null);
//                if (sapBomList == null || sapBomList.isEmpty()) {
//                    successMaterials.add(materialNo + " (SAP无数据)");
//                    continue;
//                }
//
//                // 2.3 导入到BOM导入表
//                bomImportService.importBomFromSapData(sapBomList, materialNo, plantCode);
//                successMaterials.add(materialNo + "(" + plantCode + ")");
//            } catch (Exception e) {
//                failMaterials.add(materialNo + "(" + plantCode + ")");
//                errorMap.put(materialNo + "@" + plantCode, e.getMessage());
//            }
//        }

        // 3. 构建返回信息
        StringBuilder resultMsg = new StringBuilder();
        resultMsg.append("BOM同步全部完成。成功物料：").append(successMaterials.size())
                .append("个，失败物料：").append(failMaterials.size()).append("个。");
        if (!failMaterials.isEmpty()) {
            resultMsg.append("失败详情：").append(errorMap);
        }

        return success(resultMsg.toString());
    }


    @GetMapping("/import-list")
    @Operation(summary = "查询BOM导入表数据")
    @PreAuthorize("@ss.hasPermission('wm:bom:query')")
    public CommonResult<List<BomImportDO>> getBomImportList(
            @RequestParam("mainMaterialNo") String mainMaterialNo,
            @RequestParam("plant") String plant) {

        List<BomImportDO> bomList = bomImportService.getBomImportList(mainMaterialNo, plant);
        return success(bomList);
    }

    @DeleteMapping("/clear-import")
    @Operation(summary = "清空BOM导入表数据")
    @PreAuthorize("@ss.hasPermission('wm:bom:delete')")
    public CommonResult<Boolean> clearBomImport(
            @RequestParam("mainMaterialNo") String mainMaterialNo,
            @RequestParam("plant") String plant) {

        bomImportService.clearBomImportData(mainMaterialNo, plant);
        return success(true);
    }

    @GetMapping("/import-page")
    @Operation(summary = "获得BOM导入分页")
    @PreAuthorize("@ss.hasPermission('wm:bom:query')")
    public CommonResult<PageResult<BomImportDO>> getBomImportPage(
            @RequestParam(value = "mainMaterialNo", required = false) String mainMaterialNo,
            @RequestParam(value = "plant", required = false) String plant,
            @RequestParam(value = "pageNo", defaultValue = "1") Integer pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize) {

        // 创建一个简单的分页查询（这里假设BomImportService有分页查询方法）
        // 如果没有，需要先在BomImportService中实现
        // 暂时返回空结果
        return success(new PageResult<>(Collections.emptyList(), 0L));
    }

    @PostMapping("/import-from-sap-direct")
    @Operation(summary = "直接导入SAP BOM数据到导入表")
    @PreAuthorize("@ss.hasPermission('wm:bom:sync')")
    public CommonResult<String> importBomFromSapDirect(
            @RequestParam("materialNumber") String materialNumber,
            @RequestParam("plant") String plant,
            @RequestParam(value = "force", defaultValue = "false") Boolean force) {

        try {
            // 如果强制导入，先清空已有数据
            if (force) {
                bomImportService.clearBomImportData(materialNumber, plant);
            }

            // 获取SAP数据
            List<Map<String, Object>> sapBomList = bomService.getBomFromSap(materialNumber, plant, null);

            if (sapBomList == null || sapBomList.isEmpty()) {
                return success("从SAP获取的BOM数据为空");
            }

            // 导入到BOM导入表
            String importResult = bomImportService.importBomFromSapData(sapBomList, materialNumber, plant);

            return success(importResult);

        } catch (Exception e) {
            return CommonResult.error(500, "导入失败: " + e.getMessage());
        }
    }

    @GetMapping("/check-import-exists")
    @Operation(summary = "检查BOM导入数据是否存在")
    @PreAuthorize("@ss.hasPermission('wm:bom:query')")
    public CommonResult<Boolean> checkBomImportExists(
            @RequestParam("mainMaterialNo") String mainMaterialNo,
            @RequestParam("plant") String plant) {

        List<BomImportDO> bomList = bomImportService.getBomImportList(mainMaterialNo, plant);
        return success(bomList != null && !bomList.isEmpty());
    }
}