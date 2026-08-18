package cn.iocoder.yudao.module.buyer.service.hostrequirementcomparison;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.buyer.controller.admin.hostrequirementcomparison.vo.HostRequirementComparisonPageReqVO;
import cn.iocoder.yudao.module.buyer.controller.admin.hostrequirementcomparison.vo.HostRequirementComparisonRespVO;
import cn.iocoder.yudao.module.buyer.controller.admin.hostrequirementcomparisondiff.vo.HostRequirementComparisonDiffReqVO;
import cn.iocoder.yudao.module.buyer.controller.admin.hostrequirementcomparisondiff.vo.HostRequirementComparisonDiffRespVO;
import cn.iocoder.yudao.module.buyer.controller.admin.hostrequirementcomparisondiff.vo.HostRequirementComparisonDiffVO;
import cn.iocoder.yudao.module.buyer.dal.mysql.hostrequirementcomparison.HostRequirementComparisonMapper;
import cn.iocoder.yudao.module.buyer.dal.mysql.vehicleconfig.VehicleConfigMapper;
import cn.iocoder.yudao.module.buyer.dal.mysql.vehicleplan.VehiclePlanMapper;
import com.baomidou.dynamic.datasource.annotation.DS;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@DS("oracle") // 指定使用 Oracle 数据源
@Validated
public class HostRequirementComparisonServiceImpl implements HostRequirementComparisonService {

    @Resource
    private HostRequirementComparisonMapper mapper;

    @Resource
    private VehiclePlanMapper vehiclePlanMapper;

    @Resource
    private VehicleConfigMapper vehicleConfigMapper;


//    @Override
//    public PageResult<HostRequirementComparisonRespVO> getComparisonPage(HostRequirementComparisonPageReqVO pageReqVO) {
//        // 创建分页对象
//        Page<HostRequirementComparisonRespVO> page = new Page<>(pageReqVO.getPageNo(), pageReqVO.getPageSize());
//
//        // 执行查询
//        IPage<HostRequirementComparisonRespVO> pageResult = mapper.selectComparisonPage(page, pageReqVO.getImportDate());
//
//        // 转换为 PageResult
//        return new PageResult<>(pageResult.getRecords(), pageResult.getTotal());
//    }
@Override
public PageResult<HostRequirementComparisonRespVO> getComparisonPage(HostRequirementComparisonPageReqVO pageReqVO) {
    Long pageNo = pageReqVO.getPageNo().longValue();
    Long pageSize = pageReqVO.getPageSize().longValue();

    // 获取当前版本和对比版本日期（如果没有传，则取默认值，例如最新和次新）
    String currentDate = pageReqVO.getCurrentDate();
    String compareDate = pageReqVO.getCompareDate();

    // 如果没有选择日期，可以自动获取可用日期中的最新和次新
    if (currentDate == null || compareDate == null) {
        List<String> dates = getAvailableImportDates(); // 复用已有方法
        if (dates.size() >= 2) {
            currentDate = dates.get(0);
            compareDate = dates.get(1);
        } else if (dates.size() == 1) {
            currentDate = dates.get(0);
            compareDate = dates.get(0);
        } else {
            // 无数据，返回空
            return new PageResult<>(Collections.emptyList(), 0L);
        }
    }

    List<HostRequirementComparisonRespVO> list = mapper.selectComparisonPage(
            pageNo, pageSize,
            currentDate, compareDate,
            pageReqVO.getProductModel(),
            pageReqVO.getSeqNo2026(),
            pageReqVO.getBareMachineOrderNo(),
            pageReqVO.getMaterialNo(),
            pageReqVO.getFallbackMatched()   // 新增
    );

    Long total = mapper.selectComparisonCount(
            currentDate, compareDate,
            pageReqVO.getProductModel(),
            pageReqVO.getSeqNo2026(),
            pageReqVO.getBareMachineOrderNo(),
            pageReqVO.getMaterialNo(),
            pageReqVO.getFallbackMatched()   // 新增
    );

    return new PageResult<>(list, total);
}


    /**
     * 对比两个月份的数据差异
     */
//    public PageResult<HostRequirementComparisonDiffVO> getComparisonDiff(HostRequirementComparisonDiffReqVO reqVO) {
//        // 1. 获取显示月份数据
//        List<HostRequirementComparisonRespVO> showList = mapper.selectByMonth(
//                reqVO.getShowMonth(),
//                reqVO.getProductModel(),
//                reqVO.getSeqNo2026(),
//                reqVO.getBareMachineOrderNo(),
//                reqVO.getMaterialNo()
//        );
//
//        // 2. 获取对比月份数据
//        List<HostRequirementComparisonRespVO> compareList = mapper.selectByMonth(
//                reqVO.getCompareMonth(),
//                reqVO.getProductModel(),
//                reqVO.getSeqNo2026(),
//                reqVO.getBareMachineOrderNo(),
//                reqVO.getMaterialNo()
//        );
//
//        // 3. 构建对比数据 Map（key: 车型_顺序号）
//        Map<String, HostRequirementComparisonRespVO> compareMap = compareList.stream()
//                .collect(Collectors.toMap(
//                        item -> item.getProductModel() + "_" + item.getProductionOrder(),
//                        Function.identity(),
//                        (existing, replacement) -> existing  // 避免重复 key 冲突
//                ));
//        Map<String, HostRequirementComparisonRespVO> showMap = showList.stream()
//                .collect(Collectors.toMap(
//                        item -> item.getProductModel() + "_" + item.getProductionOrder(),
//                        Function.identity(),
//                        (v1, v2) -> v1
//                ));
//
//        List<HostRequirementComparisonDiffVO> diffList = new ArrayList<>();
//        // 4. 遍历本月数据，填充差异信息
//        for (HostRequirementComparisonRespVO show : showList) {
//            String key = show.getProductModel() + "_" + show.getProductionOrder();
//            HostRequirementComparisonRespVO compare = compareMap.get(key);
//            HostRequirementComparisonDiffVO diff = new HostRequirementComparisonDiffVO();
//
//            // 基础信息
//            diff.setProductModel(show.getProductModel());
//            diff.setProductionOrder(show.getProductionOrder());
//            diff.setBareMachineOrderNo(show.getBareMachineOrderNo());
//            diff.setDrivingUnitOrderNo(show.getDrivingUnitOrderNo());
//
//            // 本月数据
//            diff.setQuota1Show(show.getQuota1());
//            diff.setMaterialNoShow(show.getMaterialNo());
//            diff.setCylinderNameShow(show.getCylinderName());
//            diff.setConfigShow(show.getConfig());
//            diff.setRequiredQuantityShow(show.getRequiredQuantity());
//            diff.setChassisOnlinePlanDateShow(show.getChassisOnlinePlanDate());
//
//            if (compare != null) {
//                // 对比月数据
//                diff.setQuota1Compare(compare.getQuota1());
//                diff.setMaterialNoCompare(compare.getMaterialNo());
//                diff.setCylinderNameCompare(compare.getCylinderName());
//                diff.setConfigCompare(compare.getConfig());
//                diff.setRequiredQuantityCompare(compare.getRequiredQuantity());
//                diff.setChassisOnlinePlanDateCompare(compare.getChassisOnlinePlanDate());
//
//                // 差异判断
//                diff.setQuota1Diff(!Objects.equals(show.getQuota1(), compare.getQuota1()));
//                diff.setMaterialNoDiff(!Objects.equals(show.getMaterialNo(), compare.getMaterialNo()));
//                diff.setCylinderNameDiff(!Objects.equals(show.getCylinderName(), compare.getCylinderName()));
//                diff.setConfigDiff(!Objects.equals(show.getConfig(), compare.getConfig()));
//                diff.setRequiredQuantityDiff(!Objects.equals(show.getRequiredQuantity(), compare.getRequiredQuantity()));
//
//                // 增量（本月 - 对比月）
//                if (show.getRequiredQuantity() != null && compare.getRequiredQuantity() != null) {
//                    diff.setRequiredQuantityIncrease(show.getRequiredQuantity().subtract(compare.getRequiredQuantity()));
//                } else {
//                    diff.setRequiredQuantityIncrease(null);
//                }
//            } else {
//                // 对比月无此数据，全部标记为差异
//                diff.setQuota1Diff(true);
//                diff.setMaterialNoDiff(true);
//                diff.setCylinderNameDiff(true);
//                diff.setConfigDiff(true);
//                diff.setRequiredQuantityDiff(true);
//                diff.setRequiredQuantityIncrease(show.getRequiredQuantity()); // 新增
//            }
//            diffList.add(diff);
//        }
//
//        // 5. 添加对比月独有、本月没有的数据
//        for (HostRequirementComparisonRespVO compare : compareList) {
//            String key = compare.getProductModel() + "_" + compare.getProductionOrder();
//            if (!showMap.containsKey(key)) {
//                HostRequirementComparisonDiffVO diff = new HostRequirementComparisonDiffVO();
//                // 基础信息（使用对比月数据）
//                diff.setProductModel(compare.getProductModel());
//                diff.setProductionOrder(compare.getProductionOrder());
//                diff.setBareMachineOrderNo(compare.getBareMachineOrderNo());
//                diff.setDrivingUnitOrderNo(compare.getDrivingUnitOrderNo());
//
//                // 对比月数据
//                diff.setQuota1Compare(compare.getQuota1());
//                diff.setMaterialNoCompare(compare.getMaterialNo());
//                diff.setCylinderNameCompare(compare.getCylinderName());
//                diff.setConfigCompare(compare.getConfig());
//                diff.setRequiredQuantityCompare(compare.getRequiredQuantity());
//                diff.setChassisOnlinePlanDateCompare(compare.getChassisOnlinePlanDate());
//
//                // 本月数据为空，设置所有差异标志为 true，增量为 -（对比月数量）
//                diff.setQuota1Diff(true);
//                diff.setMaterialNoDiff(true);
//                diff.setCylinderNameDiff(true);
//                diff.setConfigDiff(true);
//                diff.setRequiredQuantityDiff(true);
//                if (compare.getRequiredQuantity() != null) {
//                    diff.setRequiredQuantityIncrease(compare.getRequiredQuantity().negate()); // 负数表示减少
//                }
//                diffList.add(diff);
//            }
//        }
//
//
//        // 6. 分页处理
//        int pageNo = reqVO.getPageNo() != null ? reqVO.getPageNo() : 1;
//        int pageSize = reqVO.getPageSize() != null ? reqVO.getPageSize() : 10;
//        int fromIndex = (pageNo - 1) * pageSize;
//        int toIndex = Math.min(fromIndex + pageSize, diffList.size());
//        List<HostRequirementComparisonDiffVO> pageList = diffList.subList(fromIndex, toIndex);
//        return new PageResult<>(pageList, (long) diffList.size());
//    }


    // 实现类
    @Override
    public List<String> getAvailableImportDates() {
        Set<Date> dateSet = new TreeSet<>(Collections.reverseOrder());
        dateSet.addAll(vehiclePlanMapper.selectDistinctImportDates());
        dateSet.addAll(vehicleConfigMapper.selectDistinctImportDates());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return dateSet.stream().map(sdf::format).collect(Collectors.toList());
    }

    @Override
    public PageResult<HostRequirementComparisonDiffVO> getComparisonDiff(HostRequirementComparisonDiffReqVO reqVO) {
        Long pageNo = reqVO.getPageNo().longValue();
        Long pageSize = reqVO.getPageSize().longValue();

        String currentDate = reqVO.getCurrentDate();
        String compareDate = reqVO.getCompareDate();

        // 如果没有选择日期，自动取最新和次新
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

        List<HostRequirementComparisonDiffVO> list = mapper.selectDiffPage(
                pageNo, pageSize,
                currentDate, compareDate,
                reqVO.getProductModel(),
                reqVO.getSeqNo2026(),
                reqVO.getBareMachineOrderNo(),
                reqVO.getMaterialNo()
        );

        Long total = mapper.selectDiffCount(
                currentDate, compareDate,
                reqVO.getProductModel(),
                reqVO.getSeqNo2026(),
                reqVO.getBareMachineOrderNo(),
                reqVO.getMaterialNo()
        );

        return new PageResult<>(list, total);
    }

    @Override
    public List<HostRequirementComparisonRespVO> getAllForExport(HostRequirementComparisonPageReqVO pageReqVO) {
        return mapper.selectAllForExport(
                pageReqVO.getCurrentDate(),
                pageReqVO.getCompareDate(),
                pageReqVO.getProductModel(),
                pageReqVO.getSeqNo2026(),
                pageReqVO.getBareMachineOrderNo(),
                pageReqVO.getMaterialNo(),
                pageReqVO.getFallbackMatched()   // 新增
        );
    }

    @Override
    public List<HostRequirementComparisonDiffVO> getAllDiffForExport(HostRequirementComparisonDiffReqVO reqVO) {
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

        return mapper.selectDiffList(
                currentDate, compareDate,
                reqVO.getProductModel(),
                reqVO.getSeqNo2026(),
                reqVO.getBareMachineOrderNo(),
                reqVO.getMaterialNo()
        );
    }

}