package cn.iocoder.yudao.module.aps.service.ordercomponentprogressreport;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.aps.controller.admin.ordercomponentprogressreport.vo.OrderComponentProgressReportPageReqVO;
import cn.iocoder.yudao.module.aps.controller.admin.ordercomponentprogressreport.vo.OrderComponentProgressReportRespVO;
import cn.iocoder.yudao.module.aps.dal.mysql.ordercomponentprogressreport.OrderComponentProgressReportMapper;
import com.baomidou.dynamic.datasource.annotation.DS;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@DS("oracle")
@Validated
@Slf4j
public class OrderComponentProgressReportServiceImpl implements OrderComponentProgressReportService {

    @Resource
    private OrderComponentProgressReportMapper mapper;

    @Override
    public PageResult<OrderComponentProgressReportRespVO> getComponentProgressReportPage(OrderComponentProgressReportPageReqVO pageReqVO) {
        // 处理导出全部数据的情况（pageSize 为 PAGE_SIZE_NONE，即 -1）
        if (pageReqVO.getPageSize() == PageParam.PAGE_SIZE_NONE) {
            // 获取所有符合条件的明细数据（无分页）
            List<OrderComponentProgressReportRespVO> allDetails = mapper.selectAllByCondition(pageReqVO);
            if (allDetails.isEmpty()) {
                return new PageResult<>(Collections.emptyList(), 0L);
            }
            List<OrderComponentProgressReportRespVO> assignedList = assignWorkshopOutput(allDetails);
            return new PageResult<>(assignedList, (long) assignedList.size());
        }

        // 正常分页逻辑（pageSize > 0）
        // 1. 获取所有符合条件的明细数据（无分页）
        List<OrderComponentProgressReportRespVO> allDetails = mapper.selectAllByCondition(pageReqVO);
        if (allDetails.isEmpty()) {
            return new PageResult<>(Collections.emptyList(), 0L);
        }

        // 2. 分配
        List<OrderComponentProgressReportRespVO> assignedList = assignWorkshopOutput(allDetails);

        // 3. 手动分页
        int pageNo = pageReqVO.getPageNo();
        int pageSize = pageReqVO.getPageSize();
        int start = (pageNo - 1) * pageSize;
        int end = Math.min(start + pageSize, assignedList.size());
        // 注意：这里必须保证 start <= end，否则 subList 会抛异常
        if (start >= assignedList.size()) {
            return new PageResult<>(Collections.emptyList(), (long) assignedList.size());
        }
        List<OrderComponentProgressReportRespVO> pageList = assignedList.subList(start, end);

        return new PageResult<>(pageList, (long) assignedList.size());
    }

    private List<OrderComponentProgressReportRespVO> assignWorkshopOutput(List<OrderComponentProgressReportRespVO> records) {
        if (records.isEmpty()) return records;

        // 按组件物料号分组
        Map<String, List<OrderComponentProgressReportRespVO>> groupMap = records.stream()
                .collect(Collectors.groupingBy(
                        OrderComponentProgressReportRespVO::getComponentMaterialNo,
                        LinkedHashMap::new,
                        Collectors.toList()
                ));

        List<OrderComponentProgressReportRespVO> result = new ArrayList<>();

        for (Map.Entry<String, List<OrderComponentProgressReportRespVO>> entry : groupMap.entrySet()) {
            List<OrderComponentProgressReportRespVO> group = entry.getValue();

            // 组内排序：按排产日期升序，相同按订单数量升序
            group.sort(Comparator.comparing(OrderComponentProgressReportRespVO::getScheduledDate,
                    Comparator.nullsLast(Comparator.naturalOrder()))
                    .thenComparing(OrderComponentProgressReportRespVO::getOrderQuantity,
                            Comparator.nullsLast(Comparator.naturalOrder())));

            // 获取全局库存、在制、采购未清（组内所有记录共享）
            BigDecimal totalStock = group.get(0).getStockQuantity() != null ? group.get(0).getStockQuantity() : BigDecimal.ZERO;
            BigDecimal totalWip = group.get(0).getWorkInProgress() != null ? group.get(0).getWorkInProgress() : BigDecimal.ZERO;
            BigDecimal totalPo = group.get(0).getOpenPoQuantity() != null ? group.get(0).getOpenPoQuantity() : BigDecimal.ZERO;

            BigDecimal remainingStock = totalStock;
            BigDecimal remainingWip = totalWip;
            BigDecimal remainingPo = totalPo;

            for (OrderComponentProgressReportRespVO record : group) {
                BigDecimal requirement = record.getTotalRequirement() != null ? record.getTotalRequirement() : BigDecimal.ZERO;

                // 分配库存
                BigDecimal allocatedStock = BigDecimal.ZERO;
                if (remainingStock.compareTo(requirement) >= 0) {
                    allocatedStock = requirement;
                    remainingStock = remainingStock.subtract(requirement);
                } else {
                    allocatedStock = remainingStock;
                    remainingStock = BigDecimal.ZERO;
                }

                // 分配在制
                BigDecimal allocatedWip = BigDecimal.ZERO;
                BigDecimal remainingAfterStock = requirement.subtract(allocatedStock);
                if (remainingAfterStock.compareTo(BigDecimal.ZERO) > 0 && remainingWip.compareTo(BigDecimal.ZERO) > 0) {
                    if (remainingWip.compareTo(remainingAfterStock) >= 0) {
                        allocatedWip = remainingAfterStock;
                        remainingWip = remainingWip.subtract(remainingAfterStock);
                    } else {
                        allocatedWip = remainingWip;
                        remainingWip = BigDecimal.ZERO;
                    }
                }

                // 分配采购未清
                BigDecimal allocatedPo = BigDecimal.ZERO;
                BigDecimal remainingAfterWip = requirement.subtract(allocatedStock).subtract(allocatedWip);
                if (remainingAfterWip.compareTo(BigDecimal.ZERO) > 0 && remainingPo.compareTo(BigDecimal.ZERO) > 0) {
                    if (remainingPo.compareTo(remainingAfterWip) >= 0) {
                        allocatedPo = remainingAfterWip;
                        remainingPo = remainingPo.subtract(remainingAfterWip);
                    } else {
                        allocatedPo = remainingPo;
                        remainingPo = BigDecimal.ZERO;
                    }
                }

                // 设置分配后的数量（覆盖原字段）
                record.setStockQuantity(allocatedStock);
                record.setWorkInProgress(allocatedWip);
                record.setOpenPoQuantity(allocatedPo);

                // 计算是否满足
                BigDecimal totalAllocated = allocatedStock.add(allocatedWip).add(allocatedPo);
                record.setSatisfy(totalAllocated.compareTo(requirement) >= 0 ? "满足" : "不满足");

                result.add(record);
            }
        }

        // 恢复原顺序（按订单号+组件物料号排序）
        result.sort(Comparator.comparing(OrderComponentProgressReportRespVO::getProductionOrderNo)
                .thenComparing(OrderComponentProgressReportRespVO::getComponentMaterialNo));
        return result;
    }
}