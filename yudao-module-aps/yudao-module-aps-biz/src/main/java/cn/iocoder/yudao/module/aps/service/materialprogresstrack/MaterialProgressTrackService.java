package cn.iocoder.yudao.module.aps.service.materialprogresstrack;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.aps.controller.admin.materialprogresstrack.vo.MaterialChildrenRespVO;
import cn.iocoder.yudao.module.aps.controller.admin.materialprogresstrack.vo.MaterialSummaryRespVO;

import java.util.List;

public interface MaterialProgressTrackService {
    PageResult<MaterialSummaryRespVO> getMaterialSummary(String startDate, String endDate,
                                                         String workshop, String materialCode,
                                                         String materialDesc, Boolean onlyAbnormal,
                                                         Integer pageNo, Integer pageSize);

    List<MaterialChildrenRespVO> getMaterialChildren(String materialCode, String workshop, String demandMonth);
}