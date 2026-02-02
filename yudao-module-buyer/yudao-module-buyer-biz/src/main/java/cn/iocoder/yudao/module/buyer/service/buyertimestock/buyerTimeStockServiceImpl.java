package cn.iocoder.yudao.module.buyer.service.buyertimestock;

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

import cn.iocoder.yudao.module.buyer.dal.mysql.buyertimestock.buyerTimeStockMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.buyer.enums.ErrorCodeConstants.*;

/**
 * 实时库存 Service 实现类
 *
 * @author 柳文
 */
@Service
@Validated
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

}