package cn.iocoder.yudao.module.aps.dal.mysql.dataimport;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.aps.dal.dataobject.dataimport.DataImportDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.aps.controller.admin.dataimport.vo.*;

/**
 * 营销数据导入 Mapper
 *
 * @author 柳文
 */
@Mapper
public interface DataImportMapper extends BaseMapperX<DataImportDO> {

    default PageResult<DataImportDO> selectPage(DataImportPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<DataImportDO>()
                .eqIfPresent(DataImportDO::getDanhao, reqVO.getDanhao())
                .eqIfPresent(DataImportDO::getProductCode, reqVO.getProductCode())
                .eqIfPresent(DataImportDO::getQuantity, reqVO.getQuantity())
                .betweenIfPresent(DataImportDO::getOrderDate, reqVO.getOrderDate())
                .betweenIfPresent(DataImportDO::getEventTime, reqVO.getEventTime())
                .eqIfPresent(DataImportDO::getStatus, reqVO.getStatus())
                .eqIfPresent(DataImportDO::getRemark, reqVO.getRemark())
                .betweenIfPresent(DataImportDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(DataImportDO::getId));
    }

}