package cn.iocoder.yudao.module.wm.job;

import cn.iocoder.yudao.framework.quartz.core.handler.JobHandler;
import cn.iocoder.yudao.framework.tenant.core.context.TenantContextHolder;
import cn.iocoder.yudao.module.wm.service.masterallocation.MasterAllocationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("masterAllocationJob")  // 这个名称就是定时任务的 handler_name
@Slf4j
public class MasterAllocationJob implements JobHandler {

    @Autowired
    private MasterAllocationService masterAllocationService;

    @Override
    public String execute(String param) throws Exception {
        log.info("开始执行主计划分配定时任务，参数：{}", param);

        // 设置租户ID（根据你的系统实际情况调整，1 为默认租户）
        TenantContextHolder.setTenantId(1L);

        try {
            masterAllocationService.executeMasterAllocation();
            return "主计划分配存储过程执行成功";
        } catch (Exception e) {
            log.error("主计划分配存储过程执行异常", e);
            throw new RuntimeException("主计划分配存储过程执行异常: " + e.getMessage(), e);
        } finally {
            TenantContextHolder.clear(); // 清理租户上下文
        }
    }
}