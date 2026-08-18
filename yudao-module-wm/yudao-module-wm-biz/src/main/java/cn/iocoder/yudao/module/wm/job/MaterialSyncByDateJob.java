package cn.iocoder.yudao.module.wm.job;

import cn.iocoder.yudao.framework.quartz.core.handler.JobHandler;
import cn.iocoder.yudao.framework.tenant.core.context.TenantContextHolder;
import cn.iocoder.yudao.module.wm.service.material.SapMaterialService;
import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component("materialSyncByDateJob")
@Slf4j
public class MaterialSyncByDateJob implements JobHandler {

    @Autowired
    private SapMaterialService sapMaterialService;

    @Override
    public String execute(String param) throws Exception {
        log.info("开始执行物料主数据按日期同步定时任务");
        TenantContextHolder.setTenantId(1L);
        DynamicDataSourceContextHolder.push("oracle");
        try {
            LocalDate yesterday = LocalDate.now().minusDays(1);
            LocalDate endDate = LocalDate.now().plusDays(1);
            log.info("【定时任务开始】当前线程: {}, 租户ID: {}, 数据源: {}",
                    Thread.currentThread().getName(),
                    TenantContextHolder.getTenantId(),
                    DynamicDataSourceContextHolder.peek()
            );
            int count = sapMaterialService.syncMaterialsByDate(yesterday, endDate);
            log.info("【定时任务调用】当前线程: {}, 租户ID: {}, 数据源: {}",
                    Thread.currentThread().getName(),
                    TenantContextHolder.getTenantId(),
                    DynamicDataSourceContextHolder.peek()
            );
            log.info("物料主数据同步完成，共 {} 条", count);
            DynamicDataSourceContextHolder.push("master");
            return "同步成功，共 " + count + " 条";
        } catch (Exception e) {
            log.error("物料主数据同步异常", e);
            throw new RuntimeException("物料主数据同步异常: " + e.getMessage(), e);
        } finally {
            DynamicDataSourceContextHolder.clear();
            TenantContextHolder.clear();
        }
    }
}