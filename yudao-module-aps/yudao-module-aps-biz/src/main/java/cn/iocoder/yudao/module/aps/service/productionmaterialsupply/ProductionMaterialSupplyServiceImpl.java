package cn.iocoder.yudao.module.aps.service.productionmaterialsupply;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.aps.controller.admin.productionmaterialsupply.vo.ProductionMaterialSupplyPageReqVO;
import cn.iocoder.yudao.module.aps.controller.admin.productionmaterialsupply.vo.ProductionMaterialSupplyRespVO;
import cn.iocoder.yudao.module.aps.dal.dataobject.productionmaterialsupply.ProductionMaterialAvailableRow;
import cn.iocoder.yudao.module.aps.dal.dataobject.productionmaterialsupply.ProductionMaterialDemandRow;
import cn.iocoder.yudao.module.aps.dal.dataobject.productionmaterialsupply.ProductionMaterialPurchaseRow;
import cn.iocoder.yudao.module.aps.dal.mysql.productionmaterialsupply.ProductionMaterialSupplyMapper;
import com.baomidou.dynamic.datasource.annotation.DS;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@DS("oracle")
@Validated
@Slf4j
public class ProductionMaterialSupplyServiceImpl implements ProductionMaterialSupplyService {

    private static final String PROCUREMENT_EXTERNAL = "E";
    private static final String PROCUREMENT_INTERNAL = "F";
    private static final int MATERIAL_QUERY_BATCH_SIZE = 900;
    private static final int PURCHASE_KEY_QUERY_BATCH_SIZE = 500;

    @Resource
    private ProductionMaterialSupplyMapper mapper;

    @Override
    public PageResult<ProductionMaterialSupplyRespVO> getPage(ProductionMaterialSupplyPageReqVO pageReqVO) {
        long totalStart = System.currentTimeMillis();
        List<ProductionMaterialDemandRow> demands = mapper.selectDemandRows(pageReqVO);
        long demandQueryEnd = System.currentTimeMillis();
        if (demands.isEmpty()) {
            log.info("生产订单物料供需查询完成，需求查询={}ms，需求行数=0，总耗时={}ms",
                    demandQueryEnd - totalStart, demandQueryEnd - totalStart);
            return new PageResult<>(Collections.emptyList(), 0L);
        }
        List<ProductionMaterialAvailableRow> availableRows = loadAvailableRows(demands);
        long availableQueryEnd = System.currentTimeMillis();
        List<ProductionMaterialPurchaseRow> purchaseRows = loadPurchaseRows(demands);
        long purchaseQueryEnd = System.currentTimeMillis();
        List<ProductionMaterialSupplyRespVO> calculated = calculate(demands, availableRows, purchaseRows);
        long calculateEnd = System.currentTimeMillis();
        log.info("生产订单物料供需查询完成，需求查询={}ms，库存及生产在途查询={}ms，采购在途查询={}ms，"
                        + "Service计算={}ms，需求行数={}，可用量行数={}，采购明细行数={}，总耗时={}ms",
                demandQueryEnd - totalStart,
                availableQueryEnd - demandQueryEnd,
                purchaseQueryEnd - availableQueryEnd,
                calculateEnd - purchaseQueryEnd,
                demands.size(), availableRows.size(), purchaseRows.size(), calculateEnd - totalStart);
        if (Boolean.TRUE.equals(pageReqVO.getOnlyShortage())) {
            calculated = calculated.stream()
                    .filter(item -> item.getShortageQuantity().compareTo(BigDecimal.ZERO) > 0)
                    .collect(Collectors.toList());
        }
        if (pageReqVO.getPageSize() == PageParam.PAGE_SIZE_NONE) {
            return new PageResult<>(calculated, (long) calculated.size());
        }
        int start = (pageReqVO.getPageNo() - 1) * pageReqVO.getPageSize();
        if (start >= calculated.size()) {
            return new PageResult<>(Collections.emptyList(), (long) calculated.size());
        }
        int end = Math.min(start + pageReqVO.getPageSize(), calculated.size());
        return new PageResult<>(calculated.subList(start, end), (long) calculated.size());
    }

    private List<ProductionMaterialAvailableRow> loadAvailableRows(
            List<ProductionMaterialDemandRow> demands) {
        List<String> materialNos = demands.stream()
                .map(ProductionMaterialDemandRow::getComponentMaterialNo)
                .filter(Objects::nonNull)
                .distinct()
                .collect(Collectors.toList());
        List<ProductionMaterialAvailableRow> result = new ArrayList<>();
        for (int start = 0; start < materialNos.size(); start += MATERIAL_QUERY_BATCH_SIZE) {
            int end = Math.min(start + MATERIAL_QUERY_BATCH_SIZE, materialNos.size());
            result.addAll(mapper.selectAvailableRows(materialNos.subList(start, end)));
        }
        return result;
    }

    private List<ProductionMaterialPurchaseRow> loadPurchaseRows(
            List<ProductionMaterialDemandRow> demands) {
        Map<String, ProductionMaterialDemandRow> uniqueKeys = demands.stream()
                .filter(row -> PROCUREMENT_EXTERNAL.equalsIgnoreCase(row.getProcurementType()))
                .collect(Collectors.toMap(
                        row -> key(row.getProductionOrderNo(), row.getComponentMaterialNo()),
                        Function.identity(), (left, right) -> left, LinkedHashMap::new));
        List<ProductionMaterialDemandRow> keys = new ArrayList<>(uniqueKeys.values());
        List<ProductionMaterialPurchaseRow> result = new ArrayList<>();
        for (int start = 0; start < keys.size(); start += PURCHASE_KEY_QUERY_BATCH_SIZE) {
            int end = Math.min(start + PURCHASE_KEY_QUERY_BATCH_SIZE, keys.size());
            result.addAll(mapper.selectPurchaseRows(keys.subList(start, end)));
        }
        return result;
    }

    List<ProductionMaterialSupplyRespVO> calculate(
            List<ProductionMaterialDemandRow> demands,
            List<ProductionMaterialAvailableRow> availableRows,
            List<ProductionMaterialPurchaseRow> purchaseRows) {
        Map<String, ProductionMaterialAvailableRow> availableMap = availableRows.stream()
                .collect(Collectors.toMap(ProductionMaterialAvailableRow::getMaterialNo,
                        Function.identity(), (left, right) -> left));
        Map<String, List<ProductionMaterialPurchaseRow>> purchaseMap = purchaseRows.stream()
                .collect(Collectors.groupingBy(row -> key(row.getProductionOrderNo(), row.getMaterialNo())));
        Map<String, List<ProductionMaterialDemandRow>> demandGroups = demands.stream()
                .collect(Collectors.groupingBy(ProductionMaterialDemandRow::getComponentMaterialNo,
                        LinkedHashMap::new, Collectors.toList()));
        List<ProductionMaterialSupplyRespVO> result = new ArrayList<>();
        Comparator<ProductionMaterialDemandRow> order = Comparator
                .comparing(ProductionMaterialDemandRow::getScheduledDate,
                        Comparator.nullsLast(Comparator.naturalOrder()))
                .thenComparing(ProductionMaterialDemandRow::getProductionOrderNo,
                        Comparator.nullsLast(Comparator.naturalOrder()));

        for (Map.Entry<String, List<ProductionMaterialDemandRow>> entry : demandGroups.entrySet()) {
            List<ProductionMaterialDemandRow> group = entry.getValue();
            group.sort(order);
            ProductionMaterialAvailableRow available = availableMap.get(entry.getKey());
            BigDecimal remainingStock = nonNegative(available == null ? null : available.getStockQuantity());
            BigDecimal remainingProductionTransit = nonNegative(
                    available == null ? null : available.getProductionTransit());

            for (ProductionMaterialDemandRow demand : group) {
                ProductionMaterialSupplyRespVO item = toResponse(demand);
                BigDecimal demandQuantity = nonNegative(demand.getDemandQuantity());
                BigDecimal invested = min(nonNegative(demand.getInvestedQuantity()), demandQuantity);
                BigDecimal remaining = demandQuantity.subtract(invested);

                BigDecimal allocatedStock = min(remainingStock, remaining);
                remainingStock = remainingStock.subtract(allocatedStock);
                remaining = remaining.subtract(allocatedStock);

                List<ProductionMaterialPurchaseRow> purchases = purchaseMap.getOrDefault(
                        key(demand.getProductionOrderNo(), demand.getComponentMaterialNo()),
                        Collections.emptyList());
                Date referenceDate = demand.getScheduledDate();
                List<ProductionMaterialPurchaseRow> applicablePurchases = purchases.stream()
                        .filter(row -> row.getRequiredArrivalDate() != null && referenceDate != null
                                && !row.getRequiredArrivalDate().after(referenceDate))
                        .collect(Collectors.toList());
                BigDecimal purchaseAvailable = applicablePurchases.stream()
                        .map(ProductionMaterialPurchaseRow::getOpenQuantity)
                        .map(ProductionMaterialSupplyServiceImpl::nonNegative)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                BigDecimal productionTransit = BigDecimal.ZERO;
                BigDecimal purchaseTransit = BigDecimal.ZERO;
                if (PROCUREMENT_INTERNAL.equalsIgnoreCase(demand.getProcurementType())) {
                    productionTransit = min(remainingProductionTransit, remaining);
                    remainingProductionTransit = remainingProductionTransit.subtract(productionTransit);
                    remaining = remaining.subtract(productionTransit);
                } else if (PROCUREMENT_EXTERNAL.equalsIgnoreCase(demand.getProcurementType())) {
                    purchaseTransit = min(purchaseAvailable, remaining);
                    remaining = remaining.subtract(purchaseTransit);
                }

                item.setInvestedQuantity(invested);
                item.setStockQuantity(allocatedStock);
                item.setProductionTransit(productionTransit);
                item.setPurchaseTransit(purchaseTransit);
                item.setApplicableTransit(productionTransit.add(purchaseTransit));
                item.setShortageQuantity(nonNegative(remaining));
                setPurchaseSummary(item, purchases);
                result.add(item);
            }
        }
        result.sort(Comparator.comparing(ProductionMaterialSupplyRespVO::getScheduledDate,
                        Comparator.nullsLast(Comparator.naturalOrder()))
                .thenComparing(ProductionMaterialSupplyRespVO::getProductionOrderNo,
                        Comparator.nullsLast(Comparator.naturalOrder()))
                .thenComparing(ProductionMaterialSupplyRespVO::getComponentMaterialNo,
                        Comparator.nullsLast(Comparator.naturalOrder())));
        return result;
    }

    private static ProductionMaterialSupplyRespVO toResponse(ProductionMaterialDemandRow demand) {
        ProductionMaterialSupplyRespVO item = new ProductionMaterialSupplyRespVO();
        item.setProductionOrderNo(demand.getProductionOrderNo());
        item.setAssemblyMaterialNo(demand.getAssemblyMaterialNo());
        item.setAssemblyMaterialDesc(demand.getAssemblyMaterialDesc());
        item.setAssemblyDemandQuantity(nonNegative(demand.getAssemblyDemandQuantity()));
        item.setScheduledDate(demand.getScheduledDate());
        item.setComponentMaterialNo(demand.getComponentMaterialNo());
        item.setComponentMaterialDesc(demand.getComponentMaterialDesc());
        item.setDemandQuantity(nonNegative(demand.getDemandQuantity()));
        item.setProcurementType(demand.getProcurementType());
        item.setInvestmentDate(null);
        item.setDateSource("主计划排产日期（投料日期未落库）");
        return item;
    }

    private static void setPurchaseSummary(ProductionMaterialSupplyRespVO item,
                                           List<ProductionMaterialPurchaseRow> purchases) {
        if (purchases.isEmpty()) {
            return;
        }
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        ProductionMaterialPurchaseRow first = purchases.get(0);
        item.setProjectNo(first.getProductionOrderNo());
        item.setPurchaseMaterialNo(first.getMaterialNo());
        item.setPurchaseOrderSummary(joinDistinct(purchases.stream()
                .map(ProductionMaterialPurchaseRow::getBuyerOrderNo).collect(Collectors.toList())));
        item.setSupplierSummary(joinDistinct(purchases.stream()
                .map(ProductionMaterialPurchaseRow::getSupplierDesc).collect(Collectors.toList())));
        item.setPurchaseOrderDateSummary(joinDistinct(purchases.stream()
                .map(ProductionMaterialPurchaseRow::getOrderDate).filter(Objects::nonNull)
                .map(formatter::format).collect(Collectors.toList())));
        item.setDeliveryDateSummary(joinDistinct(purchases.stream()
                .map(ProductionMaterialPurchaseRow::getRequiredArrivalDate).filter(Objects::nonNull)
                .map(formatter::format).collect(Collectors.toList())));
        item.setPurchaseMaterialDesc(purchases.stream()
                .map(ProductionMaterialPurchaseRow::getMaterialDesc)
                .filter(Objects::nonNull).findFirst().orElse(item.getComponentMaterialDesc()));
    }

    private static String joinDistinct(List<String> values) {
        Set<String> distinct = values.stream().filter(value -> value != null && !value.isEmpty())
                .collect(Collectors.toCollection(java.util.TreeSet::new));
        return String.join("、", distinct);
    }

    private static String key(String productionOrderNo, String materialNo) {
        return String.valueOf(productionOrderNo) + '\u0001' + String.valueOf(materialNo);
    }

    private static BigDecimal nonNegative(BigDecimal value) {
        return value == null || value.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : value;
    }

    private static BigDecimal min(BigDecimal left, BigDecimal right) {
        return left.compareTo(right) <= 0 ? left : right;
    }
}
