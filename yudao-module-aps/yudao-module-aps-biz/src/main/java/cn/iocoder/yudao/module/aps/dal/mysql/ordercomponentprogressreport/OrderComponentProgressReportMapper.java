package cn.iocoder.yudao.module.aps.dal.mysql.ordercomponentprogressreport;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.aps.controller.admin.ordercomponentprogressreport.vo.OrderComponentProgressReportPageReqVO;
import cn.iocoder.yudao.module.aps.controller.admin.ordercomponentprogressreport.vo.OrderComponentProgressReportRespVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OrderComponentProgressReportMapper extends BaseMapperX<Object> {

    List<OrderComponentProgressReportRespVO> selectPage(
            @Param("pageNo") Long pageNo,
            @Param("pageSize") Long pageSize,
            @Param("reqVO") OrderComponentProgressReportPageReqVO reqVO);

    Long selectCount(@Param("reqVO") OrderComponentProgressReportPageReqVO reqVO);

    List<OrderComponentProgressReportRespVO> selectAllByCondition(@Param("reqVO") OrderComponentProgressReportPageReqVO reqVO);
}