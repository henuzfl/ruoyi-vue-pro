package cn.iocoder.yudao.module.aps.dal.mysql.materialprogresstrack;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.aps.controller.admin.materialprogresstrack.vo.MaterialChildrenRespVO;
import cn.iocoder.yudao.module.aps.controller.admin.materialprogresstrack.vo.MaterialSummaryRespVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MaterialProgressTrackMapper {

    Long selectMaterialSummaryCount(@Param("startDate") String startDate,
                                    @Param("endDate") String endDate,
                                    @Param("workshop") String workshop,
                                    @Param("materialCode") String materialCode,
                                    @Param("materialDesc") String materialDesc,
                                    @Param("onlyAbnormal") Boolean onlyAbnormal);

    List<MaterialSummaryRespVO> selectMaterialSummaryPage(@Param("startDate") String startDate,
                                                          @Param("endDate") String endDate,
                                                          @Param("workshop") String workshop,
                                                          @Param("materialCode") String materialCode,
                                                          @Param("materialDesc") String materialDesc,
                                                          @Param("onlyAbnormal") Boolean onlyAbnormal,
                                                          @Param("pageNo") Integer pageNo,
                                                          @Param("pageSize") Integer pageSize);

    List<MaterialChildrenRespVO> selectMaterialChildren(@Param("materialCode") String materialCode,
                                                        @Param("workshop") String workshop,
                                                        @Param("demandMonth") String demandMonth);
}