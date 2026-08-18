package cn.iocoder.yudao.module.aps.service.monthlysupplydemand;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.aps.controller.admin.monthlysupplydemand.vo.MonthlySupplyDemandSummaryPageReqVO;
import cn.iocoder.yudao.module.aps.controller.admin.monthlysupplydemand.vo.MonthlySupplyDemandSummaryRespVO;

import javax.validation.Valid;

/**
 * 月度供需总览表 Service 接口
 *
 * @author 柳文
 */
public interface MonthlySupplyDemandSummaryService {

    /**
     * 获得月度供需总览表分页
     *
     * @param pageReqVO 分页请求
     * @return 分页结果
     */
    PageResult<MonthlySupplyDemandSummaryRespVO> getPage(@Valid MonthlySupplyDemandSummaryPageReqVO pageReqVO);
}