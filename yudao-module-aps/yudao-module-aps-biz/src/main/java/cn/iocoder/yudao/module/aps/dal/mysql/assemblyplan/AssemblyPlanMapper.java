package cn.iocoder.yudao.module.aps.dal.mysql.assemblyplan;

import java.time.LocalDateTime;
import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.aps.dal.dataobject.assemblyplan.AssemblyPlanDO;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.aps.controller.admin.assemblyplan.vo.*;
import org.apache.ibatis.annotations.Param;

/**
 * 各车间开装计划 Mapper
 *
 * @author 柳文
 */
@Mapper
public interface AssemblyPlanMapper extends BaseMapperX<AssemblyPlanDO> {

    default PageResult<AssemblyPlanDO> selectPage(AssemblyPlanPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<AssemblyPlanDO>()
                .eqIfPresent(AssemblyPlanDO::getOrderNo, reqVO.getOrderNo())
                .eqIfPresent(AssemblyPlanDO::getMaterialCode, reqVO.getMaterialCode())
                .eqIfPresent(AssemblyPlanDO::getMaterialDesc, reqVO.getMaterialDesc())
                .eqIfPresent(AssemblyPlanDO::getAssemblyQuantity, reqVO.getAssemblyQuantity())
                .eqIfPresent(AssemblyPlanDO::getAssembledQuantity, reqVO.getAssembledQuantity())
                .betweenIfPresent(AssemblyPlanDO::getScheduleTime, reqVO.getScheduleTime())
                .eqIfPresent(AssemblyPlanDO::getWorkshop, reqVO.getWorkshop())
                .betweenIfPresent(AssemblyPlanDO::getImportTime, reqVO.getImportTime())
                .betweenIfPresent(AssemblyPlanDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(AssemblyPlanDO::getId));
    }

    /**
     * 批量插入
     * @param list 数据列表
     * @return 影响行数
     */
    @InterceptorIgnore(tenantLine = "true")
    int insertBatch(@Param("list") List<AssemblyPlanDO> list);

    /**
     * 根据唯一键（订单号+排产时间+车间）删除记录
     */
    @InterceptorIgnore(tenantLine = "true")
    int deleteByUniqueKey(@Param("orderNo") String orderNo,
                          @Param("scheduleTime") LocalDateTime scheduleTime,
                          @Param("workshop") String workshop);
}