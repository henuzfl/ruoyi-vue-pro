package cn.iocoder.yudao.module.marketing.service.salesorder;

import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.marketing.controller.admin.salesorder.vo.SalesOrderImportVO;
import cn.iocoder.yudao.module.marketing.controller.admin.salesorder.vo.SalesOrderPageReqVO;
import cn.iocoder.yudao.module.marketing.controller.admin.salesorder.vo.SalesOrderSaveReqVO;
import cn.iocoder.yudao.module.marketing.dal.dataobject.salesorder.SalesOrderDO;
import cn.iocoder.yudao.module.marketing.dal.mysql.salesorder.SalesOrderMapper;
import com.alibaba.excel.EasyExcel;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

@Service
@DS("oracle")
@Slf4j
public class SalesOrderServiceImpl implements SalesOrderService {

    @Resource
    private SalesOrderMapper salesOrderMapper;

    @Override
    public Long createSalesOrder(SalesOrderSaveReqVO createReqVO) {
        SalesOrderDO entity = BeanUtils.toBean(createReqVO, SalesOrderDO.class);
        salesOrderMapper.insert(entity);
        return entity.getId();
    }

    @Override
    public void updateSalesOrder(SalesOrderSaveReqVO updateReqVO) {
        if (salesOrderMapper.selectById(updateReqVO.getId()) == null) {
            throw new ServiceException(400, "记录不存在");
        }
        SalesOrderDO updateObj = BeanUtils.toBean(updateReqVO, SalesOrderDO.class);
        salesOrderMapper.updateById(updateObj);
    }

    @Override
    public void deleteSalesOrder(Long id) {
        if (salesOrderMapper.selectById(id) == null) {
            throw new ServiceException(400, "记录不存在");
        }
        salesOrderMapper.deleteById(id);
    }

    @Override
    public SalesOrderDO getSalesOrder(Long id) {
        return salesOrderMapper.selectById(id);
    }

    @Override
    public PageResult<SalesOrderDO> getSalesOrderPage(SalesOrderPageReqVO pageReqVO) {
        return salesOrderMapper.selectPage(pageReqVO);
    }

    @Override
    public List<SalesOrderDO> getExportList(SalesOrderPageReqVO reqVO) {
        return salesOrderMapper.selectList(new LambdaQueryWrapperX<SalesOrderDO>()
                .eqIfPresent(SalesOrderDO::getOrderNumber, reqVO.getOrderNumber())
                .likeIfPresent(SalesOrderDO::getMaterialCode, reqVO.getMaterialCode())
                .eqIfPresent(SalesOrderDO::getSoldToParty, reqVO.getSoldToParty())
                .eqIfPresent(SalesOrderDO::getSalesOrganization, reqVO.getSalesOrganization())
                .eqIfPresent(SalesOrderDO::getPlant, reqVO.getPlant())
                .eqIfPresent(SalesOrderDO::getDeliveryStatus, reqVO.getDeliveryStatus())
                .betweenIfPresent(SalesOrderDO::getOrderDate, reqVO.getOrderDate())
                .betweenIfPresent(SalesOrderDO::getEarliestDeliveryDate, reqVO.getEarliestDeliveryDate())
                .eqIfPresent(SalesOrderDO::getOrderQuantity, reqVO.getOrderQuantity())
                .orderByDesc(SalesOrderDO::getId)
        );
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void importExcel(MultipartFile file) throws IOException {
        log.info("开始导入销售订单，文件：{}", file.getOriginalFilename());
        ZipSecureFile.setMinInflateRatio(0.001);

        List<Map<Integer, Object>> rows;
        try (InputStream is = file.getInputStream()) {
            rows = EasyExcel.read(is)
                    .headRowNumber(0)
                    .sheet(0)
                    .doReadSync();
        }

        if (rows == null || rows.size() < 2) {
            throw new ServiceException(400, "导入文件无数据");
        }

        List<SalesOrderDO> doList = new ArrayList<>();
        for (int i = 1; i < rows.size(); i++) {
            Map<Integer, Object> row = rows.get(i);
            SalesOrderImportVO importVO = mapRowToImportVO(row, i + 1);
            SalesOrderDO entity = convertImportVOToDO(importVO);
            entity.setId(IdWorker.getId());
            doList.add(entity);
        }

        int batchSize = 40;
        for (int i = 0; i < doList.size(); i += batchSize) {
            int end = Math.min(i + batchSize, doList.size());
            salesOrderMapper.insertBatch(doList.subList(i, end));
        }
        log.info("销售订单导入完成，共 {} 条", doList.size());
    }

    // ----- 工具方法 -----
    private SalesOrderImportVO mapRowToImportVO(Map<Integer, Object> row, int rowNum) {
        SalesOrderImportVO vo = new SalesOrderImportVO();
        try {
            vo.setSalesOrganization(getString(row, 0));
            vo.setSalesDepartment(getString(row, 1));
            vo.setSoldToParty(getString(row, 2));
            vo.setSoldToPartyName(getString(row, 3));
            vo.setSalesRegion(getString(row, 4));
            vo.setOrderDateStr(getString(row, 5));
            vo.setOrderType(getString(row, 6));
            vo.setApprovalStatus(getString(row, 7));
            vo.setOrderNumber(getString(row, 8));
            vo.setOrderItem(getInteger(row, 9));
            vo.setAccountSettingGroup(getString(row, 10));
            vo.setMaterialCode(getString(row, 11));
            vo.setMaterialDescription(getString(row, 12));
            vo.setEarliestDeliveryDateStr(getString(row, 13));
            vo.setOrderQuantity(getBigDecimal(row, 14));
            vo.setUnit(getString(row, 15));
            vo.setShipToParty(getString(row, 16));
            vo.setShipToPartyName(getString(row, 17));
            vo.setUnloadingPoint(getString(row, 18));
            vo.setPriceListType(getString(row, 19));
            vo.setPricingDateStr(getString(row, 20));
            vo.setUnitPrice(getBigDecimal(row, 21));
            vo.setNetSalesValue(getBigDecimal(row, 22));
            vo.setSubtotal(getBigDecimal(row, 23));
            vo.setTaxAmount(getBigDecimal(row, 24));
            vo.setLatestSalesPrice(getBigDecimal(row, 25));
            vo.setPriceExclTax(getBigDecimal(row, 26));
            vo.setLatestAmount(getBigDecimal(row, 27));
            vo.setNetWeight(getBigDecimal(row, 28));
            vo.setGrossWeight(getBigDecimal(row, 29));
            vo.setWeightUnit(getString(row, 30));
            vo.setPlant(getString(row, 31));
            vo.setShippingPoint(getString(row, 32));
            vo.setStorageLocation(getString(row, 33));
            vo.setDeliveredQuantity(getBigDecimal(row, 34));
            vo.setShippedQuantity(getBigDecimal(row, 35));
            vo.setInvoicedQuantity(getBigDecimal(row, 36));
            vo.setDeliveryStatus(getString(row, 37));
            vo.setCreatorName(getString(row, 38));
            vo.setCreationDateStr(getString(row, 39));
            vo.setDeliveryBlock(getString(row, 40));
            vo.setInvoiceBlock(getString(row, 41));
            vo.setOrderReason(getString(row, 42));
            vo.setRejectionReason(getString(row, 43));
            vo.setInvoiceType(getString(row, 44));
            vo.setMaterialGroup(getString(row, 45));
        } catch (Exception e) {
            throw new ServiceException(400, "第 " + rowNum + " 行数据解析失败：" + e.getMessage());
        }
        return vo;
    }

    private SalesOrderDO convertImportVOToDO(SalesOrderImportVO vo) {
        SalesOrderDO entity = new SalesOrderDO();
        entity.setSalesOrganization(vo.getSalesOrganization());
        entity.setSalesDepartment(vo.getSalesDepartment());
        entity.setSoldToParty(vo.getSoldToParty());
        entity.setSoldToPartyName(vo.getSoldToPartyName());
        entity.setSalesRegion(vo.getSalesRegion());
        entity.setOrderDate(parseDate(vo.getOrderDateStr()));
        entity.setOrderType(vo.getOrderType());
        entity.setApprovalStatus(vo.getApprovalStatus());
        entity.setOrderNumber(vo.getOrderNumber());
        entity.setOrderItem(vo.getOrderItem());
        entity.setAccountSettingGroup(vo.getAccountSettingGroup());
        entity.setMaterialCode(vo.getMaterialCode());
        entity.setMaterialDescription(vo.getMaterialDescription());
        entity.setEarliestDeliveryDate(parseDate(vo.getEarliestDeliveryDateStr()));
        entity.setOrderQuantity(vo.getOrderQuantity());
        entity.setUnit(vo.getUnit());
        entity.setShipToParty(vo.getShipToParty());
        entity.setShipToPartyName(vo.getShipToPartyName());
        entity.setUnloadingPoint(vo.getUnloadingPoint());
        entity.setPriceListType(vo.getPriceListType());
        entity.setPricingDate(parseDate(vo.getPricingDateStr()));
        entity.setUnitPrice(vo.getUnitPrice());
        entity.setNetSalesValue(vo.getNetSalesValue());
        entity.setSubtotal(vo.getSubtotal());
        entity.setTaxAmount(vo.getTaxAmount());
        entity.setLatestSalesPrice(vo.getLatestSalesPrice());
        entity.setPriceExclTax(vo.getPriceExclTax());
        entity.setLatestAmount(vo.getLatestAmount());
        entity.setNetWeight(vo.getNetWeight());
        entity.setGrossWeight(vo.getGrossWeight());
        entity.setWeightUnit(vo.getWeightUnit());
        entity.setPlant(vo.getPlant());
        entity.setShippingPoint(vo.getShippingPoint());
        entity.setStorageLocation(vo.getStorageLocation());
        entity.setDeliveredQuantity(vo.getDeliveredQuantity());
        entity.setShippedQuantity(vo.getShippedQuantity());
        entity.setInvoicedQuantity(vo.getInvoicedQuantity());
        entity.setDeliveryStatus(vo.getDeliveryStatus());
        entity.setCreatorName(vo.getCreatorName());
        entity.setCreationDate(parseDate(vo.getCreationDateStr()));
        entity.setDeliveryBlock(vo.getDeliveryBlock());
        entity.setInvoiceBlock(vo.getInvoiceBlock());
        entity.setOrderReason(vo.getOrderReason());
        entity.setRejectionReason(vo.getRejectionReason());
        entity.setInvoiceType(vo.getInvoiceType());
        entity.setMaterialGroup(vo.getMaterialGroup());
        return entity;
    }

    private String getString(Map<Integer, Object> row, int index) {
        Object val = row.get(index);
        return val == null ? null : val.toString().trim();
    }

    private Integer getInteger(Map<Integer, Object> row, int index) {
        String str = getString(row, index);
        if (str == null || str.isEmpty()) return null;
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private BigDecimal getBigDecimal(Map<Integer, Object> row, int index) {
        String str = getString(row, index);
        if (str == null || str.isEmpty()) return null;
        try {
            return new BigDecimal(str);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Date parseDate(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) return null;
        dateStr = dateStr.replaceAll("^[\\s\\uFEFF]+|[\\s\\uFEFF]+$", "");
        DateTimeFormatter[] formatters = {
                DateTimeFormatter.ofPattern("yyyy/M/d"),
                DateTimeFormatter.ofPattern("yyyy-MM-dd"),
                DateTimeFormatter.ofPattern("yyyy/M/dd"),
                DateTimeFormatter.ofPattern("yyyy-MM-dd"),
                DateTimeFormatter.ofPattern("yyyy/MM/dd")
        };
        for (DateTimeFormatter formatter : formatters) {
            try {
                LocalDate localDate = LocalDate.parse(dateStr, formatter);
                return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            } catch (DateTimeParseException ignored) {}
        }
        throw new ServiceException(400, "日期格式错误：" + dateStr);
    }
}