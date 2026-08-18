package cn.iocoder.yudao.module.aps.dal.mysql.assemblyorderprogress;

import cn.iocoder.yudao.module.aps.controller.admin.assemblyorderprogress.vo.AssemblyOrderProgressPageReqVO;
import cn.iocoder.yudao.module.aps.controller.admin.assemblyorderprogress.vo.AssemblyOrderProgressRespVO;
import cn.iocoder.yudao.module.aps.controller.admin.assemblyorderprogress.vo.AssemblyOrderShortageRespVO;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AssemblyOrderProgressMapper {

    /**
     * 分页查询总成进度
     */
    @InterceptorIgnore(tenantLine = "true")
    List<AssemblyOrderProgressRespVO> selectPage(Page<AssemblyOrderProgressRespVO> page,
                                                 @Param("reqVO") AssemblyOrderProgressPageReqVO reqVO);

    /**
     * 根据总成物料编码和时间范围查询缺料零件
     */
    @InterceptorIgnore(tenantLine = "true")
    List<AssemblyOrderShortageRespVO> selectShortagesByDateRange(@Param("materialCode") String materialCode,
                                                                 @Param("startDate") String startDate,
                                                                 @Param("endDate") String endDate);

    /**
     * 导出全部数据
     */
    @InterceptorIgnore(tenantLine = "true")
    List<AssemblyOrderProgressRespVO> selectExportList(@Param("reqVO") AssemblyOrderProgressPageReqVO reqVO);
}