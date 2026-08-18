package cn.iocoder.yudao.module.marketing.dal.mysql.scissorliftwplan;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.marketing.dal.dataobject.scissorliftdplan.ScissorLiftDplanDO;
import cn.iocoder.yudao.module.marketing.dal.dataobject.scissorliftwplan.ScissorLiftWplanDO;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.marketing.controller.admin.scissorliftwplan.vo.*;
import org.apache.ibatis.annotations.Param;

/**
 * 高机剪叉周计划 Mapper
 *
 * @author 柳文
 */
@Mapper
public interface ScissorLiftWplanMapper extends BaseMapperX<ScissorLiftWplanDO> {

    default PageResult<ScissorLiftWplanDO> selectPage(ScissorLiftWplanPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ScissorLiftWplanDO>()
                .eqIfPresent(ScissorLiftWplanDO::getProductLine, reqVO.getProductLine())
                .eqIfPresent(ScissorLiftWplanDO::getPreciseModel, reqVO.getPreciseModel())
                .eqIfPresent(ScissorLiftWplanDO::getProductModel, reqVO.getProductModel())
                .eqIfPresent(ScissorLiftWplanDO::getPreciseBom, reqVO.getPreciseBom())
                .betweenIfPresent(ScissorLiftWplanDO::getPlanDate, reqVO.getPlanDate())
                .eqIfPresent(ScissorLiftWplanDO::getWeekNo, reqVO.getWeekNo())
                .betweenIfPresent(ScissorLiftWplanDO::getWeekStartDate, reqVO.getWeekStartDate())
                .betweenIfPresent(ScissorLiftWplanDO::getWeekEndDate, reqVO.getWeekEndDate())
                .eqIfPresent(ScissorLiftWplanDO::getDailyQuantity, reqVO.getDailyQuantity())
                .eqIfPresent(ScissorLiftWplanDO::getCarNumberRange, reqVO.getCarNumberRange())
                .eqIfPresent(ScissorLiftWplanDO::getProductionLineType, reqVO.getProductionLineType())
                .eqIfPresent(ScissorLiftWplanDO::getPlate, "高机剪叉")
                .betweenIfPresent(ScissorLiftWplanDO::getImportTime, reqVO.getImportTime())
                .betweenIfPresent(ScissorLiftWplanDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(ScissorLiftWplanDO::getId));
    }

    @InterceptorIgnore(tenantLine = "true")  // 添加该注解
    void insertBatchSomeColumn(@Param("list") List<ScissorLiftWplanDO> list);

}