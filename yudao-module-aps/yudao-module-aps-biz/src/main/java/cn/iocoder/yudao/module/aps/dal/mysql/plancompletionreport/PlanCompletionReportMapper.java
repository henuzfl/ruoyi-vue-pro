package cn.iocoder.yudao.module.aps.dal.mysql.plancompletionreport;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.aps.controller.admin.plancompletionreport.vo.PlanCompletionReportRespVO;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface PlanCompletionReportMapper {

    /**
     * 分页查询计划完成情况报表
     */
    @InterceptorIgnore(tenantLine = "true")
    List<PlanCompletionReportRespVO> selectPage(@Param("beginPlanDate") LocalDateTime beginPlanDate,
                                                      @Param("endPlanDate") LocalDateTime endPlanDate,
                                                      @Param("workshop") String workshop,
                                                      @Param("offset") Long offset,
                                                      @Param("pageSize") Long pageSize);

    /**
     * 查询总数
     */
    @InterceptorIgnore(tenantLine = "true")
    Long selectCount(@Param("beginPlanDate") LocalDateTime beginPlanDate,
                     @Param("endPlanDate") LocalDateTime endPlanDate,
                     @Param("workshop") String workshop);
}