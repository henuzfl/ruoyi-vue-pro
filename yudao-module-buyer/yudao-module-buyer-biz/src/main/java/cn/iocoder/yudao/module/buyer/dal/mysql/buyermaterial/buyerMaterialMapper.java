package cn.iocoder.yudao.module.buyer.dal.mysql.buyermaterial;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.buyer.dal.dataobject.buyermaterial.buyerMaterialDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.buyer.controller.admin.buyermaterial.vo.*;

/**
 * 需求输入 Mapper
 *
 * @author 柳文
 */
@Mapper
public interface buyerMaterialMapper extends BaseMapperX<buyerMaterialDO> {

    default PageResult<buyerMaterialDO> selectPage(buyerMaterialPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<buyerMaterialDO>()
                .likeIfPresent(buyerMaterialDO::getReqMaterial, reqVO.getReqMaterial())
                .likeIfPresent(buyerMaterialDO::getCustomer, reqVO.getCustomer())
                .likeIfPresent(buyerMaterialDO::getVehicleModel, reqVO.getVehicleModel())
                .likeIfPresent(buyerMaterialDO::getAssemblyQty, reqVO.getAssemblyQty())
                .likeIfPresent(buyerMaterialDO::getCompMaterial, reqVO.getCompMaterial())
                .likeIfPresent(buyerMaterialDO::getCompDesc, reqVO.getCompDesc())
                .likeIfPresent(buyerMaterialDO::getSpecModel, reqVO.getSpecModel())
                .likeIfPresent(buyerMaterialDO::getUnitUsage, reqVO.getUnitUsage())
                .likeIfPresent(buyerMaterialDO::getCompDemandQty, reqVO.getCompDemandQty())
                .likeIfPresent(buyerMaterialDO::getPreparedQty1, reqVO.getPreparedQty1())
                .likeIfPresent(buyerMaterialDO::getPreparedQty2, reqVO.getPreparedQty2())
                .likeIfPresent(buyerMaterialDO::getPreparedQty3, reqVO.getPreparedQty3())
                .likeIfPresent(buyerMaterialDO::getPreparedQty4, reqVO.getPreparedQty4())
                .likeIfPresent(buyerMaterialDO::getPreparedQty5, reqVO.getPreparedQty5())
                .likeIfPresent(buyerMaterialDO::getSupplier, reqVO.getSupplier())
                .likeIfPresent(buyerMaterialDO::getBuyer, reqVO.getBuyer())
                .likeIfPresent(buyerMaterialDO::getProcGroup, reqVO.getProcGroup())
                .eqIfPresent(buyerMaterialDO::getDemandMonth, reqVO.getDemandMonth())
                .eqIfPresent(buyerMaterialDO::getStockStatus, reqVO.getStockStatus())
                .betweenIfPresent(buyerMaterialDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(buyerMaterialDO::getReqMaterial));
    }
}