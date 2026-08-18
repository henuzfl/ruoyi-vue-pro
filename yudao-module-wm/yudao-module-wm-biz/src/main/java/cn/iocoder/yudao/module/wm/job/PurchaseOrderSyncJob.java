package cn.iocoder.yudao.module.wm.job;

import cn.iocoder.yudao.framework.quartz.core.handler.JobHandler;
import cn.iocoder.yudao.module.wm.controller.admin.openordersync.vo.SapPurchaseParamDTO;
import cn.iocoder.yudao.module.wm.dal.mysql.openordersync.OpenOrderSyncMapper;
import cn.iocoder.yudao.module.wm.service.openordersync.OpenOrderSyncService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@Slf4j
public class PurchaseOrderSyncJob implements JobHandler {

    @Autowired
    private OpenOrderSyncService syncService;

    @Autowired
    private OpenOrderSyncMapper openOrderSyncMapper;  // 用于查询物料号

    @Override
    public String execute(String param) throws Exception {
        log.info("[execute][开始异步分批同步 SAP 采购订单，参数: {}]", param);

        // 1. 从 Oracle 查询需要同步的物料号列表
        List<String> materialNos = openOrderSyncMapper.selectComponentMaterialNos();
        log.info("查询到待同步物料号共 {} 个", materialNos.size());

        if (materialNos.isEmpty()) {
            return "无物料号需同步";
        }

        // 2. 构造基础查询参数
        SapPurchaseParamDTO baseParam = new SapPurchaseParamDTO();
        baseParam.setWerks("6400");
        //baseParam.setIsAll("N");
        baseParam.setBedatS(LocalDate.of(2022, 6, 30));
        baseParam.setBedatE(LocalDate.now().plusDays(1));
        baseParam.setUsername("JOB_SYNC");   // 日志用

        // 3. 调用异步分批同步方法
        int totalOrders = syncService.syncPurchaseOrdersListAsync(baseParam, null);

        String result = String.format("同步完成，物料号 %d 个，共插入 %d 条订单记录", materialNos.size(), totalOrders);
        log.info(result);
        return result;
    }
}