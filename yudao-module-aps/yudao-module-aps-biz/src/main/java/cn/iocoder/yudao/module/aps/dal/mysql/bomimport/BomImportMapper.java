package cn.iocoder.yudao.module.aps.dal.mysql.bomimport;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.aps.dal.dataobject.bomimport.BomImportDO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.aps.controller.admin.bomimport.vo.*;
import org.apache.ibatis.annotations.Param;


/**
 * 物料BOM导入 Mapper
 *
 * @author 柳文
 */
@Mapper
public interface BomImportMapper extends BaseMapperX<BomImportDO> {

    default PageResult<BomImportDO> selectPage(BomImportPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<BomImportDO>()
                .eqIfPresent(BomImportDO::getParentLineNo, reqVO.getParentLineNo())
                .eqIfPresent(BomImportDO::getLineNo, reqVO.getLineNo())
                .eqIfPresent(BomImportDO::getLevelNo, reqVO.getLevelNo())
                .eqIfPresent(BomImportDO::getPlant, reqVO.getPlant())
                .eqIfPresent(BomImportDO::getMainMaterialNo, reqVO.getMainMaterialNo())
                .eqIfPresent(BomImportDO::getComponentMaterialNo, reqVO.getComponentMaterialNo())
                .eqIfPresent(BomImportDO::getComponentDesc, reqVO.getComponentDesc())
                .eqIfPresent(BomImportDO::getSpecModel, reqVO.getSpecModel())
                .eqIfPresent(BomImportDO::getGrossWeight, reqVO.getGrossWeight())
                .eqIfPresent(BomImportDO::getNetWeight, reqVO.getNetWeight())
                .eqIfPresent(BomImportDO::getComponentQty, reqVO.getComponentQty())
                .eqIfPresent(BomImportDO::getUnitUsage, reqVO.getUnitUsage())
                .eqIfPresent(BomImportDO::getMaterialType, reqVO.getMaterialType())
                .eqIfPresent(BomImportDO::getSpecialProcurementType, reqVO.getSpecialProcurementType())
                .eqIfPresent(BomImportDO::getStorageLocation, reqVO.getStorageLocation())
                .eqIfPresent(BomImportDO::getUnit, reqVO.getUnit())
                .eqIfPresent(BomImportDO::getProcurementType, reqVO.getProcurementType())
                .eqIfPresent(BomImportDO::getPurchasingGroup, reqVO.getPurchasingGroup())
                .betweenIfPresent(BomImportDO::getImportDate, reqVO.getImportDate())
                .betweenIfPresent(BomImportDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(BomImportDO::getId));
    }

    /**
     * 物理删除指定物料+工厂的BOM导入数据
     */
    @Delete("DELETE FROM material_bom_import WHERE MAIN_MATERIAL_NO = #{mainMaterialNo} AND PLANT = #{plant}")
    int physicalDeleteByMaterialAndPlant(@Param("mainMaterialNo") String mainMaterialNo,
                                         @Param("plant") String plant);
}