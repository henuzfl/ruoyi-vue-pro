package cn.iocoder.yudao.module.aps.dal.mysql.masterimport;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.aps.dal.dataobject.masterimport.MasterImportDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.aps.controller.admin.masterimport.vo.*;

/**
 * 物料主数据导入 Mapper
 *
 * @author 柳文
 */
@Mapper
public interface MasterImportMapper extends BaseMapperX<MasterImportDO> {

    default PageResult<MasterImportDO> selectPage(MasterImportPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<MasterImportDO>()
                .eqIfPresent(MasterImportDO::getMaterialNo, reqVO.getMaterialNo())
                .eqIfPresent(MasterImportDO::getMaterialDesc, reqVO.getMaterialDesc())
                .eqIfPresent(MasterImportDO::getMaterialType, reqVO.getMaterialType())
                .eqIfPresent(MasterImportDO::getGrossWeight, reqVO.getGrossWeight())
                .eqIfPresent(MasterImportDO::getNetWeight, reqVO.getNetWeight())
                .eqIfPresent(MasterImportDO::getBaseUom, reqVO.getBaseUom())
                .eqIfPresent(MasterImportDO::getValuationClass, reqVO.getValuationClass())
                .eqIfPresent(MasterImportDO::getPriceControl, reqVO.getPriceControl())
                .eqIfPresent(MasterImportDO::getNoCostEstimation, reqVO.getNoCostEstimation())
                .eqIfPresent(MasterImportDO::getQsCostEstimate, reqVO.getQsCostEstimate())
                .eqIfPresent(MasterImportDO::getSizeDimension, reqVO.getSizeDimension())
                .eqIfPresent(MasterImportDO::getProcurementType, reqVO.getProcurementType())
                .eqIfPresent(MasterImportDO::getProductionStorageLocation, reqVO.getProductionStorageLocation())
                .eqIfPresent(MasterImportDO::getProductionScheduler, reqVO.getProductionScheduler())
                .eqIfPresent(MasterImportDO::getDistributionFlag, reqVO.getDistributionFlag())
                .eqIfPresent(MasterImportDO::getMaterialCategory, reqVO.getMaterialCategory())
                .eqIfPresent(MasterImportDO::getExternalProcurementStorage, reqVO.getExternalProcurementStorage())
                .betweenIfPresent(MasterImportDO::getPlannedDeliveryTime, reqVO.getPlannedDeliveryTime())
                .betweenIfPresent(MasterImportDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(MasterImportDO::getId));
    }

}