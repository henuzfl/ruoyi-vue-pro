package cn.iocoder.yudao.module.marketing.dal.mysql.asmrequirement;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.marketing.dal.dataobject.asmrequirement.AsmRequirementDO;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.marketing.controller.admin.asmrequirement.vo.*;
import org.apache.ibatis.annotations.Param;

/**
 * 营销总成需求 Mapper
 *
 * @author 柳文
 */
@Mapper
public interface AsmRequirementMapper extends BaseMapperX<AsmRequirementDO> {

    default PageResult<AsmRequirementDO> selectPage(AsmRequirementPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<AsmRequirementDO>()
                .eqIfPresent(AsmRequirementDO::getHostUnit, reqVO.getHostUnit())
                .eqIfPresent(AsmRequirementDO::getVehicleModel, reqVO.getVehicleModel())
                .eqIfPresent(AsmRequirementDO::getAssemblyMaterialNo, reqVO.getAssemblyMaterialNo())
                .eqIfPresent(AsmRequirementDO::getMainMaterialDesc, reqVO.getMainMaterialDesc())
                .eqIfPresent(AsmRequirementDO::getRequireQuantity, reqVO.getRequireQuantity())
                .betweenIfPresent(AsmRequirementDO::getRequireDate, reqVO.getRequireDate())
                .betweenIfPresent(AsmRequirementDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(AsmRequirementDO::getId));
    }

    /**
     * 批量插入（Oracle 使用 INSERT ALL）
     */
    @InterceptorIgnore(tenantLine = "true")  // 若有租户隔离需忽略
    void insertBatch(@Param("list") List<AsmRequirementDO> list);

}