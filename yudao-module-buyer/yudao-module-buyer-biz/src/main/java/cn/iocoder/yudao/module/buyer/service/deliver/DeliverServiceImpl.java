package cn.iocoder.yudao.module.buyer.service.deliver;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.buyer.controller.admin.deliver.vo.*;
import cn.iocoder.yudao.module.buyer.dal.dataobject.deliver.DeliverDO;
import cn.iocoder.yudao.module.buyer.dal.mysql.deliver.DeliverMapper;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.buyer.enums.ErrorCodeConstants.DELIVER_NOT_EXISTS;

/**
 * 配送与采购报表 Service 实现类
 *
 * @author 柳文
 */
@Service
@DS("oracle") // 指定使用 Oracle 数据源
@Validated
@Slf4j
public class DeliverServiceImpl extends ServiceImpl<DeliverMapper, DeliverDO> implements DeliverService {

    @Override
    public Long createDeliver(DeliverSaveReqVO createReqVO) {
        DeliverDO deliver = BeanUtils.toBean(createReqVO, DeliverDO.class);
        baseMapper.insert(deliver);   // id 由雪花自动填充
        return deliver.getId();       // 返回 Long
    }

    @Override
    public void updateDeliver(DeliverSaveReqVO updateReqVO) {
        validateDeliverExists(updateReqVO.getId());
        DeliverDO updateObj = BeanUtils.toBean(updateReqVO, DeliverDO.class);
        baseMapper.updateById(updateObj);
    }

    @Override
    public void deleteDeliver(Long id) {
        // 校验存在
        validateDeliverExists(id);
        // 删除
        baseMapper.deleteById(id);
    }

    private void validateDeliverExists(Long id) {
        if (baseMapper.selectById(id) == null) {
            throw exception(DELIVER_NOT_EXISTS);
        }
    }

    @Override
    public DeliverDO getDeliver(Long id) {
        return baseMapper.selectById(id);
    }

    @Override
    public PageResult<DeliverDO> getDeliverPage(DeliverPageReqVO pageReqVO) {
        return baseMapper.selectPage(pageReqVO);
    }
    // ========== 导入功能（使用 Joda-Time）==========
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int importDeliver(MultipartFile file) throws IOException {
        List<DeliverImportVO> importList = new ArrayList<>();

        EasyExcel.read(file.getInputStream())
                .sheet()
                .registerReadListener(new AnalysisEventListener<Map<Integer, Object>>() {
                    private int rowNum = 0;

                    @Override
                    public void invoke(Map<Integer, Object> rowMap, AnalysisContext context) {
                        rowNum++;
                        if (rowNum == 1) return; // 跳过标题行
                        String deliveryOrderNo = getString(rowMap.get(1)); // 提前获取配送单号
                        DeliverImportVO vo = new DeliverImportVO();
                        vo.setPlant(getString(rowMap.get(0)));
                        vo.setDeliveryOrderNo(deliveryOrderNo);
                        vo.setDeliveryDate(parseLocalDateTime(rowMap.get(2)));
                        vo.setCreationDate(parseLocalDateTime(rowMap.get(3)));
                        vo.setCreationTime(getString(rowMap.get(4)));
                        vo.setCreatedBy(getString(rowMap.get(5)));
                        vo.setLastUpdateDate(parseLocalDateTime(rowMap.get(6)));
                        vo.setLastUpdateTime(getString(rowMap.get(7)));
                        vo.setLastUpdatedBy(getString(rowMap.get(8)));
                        vo.setPartOrderNo(getString(rowMap.get(9)));
                        vo.setProductionWorkshop(getString(rowMap.get(10)));
                        vo.setPartMatCode(getString(rowMap.get(11)));
                        vo.setPartMatDesc(getString(rowMap.get(12)));
                        vo.setReservationNo(getString(rowMap.get(13)));
                        vo.setReservationItem(parseLong(rowMap.get(14), rowNum, deliveryOrderNo));
                        vo.setPlannedIssueQty(parseBigDecimal(rowMap.get(15), rowNum, deliveryOrderNo));
                        vo.setDeliveredQty(parseBigDecimal(rowMap.get(16), rowNum, deliveryOrderNo));
                        vo.setUndeliveredQty(parseBigDecimal(rowMap.get(17), rowNum, deliveryOrderNo));
                        vo.setBuyerMaterialNo(getString(rowMap.get(18)));
                        vo.setOldMaterialNo(getString(rowMap.get(19)));
                        vo.setBuyerMaterialDesc(getString(rowMap.get(20)));
                        vo.setDeliverySupplierCode(getString(rowMap.get(21)));
                        vo.setDeliverySupplierName(getString(rowMap.get(22)));
                        vo.setStockSufficientFlag(getString(rowMap.get(23)));
                        vo.setTotalStockQty(parseBigDecimal(rowMap.get(24), rowNum, deliveryOrderNo));
                        vo.setCurrentStockConsumeQty(parseBigDecimal(rowMap.get(25), rowNum, deliveryOrderNo));
                        vo.setDeliveryStorageLoc(getString(rowMap.get(26)));
                        vo.setPoSufficientFlag(getString(rowMap.get(27)));
                        vo.setBuyerOrderNo(getString(rowMap.get(28)));
                        vo.setLineItem(parseLong(rowMap.get(29), rowNum, deliveryOrderNo));
                        vo.setRequirementTrackingNo(getString(rowMap.get(30)));
                        vo.setOrderQty(parseBigDecimal(rowMap.get(31), rowNum, deliveryOrderNo));
                        vo.setOpenQty(parseBigDecimal(rowMap.get(32), rowNum, deliveryOrderNo));
                        vo.setReceivedQty(parseBigDecimal(rowMap.get(33), rowNum, deliveryOrderNo));
                        vo.setPoDeliveryDate(parseLocalDateTime(rowMap.get(34)));
                        vo.setPoSupplierCode(getString(rowMap.get(35)));
                        vo.setSupplierDesc(getString(rowMap.get(36)));
                        vo.setBuyerPurchasingGroup(getString(rowMap.get(37)));
                        vo.setOrderingBuyer(getString(rowMap.get(38)));
                        vo.setDeliveryBuyer(getString(rowMap.get(39)));

                        if (vo.getDeliveryOrderNo() == null || vo.getDeliveryOrderNo().isEmpty()) {
                            throw new RuntimeException("第" + rowNum + "行：配送单号不能为空");
                        }
                        importList.add(vo);
                    }

                    @Override
                    public void doAfterAllAnalysed(AnalysisContext context) {
                        log.info("Excel 解析完成，共 {} 行数据", importList.size());
                    }
                })
                .doRead();

        if (importList.isEmpty()) {
            return 0;
        }

        // 2. 物理删除表中所有现有数据（全量覆盖）
        baseMapper.physicalDeleteAll();

        List<DeliverDO> doList = new ArrayList<>();
        for (DeliverImportVO vo : importList) {
            DeliverDO entity = BeanUtils.toBean(vo, DeliverDO.class);
            long snowId = IdWorker.getId();
            entity.setId(snowId);
            doList.add(entity);
        }

        saveBatch(doList, 500);
        return doList.size();
    }

    // ---------- Joda-Time 日期解析辅助方法 ----------
    private LocalDateTime parseLocalDateTime(Object obj) {
        if (obj == null) return null;
        String str = obj.toString().trim();
        if (str.isEmpty()) return null;

        // 处理 Excel 数值日期
        try {
            double excelDateNum = Double.parseDouble(str);
            if (excelDateNum > 0 && excelDateNum < 60000) {
                // Joda-Time: Excel 起始日期为 1900-01-01，偏移 -2 天
                int days = (int) (excelDateNum - 2);   // 转为 int
                LocalDate date = new LocalDate(1900, 1, 1).plusDays(days);
                return date.toLocalDateTime(LocalTime.MIDNIGHT); // 使用 LocalTime.MIDNIGHT
            }
        } catch (NumberFormatException ignored) {
        }

        // 处理字符串日期格式
        String normalized = str.replace('/', '-');
        List<String> patterns = Arrays.asList(
                "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd",
                "yyyy-M-d HH:mm:ss", "yyyy-M-d",
                "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd",
                "yyyy/M/d HH:mm:ss", "yyyy/M/d"
        );
        for (String pattern : patterns) {
            try {
                DateTimeFormatter formatter = DateTimeFormat.forPattern(pattern);
                if (pattern.contains("HH:mm:ss")) {
                    return LocalDateTime.parse(normalized, formatter);
                } else {
                    LocalDate date = LocalDate.parse(normalized, formatter);
                    return date.toLocalDateTime(LocalTime.MIDNIGHT);
                }
            } catch (Exception ignored) {
            }
        }
        log.warn("日期解析失败: {}", str);
        return null;
    }

    private BigDecimal parseBigDecimal(Object obj, int rowNum, String deliveryOrderNo) {
        if (obj == null) return null;
        String str = obj.toString().trim();
        if (str.isEmpty()) return null;

        // 移除千分位逗号
        String normalized = str.replace(",", "");

        try {
            return new BigDecimal(normalized);
        } catch (NumberFormatException e) {
            log.warn("数字解析失败，行号: {}, 配送单号: {}, 原始值: {}", rowNum, deliveryOrderNo, str);
            return null;
        }
    }

    private Long parseLong(Object obj, int rowNum, String deliveryOrderNo) {
        if (obj == null) return null;
        String str = obj.toString().trim();
        if (str.isEmpty()) return null;

        // 移除千分位逗号
        String normalized = str.replace(",", "");

        try {
            return Long.parseLong(normalized);
        } catch (NumberFormatException e) {
            log.warn("Long解析失败，行号: {}, 配送单号: {}, 原始值: {}", rowNum, deliveryOrderNo, str);
            return null;
        }
    }

    private String getString(Object obj) {
        return obj == null ? null : obj.toString();
    }
}