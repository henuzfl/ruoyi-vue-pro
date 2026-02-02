package cn.iocoder.yudao.module.buyer.dal.mysql.hostconfig;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.buyer.dal.dataobject.hostconfig.HostConfigDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.buyer.controller.admin.hostconfig.vo.*;

/**
 * 主机配置 Mapper
 *
 * @author 柳文
 */
@Mapper
public interface HostConfigMapper extends BaseMapperX<HostConfigDO> {

    default PageResult<HostConfigDO> selectPage(HostConfigPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<HostConfigDO>()
                .eqIfPresent(HostConfigDO::getVehicleModel, reqVO.getVehicleModel())
                .eqIfPresent(HostConfigDO::getSpecialDrawingNo, reqVO.getSpecialDrawingNo())
                .eqIfPresent(HostConfigDO::getConfigQuantity, reqVO.getConfigQuantity())
                .eqIfPresent(HostConfigDO::getBusinessUnit, reqVO.getBusinessUnit())
                .betweenIfPresent(HostConfigDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(HostConfigDO::getId));
    }

}