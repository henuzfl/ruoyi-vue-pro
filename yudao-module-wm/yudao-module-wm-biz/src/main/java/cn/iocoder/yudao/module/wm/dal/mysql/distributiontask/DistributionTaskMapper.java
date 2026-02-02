package cn.iocoder.yudao.module.wm.dal.mysql.distributiontask;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.wm.dal.dataobject.distributiontask.DistributionTaskDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.wm.controller.admin.distributiontask.vo.*;

/**
 * 配送任务下发 Mapper
 *
 * @author 柳文
 */
@Mapper
public interface DistributionTaskMapper extends BaseMapperX<DistributionTaskDO> {

    default PageResult<DistributionTaskDO> selectPage(DistributionTaskPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<DistributionTaskDO>()
                .eqIfPresent(DistributionTaskDO::getOrderNo, reqVO.getOrderNo())
                .eqIfPresent(DistributionTaskDO::getReservationNo, reqVO.getReservationNo())
                .eqIfPresent(DistributionTaskDO::getReservationLineNo, reqVO.getReservationLineNo())
                .eqIfPresent(DistributionTaskDO::getProcessNo, reqVO.getProcessNo())
                .eqIfPresent(DistributionTaskDO::getDistributionTaskNo, reqVO.getDistributionTaskNo())
                .eqIfPresent(DistributionTaskDO::getProductCode, reqVO.getProductCode())
                .likeIfPresent(DistributionTaskDO::getProductName, reqVO.getProductName())
                .eqIfPresent(DistributionTaskDO::getMaterialNo, reqVO.getMaterialNo())
                .likeIfPresent(DistributionTaskDO::getMaterialName, reqVO.getMaterialName())
                .eqIfPresent(DistributionTaskDO::getQuantity, reqVO.getQuantity())
                .eqIfPresent(DistributionTaskDO::getUnit, reqVO.getUnit())
                .eqIfPresent(DistributionTaskDO::getTaskStatus, reqVO.getTaskStatus())
                .eqIfPresent(DistributionTaskDO::getDistributionStatus, reqVO.getDistributionStatus())
                .betweenIfPresent(DistributionTaskDO::getDistributionTime, reqVO.getDistributionTime())
                .eqIfPresent(DistributionTaskDO::getDistributionOperator, reqVO.getDistributionOperator())
                .eqIfPresent(DistributionTaskDO::getDeliveryLocation, reqVO.getDeliveryLocation())
                .betweenIfPresent(DistributionTaskDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(DistributionTaskDO::getId));
    }

}