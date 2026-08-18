package cn.iocoder.yudao.module.wm.service.openordersync;


import cn.iocoder.yudao.module.wm.controller.admin.openordersync.vo.SapPurchaseParamDTO;
import cn.iocoder.yudao.module.wm.controller.admin.openordersync.vo.SapPurchaseResultDTO;
import cn.iocoder.yudao.module.wm.dal.dataobject.openordersync.SyncOpenOrderDO;
import cn.iocoder.yudao.module.wm.dal.mysql.openordersync.OpenOrderSyncMapper;
import cn.iocoder.yudao.module.wm.util.SapPurchaseUtils;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.beans.factory.annotation.Qualifier;
import com.google.common.util.concurrent.RateLimiter;
import org.springframework.transaction.support.TransactionTemplate;


import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@DS("oracle") // 指定使用 Oracle 数据源
@Slf4j
public class OpenOrderSyncService {

    @Autowired
    private SapPurchaseUtils sapPurchaseUtils;

    @Autowired
    private OpenOrderSyncMapper openOrderSyncMapper;

    @Autowired
    private PlatformTransactionManager transactionManager;   // 新增

    private TransactionTemplate transactionTemplate;        // 改为非注入

    @PostConstruct
    public void init() {
        transactionTemplate = new TransactionTemplate(transactionManager);
        transactionTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        transactionTemplate.setTimeout(3600);
    }

    @Autowired
    @Qualifier("sapSyncExecutor")
    private ThreadPoolTaskExecutor sapSyncExecutor;

    // 全局限流器，每秒最多 10 个 SAP 请求（可根据 SAP 承受能力调整）
    private final RateLimiter rateLimiter = RateLimiter.create(10.0);

    /**
     * 同步采购未清订单数据（全量或按条件）
     * @param param SAP查询参数
     * @return 同步记录数
     */
    @Transactional(rollbackFor = Exception.class)
    public int syncPurchaseOrders(SapPurchaseParamDTO param) {
        log.info("开始同步采购订单数据，参数: {}", param);

        // 1. 从 SAP 获取数据
        List<SapPurchaseResultDTO> sapList = sapPurchaseUtils.getPurchaseOrders(param);
        if (sapList.isEmpty()) {
            log.info("SAP 无返回数据");
            return 0;
        }

        // 2. 转换为 DO，并补全公共字段
        List<SyncOpenOrderDO> doList = sapList.stream()
                .map(this::convertToDO)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        // 3. 先物理删除全部旧数据
        int deleted = openOrderSyncMapper.physicalDeleteAll();
        log.info("物理删除了 {} 条旧数据", deleted);

        // 4. 批量插入新数据
        int batchSize = 30;
        for (int i = 0; i < doList.size(); i += batchSize) {
            int end = Math.min(i + batchSize, doList.size());
            List<SyncOpenOrderDO> batch = doList.subList(i, end);
            openOrderSyncMapper.batchInsert(batch);
            log.info("已插入 {} 条", end);
        }

        return doList.size();
    }
    /**
     * 同步采购未清订单数据（直接展示）
     * @param param SAP查询参数
     * @return 同步记录数
     */
    @Transactional(rollbackFor = Exception.class)
    public List<SyncOpenOrderDO> syncPurchaseOrdersSap(SapPurchaseParamDTO param) {
        log.info("开始同步采购订单数据，参数: {}", param);

        // 1. 从 SAP 获取数据
        List<SapPurchaseResultDTO> sapList = sapPurchaseUtils.getPurchaseOrders(param);
        if (sapList.isEmpty()) {
            log.info("SAP 无返回数据");
            return null;
        }

        // 2. 转换为 DO，并补全公共字段
        List<SyncOpenOrderDO> doList = sapList.stream()
                .map(this::convertToDO)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        return doList;
    }

    /**
     * 异步分批 + 限流同步采购订单（专供定时任务使用）
     *
     * @param baseParam   基础查询参数（工厂、日期等）
     * @param materialNos 物料号列表
     * @return 同步的总订单行数
     */
    //@Transactional(rollbackFor = Exception.class, timeout = 3600) // 1小时事务超时
//    public int syncPurchaseOrdersByMaterialListAsync(SapPurchaseParamDTO baseParam, List<String> materialNos) {
//        if (materialNos == null || materialNos.isEmpty()) {
//            log.info("物料号列表为空，无需同步");
//            return 0;
//        }
//
//        log.info("开始异步分批同步，物料号总数: {}", materialNos.size());
//
//        // 1. 分批：每 50 个物料号为一个批次
//        int batchSize = 50;
//        List<List<String>> batches = partitionList(materialNos, batchSize);
//        log.info("共分为 {} 个批次，每批最多 {} 个物料号", batches.size(), batchSize);
//
//        // 2. 用于汇总所有订单数据的线程安全列表
//        List<SyncOpenOrderDO> allOrders = Collections.synchronizedList(new ArrayList<>());
//
//        // 3. 使用 CountDownLatch 等待所有批次完成
//        CountDownLatch latch = new CountDownLatch(batches.size());
//
//        // 4. 提交批次任务到线程池
//        for (List<String> batch : batches) {
//            sapSyncExecutor.submit(() -> {
//                try {
//                    processBatch(baseParam, batch, allOrders);
//                } catch (Exception e) {
//                    log.error("批次处理异常", e);
//                } finally {
//                    latch.countDown();
//                }
//            });
//        }
//
//        // 5. 等待所有批次完成（最长等待 2 小时）
//        try {
//            boolean completed = latch.await(2, TimeUnit.HOURS);
//            if (!completed) {
//                log.error("同步任务等待超时，部分批次未完成");
//                throw new RuntimeException("同步任务执行超时");
//            }
//        } catch (InterruptedException e) {
//            Thread.currentThread().interrupt();
//            throw new RuntimeException("同步任务被中断", e);
//        }
//
//        log.info("所有批次处理完成，总获取订单行数: {}", allOrders.size());
//
//        // 6. 全量更新本地表：先物理删除，再批量插入汇总数据
//        if (!allOrders.isEmpty()) {
//            int deleted = openOrderSyncMapper.physicalDeleteAll();
//            log.info("物理删除了 {} 条旧数据", deleted);
//
//            // 批量插入（每 1000 条一批，避免单次插入过大）
//            int insertBatchSize = 30;
//            for (int i = 0; i < allOrders.size(); i += insertBatchSize) {
//                int end = Math.min(i + insertBatchSize, allOrders.size());
//                List<SyncOpenOrderDO> subList = allOrders.subList(i, end);
//                openOrderSyncMapper.batchInsert(subList);
//                log.info("已插入 {} / {} 条订单数据", end, allOrders.size());
//            }
//        }
//
//        return allOrders.size();
//    }
// 移除 @Transactional 注解
    /**
     * 异步分批 + 限流同步采购订单（专供定时任务使用）
     * 已移除 @Transactional，改用编程式事务分段提交
     */
    public int syncPurchaseOrdersByMaterialListAsync(SapPurchaseParamDTO baseParam, List<String> materialNos) {
        if (materialNos == null || materialNos.isEmpty()) {
            log.info("物料号列表为空，无需同步");
            return 0;
        }

        log.info("开始异步分批同步，物料号总数: {}", materialNos.size());

        // 1. 分批拉取 SAP 数据（每批 50 个物料号）
        int batchSize = 50;
        List<List<String>> batches = partitionList(materialNos, batchSize);
        log.info("共分为 {} 个批次，每批最多 {} 个物料号", batches.size(), batchSize);

        List<SyncOpenOrderDO> allOrders = Collections.synchronizedList(new ArrayList<>());
        CountDownLatch latch = new CountDownLatch(batches.size());

        // 2. 提交批次任务到线程池
        for (List<String> batch : batches) {
            sapSyncExecutor.submit(() -> {
                try {
                    processBatch(baseParam, batch, allOrders);
                } catch (Exception e) {
                    log.error("批次处理异常", e);
                } finally {
                    latch.countDown();
                }
            });
        }

        // 3. 等待所有批次完成
        try {
            boolean completed = latch.await(2, TimeUnit.HOURS);
            if (!completed) {
                log.error("同步任务等待超时，部分批次未完成");
                throw new RuntimeException("同步任务执行超时");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("同步任务被中断", e);
        }

        log.info("所有批次处理完成，总获取订单行数: {}", allOrders.size());

        if (allOrders.isEmpty()) {
            return 0;
        }

        // 4. 分段提交（每 1000 条订单为一个独立事务）
        int segmentSize = 1000;
        for (int i = 0; i < allOrders.size(); i += segmentSize) {
            int end = Math.min(i + segmentSize, allOrders.size());
            List<SyncOpenOrderDO> segment = allOrders.subList(i, end);
            final int startIdx = i;

            transactionTemplate.execute(status -> {
                try {
                    // 第一段先物理删除全表
                    if (startIdx == 0) {
                        int deleted = openOrderSyncMapper.physicalDeleteAll();
                        log.info("物理删除了 {} 条旧数据", deleted);
                    }
                    // 分批插入
                    int insertBatchSize = 30;
                    for (int j = 0; j < segment.size(); j += insertBatchSize) {
                        int subEnd = Math.min(j + insertBatchSize, segment.size());
                        openOrderSyncMapper.batchInsert(segment.subList(j, subEnd));
                    }
                    log.info("已提交段 {}/{}，本段 {} 条", end, allOrders.size(), segment.size());
                    return null;
                } catch (Exception e) {
                    status.setRollbackOnly();
                    log.error("段插入失败，已回滚", e);
                    throw new RuntimeException("段插入失败", e);
                }
            });
        }

        return allOrders.size();
    }

    /**
     * 异步分批 + 限流同步采购订单（支持全量与按物料号）
     *
     * @param baseParam   基础查询参数（工厂、日期等）
     * @param materialNos 物料号列表（可为 null 或空，表示全量同步）
     * @return 同步的总订单行数
     */
    public int syncPurchaseOrdersListAsync(SapPurchaseParamDTO baseParam, List<String> materialNos) {
        // ----- 全量同步分支 -----
        if (materialNos == null || materialNos.isEmpty()) {
            log.info("物料号列表为空，开始全量同步");
            return syncFullPurchaseOrders(baseParam);
        }

        // ----- 按物料号同步（原有逻辑） -----
        log.info("开始异步分批同步，物料号总数: {}", materialNos.size());

        // 1. 分批拉取 SAP 数据（每批 50 个物料号）
        int batchSize = 50;
        List<List<String>> batches = partitionList(materialNos, batchSize);
        log.info("共分为 {} 个批次，每批最多 {} 个物料号", batches.size(), batchSize);

        List<SyncOpenOrderDO> allOrders = Collections.synchronizedList(new ArrayList<>());
        CountDownLatch latch = new CountDownLatch(batches.size());

        // 2. 提交批次任务到线程池
        for (List<String> batch : batches) {
            sapSyncExecutor.submit(() -> {
                try {
                    processBatch(baseParam, batch, allOrders);
                } catch (Exception e) {
                    log.error("批次处理异常", e);
                } finally {
                    latch.countDown();
                }
            });
        }

        // 3. 等待所有批次完成
        try {
            boolean completed = latch.await(2, TimeUnit.HOURS);
            if (!completed) {
                log.error("同步任务等待超时，部分批次未完成");
                throw new RuntimeException("同步任务执行超时");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("同步任务被中断", e);
        }

        log.info("所有批次处理完成，总获取订单行数: {}", allOrders.size());

        if (allOrders.isEmpty()) {
            return 0;
        }

        // 4. 分段提交（每 1000 条订单为一个独立事务）
        int segmentSize = 1000;
        for (int i = 0; i < allOrders.size(); i += segmentSize) {
            int end = Math.min(i + segmentSize, allOrders.size());
            List<SyncOpenOrderDO> segment = allOrders.subList(i, end);
            final int startIdx = i;

            transactionTemplate.execute(status -> {
                try {
                    // 第一段先物理删除全表
                    if (startIdx == 0) {
                        int deleted = openOrderSyncMapper.physicalDeleteAll();
                        log.info("物理删除了 {} 条旧数据", deleted);
                    }
                    // 分批插入
                    int insertBatchSize = 30;
                    for (int j = 0; j < segment.size(); j += insertBatchSize) {
                        int subEnd = Math.min(j + insertBatchSize, segment.size());
                        openOrderSyncMapper.batchInsert(segment.subList(j, subEnd));
                    }
                    log.info("已提交段 {}/{}，本段 {} 条", end, allOrders.size(), segment.size());
                    return null;
                } catch (Exception e) {
                    status.setRollbackOnly();
                    log.error("段插入失败，已回滚", e);
                    throw new RuntimeException("段插入失败", e);
                }
            });
        }

        return allOrders.size();
    }

    /**
     * 全量同步（不带物料号，直接调用 SAP 全量接口）
     */
    private int syncFullPurchaseOrders(SapPurchaseParamDTO baseParam) {
        log.info("开始全量同步，参数: {}", baseParam);

        // 1. 调用 SAP 全量接口（注意：物料号需设为 null 或空）
        //baseParam.setMatnr(null); // 确保不传物料号
        List<SyncOpenOrderDO> allOrders = syncPurchaseOrdersSap(baseParam);
        if (allOrders == null || allOrders.isEmpty()) {
            log.info("全量同步无数据返回");
            return 0;
        }

        log.info("全量同步获取订单行数: {}", allOrders.size());

        // 2. 物理删除全表 + 分段插入（使用编程式事务）
        int segmentSize = 1000;
        for (int i = 0; i < allOrders.size(); i += segmentSize) {
            int end = Math.min(i + segmentSize, allOrders.size());
            List<SyncOpenOrderDO> segment = allOrders.subList(i, end);
            final int startIdx = i;

            transactionTemplate.execute(status -> {
                try {
                    if (startIdx == 0) {
                        int deleted = openOrderSyncMapper.physicalDeleteAll();
                        log.info("全量同步-物理删除了 {} 条旧数据", deleted);
                    }
                    int insertBatchSize = 30;
                    for (int j = 0; j < segment.size(); j += insertBatchSize) {
                        int subEnd = Math.min(j + insertBatchSize, segment.size());
                        openOrderSyncMapper.batchInsert(segment.subList(j, subEnd));
                    }
                    log.info("全量同步-已提交段 {}/{}，本段 {} 条", end, allOrders.size(), segment.size());
                    return null;
                } catch (Exception e) {
                    status.setRollbackOnly();
                    log.error("全量同步-段插入失败，已回滚", e);
                    throw new RuntimeException("全量同步-段插入失败", e);
                }
            });
        }

        return allOrders.size();
    }




    /**
     * 处理一个物料号批次
     */
    private void processBatch(SapPurchaseParamDTO baseParam, List<String> materialBatch,
                              List<SyncOpenOrderDO> allOrders) {
        for (String matnr : materialBatch) {
            try {
                // 限流：获取令牌，若暂无则阻塞等待
                rateLimiter.acquire();

                SapPurchaseParamDTO param = new SapPurchaseParamDTO();
                BeanUtils.copyProperties(baseParam, param);
                param.setMatnr(matnr);
                param.setUsername(baseParam.getUsername());

                log.debug("线程 {} 正在拉取物料号 [{}]", Thread.currentThread().getName(), matnr);
                List<SyncOpenOrderDO> orders = syncPurchaseOrdersSap(param);
                if (orders != null && !orders.isEmpty()) {
                    allOrders.addAll(orders);
                    log.debug("物料号 [{}] 返回 {} 条订单", matnr, orders.size());
                }
            } catch (Exception e) {
                log.error("物料号 [{}] 同步失败: {}", matnr, e.getMessage(), e);
                // 继续处理下一个物料号，不中断整个批次
            }
        }
    }

    /**
     * 将列表按指定大小切分
     */
    private <T> List<List<T>> partitionList(List<T> list, int size) {
        if (list == null || list.isEmpty()) {
            return Collections.emptyList();
        }
        List<List<T>> partitions = new ArrayList<>();
        for (int i = 0; i < list.size(); i += size) {
            partitions.add(list.subList(i, Math.min(i + size, list.size())));
        }
        return partitions;
    }


    private SyncOpenOrderDO convertToDO(SapPurchaseResultDTO dto) {
        if (dto.getEmenge() != null && dto.getEmenge().compareTo(BigDecimal.ZERO) < 0) {
            log.warn("过滤已收货数量小于0的订单，采购单号: {}, 行号: {}, receivedQty: {}",
                    dto.getEbeln(), dto.getEbelp(), dto.getEmenge());
            return null;
        }
        SyncOpenOrderDO order = new SyncOpenOrderDO();
        // 注意：不要手动设置 id，由雪花算法自动生成
        order.setOrderDate(dto.getBedat() != null ? dto.getBedat().atStartOfDay() : null);
        order.setBuyerOrderNo(dto.getEbeln());
        order.setLineItem(dto.getEbelp() != null ? Long.valueOf(dto.getEbelp()) : null);
        if (dto.getMatnr() == null || dto.getMatnr().isEmpty()) {
            log.warn("SAP 返回一条物料号为空的数据，采购单号: {}", dto.getEbeln());
            // 可设置默认值或直接跳过 return null;
            order.setMaterialNo("UNKNOWN");
        }else{
            order.setMaterialNo(dto.getMatnr());
        }
        order.setMaterialDesc(dto.getMaktx());
        order.setOrderQty(dto.getMenge());
        order.setReceivedQty(dto.getEmenge());
        if (dto.getMenge() != null && dto.getEmenge() != null) {
            order.setOpenQty(dto.getMenge().subtract(dto.getEmenge()));
        } else {
            order.setOpenQty(dto.getMenge());
        }
        order.setUnit(dto.getMeins());
        order.setRequiredArrivalDate(dto.getEindt() != null ? dto.getEindt().atStartOfDay() : null);
        order.setActualArrivalDate(dto.getBudat() != null ? dto.getBudat().atStartOfDay() : null);
        order.setSupplierDesc(dto.getName1());
        order.setCustomer(null);   // 根据业务填充
        order.setBuyerGroup(dto.getEknam());
        order.setDocumentType(dto.getBsart());
        order.setProductionOrderNo(dto.getAufnr());
        order.setBrandInfo(dto.getBednr());
        order.setUnitPrice(dto.getNetpr());
        order.setSupplierCode(dto.getLifnr());
        order.setReceivingWarehouse(dto.getLgort());
        if (dto.getNetpr() != null && dto.getMenge() != null) {
            order.setTotalAmount(dto.getNetpr().multiply(dto.getMenge()));
        }
        order.setBuyerReqNo(null);
        order.setCreateTime(LocalDateTime.now());
        order.setUpdateTime(LocalDateTime.now());
        order.setDeleted(false);
        // 如果启用了多租户，设置租户ID（示例）
//        order.setTenantId(1L);
        // 若未启用多租户，表默认 tenant_id=1，可设置 order.setTenantId(1L);
        return order;
    }

    private Set<String> getExistOrderKeys(List<SyncOpenOrderDO> orders) {
        if (orders.isEmpty()) return Collections.emptySet();
        Set<String> keys = orders.stream()
                .map(o -> o.getBuyerOrderNo() + "_" + o.getLineItem())
                .collect(Collectors.toSet());
        // 查询数据库中已存在的业务主键
        LambdaQueryWrapper<SyncOpenOrderDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(SyncOpenOrderDO::getBuyerOrderNo, SyncOpenOrderDO::getLineItem)
                .in(SyncOpenOrderDO::getBuyerOrderNo, keys.stream().map(k -> k.split("_")[0]).collect(Collectors.toList()));
        List<SyncOpenOrderDO> existList = openOrderSyncMapper.selectList(wrapper);
        return existList.stream()
                .map(e -> e.getBuyerOrderNo() + "_" + e.getLineItem())
                .collect(Collectors.toSet());
    }
}