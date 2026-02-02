package cn.iocoder.yudao.module.aps.dal.mysql.plan;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.aps.dal.dataobject.plan.PlanDO;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param; // 添加这个导入
import cn.iocoder.yudao.module.aps.controller.admin.plan.vo.*;

/**
 * 设备调度 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface PlanMapper extends BaseMapperX<PlanDO> {

    default PageResult<PlanDO> selectPage(PlanPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<PlanDO>()
                .eqIfPresent(PlanDO::getStationId, reqVO.getStationId())
                .eqIfPresent(PlanDO::getDeviceId, reqVO.getDeviceId())
                .eqIfPresent(PlanDO::getDeviceSeq, reqVO.getDeviceSeq())
                .eqIfPresent(PlanDO::getOperationText, reqVO.getOperationText())
                .eqIfPresent(PlanDO::getProductCode, reqVO.getProductCode())
                .eqIfPresent(PlanDO::getDanhao, reqVO.getDanhao())
                .eqIfPresent(PlanDO::getLineNo, reqVO.getLineNo())
                .eqIfPresent(PlanDO::getMaterialNo, reqVO.getMaterialNo())
                .betweenIfPresent(PlanDO::getEventTime, reqVO.getEventTime())
                .eqIfPresent(PlanDO::getOperationSeq, reqVO.getOperationSeq())
                .betweenIfPresent(PlanDO::getWorkDate, reqVO.getWorkDate())
                .betweenIfPresent(PlanDO::getStartTime, reqVO.getStartTime())
                .betweenIfPresent(PlanDO::getEndTime, reqVO.getEndTime())
                .eqIfPresent(PlanDO::getQuantity, reqVO.getQuantity())
                .eqIfPresent(PlanDO::getSizeDimension, reqVO.getSizeDimension())
                .eqIfPresent(PlanDO::getState, reqVO.getState())
                .eqIfPresent(PlanDO::getDurationMinutes, reqVO.getDurationMinutes())
                .betweenIfPresent(PlanDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(PlanDO::getId));
    }
    // 添加存储过程调用方法
    @InterceptorIgnore(tenantLine = "true")
    void callUpdateStockProcedure(@Param("schema") String schema);

}