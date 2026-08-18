package cn.iocoder.yudao.module.marketing.dal.mysql.scissorliftdplan;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.marketing.dal.dataobject.scissorliftdplan.ScissorLiftDplanDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.marketing.controller.admin.scissorliftdplan.vo.*;

/**
 * 高机剪叉日计划 Mapper
 *
 * @author 柳文
 */
@Mapper
public interface ScissorLiftDplanMapper extends BaseMapperX<ScissorLiftDplanDO> {

    default PageResult<ScissorLiftDplanDO> selectPage(ScissorLiftDplanPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ScissorLiftDplanDO>()
                .eqIfPresent(ScissorLiftDplanDO::getLineType, reqVO.getLineType())
                .eqIfPresent(ScissorLiftDplanDO::getPreciseModel, reqVO.getPreciseModel())
                .eqIfPresent(ScissorLiftDplanDO::getProductModel, reqVO.getProductModel())
                .eqIfPresent(ScissorLiftDplanDO::getZpsModel, reqVO.getZpsModel())
                .eqIfPresent(ScissorLiftDplanDO::getPreciseBom, reqVO.getPreciseBom())
                .eqIfPresent(ScissorLiftDplanDO::getCarNo, reqVO.getCarNo())
                .eqIfPresent(ScissorLiftDplanDO::getOrderNo, reqVO.getOrderNo())
                .eqIfPresent(ScissorLiftDplanDO::getRemark, reqVO.getRemark())
                .eqIfPresent(ScissorLiftDplanDO::getTradeVersion, reqVO.getTradeVersion())
                .eqIfPresent(ScissorLiftDplanDO::getUnitCount, reqVO.getUnitCount())
                .eqIfPresent(ScissorLiftDplanDO::getOnlinePlan, reqVO.getOnlinePlan())
                .eqIfPresent(ScissorLiftDplanDO::getCompletePlan, reqVO.getCompletePlan())
                .betweenIfPresent(ScissorLiftDplanDO::getReportDate, reqVO.getReportDate())
                .eqIfPresent(ScissorLiftDplanDO::getCountry, reqVO.getCountry())
                .eqIfPresent(ScissorLiftDplanDO::getContractNo, reqVO.getContractNo())
                .betweenIfPresent(ScissorLiftDplanDO::getMarketingNoticeTime, reqVO.getMarketingNoticeTime())
                .betweenIfPresent(ScissorLiftDplanDO::getOrderCreateTime, reqVO.getOrderCreateTime())
                .eqIfPresent(ScissorLiftDplanDO::getPlate, "高机剪叉")
                .betweenIfPresent(ScissorLiftDplanDO::getImportTime, reqVO.getImportTime())
                .betweenIfPresent(ScissorLiftDplanDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(ScissorLiftDplanDO::getId));
    }

}