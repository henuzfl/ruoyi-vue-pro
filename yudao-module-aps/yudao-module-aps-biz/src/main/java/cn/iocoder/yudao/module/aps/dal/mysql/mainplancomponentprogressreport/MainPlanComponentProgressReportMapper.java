package cn.iocoder.yudao.module.aps.dal.mysql.mainplancomponentprogressreport;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.aps.controller.admin.mainplancomponentprogressreport.vo.MainPlanComponentProgressReportRespVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface MainPlanComponentProgressReportMapper {

    /**
     * 查询组件物料需求报表总数
     */
    Long selectCount(@Param("assemblyMaterialNo") String assemblyMaterialNo,
                     @Param("componentMaterialNo") String componentMaterialNo,
                     @Param("materialDesc") String materialDesc,
                     @Param("productionWorkshop") String productionWorkshop,
                     @Param("scheduledDateStart") LocalDateTime scheduledDateStart,
                     @Param("scheduledDateEnd") LocalDateTime scheduledDateEnd);

    /**
     * 分页查询组件物料需求报表
     */
    List<MainPlanComponentProgressReportRespVO> selectPage(@Param("pageNo") Long pageNo,
                                                           @Param("pageSize") Long pageSize,
                                                           @Param("assemblyMaterialNo") String assemblyMaterialNo,
                                                           @Param("componentMaterialNo") String componentMaterialNo,
                                                           @Param("materialDesc") String materialDesc,
                                                           @Param("productionWorkshop") String productionWorkshop,
                                                           @Param("scheduledDateStart") LocalDateTime scheduledDateStart,
                                                           @Param("scheduledDateEnd") LocalDateTime scheduledDateEnd);
}