package cn.iocoder.yudao.module.aps.service.materialshortage;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.aps.controller.admin.materialshortage.vo.*;
import cn.iocoder.yudao.module.aps.dal.dataobject.materialshortage.MaterialShortageComponentSummaryDO;
import cn.iocoder.yudao.module.aps.dal.dataobject.materialshortage.MaterialShortageDO;
import cn.iocoder.yudao.module.aps.dal.mysql.materialshortage.MaterialShortageMapper;
import com.baomidou.dynamic.datasource.annotation.DS;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.aps.enums.ErrorCodeConstants.MATERIAL_SHORTAGE_REFRESH_FAILED;

@Service
@DS("oracle")
@Validated
@Slf4j
public class MaterialShortageServiceImpl implements MaterialShortageService {

    @Resource
    private MaterialShortageMapper materialShortageMapper;

    @Resource
    private MaterialShortageJdbcService materialShortageJdbcService;

    @Override
    public PageResult<MaterialShortageSummaryRespVO> getShortageSummaryPage(MaterialShortagePageReqVO pageReqVO) {
        // 1. 查询所有明细（不提前过滤）
        List<MaterialShortageDO> allList = materialShortageMapper.selectList(
                new LambdaQueryWrapperX<MaterialShortageDO>()
                        .eqIfPresent(MaterialShortageDO::getMainMaterialNo, pageReqVO.getMainMaterialNo())
                        .likeIfPresent(MaterialShortageDO::getMaterialDesc, pageReqVO.getMaterialDesc())
                        .eq(MaterialShortageDO::getDeleted, 0)
                        .orderByDesc(MaterialShortageDO::getShortageQty)
        );

        if (allList.isEmpty()) {
            return new PageResult<>(0L);
        }

        // 2. 按成品分组（不过滤，所有成品都保留）
        Map<String, List<MaterialShortageDO>> groupMap = allList.stream()
                .collect(Collectors.groupingBy(MaterialShortageDO::getMainMaterialNo));

        List<MaterialShortageSummaryRespVO> summaryList = groupMap.entrySet().stream()
                .map(entry -> {
                    String mainMaterialNo = entry.getKey();
                    List<MaterialShortageDO> details = entry.getValue();
                    MaterialShortageDO first = details.get(0);

                    MaterialShortageSummaryRespVO vo = new MaterialShortageSummaryRespVO();
                    vo.setMainMaterialNo(mainMaterialNo);
                    vo.setMaterialDesc(first.getMaterialDesc());

                    // ===== 缺口子件数：只统计缺口>0且去重物料号 =====
                    long distinctShortageCount = details.stream()
                            .filter(d -> d.getShortageQty() != null && d.getShortageQty().compareTo(BigDecimal.ZERO) > 0)
                            .map(MaterialShortageDO::getComponentMaterialNo)
                            .distinct()
                            .count();
                    vo.setComponentCount((int) distinctShortageCount);

                    // 总缺口 = 所有子件缺口之和（包含0）
                    BigDecimal total = details.stream()
                            .map(MaterialShortageDO::getShortageQty)
                            .filter(Objects::nonNull)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    vo.setTotalShortageQty(total);

                    // 总成层级字段
                    vo.setMainRequirement(first.getMainRequirement());
                    vo.setMainStockQuantity(first.getMainStockQuantity());
                    vo.setMainTransit(first.getMainTransit());
                    vo.setMainDelivered(first.getMainDelivered());

                    return vo;
                })
                // ===== 按成品物料号升序排序 =====
                .sorted(Comparator.comparing(MaterialShortageSummaryRespVO::getMainMaterialNo))
                .collect(Collectors.toList());

        // 3. 内存分页
        int fromIndex = (pageReqVO.getPageNo() - 1) * pageReqVO.getPageSize();
        int toIndex = Math.min(fromIndex + pageReqVO.getPageSize(), summaryList.size());
        List<MaterialShortageSummaryRespVO> subList = summaryList.subList(fromIndex, toIndex);

        return new PageResult<>(subList, (long) summaryList.size());
    }

    @Override
    public List<MaterialShortageDetailRespVO> getShortageDetails(String mainMaterialNo) {
        List<MaterialShortageDO> list = materialShortageMapper.selectDetailsByMainMaterial(mainMaterialNo);

        // ===== 返回所有子件明细（缺口为0也显示），按组件物料号升序 =====
        return list.stream()
                .sorted(Comparator.comparing(MaterialShortageDO::getComponentMaterialNo))
                .map(doObj -> {
                    MaterialShortageDetailRespVO vo = new MaterialShortageDetailRespVO();
                    vo.setComponentMaterialNo(doObj.getComponentMaterialNo());
                    vo.setComponentDesc(doObj.getComponentDesc());
                    vo.setUnitUsage(doObj.getUnitUsage());
                    vo.setStockQuantity(doObj.getStockQuantity());
                    vo.setTransit(doObj.getTransit());
                    vo.setIssue(doObj.getIssue());
                    vo.setShortageQty(doObj.getShortageQty()); // 可能为0
                    return vo;
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void refreshShortageData() {
        try {
            materialShortageJdbcService.callRefreshProcedure();
            log.info("物料缺口数据刷新成功");
        } catch (Exception e) {
            log.error("物料缺口数据刷新失败", e);
            throw exception(MATERIAL_SHORTAGE_REFRESH_FAILED);
        }
    }

    @Override
    public List<MaterialShortageSummaryRespVO> exportShortageSummary(MaterialShortagePageReqVO reqVO) {
        reqVO.setPageNo(1);
        reqVO.setPageSize(10000);
        PageResult<MaterialShortageSummaryRespVO> pageResult = getShortageSummaryPage(reqVO);
        return pageResult.getList();
    }
    @Override
    public PageResult<MaterialShortageComponentSummaryRespVO> getComponentShortagePage(MaterialShortageComponentPageReqVO reqVO) {
        // 查询所有组件汇总
        List<MaterialShortageComponentSummaryDO> list;
        if (reqVO.getComponentMaterialNo() != null || reqVO.getComponentDesc() != null ||
                (reqVO.getOnlyShortage() != null && reqVO.getOnlyShortage())) {
            list = materialShortageMapper.selectComponentSummaryWithFilter(
                    reqVO.getComponentMaterialNo(),
                    reqVO.getComponentDesc(),
                    reqVO.getOnlyShortage());
        } else {
            list = materialShortageMapper.selectComponentSummary();
        }

        if (list.isEmpty()) {
            return new PageResult<>(0L);
        }

        // 转换为 VO
        List<MaterialShortageComponentSummaryRespVO> voList = list.stream()
                .map(item -> {
                    MaterialShortageComponentSummaryRespVO vo = new MaterialShortageComponentSummaryRespVO();
                    vo.setComponentMaterialNo(item.getComponentMaterialNo());
                    vo.setComponentDesc(item.getComponentDesc());
                    vo.setTotalRequirement(item.getTotalRequirement());
                    vo.setStockQuantity(item.getStockQuantity());
                    vo.setTransit(item.getTransit());
                    vo.setTotalIssue(item.getTotalIssue());
                    vo.setShortageQty(item.getShortageQty());
                    vo.setMainCount(item.getMainCount());
                    vo.setMainMaterialNos(item.getMainMaterialNos());
                    return vo;
                })
                .sorted(Comparator.comparing(MaterialShortageComponentSummaryRespVO::getShortageQty).reversed())
                .collect(Collectors.toList());

        // 内存分页
        int fromIndex = (reqVO.getPageNo() - 1) * reqVO.getPageSize();
        int toIndex = Math.min(fromIndex + reqVO.getPageSize(), voList.size());
        List<MaterialShortageComponentSummaryRespVO> subList = voList.subList(fromIndex, toIndex);

        return new PageResult<>(subList, (long) voList.size());
    }

    @Override
    public List<MaterialShortageComponentSummaryRespVO> exportComponentShortage(MaterialShortageComponentPageReqVO reqVO) {
        reqVO.setPageNo(1);
        reqVO.setPageSize(10000);
        PageResult<MaterialShortageComponentSummaryRespVO> pageResult = getComponentShortagePage(reqVO);
        return pageResult.getList();
    }


}