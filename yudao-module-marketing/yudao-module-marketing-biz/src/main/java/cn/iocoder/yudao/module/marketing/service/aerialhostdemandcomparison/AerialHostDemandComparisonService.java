package cn.iocoder.yudao.module.marketing.service.aerialhostdemandcomparison;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.marketing.controller.admin.aerialhostdemandcomparison.vo.AerialHostDemandComparisonPageReqVO;
import cn.iocoder.yudao.module.marketing.controller.admin.aerialhostdemandcomparison.vo.AerialHostDemandComparisonRespVO;
import cn.iocoder.yudao.module.marketing.controller.admin.aerialhostdemandcomparison.vo.AerialHostDemandComparisonWeekRespVO;

import java.util.List;

public interface AerialHostDemandComparisonService {

    PageResult<AerialHostDemandComparisonRespVO> getComparisonPage(AerialHostDemandComparisonPageReqVO pageReqVO);

    List<String> getAvailableImportDates();          // 无参（兼容原有）

    List<String> getAvailableImportDates(String plate); // 带板块过滤

    List<AerialHostDemandComparisonRespVO> getAllForExport(AerialHostDemandComparisonPageReqVO pageReqVO);

    List<AerialHostDemandComparisonRespVO> getDayComparisonData(AerialHostDemandComparisonPageReqVO reqVO);

    List<AerialHostDemandComparisonWeekRespVO> getWeekComparisonData(AerialHostDemandComparisonPageReqVO reqVO);

    List<String> getAvailablePlates();

}