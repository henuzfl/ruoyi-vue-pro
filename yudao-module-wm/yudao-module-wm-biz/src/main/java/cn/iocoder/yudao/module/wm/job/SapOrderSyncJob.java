package cn.iocoder.yudao.module.wm.job;

import cn.iocoder.yudao.framework.quartz.core.handler.JobHandler;
import cn.iocoder.yudao.framework.tenant.core.context.TenantContextHolder;
import cn.iocoder.yudao.module.wm.controller.admin.sapordersync.vo.SapOrderQueryReqVO;
import cn.iocoder.yudao.module.wm.dal.mysql.sapordersync.SapOrderSyncMapper;
import cn.iocoder.yudao.module.wm.service.sapordersync.SapOrderSyncService;
import com.google.common.util.concurrent.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class SapOrderSyncJob implements JobHandler {

    @Autowired
    private SapOrderSyncService sapOrderSyncService;

    @Autowired
    private SapOrderSyncMapper sapOrderSyncMapper;

    @Autowired
    @Qualifier("sapOrderSyncExecutor")
    private ThreadPoolTaskExecutor executor;

    @Value("${sap.default-plant:6400}")
    private String defaultPlant;

    // 限流器：每秒最多 5 次 SAP 调用（可根据实际调整）
    private final RateLimiter rateLimiter = RateLimiter.create(5.0);

    @Override
    public String execute(String param) throws Exception {
        log.info("[SapOrderSyncJob] 开始异步分批同步 SAP 生产订单");
        TenantContextHolder.setTenantId(1L);
        try {
            // 1. 获取所有订单号
            List<String> allOrders = sapOrderSyncMapper.selectDistinctProductionOrderNos();
            if (allOrders.isEmpty()) {
                return "无订单号需同步";
            }
            log.info("待同步订单总数: {}", allOrders.size());

            // 2. 分批，每批 50 个订单号
            int batchSize = 50;
            List<List<String>> batches = partitionList(allOrders, batchSize);
            log.info("共分为 {} 个批次，每批最多 {} 个", batches.size(), batchSize);

            CountDownLatch latch = new CountDownLatch(batches.size());
            // 线程安全的结果收集
            int[] successTotal = {0};
            int[] failTotal = {0};
            long[] totalSyncedRecords = {0};

            // 3. 提交批次任务到线程池
            for (List<String> batch : batches) {
                executor.submit(() -> {
                    int success = 0;
                    int fail = 0;
                    long records = 0;
                    for (String aufnr : batch) {
                        if (aufnr == null || aufnr.trim().isEmpty()) continue;
                        try {
                            rateLimiter.acquire(); // 限流
                            SapOrderQueryReqVO reqVO = new SapOrderQueryReqVO();
                            reqVO.setPlant(defaultPlant);
                            reqVO.setAufnr(aufnr.trim());
                            int count = sapOrderSyncService.syncOrderFromSap(reqVO);
                            success++;
                            records += count;
                            log.debug("订单 [{}] 同步成功，更新 {} 条", aufnr, count);
                        } catch (Exception e) {
                            fail++;
                            log.error("订单 [{}] 同步失败: {}", aufnr, e.getMessage(), e);
                        }
                    }
                    synchronized (SapOrderSyncJob.class) {
                        successTotal[0] += success;
                        failTotal[0] += fail;
                        totalSyncedRecords[0] += records;
                    }
                    latch.countDown();
                });
            }

            // 4. 等待所有批次完成，超时时间 2 小时
            boolean finished = latch.await(2, TimeUnit.HOURS);
            if (!finished) {
                log.warn("同步任务等待超时，部分批次可能未完成");
            }

            String result = String.format("同步完成，成功 %d 个订单 (共 %d 条记录)，失败 %d 个",
                    successTotal[0], totalSyncedRecords[0], failTotal[0]);
            log.info(result);
            return result;
        } finally {
            TenantContextHolder.clear();
        }
    }

    // 列表切分工具方法
    private <T> List<List<T>> partitionList(List<T> list, int size) {
        if (list == null || list.isEmpty()) return Collections.emptyList();
        List<List<T>> partitions = new ArrayList<>();
        for (int i = 0; i < list.size(); i += size) {
            partitions.add(list.subList(i, Math.min(i + size, list.size())));
        }
        return partitions;
    }
}