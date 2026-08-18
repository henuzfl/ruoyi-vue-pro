package cn.iocoder.yudao.module.wm.job;

import cn.iocoder.yudao.framework.quartz.core.handler.JobHandler;
import cn.iocoder.yudao.framework.tenant.core.context.TenantContextHolder;
import cn.iocoder.yudao.module.wm.service.sapordersync.SapOrderSyncService;
import cn.iocoder.yudao.module.wm.util.sap.SapOrderUtils;
import com.google.common.util.concurrent.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * SAP 订单按更新日期同步 Job
 * 只调用一次 SAP RFC，获取完整数据后批量处理
 */
@Component
@Slf4j
public class SapOrderSyncByDateJob implements JobHandler {

    @Autowired
    private SapOrderSyncService sapOrderSyncService;

    @Autowired
    private SapOrderUtils sapOrderUtils;

    @Autowired
    @Qualifier("sapOrderSyncExecutor")
    private ThreadPoolTaskExecutor executor;

    @Value("${sap.default-plant:6400}")
    private String defaultPlant;

    // 限流器：每秒最多 5 次 SAP 调用
    private final RateLimiter rateLimiter = RateLimiter.create(5.0);

    @Override
    public String execute(String param) throws Exception {
        log.info("[SapOrderSyncByDateJob] 开始同步 SAP 生产订单, 参数: {}", param);

        // 1. 解析日期参数
        DateRange dateRange = parseDateRange(param);
        log.info("同步日期范围: {} 至 {}", dateRange.startDate, dateRange.endDate);

        TenantContextHolder.setTenantId(1L);
        try {
            // 2. 【核心优化】只调用一次 SAP，获取完整数据
            String dateParam = formatDateRangeForSap(dateRange.startDate, dateRange.endDate);
            log.info("调用 SAP RFC 查询数据，日期参数: {}", dateParam);

            // 限流（控制 SAP 调用频率）
            rateLimiter.acquire();

            List<Map<String, Object>> rawData = sapOrderUtils.searchOrders(defaultPlant, null, dateParam);

            if (rawData == null || rawData.isEmpty()) {
                return String.format("日期范围 [%s 至 %s] 无更新的订单", dateRange.startDate, dateRange.endDate);
            }

            log.info("SAP 返回原始数据行数: {}", rawData.size());

            // 3. 【核心优化】直接批量同步，一次性完成删除+插入
            int insertedCount = sapOrderSyncService.syncOrdersFromSapRawData(rawData);

            String result = String.format("日期范围 [%s 至 %s] 同步完成，共插入 %d 条记录",
                    dateRange.startDate, dateRange.endDate, insertedCount);
            log.info(result);
            return result;

        } catch (Exception e) {
            log.error("SAP 订单同步失败", e);
            throw e;
        } finally {
            TenantContextHolder.clear();
        }
    }

    /**
     * 解析日期参数
     */
    private DateRange parseDateRange(String param) {
        LocalDate today = LocalDate.now();

        // 默认：昨天到明天（3天）
        if (param == null || param.trim().isEmpty()) {
            return new DateRange(today.minusDays(1), today.plusDays(1));
        }

        String trimmed = param.trim().toLowerCase();

        // 特殊关键字
        switch (trimmed) {
            case "yesterday":
                return new DateRange(today.minusDays(1), today.minusDays(1));
            case "today":
                return new DateRange(today, today);
            case "tomorrow":
                return new DateRange(today.plusDays(1), today.plusDays(1));
            case "week":
                LocalDate monday = today.minusDays(today.getDayOfWeek().getValue() - 1);
                return new DateRange(monday, today);
            case "month":
                LocalDate firstDay = today.withDayOfMonth(1);
                return new DateRange(firstDay, today);
            case "last7days":
                return new DateRange(today.minusDays(6), today);
            case "last30days":
                return new DateRange(today.minusDays(29), today);
        }

        // 尝试解析日期范围
        if (trimmed.contains(",")) {
            String[] parts = trimmed.split(",");
            if (parts.length >= 2) {
                try {
                    LocalDate start = LocalDate.parse(parts[0].trim(), DateTimeFormatter.ISO_LOCAL_DATE);
                    LocalDate end = LocalDate.parse(parts[1].trim(), DateTimeFormatter.ISO_LOCAL_DATE);
                    return new DateRange(start, end);
                } catch (Exception e) {
                    log.warn("解析日期范围失败: {}, 使用默认值", param);
                }
            }
        }

        // 尝试解析单天
        try {
            LocalDate date = LocalDate.parse(trimmed, DateTimeFormatter.ISO_LOCAL_DATE);
            return new DateRange(date, date);
        } catch (Exception e) {
            log.warn("解析日期参数失败: {}, 使用默认值（昨天到明天）", param);
            return new DateRange(today.minusDays(1), today.plusDays(1));
        }
    }

    /**
     * 格式化日期范围为 SAP 格式
     */
    private String formatDateRangeForSap(LocalDate startDate, LocalDate endDate) {
        DateTimeFormatter sapFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        if (startDate.equals(endDate)) {
            return startDate.format(sapFormatter);
        } else {
            return startDate.format(sapFormatter) + "-" + endDate.format(sapFormatter);
        }
    }

    /**
     * 日期范围内部类
     */
    private static class DateRange {
        final LocalDate startDate;
        final LocalDate endDate;

        DateRange(LocalDate startDate, LocalDate endDate) {
            this.startDate = startDate;
            this.endDate = endDate;
        }
    }
}