package cn.iocoder.yudao.module.buyer.dal.mysql.buyertimestock;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.buyer.dal.dataobject.buyertimestock.buyerTimeStockDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.buyer.controller.admin.buyertimestock.vo.*;

/**
 * 实时库存 Mapper
 *
 * @author 柳文
 */
@Mapper
public interface buyerTimeStockMapper extends BaseMapperX<buyerTimeStockDO> {

    default PageResult<buyerTimeStockDO> selectPage(buyerTimeStockPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<buyerTimeStockDO>()
                .eqIfPresent(buyerTimeStockDO::getMaterialNo, reqVO.getMaterialNo())
                .eqIfPresent(buyerTimeStockDO::getMaterialDesc, reqVO.getMaterialDesc())
                .eqIfPresent(buyerTimeStockDO::getStockLocation, reqVO.getStockLocation())
                .eqIfPresent(buyerTimeStockDO::getStockQuantity, reqVO.getStockQuantity())
                .eqIfPresent(buyerTimeStockDO::getAvailableQuantity, reqVO.getAvailableQuantity())
                .eqIfPresent(buyerTimeStockDO::getStatus, reqVO.getStatus())
                .eqIfPresent(buyerTimeStockDO::getRemark, reqVO.getRemark())
                .betweenIfPresent(buyerTimeStockDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(buyerTimeStockDO::getId));
    }

}