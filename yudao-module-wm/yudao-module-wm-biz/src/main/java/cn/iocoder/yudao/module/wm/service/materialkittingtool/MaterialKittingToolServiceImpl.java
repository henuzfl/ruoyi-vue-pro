package cn.iocoder.yudao.module.wm.service.materialkittingtool;

import com.baomidou.dynamic.datasource.annotation.DS;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.util.*;
import cn.iocoder.yudao.module.wm.controller.admin.materialkittingtool.vo.*;
import cn.iocoder.yudao.module.wm.dal.dataobject.materialkittingtool.MaterialKittingToolDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

import cn.iocoder.yudao.module.wm.dal.mysql.materialkittingtool.MaterialKittingToolMapper;

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
public class MaterialKittingToolServiceImpl implements MaterialKittingToolService {

    @Resource
    private MaterialKittingToolMapper materialKittingToolMapper;

    @Override
    public PageResult<MaterialKittingToolDO> selectMaterialKittingToolByParams(MaterialKittingToolPageReqVO pageReqVO) {
        // 调试：检查日期参数
        log.info("=== 参数明细调试开始 ===");
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
        List<MaterialKittingToolDO> list = materialKittingToolMapper.selectMaterialKittingToolByParams(pageReqVO);

        log.info("查询结果数量: {}", list.size());

        // 详细记录前几条数据的所有字段
        if (!list.isEmpty()) {
            log.info("=== 详细数据字段调试开始 ===");
            for (int i = 0; i < Math.min(3, list.size()); i++) {
                MaterialKittingToolDO item = list.get(i);
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
                log.info("  kittingStatus: {} (类型: {})",
                        item.getKittingStatus(),
                        item.getKittingStatus() != null ? item.getKittingStatus().getClass().getName() : "null");
                log.info("  productionWorkshop: {} (类型: {})",
                        item.getProductionWorkshop(),
                        item.getProductionWorkshop() != null ? item.getProductionWorkshop().getClass().getName() : "null");
                log.info("  scheduledQuantity: {} (类型: {})",
                        item.getScheduledQuantity(),
                        item.getScheduledQuantity() != null ? item.getScheduledQuantity().getClass().getName() : "null");
                log.info("  componentMaterialNo: {} (类型: {})",
                        item.getComponentMaterialNo(),
                        item.getComponentMaterialNo() != null ? item.getComponentMaterialNo().getClass().getName() : "null");
                log.info("  componentDesc: {} (类型: {})",
                        item.getComponentDesc(),
                        item.getComponentDesc() != null ? item.getComponentDesc().getClass().getName() : "null");
                log.info("  unitUsage: {} (类型: {})",
                        item.getUnitUsage(),
                        item.getUnitUsage() != null ? item.getUnitUsage().getClass().getName() : "null");
                log.info("  requiredQty: {} (类型: {})",
                        item.getRequiredQty(),
                        item.getRequiredQty() != null ? item.getRequiredQty().getClass().getName() : "null");
                log.info("  stockTomorrow: {} (类型: {})",
                        item.getStockTomorrow(),
                        item.getStockTomorrow() != null ? item.getStockTomorrow().getClass().getName() : "null");
                log.info("  stockDayAfterTomorrow: {} (类型: {})",
                        item.getStockDayAfterTomorrow(),
                        item.getStockDayAfterTomorrow() != null ? item.getStockDayAfterTomorrow().getClass().getName() : "null");
                log.info("  stockThirdDay: {} (类型: {})",
                        item.getStockThirdDay(),
                        item.getStockThirdDay() != null ? item.getStockThirdDay().getClass().getName() : "null");
                log.info("  stockQuantity: {} (类型: {})",
                        item.getStockQuantity(),
                        item.getStockQuantity() != null ? item.getStockQuantity().getClass().getName() : "null");
                log.info("  shortageQty: {} (类型: {})",
                        item.getShortageQty(),
                        item.getShortageQty() != null ? item.getShortageQty().getClass().getName() : "null");
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

        List<MaterialKittingToolDO> pageList = list.subList(fromIndex, toIndex);
        return new PageResult<>(pageList, (long) total);
    }

    @Override
    public MaterialKittingToolDO getMaterialKittingTool(BigDecimal id) {
        // 根据你的业务逻辑实现这个方法
        // 例如：根据 ID 查询单个齐套分析记录
        // 如果这个方法暂时不需要，可以返回 null 或者抛出异常

        // 临时实现 - 返回 null 或空对象
        return null;

        // 或者如果你需要具体实现：
        // return materialKittingToolMapper.selectById(id);
    }

    // 如果接口中还有其他方法，也需要在这里实现
}