package cn.iocoder.yudao.module.marketing.dal.mysql.concreteplan;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.marketing.dal.dataobject.concreteplan.ConcretePlanDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.marketing.controller.admin.concreteplan.vo.*;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 混凝土计划需求 Mapper
 *
 * @author 管理员
 */
@Mapper
public interface ConcretePlanMapper extends BaseMapperX<ConcretePlanDO> {

    default PageResult<ConcretePlanDO> selectPage(ConcretePlanPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ConcretePlanDO>()
                .eqIfPresent(ConcretePlanDO::getPlanNo, reqVO.getPlanNo())
                .eqIfPresent(ConcretePlanDO::getSeqNo, reqVO.getSeqNo())
                .eqIfPresent(ConcretePlanDO::getMeter, reqVO.getMeter())
                .likeIfPresent(ConcretePlanDO::getModelName, reqVO.getModelName())
                .likeIfPresent(ConcretePlanDO::getMaterialName, reqVO.getMaterialName())
                .eqIfPresent(ConcretePlanDO::getMaterialCode, reqVO.getMaterialCode())
                .eqIfPresent(ConcretePlanDO::getProdNo, reqVO.getProdNo())
                .eqIfPresent(ConcretePlanDO::getQuantity, reqVO.getQuantity())
                .eqIfPresent(ConcretePlanDO::getStructSerialNo, reqVO.getStructSerialNo())
                .eqIfPresent(ConcretePlanDO::getOrderNo, reqVO.getOrderNo())
                .eqIfPresent(ConcretePlanDO::getBatchNo, reqVO.getBatchNo())
                .eqIfPresent(ConcretePlanDO::getGroupStatus, reqVO.getGroupStatus())
                .eqIfPresent(ConcretePlanDO::getLegType, reqVO.getLegType())
                .eqIfPresent(ConcretePlanDO::getCountryType, reqVO.getCountryType())
                .betweenIfPresent(ConcretePlanDO::getPlanIssueTime, reqVO.getPlanIssueTime())
                .betweenIfPresent(ConcretePlanDO::getAssemblyStartTime, reqVO.getAssemblyStartTime())
                .betweenIfPresent(ConcretePlanDO::getAssemblyEndTime, reqVO.getAssemblyEndTime())
                .betweenIfPresent(ConcretePlanDO::getDebugTime, reqVO.getDebugTime())
                .betweenIfPresent(ConcretePlanDO::getPaintingTime, reqVO.getPaintingTime())
                .betweenIfPresent(ConcretePlanDO::getWarehouseTime, reqVO.getWarehouseTime())
                .eqIfPresent(ConcretePlanDO::getOutputMonth, reqVO.getOutputMonth())
                .eqIfPresent(ConcretePlanDO::getSpecialReq, reqVO.getSpecialReq())
                .eqIfPresent(ConcretePlanDO::getPaintingReq, reqVO.getPaintingReq())
                .eqIfPresent(ConcretePlanDO::getExceptionNote, reqVO.getExceptionNote())
                .betweenIfPresent(ConcretePlanDO::getScheduleTime, reqVO.getScheduleTime())
                .eqIfPresent(ConcretePlanDO::getDeliveryReq, reqVO.getDeliveryReq())
                .eqIfPresent(ConcretePlanDO::getFactory, reqVO.getFactory())
                .eqIfPresent(ConcretePlanDO::getStatus, reqVO.getStatus())
                .eqIfPresent(ConcretePlanDO::getCustomer, reqVO.getCustomer())
                .eqIfPresent(ConcretePlanDO::getModifiedCarNo, reqVO.getModifiedCarNo())
                .eqIfPresent(ConcretePlanDO::getPlate, reqVO.getPlate())
                .betweenIfPresent(ConcretePlanDO::getImportTime, reqVO.getImportTime())
                .betweenIfPresent(ConcretePlanDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(ConcretePlanDO::getId));
    }

    int insertBatchSomeColumn(List<ConcretePlanDO> list);

    /**
     * 根据导入时间删除所有数据（用于导入前清空）
     * @param importTime 导入时间
     * @return 删除条数
     */
    int deleteByImportTime(@Param("importTime") LocalDateTime importTime);

}