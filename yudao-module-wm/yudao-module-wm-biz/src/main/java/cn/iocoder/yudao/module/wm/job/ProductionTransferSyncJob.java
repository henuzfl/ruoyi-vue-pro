package cn.iocoder.yudao.module.wm.job;

import cn.iocoder.yudao.framework.quartz.core.handler.JobHandler;
import cn.iocoder.yudao.module.buyer.controller.admin.productiontransfer.vo.MesSyncReqVO;
import cn.iocoder.yudao.module.buyer.service.productiontransfer.ProductionTransferService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
@Slf4j
public class ProductionTransferSyncJob implements JobHandler {

    @Resource
    private ProductionTransferService productionTransferService;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public String execute(String param) throws Exception {
        log.info("[execute][开始同步MES转序单数据，参数: {}]", param);

        // 1. 计算时间范围：昨天 00:00:00 至 今天 00:00:00（即昨天全天）
        LocalDate today = LocalDate.now();
        LocalDate yesterday = today.minusDays(1);
        LocalDate tomorrow = LocalDate.now().plusDays(1);
//        String beginTime = yesterday.format(DATE_FORMATTER);   // 昨天
//        String endTime = tomorrow.format(DATE_FORMATTER);         // 明天

        String beginTime = "2025-06-01";
        String endTime = tomorrow.format(DATE_FORMATTER);

        // 2. 工厂编号固定 6400
        String plantNo = "6400";
        if (StringUtils.hasText(param)) {
            log.debug("任务参数: {}，但工厂编号固定使用 6400", param);
        }

        // 3. 构造同步请求 VO
        MesSyncReqVO syncReqVO = new MesSyncReqVO();
        syncReqVO.setPlantNo(plantNo);
        syncReqVO.setBeginTime(beginTime);
        syncReqVO.setEndTime(endTime);
        syncReqVO.setPlannerName("");
        syncReqVO.setOrderNo("");

        // 4. 调用服务同步
        int count = productionTransferService.syncFromMes(syncReqVO);

        String result = String.format("同步成功，日期范围：%s ~ %s，工厂：%s，共处理 %d 条数据",
                beginTime, endTime, plantNo, count);
        log.info("[execute][{}]", result);
        return result;
    }
}