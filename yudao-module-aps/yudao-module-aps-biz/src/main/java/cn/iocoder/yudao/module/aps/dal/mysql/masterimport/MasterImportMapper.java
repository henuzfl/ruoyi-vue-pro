package cn.iocoder.yudao.module.aps.dal.mysql.masterimport;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.aps.dal.dataobject.masterimport.MasterImportDO;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.aps.controller.admin.masterimport.vo.*;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 物料主数据导入 Mapper
 *
 * @author 柳文
 */
@Mapper
@DS("oracle")   // 关键：指定 Oracle
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

    /**
     * 根据物料号列表物理删除（直接 DELETE SQL）
     * @param materialNos 物料号集合
     * @return 删除行数
     */
    @InterceptorIgnore(tenantLine = "true")
    @DS("oracle") // 指定使用 Oracle 数据源
    int deleteByMaterialNos(@Param("materialNos") Collection<String> materialNos);

    /**
     * 批量插入物料主数据
     * @param list 待插入的数据
     */
    @InterceptorIgnore(tenantLine = "true")
    @DS("oracle") // 指定使用 Oracle 数据源
    void batchInsert(@Param("list") List<MasterImportDO> list);

    /**
     * 批量获取指定数量的序列下一个值
     * @param count 需要的序列值个数
     * @return 序列值列表
     */
    @Select("SELECT material_master_import_seq.NEXTVAL FROM DUAL CONNECT BY LEVEL <= #{count}")
    List<Long> selectNextIds(@Param("count") int count);
}