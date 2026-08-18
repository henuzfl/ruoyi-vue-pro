package cn.iocoder.yudao.module.marketing.service.aerialhostdemandcomparison;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.marketing.controller.admin.aerialhostdemandcomparison.vo.AerialHostDemandComparisonPageReqVO;
import cn.iocoder.yudao.module.marketing.controller.admin.aerialhostdemandcomparison.vo.AerialHostDemandComparisonRespVO;
import cn.iocoder.yudao.module.marketing.controller.admin.aerialhostdemandcomparison.vo.AerialHostDemandComparisonWeekRespVO;
import cn.iocoder.yudao.module.marketing.dal.mysql.aerialhostdemandcomparison.AerialHostDemandComparisonMapper;
import cn.iocoder.yudao.module.marketing.dal.mysql.aerialboomdplan.AerialBoomDplanMapper; // 假设存在此 Mapper
import com.baomidou.dynamic.datasource.annotation.DS;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@DS("oracle")
@Validated
public class AerialHostDemandComparisonServiceImpl implements AerialHostDemandComparisonService {

    @Resource
    private AerialHostDemandComparisonMapper mapper;

    @Resource
    private AerialBoomDplanMapper aerialBoomDplanMapper; // 用于获取导入批次日期

    @Override
    public PageResult<AerialHostDemandComparisonRespVO> getComparisonPage(AerialHostDemandComparisonPageReqVO pageReqVO) {
        Long pageNo = pageReqVO.getPageNo().longValue();
        Long pageSize = pageReqVO.getPageSize().longValue();

        String currentDate = pageReqVO.getCurrentDate();
        String compareDate = pageReqVO.getCompareDate();
        if (currentDate == null || compareDate == null) {
            List<String> dates = getAvailableImportDates();
            if (dates.size() >= 2) {
                currentDate = dates.get(0);
                compareDate = dates.get(1);
            } else if (dates.size() == 1) {
                currentDate = dates.get(0);
                compareDate = dates.get(0);
            } else {
                return new PageResult<>(Collections.emptyList(), 0L);
            }
        }

        List<AerialHostDemandComparisonRespVO> list = mapper.selectComparisonPage(
                pageNo, pageSize, currentDate, compareDate,
                pageReqVO.getMaterialCode(), pageReqVO.getProductModel(), pageReqVO.getPlate()
        );
        Long total = mapper.selectComparisonCount(currentDate, compareDate,
                pageReqVO.getMaterialCode(), pageReqVO.getProductModel(), pageReqVO.getPlate());
        return new PageResult<>(list, total);
    }

    @Override
    public List<String> getAvailableImportDates(String plate) {
        return aerialBoomDplanMapper.selectDistinctImportDates(plate);
    }

    @Override
    public List<AerialHostDemandComparisonRespVO> getAllForExport(AerialHostDemandComparisonPageReqVO pageReqVO) {
        String currentDate = pageReqVO.getCurrentDate();
        String compareDate = pageReqVO.getCompareDate();
        if (currentDate == null || compareDate == null) {
            List<String> dates = getAvailableImportDates();
            if (dates.size() >= 2) {
                currentDate = dates.get(0);
                compareDate = dates.get(1);
            } else if (dates.size() == 1) {
                currentDate = dates.get(0);
                compareDate = dates.get(0);
            } else {
                return Collections.emptyList();
            }
        }
        return mapper.selectAllForExport(currentDate, compareDate,
                pageReqVO.getMaterialCode(), pageReqVO.getProductModel(), pageReqVO.getPlate());
    }

    @Override
    public List<AerialHostDemandComparisonRespVO> getDayComparisonData(AerialHostDemandComparisonPageReqVO reqVO) {
        String currentDate = reqVO.getCurrentDate();
        String compareDate = reqVO.getCompareDate();
        if (currentDate == null || compareDate == null) {
            List<String> dates = getAvailableImportDates();
            if (dates.size() >= 2) {
                currentDate = dates.get(0);
                compareDate = dates.get(1);
            } else if (dates.size() == 1) {
                currentDate = dates.get(0);
                compareDate = dates.get(0);
            } else {
                return Collections.emptyList();
            }
        }
        return mapper.selectDayComparison(currentDate, compareDate,
                reqVO.getMaterialCode(), reqVO.getProductModel(), reqVO.getPlate());
    }

    @Override
    public List<AerialHostDemandComparisonWeekRespVO> getWeekComparisonData(AerialHostDemandComparisonPageReqVO reqVO) {
        String currentDate = reqVO.getCurrentDate();
        String compareDate = reqVO.getCompareDate();
        if (currentDate == null || compareDate == null) {
            List<String> dates = getAvailableImportDates();
            if (dates.size() >= 2) {
                currentDate = dates.get(0);
                compareDate = dates.get(1);
            } else if (dates.size() == 1) {
                currentDate = dates.get(0);
                compareDate = dates.get(0);
            } else {
                return Collections.emptyList();
            }
        }
        return mapper.selectWeekComparison(currentDate, compareDate,
                reqVO.getMaterialCode(), reqVO.getProductModel(), reqVO.getPlate());
    }

    @Override
    public List<String> getAvailableImportDates() {
        return getAvailableImportDates(null);
    }

    @Override
    public List<String> getAvailablePlates() {
        return aerialBoomDplanMapper.selectDistinctPlates();
    }

}