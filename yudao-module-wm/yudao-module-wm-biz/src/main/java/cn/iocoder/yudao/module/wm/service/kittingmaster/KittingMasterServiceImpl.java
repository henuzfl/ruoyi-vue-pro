package cn.iocoder.yudao.module.wm.service.kittingmaster;

import cn.iocoder.yudao.module.wm.service.kittingmaster.KittingMasterService;
import com.baomidou.dynamic.datasource.annotation.DS;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.util.*;
import cn.iocoder.yudao.module.wm.controller.admin.kittingmaster.vo.*;
import cn.iocoder.yudao.module.wm.dal.dataobject.kittingmaster.KittingMasterDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

import cn.iocoder.yudao.module.wm.dal.mysql.kittingmaster.KittingMasterMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.wm.enums.ErrorCodeConstants.*;

/**
 * 主计划 Service 实现类
 *
 * @author 柳文
 */
@Service
@DS("oracle") // 指定使用 Oracle 数据源
@Validated
@Slf4j
public class KittingMasterServiceImpl implements KittingMasterService {

    @Resource
    private KittingMasterMapper kittingMasterMapper;

    @Override
    public PageResult<KittingMasterDO> selectKittingMasterByParams(KittingMasterPageReqVO pageReqVO) {
        // 调试：检查日期参数
        log.info("=== 参数齐套调试开始 ===");
        log.info("接收到的 scheduledDate 数组: {}", Arrays.toString(pageReqVO.getScheduledDate()));

        if (pageReqVO.getScheduledDate() != null && pageReqVO.getScheduledDate().length >= 2) {
            log.info("开始日期: {}, 类型: {}",
                    pageReqVO.getScheduledDate()[0],
                    pageReqVO.getScheduledDate()[0].getClass().getName());
            log.info("结束日期: {}, 类型: {}",
                    pageReqVO.getScheduledDate()[1],
                    pageReqVO.getScheduledDate()[1].getClass().getName());
        }
        log.info("=== 参数调试结束 ===");

        // 直接传入 VO 对象
        List<KittingMasterDO> list = kittingMasterMapper.selectKittingMasterByParams(pageReqVO);

        log.info("查询结果数量: {}", list.size());

        // 详细记录前几条数据的所有字段
        if (!list.isEmpty()) {
            log.info("=== 详细数据字段调试开始 ===");
            for (int i = 0; i < Math.min(3, list.size()); i++) {
                KittingMasterDO item = list.get(i);
                log.info("数据 {} 详细字段:", i + 1);
                log.info("  productionOrderNo: {} (类型: {})",
                        item.getProductionOrderNo(),
                        item.getProductionOrderNo() != null ? item.getProductionOrderNo().getClass().getName() : "null");
                log.info("  assemblyMaterialNo: {} (类型: {})",
                        item.getAssemblyMaterialNo(),
                        item.getAssemblyMaterialNo() != null ? item.getAssemblyMaterialNo().getClass().getName() : "null");
                log.info("  mainMaterialDesc: {} (类型: {})",
                        item.getMainMaterialDesc(),
                        item.getMainMaterialDesc() != null ? item.getMainMaterialDesc().getClass().getName() : "null");
                log.info("  scheduledDate: {} (类型: {})",
                        item.getScheduledDate(),
                        item.getScheduledDate() != null ? item.getScheduledDate().getClass().getName() : "null");
                log.info("  productionWorkshop: {} (类型: {})",
                        item.getProductionWorkshop(),
                        item.getProductionWorkshop() != null ? item.getProductionWorkshop().getClass().getName() : "null");
                log.info("  scheduledQuantity: {} (类型: {})",
                        item.getScheduledQuantity(),
                        item.getScheduledQuantity() != null ? item.getScheduledQuantity().getClass().getName() : "null");
                log.info("  openFlag: {} (类型: {})",
                        item.getOpenFlag(),
                        item.getOpenFlag() != null ? item.getOpenFlag().getClass().getName() : "null");
                log.info("  shortageQty: {} (类型: {})",
                        item.getOrderMessage(),
                        item.getOrderMessage() != null ? item.getOrderMessage().getClass().getName() : "null");
                log.info("  shortageQty: {} (类型: {})",
                        item.getParentOrderNo(),
                        item.getParentOrderNo() != null ? item.getParentOrderNo().getClass().getName() : "null");

                log.info("---");
            }
            log.info("=== 详细数据字段调试结束 ===");
        } else {
            log.info("未查询到任何数据");
        }


        // 手动分页逻辑
        int total = list.size();
        int fromIndex = (pageReqVO.getPageNo() - 1) * pageReqVO.getPageSize();
        int toIndex = Math.min(fromIndex + pageReqVO.getPageSize(), total);

        if (fromIndex >= total) {
            return new PageResult<>(Collections.emptyList(), (long) total);
        }

        List<KittingMasterDO> pageList = list.subList(fromIndex, toIndex);
        return new PageResult<>(pageList, (long) total);
    }

    @Override
    public PageResult<KittingMasterDO> selectKittingcalculateByParams(KittingMasterPageReqVO pageReqVO) {
        // 调试：检查日期参数
        log.info("=== 参数齐套调试开始 ===");
        log.info("接收到的 scheduledDate 数组: {}", Arrays.toString(pageReqVO.getScheduledDate()));

        if (pageReqVO.getScheduledDate() != null && pageReqVO.getScheduledDate().length >= 2) {
            log.info("开始日期: {}, 类型: {}",
                    pageReqVO.getScheduledDate()[0],
                    pageReqVO.getScheduledDate()[0].getClass().getName());
            log.info("结束日期: {}, 类型: {}",
                    pageReqVO.getScheduledDate()[1],
                    pageReqVO.getScheduledDate()[1].getClass().getName());
        }
        log.info("=== 参数调试结束 ===");

        // 直接传入 VO 对象
        List<KittingMasterDO> list = kittingMasterMapper.selectKittingcalculateByParams(pageReqVO);

        log.info("查询结果数量: {}", list.size());

        // 详细记录前几条数据的所有字段
        if (!list.isEmpty()) {
            log.info("=== 详细数据字段调试开始 ===");
            for (int i = 0; i < Math.min(3, list.size()); i++) {
                KittingMasterDO item = list.get(i);
                log.info("数据 {} 详细字段:", i + 1);
                log.info("  productionOrderNo: {} (类型: {})",
                        item.getProductionOrderNo(),
                        item.getProductionOrderNo() != null ? item.getProductionOrderNo().getClass().getName() : "null");
                log.info("  assemblyMaterialNo: {} (类型: {})",
                        item.getAssemblyMaterialNo(),
                        item.getAssemblyMaterialNo() != null ? item.getAssemblyMaterialNo().getClass().getName() : "null");
                log.info("  mainMaterialDesc: {} (类型: {})",
                        item.getMainMaterialDesc(),
                        item.getMainMaterialDesc() != null ? item.getMainMaterialDesc().getClass().getName() : "null");
                log.info("  scheduledDate: {} (类型: {})",
                        item.getScheduledDate(),
                        item.getScheduledDate() != null ? item.getScheduledDate().getClass().getName() : "null");
                log.info("  productionWorkshop: {} (类型: {})",
                        item.getProductionWorkshop(),
                        item.getProductionWorkshop() != null ? item.getProductionWorkshop().getClass().getName() : "null");
                log.info("  scheduledQuantity: {} (类型: {})",
                        item.getScheduledQuantity(),
                        item.getScheduledQuantity() != null ? item.getScheduledQuantity().getClass().getName() : "null");
                log.info("  openFlag: {} (类型: {})",
                        item.getOpenFlag(),
                        item.getOpenFlag() != null ? item.getOpenFlag().getClass().getName() : "null");
                log.info("  shortageQty: {} (类型: {})",
                        item.getOrderMessage(),
                        item.getOrderMessage() != null ? item.getOrderMessage().getClass().getName() : "null");
                log.info("  shortageQty: {} (类型: {})",
                        item.getParentOrderNo(),
                        item.getParentOrderNo() != null ? item.getParentOrderNo().getClass().getName() : "null");
                log.info("---");
            }
            log.info("=== 详细数据字段调试结束 ===");
        } else {
            log.info("未查询到任何数据");
        }


        // 手动分页逻辑
        int total = list.size();
        int fromIndex = (pageReqVO.getPageNo() - 1) * pageReqVO.getPageSize();
        int toIndex = Math.min(fromIndex + pageReqVO.getPageSize(), total);

        if (fromIndex >= total) {
            return new PageResult<>(Collections.emptyList(), (long) total);
        }

        List<KittingMasterDO> pageList = list.subList(fromIndex, toIndex);
        return new PageResult<>(pageList, (long) total);
    }

    @Override
    public List<KittingMasterDO> selectKittingMasterForExport(KittingMasterPageReqVO exportReqVO) {
        log.info("导出查询参数: scheduledDate={}, productionOrderNo={}",
                Arrays.toString(exportReqVO.getScheduledDate()),
                exportReqVO.getProductionOrderNo());

        // 方法1：创建新的查询参数，忽略分页
        KittingMasterPageReqVO queryParams = new KittingMasterPageReqVO();

        // 复制查询条件
        queryParams.setProductionOrderNo(exportReqVO.getProductionOrderNo());
        queryParams.setAssemblyMaterialNo(exportReqVO.getAssemblyMaterialNo());
        queryParams.setComponentMaterialNo(exportReqVO.getComponentMaterialNo());
        queryParams.setProductionWorkshop(exportReqVO.getProductionWorkshop());
        queryParams.setScheduledDate(exportReqVO.getScheduledDate());

        // 导出时不分页，设置足够大的页大小
        queryParams.setPageNo(1);
        queryParams.setPageSize(Integer.MAX_VALUE);

        // 调用分页方法
        PageResult<KittingMasterDO> pageResult = selectKittingMasterByParams(queryParams);

        log.info("导出查询结果数量: {}", pageResult.getList().size());
        return pageResult.getList();

        // 或者方法2：直接调用Mapper查询（如果需要）
        // return kittingMasterMapper.selectKittingMasterByParams(queryParams);
    }

    @Override
    public KittingMasterDO getKittingMaster(BigDecimal id) {
        // 根据你的业务逻辑实现这个方法
        // 例如：根据 ID 查询单个齐套分析记录
        // 如果这个方法暂时不需要，可以返回 null 或者抛出异常

        // 临时实现 - 返回 null 或空对象
        return null;

        // 或者如果你需要具体实现：
        // return kittingMasterMapper.selectById(id);
    }

    @Override
    @Transactional
    public void callUpdatecompProcedure() {
        try {
            log.info("开始调用存储过程 SP_KITTING_AUTO()");
            kittingMasterMapper.callcompleProcedure();
            log.info("存储过程调用成功");
        } catch (Exception e) {
            log.error("存储过程调用失败", e);
            // 打印详细的错误信息
            if (e.getCause() != null) {
                log.error("根本原因: {}", e.getCause().getMessage());
            }
            throw new RuntimeException("齐套计算失败: " + e.getMessage(), e);
        }
    }
}