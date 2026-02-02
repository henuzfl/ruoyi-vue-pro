package cn.iocoder.yudao.module.buyer.service.buyerstock;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.buyer.controller.admin.buyerstock.vo.*;
import cn.iocoder.yudao.module.buyer.dal.dataobject.buyerstock.buyerStockDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.buyer.dal.mysql.buyerstock.buyerStockMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.buyer.enums.ErrorCodeConstants.*;

/**
 * 供应商库存 Service 实现类
 *
 * @author 柳文
 */
@Service
@DS("oracle") // 指定使用 Oracle 数据源
@Validated
public class buyerStockServiceImpl implements buyerStockService {

    @Resource
    private buyerStockMapper buyerStockMapper;

    @Override
    public Long createbuyerStock(buyerStockSaveReqVO createReqVO) {
        // 插入
        buyerStockDO buyerStock = BeanUtils.toBean(createReqVO, buyerStockDO.class);
        buyerStockMapper.insert(buyerStock);
        // 返回
        return buyerStock.getId();
    }

    @Override
    public void updatebuyerStock(buyerStockSaveReqVO updateReqVO) {
        // 校验存在
        validatebuyerStockExists(updateReqVO.getId());
        // 更新
        buyerStockDO updateObj = BeanUtils.toBean(updateReqVO, buyerStockDO.class);
        buyerStockMapper.updateById(updateObj);
    }

    @Override
    public void deletebuyerStock(Long id) {
        // 校验存在
        validatebuyerStockExists(id);
        // 删除
        buyerStockMapper.deleteById(id);
    }

    private void validatebuyerStockExists(Long id) {
        if (buyerStockMapper.selectById(id) == null) {
            throw exception(BUYER_STOCK_NOT_EXISTS);
        }
    }

    @Override
    public buyerStockDO getbuyerStock(Long id) {
        return buyerStockMapper.selectById(id);
    }

    @Override
    public PageResult<buyerStockDO> getbuyerStockPage(buyerStockPageReqVO pageReqVO) {
        return buyerStockMapper.selectPage(pageReqVO);
    }

}