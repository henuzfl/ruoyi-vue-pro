package cn.iocoder.yudao.module.buyer.dal.mysql.supplierbuyer;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.buyer.dal.dataobject.supplierbuyer.buyerSupplierBuyerDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.buyer.controller.admin.supplierbuyer.vo.*;

/**
 * 物料供应商采购员对应 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface buyerSupplierBuyerMapper extends BaseMapperX<buyerSupplierBuyerDO> {

    default PageResult<buyerSupplierBuyerDO> selectPage(buyerSupplierBuyerPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<buyerSupplierBuyerDO>()
                .eqIfPresent(buyerSupplierBuyerDO::getMaterialNo, reqVO.getMaterialNo())
                .eqIfPresent(buyerSupplierBuyerDO::getMaterialDesc, reqVO.getMaterialDesc())
                .eqIfPresent(buyerSupplierBuyerDO::getSupplier, reqVO.getSupplier())
                .eqIfPresent(buyerSupplierBuyerDO::getBuyer, reqVO.getBuyer())
                .eqIfPresent(buyerSupplierBuyerDO::getProcurementGroup, reqVO.getProcurementGroup())
                .eqIfPresent(buyerSupplierBuyerDO::getMaterialType, reqVO.getMaterialType())
                .eqIfPresent(buyerSupplierBuyerDO::getMaterialCategory, reqVO.getMaterialCategory())
                .eqIfPresent(buyerSupplierBuyerDO::getProcurementType, reqVO.getProcurementType())
                .eqIfPresent(buyerSupplierBuyerDO::getStatus, reqVO.getStatus())
                .eqIfPresent(buyerSupplierBuyerDO::getRemark, reqVO.getRemark())
                .betweenIfPresent(buyerSupplierBuyerDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(buyerSupplierBuyerDO::getId));
    }

}