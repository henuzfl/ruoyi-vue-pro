package cn.iocoder.yudao.module.marketing.dal.mysql.aerialhostdemandcomparison;

import cn.iocoder.yudao.module.marketing.controller.admin.aerialhostdemandcomparison.vo.AerialHostDemandComparisonRespVO;
import cn.iocoder.yudao.module.marketing.controller.admin.aerialhostdemandcomparison.vo.AerialHostDemandComparisonWeekRespVO;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface AerialHostDemandComparisonMapper {

    @InterceptorIgnore(tenantLine = "true")
    List<AerialHostDemandComparisonRespVO> selectComparisonPage(@Param("pageNo") Long pageNo,
                                                                @Param("pageSize") Long pageSize,
                                                                @Param("currentDate") String currentDate,
                                                                @Param("compareDate") String compareDate,
                                                                @Param("materialCode") String materialCode,
                                                                @Param("productModel") String productModel,
                                                                @Param("plate") String plate);

    @InterceptorIgnore(tenantLine = "true")
    Long selectComparisonCount(@Param("currentDate") String currentDate,
                               @Param("compareDate") String compareDate,
                               @Param("materialCode") String materialCode,
                               @Param("productModel") String productModel,
                               @Param("plate") String plate);

    @InterceptorIgnore(tenantLine = "true")
    List<AerialHostDemandComparisonRespVO> selectAllForExport(@Param("currentDate") String currentDate,
                                                              @Param("compareDate") String compareDate,
                                                              @Param("materialCode") String materialCode,
                                                              @Param("productModel") String productModel,
                                                              @Param("plate") String plate);
    @InterceptorIgnore(tenantLine = "true")
    List<AerialHostDemandComparisonRespVO> selectDayComparison(
            @Param("currentDate") String currentDate,
            @Param("compareDate") String compareDate,
            @Param("materialCode") String materialCode,
            @Param("productModel") String productModel,
            @Param("plate") String plate
    );

    @InterceptorIgnore(tenantLine = "true")
    List<AerialHostDemandComparisonWeekRespVO> selectWeekComparison(
            @Param("currentDate") String currentDate,
            @Param("compareDate") String compareDate,
            @Param("materialCode") String materialCode,
            @Param("productModel") String productModel,
            @Param("plate") String plate
    );

    @InterceptorIgnore(tenantLine = "true")
    @Select("SELECT DISTINCT plate FROM marketing_aerial_boom_dplan WHERE plate IS NOT NULL ORDER BY plate")
    List<String> selectDistinctPlates();
}