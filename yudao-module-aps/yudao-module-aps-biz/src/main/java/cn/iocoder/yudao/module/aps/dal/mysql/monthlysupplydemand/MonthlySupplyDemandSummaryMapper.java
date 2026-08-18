package cn.iocoder.yudao.module.aps.dal.mysql.monthlysupplydemand;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.aps.controller.admin.monthlysupplydemand.vo.MonthlySupplyDemandSummaryPageReqVO;
import cn.iocoder.yudao.module.aps.dal.dataobject.monthlysupplydemand.MonthlySupplyDemandSummaryDO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.util.StringUtils;

/**
 * 月度供需总览表 Mapper
 *
 * @author 柳文
 */
@Mapper
public interface MonthlySupplyDemandSummaryMapper extends BaseMapperX<MonthlySupplyDemandSummaryDO> {

    /**
     * 分页查询
     *
     * @param reqVO 查询请求
     * @return 分页结果
     */
    default PageResult<MonthlySupplyDemandSummaryDO> selectPage(MonthlySupplyDemandSummaryPageReqVO reqVO) {
        LambdaQueryWrapperX<MonthlySupplyDemandSummaryDO> wrapper = new LambdaQueryWrapperX<MonthlySupplyDemandSummaryDO>()
                .likeIfPresent(MonthlySupplyDemandSummaryDO::getAssemblyMaterialNo, reqVO.getAssemblyMaterialNo())
                .likeIfPresent(MonthlySupplyDemandSummaryDO::getMaterialDesc, reqVO.getMaterialDesc())
                .eqIfPresent(MonthlySupplyDemandSummaryDO::getScheduledDate, reqVO.getScheduledDate());

        // 手动处理 BETWEEN 条件，空字符串不添加
        if (StringUtils.hasText(reqVO.getScheduledDateStart()) && StringUtils.hasText(reqVO.getScheduledDateEnd())) {
            wrapper.between(MonthlySupplyDemandSummaryDO::getScheduledDate,
                    reqVO.getScheduledDateStart(),
                    reqVO.getScheduledDateEnd());
        }

        // 处理创建时间范围
        if (reqVO.getCreateTimeStart() != null && reqVO.getCreateTimeEnd() != null) {
            wrapper.between(MonthlySupplyDemandSummaryDO::getCreateTime,
                    reqVO.getCreateTimeStart(),
                    reqVO.getCreateTimeEnd());
        }

        return selectPage(reqVO, wrapper
                .orderByAsc(MonthlySupplyDemandSummaryDO::getScheduledDate)
                .orderByAsc(MonthlySupplyDemandSummaryDO::getAssemblyMaterialNo)
        );
    }
}