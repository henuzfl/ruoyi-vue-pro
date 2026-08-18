package cn.iocoder.yudao.module.aps.dal.mysql.mainplanprogressreport;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.aps.controller.admin.mainplanprogressreport.vo.*;
import cn.iocoder.yudao.module.aps.dal.dataobject.mainplan.MainPlanDO;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.aps.controller.admin.mainplan.vo.*;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;


/**
 * 主计划 Mapper
 *
 * @author 柳文
 */
@Mapper
public interface MainPlanProgressReportMapper extends BaseMapperX<MainPlanDO> {

    // 全量查询（用于导出）
    List<MainPlanProgressReportRespVO> selectProgressReport();

    // 分页查询
    @InterceptorIgnore(tenantLine = "true")
    List<MainPlanProgressReportRespVO> selectProgressReportPage(
            @Param("pageNo") Long pageNo,
            @Param("pageSize") Long pageSize,
            @Param("productionOrderNo") String productionOrderNo,
            @Param("assemblyMaterialNo") String assemblyMaterialNo,
            @Param("mainMaterialDesc") String mainMaterialDesc,
            @Param("scheduledDateStart") Date scheduledDateStart,
            @Param("scheduledDateEnd") Date scheduledDateEnd,
            @Param("scheduledQuantity") BigDecimal scheduledQuantity,
            @Param("productionWorkshop") String productionWorkshop,
            @Param("createTimeStart") Date createTimeStart,
            @Param("createTimeEnd") Date createTimeEnd
    );

    // 查询总数
    @InterceptorIgnore(tenantLine = "true")
    Long selectProgressReportCount(
            @Param("productionOrderNo") String productionOrderNo,
            @Param("assemblyMaterialNo") String assemblyMaterialNo,
            @Param("mainMaterialDesc") String mainMaterialDesc,
            @Param("scheduledDateStart") Date scheduledDateStart,
            @Param("scheduledDateEnd") Date scheduledDateEnd,
            @Param("scheduledQuantity") BigDecimal scheduledQuantity,
            @Param("productionWorkshop") String productionWorkshop,
            @Param("createTimeStart") Date createTimeStart,
            @Param("createTimeEnd") Date createTimeEnd
    );


    @InterceptorIgnore(tenantLine = "true")
    DashboardOverviewRespVO selectOverview(@Param("startDate") String startDate,
                                           @Param("endDate") String endDate,
                                           @Param("workshop") String workshop,
                                           @Param("supplier") String supplier);

    @InterceptorIgnore(tenantLine = "true")
    List<DashboardWorkshopRespVO> selectWorkshopStats(@Param("startDate") String startDate,
                                                      @Param("endDate") String endDate,
                                                      @Param("workshop") String workshop,
                                                      @Param("supplier") String supplier);

    @InterceptorIgnore(tenantLine = "true")
    List<DashboardSupplierRespVO> selectSupplierStats(@Param("startDate") String startDate,
                                                      @Param("endDate") String endDate,
                                                      @Param("workshop") String workshop,
                                                      @Param("supplier") String supplier);

    @InterceptorIgnore(tenantLine = "true")
    List<DashboardMaterialShortageRespVO> selectMaterialShortage(@Param("startDate") String startDate,
                                                                 @Param("endDate") String endDate,
                                                                 @Param("workshop") String workshop,
                                                                 @Param("supplier") String supplier);

    @InterceptorIgnore(tenantLine = "true")
    List<OrderShortageRespVO> selectOrderShortages(@Param("orderNo") String orderNo);

    @InterceptorIgnore(tenantLine = "true")
    List<ComponentPurchaseRespVO> selectComponentPurchases(@Param("componentCode") String componentCode,
                                                           @Param("orderNo") String orderNo);

    // 订单分页总数
    @InterceptorIgnore(tenantLine = "true")
    Long selectOrderPageCount(@Param("req") DashboardOrderPageReqVO reqVO);

    // 订单分页列表（已存在于XML，但需要修改参数名统一为 req）
    @InterceptorIgnore(tenantLine = "true")
    List<DashboardOrderRespVO> selectOrderPage(@Param("req") DashboardOrderPageReqVO reqVO);

    // 查询数据库中最新的 create_time 日期（精确到天）
    @Select("SELECT TRUNC(MAX(create_time)) FROM aps_matching_result WHERE deleted = 0")
    @InterceptorIgnore(tenantLine = "true")
    Date selectLatestCreateDate();

    // 根据指定的创建日期查询概览数据（与原有 selectOverview 逻辑类似，但 create_time 条件使用参数）
    @InterceptorIgnore(tenantLine = "true")
    DashboardOverviewRespVO selectOverviewByCreateDate(@Param("startDate") String startDate,
                                                       @Param("endDate") String endDate,
                                                       @Param("workshop") String workshop,
                                                       @Param("supplier") String supplier,
                                                       @Param("createDate") String createDate);
}