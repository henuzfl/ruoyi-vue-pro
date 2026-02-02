package cn.iocoder.yudao.module.buyer.dal.mysql.materialforecast;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.buyer.dal.dataobject.materialforecast.MaterialForecastDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.buyer.controller.admin.materialforecast.vo.*;

/**
 * 营销材料备料预测 Mapper
 *
 * @author 柳文
 */
@Mapper
public interface MaterialForecastMapper extends BaseMapperX<MaterialForecastDO> {

    default PageResult<MaterialForecastDO> selectPage(MaterialForecastPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<MaterialForecastDO>()
                .likeIfPresent(MaterialForecastDO::getCustomerName, reqVO.getCustomerName())
                .eqIfPresent(MaterialForecastDO::getProductLine, reqVO.getProductLine())
                .eqIfPresent(MaterialForecastDO::getTonnageSegment, reqVO.getTonnageSegment())
                .eqIfPresent(MaterialForecastDO::getVehicleModel, reqVO.getVehicleModel())
                .eqIfPresent(MaterialForecastDO::getForecastMonth, reqVO.getForecastMonth())
                .eqIfPresent(MaterialForecastDO::getForecastQuantity, reqVO.getForecastQuantity())
                .betweenIfPresent(MaterialForecastDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(MaterialForecastDO::getId));
    }

}