package cn.iocoder.yudao.module.aps.service.monthlysupplydemand;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.aps.controller.admin.monthlysupplydemand.vo.MonthlySupplyDemandSummaryPageReqVO;
import cn.iocoder.yudao.module.aps.controller.admin.monthlysupplydemand.vo.MonthlySupplyDemandSummaryRespVO;
import cn.iocoder.yudao.module.aps.dal.dataobject.monthlysupplydemand.MonthlySupplyDemandSummaryDO;
import cn.iocoder.yudao.module.aps.dal.mysql.monthlysupplydemand.MonthlySupplyDemandSummaryMapper;
import com.baomidou.dynamic.datasource.annotation.DS;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;

/**
 * 月度供需总览表 Service 实现类
 *
 * @author 柳文
 */
@Service
@DS("oracle")
@Validated
@Slf4j
public class MonthlySupplyDemandSummaryServiceImpl implements MonthlySupplyDemandSummaryService {

    @Resource
    private MonthlySupplyDemandSummaryMapper mapper;

    @Override
    public PageResult<MonthlySupplyDemandSummaryRespVO> getPage(MonthlySupplyDemandSummaryPageReqVO pageReqVO) {
        PageResult<MonthlySupplyDemandSummaryDO> pageResult = mapper.selectPage(pageReqVO);
        return BeanUtils.toBean(pageResult, MonthlySupplyDemandSummaryRespVO.class);
    }
}