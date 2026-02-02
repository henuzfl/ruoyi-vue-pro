package cn.iocoder.yudao.module.buyer.dal.mysql.buyerstock;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.buyer.dal.dataobject.buyerstock.buyerStockDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.buyer.controller.admin.buyerstock.vo.*;

/**
 * 供应商库存 Mapper
 *
 * @author 柳文
 */
@Mapper
public interface buyerStockMapper extends BaseMapperX<buyerStockDO> {

    default PageResult<buyerStockDO> selectPage(buyerStockPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<buyerStockDO>()
                .eqIfPresent(buyerStockDO::getMaterialNo, reqVO.getMaterialNo())
                .eqIfPresent(buyerStockDO::getMaterialDesc, reqVO.getMaterialDesc())
                .eqIfPresent(buyerStockDO::getSupplier, reqVO.getSupplier())
                .eqIfPresent(buyerStockDO::getStockLocation, reqVO.getStockLocation())
                .eqIfPresent(buyerStockDO::getStockQuantity, reqVO.getStockQuantity())
                .eqIfPresent(buyerStockDO::getPreparedQuantity, reqVO.getPreparedQuantity())
                .eqIfPresent(buyerStockDO::getStockMonth, reqVO.getStockMonth())
                .eqIfPresent(buyerStockDO::getStatus, reqVO.getStatus())
                .eqIfPresent(buyerStockDO::getRemark, reqVO.getRemark())
                .betweenIfPresent(buyerStockDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(buyerStockDO::getId));
    }

}