package cn.iocoder.yudao.module.wm.job;

import cn.iocoder.yudao.framework.quartz.core.handler.JobHandler;
import cn.iocoder.yudao.framework.tenant.core.context.TenantContextHolder;
import cn.iocoder.yudao.module.aps.service.bomimport.BomImportService;
import cn.iocoder.yudao.module.aps.service.mainplan.MainPlanService;
import cn.iocoder.yudao.module.wm.service.bom.BomService;
import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class BomSyncJob implements JobHandler {

    @Resource
    private MainPlanService mainPlanService;

    @Resource
    private BomService bomService;

    @Resource
    private BomImportService bomImportService;
    @Override
    public String execute(String param) throws Exception {
        log.info("[execute][开始执行BOM同步定时任务，参数: {}]", param);

        // 显式切换到 Oracle 数据源（确保后续物料主数据查询能正确执行）
        DynamicDataSourceContextHolder.push("oracle");
        try {
            TenantContextHolder.setTenantId(1L);

            // 1. 获取需要同步的物料号列表
            List<String> distinctMaterialNoList = mainPlanService.getDistinctAssemblyMaterialNo();
            if (distinctMaterialNoList == null || distinctMaterialNoList.isEmpty()) {
                return "APS主计划表中无物料数据，无需同步";
            }

            int totalCount = distinctMaterialNoList.size();
            log.info("开始批量同步BOM，共 {} 个物料", totalCount);

            String plantCode = "6400";  // 固定工厂
            List<String> successMaterials = new ArrayList<>();
            List<String> failMaterials = new ArrayList<>();
            Map<String, String> errorMap = new HashMap<>();

            int currentIndex = 0;
            for (String materialNo : distinctMaterialNoList) {
                currentIndex++;
                log.info("正在处理物料 [{}/{}]：{}", currentIndex, totalCount, materialNo);

                try {
                    // 2.1 删除该物料在BOM导入表中的历史数据
                    bomImportService.clearBomImportData(materialNo, plantCode);

                    // 2.2 从SAP获取BOM数据（内含过滤逻辑）
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
                    log.error("同步物料 {} 失败", materialNo, e);
                }
            }

            // 3. 构建返回信息
            StringBuilder resultMsg = new StringBuilder();
            resultMsg.append("BOM同步全部完成。成功物料：").append(successMaterials.size())
                    .append("个，失败物料：").append(failMaterials.size()).append("个。");
            if (!failMaterials.isEmpty()) {
                resultMsg.append("失败详情：").append(errorMap);
            }

            log.info(resultMsg.toString());
            return resultMsg.toString();

        } finally {
            TenantContextHolder.clear();
            DynamicDataSourceContextHolder.poll(); // 确保弹出数据源
        }
    }
}