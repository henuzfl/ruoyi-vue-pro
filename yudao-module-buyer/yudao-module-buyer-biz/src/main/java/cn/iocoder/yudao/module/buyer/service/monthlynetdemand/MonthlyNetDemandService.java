package cn.iocoder.yudao.module.buyer.service.monthlynetdemand;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.buyer.controller.admin.monthlynetdemand.vo.MonthlyNetDemandPageReqVO;
import cn.iocoder.yudao.module.buyer.controller.admin.monthlynetdemand.vo.MonthlyNetDemandRespVO;

public interface MonthlyNetDemandService {
    PageResult<MonthlyNetDemandRespVO> getPage(MonthlyNetDemandPageReqVO reqVO);
}
