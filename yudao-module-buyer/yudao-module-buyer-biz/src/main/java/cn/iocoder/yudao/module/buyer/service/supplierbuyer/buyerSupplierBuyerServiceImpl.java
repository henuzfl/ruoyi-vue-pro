package cn.iocoder.yudao.module.buyer.service.supplierbuyer;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.buyer.controller.admin.supplierbuyer.vo.*;
import cn.iocoder.yudao.module.buyer.dal.dataobject.supplierbuyer.buyerSupplierBuyerDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.buyer.dal.mysql.supplierbuyer.buyerSupplierBuyerMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.buyer.enums.ErrorCodeConstants.*;

/**
 * 物料供应商采购员对应 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@DS("oracle") // 指定使用 Oracle 数据源
@Validated
public class buyerSupplierBuyerServiceImpl implements buyerSupplierBuyerService {

    @Resource
    private buyerSupplierBuyerMapper buyerSupplierBuyerMapper;

    @Override
    public Long createbuyerSupplierBuyer(buyerSupplierBuyerSaveReqVO createReqVO) {
        // 插入
        buyerSupplierBuyerDO buyerSupplierBuyer = BeanUtils.toBean(createReqVO, buyerSupplierBuyerDO.class);
        buyerSupplierBuyerMapper.insert(buyerSupplierBuyer);
        // 返回
        return buyerSupplierBuyer.getId();
    }

    @Override
    public void updatebuyerSupplierBuyer(buyerSupplierBuyerSaveReqVO updateReqVO) {
        // 校验存在
        validatebuyerSupplierBuyerExists(updateReqVO.getId());
        // 更新
        buyerSupplierBuyerDO updateObj = BeanUtils.toBean(updateReqVO, buyerSupplierBuyerDO.class);
        buyerSupplierBuyerMapper.updateById(updateObj);
    }

    @Override
    public void deletebuyerSupplierBuyer(Long id) {
        // 校验存在
        validatebuyerSupplierBuyerExists(id);
        // 删除
        buyerSupplierBuyerMapper.deleteById(id);
    }

    private void validatebuyerSupplierBuyerExists(Long id) {
        if (buyerSupplierBuyerMapper.selectById(id) == null) {
            throw exception(BUYER_SUPPLIER_BUYER_NOT_EXISTS);
        }
    }

    @Override
    public buyerSupplierBuyerDO getbuyerSupplierBuyer(Long id) {
        return buyerSupplierBuyerMapper.selectById(id);
    }

    @Override
    public PageResult<buyerSupplierBuyerDO> getbuyerSupplierBuyerPage(buyerSupplierBuyerPageReqVO pageReqVO) {
        return buyerSupplierBuyerMapper.selectPage(pageReqVO);
    }

}