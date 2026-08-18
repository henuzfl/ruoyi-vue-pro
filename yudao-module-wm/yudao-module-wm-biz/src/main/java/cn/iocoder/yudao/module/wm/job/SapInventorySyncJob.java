package cn.iocoder.yudao.module.wm.job;

import cn.iocoder.yudao.framework.quartz.core.handler.JobHandler;
import cn.iocoder.yudao.framework.tenant.core.context.TenantContextHolder;
import cn.iocoder.yudao.module.wm.controller.admin.realtimestock.vo.SyncResultVO;
import cn.iocoder.yudao.module.wm.service.realtimestock.SapInventoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("sapInventorySyncJob")
@Slf4j
public class SapInventorySyncJob implements JobHandler {

    @Autowired
    private SapInventoryService sapInventoryService;

    @Override
    public String execute(String param) throws Exception {
        log.info("开始执行 SAP 库存同步定时任务");
        // 设置租户 ID（默认为 1）
        TenantContextHolder.setTenantId(1L);
        try {
            SyncResultVO result = sapInventoryService.syncAllStock();
            if (result.getAllSuccess()) {
                log.info("SAP 库存同步完成，共处理 {} 条记录", result.getTotal());
                return String.format("同步成功，共 %d 条", result.getTotal());
            } else {
                log.warn("SAP 库存同步部分失败，失败数: {}", result.getFailCount());
                return String.format("同步失败 %d 条", result.getFailCount());
            }
        } catch (Exception e) {
            log.error("SAP 库存同步异常", e);
            throw new RuntimeException("SAP 库存同步异常: " + e.getMessage(), e);
        } finally {
            TenantContextHolder.clear();
        }
    }
}