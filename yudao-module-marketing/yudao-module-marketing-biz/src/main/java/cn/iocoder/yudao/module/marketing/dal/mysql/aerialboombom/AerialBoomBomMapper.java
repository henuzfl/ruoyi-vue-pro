package cn.iocoder.yudao.module.marketing.dal.mysql.aerialboombom;

import java.time.LocalDateTime;
import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.marketing.dal.dataobject.aerialboombom.AerialBoomBomDO;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.marketing.controller.admin.aerialboombom.vo.*;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import org.apache.ibatis.annotations.Param;

/**
 * 高机臂式/剪叉BOM物料清单 Mapper
 *
 * @author 柳文
 */
@Mapper
public interface AerialBoomBomMapper extends BaseMapperX<AerialBoomBomDO> {

    default PageResult<AerialBoomBomDO> selectPage(AerialBoomBomPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<AerialBoomBomDO>()
                .eqIfPresent(AerialBoomBomDO::getMaterialCode, reqVO.getMaterialCode())
                .eqIfPresent(AerialBoomBomDO::getMaterialDesc, reqVO.getMaterialDesc())
                .eqIfPresent(AerialBoomBomDO::getSupplier, reqVO.getSupplier())
                .eqIfPresent(AerialBoomBomDO::getJitFlag, reqVO.getJitFlag())
                .eqIfPresent(AerialBoomBomDO::getColorManagement, reqVO.getColorManagement())
                .eqIfPresent(AerialBoomBomDO::getSupplyOnDemand, reqVO.getSupplyOnDemand())
                .eqIfPresent(AerialBoomBomDO::getApplicableModel, reqVO.getApplicableModel())
                .eqIfPresent(AerialBoomBomDO::getRemark, reqVO.getRemark())
                .eqIfPresent(AerialBoomBomDO::getProductModel, reqVO.getProductModel())
                .eqIfPresent(AerialBoomBomDO::getPreciseBom, reqVO.getPreciseBom())
                .eqIfPresent(AerialBoomBomDO::getQuantity, reqVO.getQuantity())
                .eqIfPresent(AerialBoomBomDO::getSourceCategory, reqVO.getSourceCategory())
                .eqIfPresent(AerialBoomBomDO::getPlate, reqVO.getPlate())
                .betweenIfPresent(AerialBoomBomDO::getImportTime, reqVO.getImportTime())
                .betweenIfPresent(AerialBoomBomDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(AerialBoomBomDO::getId));
    }

    /**
     * 物理删除指定导入批次的所有数据（忽略逻辑删除）
     * @param importTime 导入时间
     * @return 删除条数
     */
    @Delete("DELETE FROM marketing_aerial_boom_bom WHERE import_time = #{importTime}")
    @InterceptorIgnore(tenantLine = "true")
    int physicalDeleteByImportTime(@Param("importTime") LocalDateTime importTime);

}