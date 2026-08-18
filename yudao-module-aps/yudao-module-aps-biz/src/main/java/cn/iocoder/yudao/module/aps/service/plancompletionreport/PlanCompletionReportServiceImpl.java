package cn.iocoder.yudao.module.aps.service.plancompletionreport;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.aps.controller.admin.plancompletionreport.vo.PlanCompletionReportPageReqVO;
import cn.iocoder.yudao.module.aps.controller.admin.plancompletionreport.vo.PlanCompletionReportRespVO;
import cn.iocoder.yudao.module.aps.dal.mysql.plancompletionreport.PlanCompletionReportMapper;
import com.baomidou.dynamic.datasource.annotation.DS;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;

@Service
@DS("oracle")
@Validated
@Slf4j
public class PlanCompletionReportServiceImpl implements PlanCompletionReportService {

    @Resource
    private PlanCompletionReportMapper planCompletionReportMapper;

    @Override
    public PageResult<PlanCompletionReportRespVO> getReportPage(PlanCompletionReportPageReqVO pageReqVO) {
        // 计算分页参数
        Long offset = (long) (pageReqVO.getPageNo() - 1) * pageReqVO.getPageSize();
        Long pageSize = (long) pageReqVO.getPageSize();

        // 查询总数
        Long total = planCompletionReportMapper.selectCount(
                pageReqVO.getBeginPlanDate(),
                pageReqVO.getEndPlanDate(),
                pageReqVO.getWorkshop()
        );

        if (total == 0) {
            return PageResult.empty();
        }

        // 查询分页数据
        java.util.List<PlanCompletionReportRespVO> list = planCompletionReportMapper.selectPage(
                pageReqVO.getBeginPlanDate(),
                pageReqVO.getEndPlanDate(),
                pageReqVO.getWorkshop(),
                offset,
                pageSize
        );

        return new PageResult<>(list, total);
    }
}