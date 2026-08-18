package cn.iocoder.yudao.module.aps.service.mainplancomponentprogressreport;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.aps.controller.admin.mainplancomponentprogressreport.vo.MainPlanComponentProgressReportPageReqVO;
import cn.iocoder.yudao.module.aps.controller.admin.mainplancomponentprogressreport.vo.MainPlanComponentProgressReportRespVO;
import cn.iocoder.yudao.module.aps.dal.mysql.mainplancomponentprogressreport.MainPlanComponentProgressReportMapper;
import com.baomidou.dynamic.datasource.annotation.DS;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.List;

@Service
@DS("oracle")
@Validated
@Slf4j
public class MainPlanComponentProgressReportServiceImpl implements MainPlanComponentProgressReportService {

    @Resource
    private MainPlanComponentProgressReportMapper mapper;

    @Override
    public PageResult<MainPlanComponentProgressReportRespVO> getComponentProgressReportPage(MainPlanComponentProgressReportPageReqVO pageReqVO) {
        Long pageNo = pageReqVO.getPageNo().longValue();
        Long pageSize = pageReqVO.getPageSize().longValue();

        List<MainPlanComponentProgressReportRespVO> list = mapper.selectPage(
                pageNo, pageSize,
                pageReqVO.getAssemblyMaterialNo(),
                pageReqVO.getComponentMaterialNo(),
                pageReqVO.getMaterialDesc(),
                pageReqVO.getProductionWorkshop(),
                pageReqVO.getScheduledDateStart(),
                pageReqVO.getScheduledDateEnd()
        );

        Long total = mapper.selectCount(
                pageReqVO.getAssemblyMaterialNo(),
                pageReqVO.getComponentMaterialNo(),
                pageReqVO.getMaterialDesc(),
                pageReqVO.getProductionWorkshop(),
                pageReqVO.getScheduledDateStart(),
                pageReqVO.getScheduledDateEnd()
        );

        return new PageResult<>(list, total);
    }
}