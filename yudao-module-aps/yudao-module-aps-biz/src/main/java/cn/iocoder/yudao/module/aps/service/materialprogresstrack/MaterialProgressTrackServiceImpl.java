package cn.iocoder.yudao.module.aps.service.materialprogresstrack;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.aps.controller.admin.materialprogresstrack.vo.MaterialChildrenRespVO;
import cn.iocoder.yudao.module.aps.controller.admin.materialprogresstrack.vo.MaterialSummaryRespVO;
import cn.iocoder.yudao.module.aps.controller.admin.materialprogresstrack.vo.SupplierInfo;
import cn.iocoder.yudao.module.aps.dal.mysql.materialprogresstrack.MaterialProgressTrackMapper;
import com.baomidou.dynamic.datasource.annotation.DS;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
@DS("oracle")
@Validated
@Slf4j
public class MaterialProgressTrackServiceImpl implements MaterialProgressTrackService {

    @Resource
    private MaterialProgressTrackMapper materialProgressTrackMapper;

    @Override
    public PageResult<MaterialSummaryRespVO> getMaterialSummary(String startDate, String endDate,
                                                                String workshop, String materialCode,
                                                                String materialDesc, Boolean onlyAbnormal,
                                                                Integer pageNo, Integer pageSize) {
        Long total = materialProgressTrackMapper.selectMaterialSummaryCount(
                startDate, endDate, workshop, materialCode, materialDesc, onlyAbnormal);
        if (total == null || total == 0) {
            return new PageResult<>(Collections.emptyList(), 0L);
        }
        List<MaterialSummaryRespVO> list = materialProgressTrackMapper.selectMaterialSummaryPage(
                startDate, endDate, workshop, materialCode, materialDesc, onlyAbnormal, pageNo, pageSize);
        return new PageResult<>(list, total);
    }

    @Override
    public List<MaterialChildrenRespVO> getMaterialChildren(String materialCode, String workshop, String demandMonth) {
        // 从Mapper查询原始数据（每个供应商一行）
        List<MaterialChildrenRespVO> raw = materialProgressTrackMapper.selectMaterialChildren(
                materialCode, workshop, demandMonth);

        // 手动按子件物料号聚合供应商
        Map<String, MaterialChildrenRespVO> resultMap = new LinkedHashMap<>();
        for (MaterialChildrenRespVO item : raw) {
            String key = item.getChildMaterialCode();
            if (!resultMap.containsKey(key)) {
                // 新建聚合对象
                MaterialChildrenRespVO agg = new MaterialChildrenRespVO();
                agg.setChildMaterialCode(key);
                agg.setChildMaterialDesc(item.getChildMaterialDesc());
                agg.setTotalDemand(item.getTotalDemand());
                agg.setTotalIssued(item.getTotalIssued());
                agg.setStockQty(item.getStockQty());
                agg.setShortageQty(item.getShortageQty());
                agg.setSuppliers(new ArrayList<>());
                resultMap.put(key, agg);
            }
            // 添加供应商信息（如果当前行有供应商）
            if (item.getSuppliers() != null && !item.getSuppliers().isEmpty()) {
                resultMap.get(key).getSuppliers().addAll(item.getSuppliers());
            }
        }
        return new ArrayList<>(resultMap.values());
    }
}