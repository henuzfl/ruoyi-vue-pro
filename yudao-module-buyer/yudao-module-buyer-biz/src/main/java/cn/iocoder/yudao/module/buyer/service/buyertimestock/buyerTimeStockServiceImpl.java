package cn.iocoder.yudao.module.buyer.service.buyertimestock;

import com.baomidou.dynamic.datasource.annotation.DS;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.buyer.controller.admin.buyertimestock.vo.*;
import cn.iocoder.yudao.module.buyer.dal.dataobject.buyertimestock.buyerTimeStockDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import java.time.LocalDateTime;
import org.springframework.util.StringUtils;
import java.math.BigDecimal;

import cn.iocoder.yudao.module.buyer.dal.mysql.buyertimestock.buyerTimeStockMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.buyer.enums.ErrorCodeConstants.*;

/**
 * 实时库存 Service 实现类
 *
 * @author 柳文
 */
@Service
@DS("oracle") // 指定使用 Oracle 数据源
@Validated
@Slf4j
public class buyerTimeStockServiceImpl implements buyerTimeStockService {

    @Resource
    private buyerTimeStockMapper buyerTimeStockMapper;

    @Override
    public Long createbuyerTimeStock(buyerTimeStockSaveReqVO createReqVO) {
        // 插入
        buyerTimeStockDO buyerTimeStock = BeanUtils.toBean(createReqVO, buyerTimeStockDO.class);
        buyerTimeStockMapper.insert(buyerTimeStock);
        // 返回
        return buyerTimeStock.getId();
    }

    @Override
    public void updatebuyerTimeStock(buyerTimeStockSaveReqVO updateReqVO) {
        // 校验存在
        validatebuyerTimeStockExists(updateReqVO.getId());
        // 更新
        buyerTimeStockDO updateObj = BeanUtils.toBean(updateReqVO, buyerTimeStockDO.class);
        buyerTimeStockMapper.updateById(updateObj);
    }

    @Override
    public void deletebuyerTimeStock(Long id) {
        // 校验存在
        validatebuyerTimeStockExists(id);
        // 删除
        buyerTimeStockMapper.deleteById(id);
    }

    private void validatebuyerTimeStockExists(Long id) {
        if (buyerTimeStockMapper.selectById(id) == null) {
            throw exception(BUYER_TIME_STOCK_NOT_EXISTS);
        }
    }

    @Override
    public buyerTimeStockDO getbuyerTimeStock(Long id) {
        return buyerTimeStockMapper.selectById(id);
    }

    @Override
    public PageResult<buyerTimeStockDO> getbuyerTimeStockPage(buyerTimeStockPageReqVO pageReqVO) {
        return buyerTimeStockMapper.selectPage(pageReqVO);
    }
    /**
     * 导入SAP库存数据到实时库存表（先全部删除，后全部插入）
     * @param sapStockList SAP库存数据列表
     * @param mainMaterialNo 主物料号（可选，用于标识同步批次）
     * @param plant 工厂（用于筛选删除条件）
     * @return 导入结果信息
     */


    /**
     * 批量插入或更新库存数据
     * @param stockList 库存数据列表
     * @return 处理结果信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String batchInsertOrUpdateStock(List<buyerTimeStockDO> stockList) {
        log.info("开始批量处理库存数据，数量: {}", stockList.size());

        if (stockList == null || stockList.isEmpty()) {
            return "库存数据为空，无需处理";
        }

        // 1. 先删除所有库存数据
        try {
            buyerTimeStockMapper.deleteAll();
            log.info("已清空所有库存数据");
        } catch (Exception e) {
            log.error("清空库存数据失败", e);
            throw new RuntimeException("清空库存数据失败: " + e.getMessage());
        }

        int successCount = 0;
        int errorCount = 0;
        List<String> errorMessages = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        // 2. 批量插入新的库存数据
        for (int i = 0; i < stockList.size(); i++) {
            buyerTimeStockDO stock = stockList.get(i);
            try {
                // 检查数据完整性
                if (!validateStockData(stock)) {
                    errorCount++;
                    String errorMsg = String.format("第%d条库存数据不完整，跳过", i + 1);
                    errorMessages.add(errorMsg);
                    log.warn(errorMsg + ": {}", stock);
                    continue;
                }

                // 设置创建时间
                stock.setCreateTime(now);
                stock.setUpdateTime(now);

                // 插入操作（不需要查询是否已存在，因为已经全部删除了）
                buyerTimeStockMapper.insert(stock);
                successCount++;

            } catch (Exception e) {
                errorCount++;
                String errorMsg = String.format("第%d条库存数据插入失败: %s", i + 1, e.getMessage());
                errorMessages.add(errorMsg);
                log.error(errorMsg, e);
            }
        }

        log.info("批量处理库存数据完成: 成功={}, 失败={}", successCount, errorCount);

        // 构建返回消息
        StringBuilder resultMsg = new StringBuilder();
        resultMsg.append(String.format("处理完成: 成功插入 %d 条, 失败 %d 条", successCount, errorCount));
        if (!errorMessages.isEmpty()) {
            resultMsg.append("\n失败详情:");
            for (int i = 0; i < Math.min(errorMessages.size(), 10); i++) {
                resultMsg.append("\n").append(errorMessages.get(i));
            }
        }

        return resultMsg.toString();
    }

    /**
     * 将SAP数据转换为buyerTimeStockDO对象
     */
    private buyerTimeStockDO convertSapDataToStockDO(Map<String, Object> sapData, String mainMaterialNo, int seq) {
        try {
            buyerTimeStockDO stock = new buyerTimeStockDO();

            // 映射SAP字段到本地字段
            // SAP字段"MATNR"映射到materialNo
            stock.setMaterialNo(getStringValue(sapData, "MATNR"));
            // SAP字段"MAKTX"映射到materialDesc
            stock.setMaterialDesc(getStringValue(sapData, "MAKTX"));
            // SAP字段"LGORT"映射到stockLocation
            stock.setStockLocation(getStringValue(sapData, "LGORT"));

            // 处理库存数量
            Object stockQty = sapData.get("LABST");
            if (stockQty != null) {
                try {
                    BigDecimal stockQuantity = new BigDecimal(stockQty.toString());
                    stock.setStockQuantity(stockQuantity);
                    // 可用数量默认为库存数量
                    stock.setAvailableQuantity(stockQuantity);
                } catch (NumberFormatException e) {
                    stock.setStockQuantity(BigDecimal.ZERO);
                    stock.setAvailableQuantity(BigDecimal.ZERO);
                }
            } else {
                stock.setStockQuantity(BigDecimal.ZERO);
                stock.setAvailableQuantity(BigDecimal.ZERO);
            }

            // 默认状态为正常
            stock.setStatus(0);

            // 设置备注信息
            String remark = String.format("从SAP同步，批次号：%s，序号：%d",
                    mainMaterialNo != null ? mainMaterialNo : "未知", seq);
            stock.setRemark(remark);

            return stock;
        } catch (Exception e) {
            log.error("转换SAP数据失败: {}", sapData, e);
            return null;
        }
    }

    /**
     * 安全获取字符串值
     */
    private String getStringValue(Map<String, Object> map, String key) {
        Object value = map.get(key);
        return value != null ? value.toString() : "";
    }

    /**
     * 验证库存数据的完整性
     */
    private boolean validateStockData(buyerTimeStockDO stock) {
        if (stock == null) {
            return false;
        }

        if (!StringUtils.hasText(stock.getMaterialNo())) {
            return false;
        }

        if (!StringUtils.hasText(stock.getStockLocation())) {
            return false;
        }

        return true;
    }
}