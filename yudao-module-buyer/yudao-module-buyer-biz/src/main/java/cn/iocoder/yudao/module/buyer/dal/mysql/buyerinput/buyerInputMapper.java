package cn.iocoder.yudao.module.buyer.dal.mysql.buyerinput;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.buyer.dal.dataobject.buyerinput.buyerInputDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.buyer.controller.admin.buyerinput.vo.*;

/**
 * 需求输入 Mapper
 *
 * @author 柳文
 */
@Mapper
public interface buyerInputMapper extends BaseMapperX<buyerInputDO> {

    default PageResult<buyerInputDO> selectPage(buyerInputPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<buyerInputDO>()
                .eqIfPresent(buyerInputDO::getCustomer, reqVO.getCustomer())
                .eqIfPresent(buyerInputDO::getVehicleModel, reqVO.getVehicleModel())
                .eqIfPresent(buyerInputDO::getAssemblyMaterial, reqVO.getAssemblyMaterial())
                .eqIfPresent(buyerInputDO::getAssemblyQuantity, reqVO.getAssemblyQuantity())
                .eqIfPresent(buyerInputDO::getDemandMonth, reqVO.getDemandMonth())
                .eqIfPresent(buyerInputDO::getStatus, reqVO.getStatus())
                .eqIfPresent(buyerInputDO::getRemark, reqVO.getRemark())
                .betweenIfPresent(buyerInputDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(buyerInputDO::getId));
    }

}