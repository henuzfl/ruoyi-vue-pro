package cn.iocoder.yudao.module.aps.service.productionmaterialsupply;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.aps.controller.admin.productionmaterialsupply.vo.ProductionMaterialSupplyPageReqVO;
import cn.iocoder.yudao.module.aps.controller.admin.productionmaterialsupply.vo.ProductionMaterialSupplyRespVO;
import cn.iocoder.yudao.module.aps.dal.dataobject.productionmaterialsupply.ProductionMaterialAvailableRow;
import cn.iocoder.yudao.module.aps.dal.dataobject.productionmaterialsupply.ProductionMaterialDemandRow;
import cn.iocoder.yudao.module.aps.dal.dataobject.productionmaterialsupply.ProductionMaterialPurchaseRow;
import cn.iocoder.yudao.module.aps.dal.mysql.productionmaterialsupply.ProductionMaterialSupplyMapper;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ProductionMaterialSupplyServiceImplTest {

    private final ProductionMaterialSupplyServiceImpl service = new ProductionMaterialSupplyServiceImpl();

    @Test
    void pageReq_shouldRequireValidDateRangeWithinThreeCalendarMonths() {
        Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
        ProductionMaterialSupplyPageReqVO missing = new ProductionMaterialSupplyPageReqVO();
        Set<ConstraintViolation<ProductionMaterialSupplyPageReqVO>> missingViolations =
                validator.validate(missing);
        assertThat(missingViolations).extracting(ConstraintViolation::getMessage)
                .contains("排产日期开始不能为空", "排产日期结束不能为空");

        ProductionMaterialSupplyPageReqVO reversed = request(date(2026, Calendar.APRIL, 2),
                date(2026, Calendar.APRIL, 1));
        assertThat(validator.validate(reversed)).extracting(ConstraintViolation::getMessage)
                .contains("排产日期开始不能晚于结束日期");

        ProductionMaterialSupplyPageReqVO exactThreeMonths = request(
                date(2026, Calendar.JANUARY, 31), date(2026, Calendar.APRIL, 30));
        assertThat(validator.validate(exactThreeMonths)).isEmpty();

        ProductionMaterialSupplyPageReqVO overThreeMonths = request(
                date(2026, Calendar.JANUARY, 31), date(2026, Calendar.MAY, 1));
        assertThat(validator.validate(overThreeMonths)).extracting(ConstraintViolation::getMessage)
                .contains("排产日期范围不能超过 3 个自然月");
    }

    @Test
    void calculate_shouldUseProductionTransitOnlyForInternalMaterial() {
        ProductionMaterialDemandRow demand = demand("1001", "M1", "F", 10, new Date(2_000_000));
        ProductionMaterialAvailableRow available = available("M1", 2, 4);
        ProductionMaterialPurchaseRow purchase = purchase("1001", "M1", 100, new Date(1_000_000));

        ProductionMaterialSupplyRespVO result = service.calculate(
                Collections.singletonList(demand), Collections.singletonList(available),
                Collections.singletonList(purchase)).get(0);

        assertThat(result.getStockQuantity()).isEqualByComparingTo("2");
        assertThat(result.getProductionTransit()).isEqualByComparingTo("4");
        assertThat(result.getPurchaseTransit()).isZero();
        assertThat(result.getShortageQuantity()).isEqualByComparingTo("4");
        assertThat(result.getProjectNo()).isEqualTo("1001");
        assertThat(result.getPurchaseMaterialNo()).isEqualTo("M1");
        assertThat(result.getPurchaseMaterialDesc()).isEqualTo("采购物料");
    }

    @Test
    void calculate_shouldAllocateSharedStockByScheduleAndApplyEligiblePurchaseTransit() {
        ProductionMaterialDemandRow first = demand("1001", "M1", "E", 4, new Date(1_000_000));
        ProductionMaterialDemandRow second = demand("1002", "M1", "E", 6, new Date(2_000_000));
        ProductionMaterialAvailableRow available = available("M1", 5, 100);
        ProductionMaterialPurchaseRow latePurchase = purchase("1001", "M1", 9, new Date(3_000_000));
        ProductionMaterialPurchaseRow eligiblePurchase = purchase("1002", "M1", 3, new Date(1_500_000));

        List<ProductionMaterialSupplyRespVO> results = service.calculate(
                Arrays.asList(second, first), Collections.singletonList(available),
                Arrays.asList(latePurchase, eligiblePurchase));

        assertThat(results.get(0).getProductionOrderNo()).isEqualTo("1001");
        assertThat(results.get(0).getStockQuantity()).isEqualByComparingTo("4");
        assertThat(results.get(0).getPurchaseTransit()).isZero();
        assertThat(results.get(0).getShortageQuantity()).isZero();
        assertThat(results.get(1).getStockQuantity()).isEqualByComparingTo("1");
        assertThat(results.get(1).getPurchaseTransit()).isEqualByComparingTo("3");
        assertThat(results.get(1).getShortageQuantity()).isEqualByComparingTo("2");
    }

    @Test
    void getPage_shouldQueryAvailabilityByDemandMaterialsAndSkipPurchasesForInternalMaterial() {
        ProductionMaterialSupplyMapper mapper = mock(ProductionMaterialSupplyMapper.class);
        ReflectionTestUtils.setField(service, "mapper", mapper);
        ProductionMaterialDemandRow demand = demand("1001", "M1", "F", 10, new Date(2_000_000));
        when(mapper.selectDemandRows(org.mockito.ArgumentMatchers.any()))
                .thenReturn(Collections.singletonList(demand));
        when(mapper.selectAvailableRows(anyList()))
                .thenReturn(Collections.singletonList(available("M1", 2, 4)));
        ProductionMaterialSupplyPageReqVO reqVO = new ProductionMaterialSupplyPageReqVO();
        reqVO.setPageNo(1);
        reqVO.setPageSize(10);

        PageResult<ProductionMaterialSupplyRespVO> result = service.getPage(reqVO);

        assertThat(result.getList()).hasSize(1);
        ProductionMaterialSupplyRespVO item = result.getList().get(0);
        assertThat(item.getPurchaseOrderSummary()).isNull();
        assertThat(item.getProjectNo()).isNull();
        assertThat(item.getPurchaseMaterialNo()).isNull();
        assertThat(item.getPurchaseMaterialDesc()).isNull();
        verify(mapper).selectAvailableRows(Collections.singletonList("M1"));
        verify(mapper, never()).selectPurchaseRows(anyList());
    }

    private static ProductionMaterialDemandRow demand(String orderNo, String materialNo,
                                                       String procurementType, int quantity, Date date) {
        ProductionMaterialDemandRow row = new ProductionMaterialDemandRow();
        row.setProductionOrderNo(orderNo);
        row.setAssemblyMaterialNo("A1");
        row.setComponentMaterialNo(materialNo);
        row.setComponentMaterialDesc("测试物料");
        row.setProcurementType(procurementType);
        row.setDemandQuantity(BigDecimal.valueOf(quantity));
        row.setInvestedQuantity(BigDecimal.ZERO);
        row.setScheduledDate(date);
        return row;
    }

    private static ProductionMaterialSupplyPageReqVO request(Date start, Date end) {
        ProductionMaterialSupplyPageReqVO reqVO = new ProductionMaterialSupplyPageReqVO();
        reqVO.setScheduledDateStart(start);
        reqVO.setScheduledDateEnd(end);
        return reqVO;
    }

    private static Date date(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(year, month, day);
        return calendar.getTime();
    }

    private static ProductionMaterialAvailableRow available(String materialNo, int stock, int transit) {
        ProductionMaterialAvailableRow row = new ProductionMaterialAvailableRow();
        row.setMaterialNo(materialNo);
        row.setStockQuantity(BigDecimal.valueOf(stock));
        row.setProductionTransit(BigDecimal.valueOf(transit));
        return row;
    }

    private static ProductionMaterialPurchaseRow purchase(String orderNo, String materialNo,
                                                           int quantity, Date deliveryDate) {
        ProductionMaterialPurchaseRow row = new ProductionMaterialPurchaseRow();
        row.setProductionOrderNo(orderNo);
        row.setMaterialNo(materialNo);
        row.setMaterialDesc("采购物料");
        row.setBuyerOrderNo("PO-1");
        row.setOpenQuantity(BigDecimal.valueOf(quantity));
        row.setRequiredArrivalDate(deliveryDate);
        return row;
    }
}
