package cn.iocoder.yudao.module.wm.service.material;

import cn.iocoder.yudao.module.aps.dal.dataobject.masterimport.MasterImportDO;
import cn.iocoder.yudao.module.aps.service.masterimport.*;
import cn.iocoder.yudao.module.wm.controller.admin.material.vo.MaterialResultVO;
import cn.iocoder.yudao.module.wm.controller.admin.material.vo.SapMaterialQueryReqVO;
import cn.iocoder.yudao.module.wm.util.*;
import com.baomidou.dynamic.datasource.annotation.DS;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * SAP物料主数据查询服务实现
 */
@Service
@Slf4j
public class SapMaterialServiceImpl implements SapMaterialService {

    @Autowired
    private SapMaterialUtils sapMaterialUtils;

    @Resource
    private MasterImportService masterImportService;

    @Value("${sap.default-plant:6400}")
    private String defaultPlant;

    @Override
    public List<MaterialResultVO> searchMaterials(SapMaterialQueryReqVO reqVO) {
        if (reqVO == null || CollectionUtils.isEmpty(reqVO.getMaterialNumbers())) {
            log.warn("物料号列表为空，直接返回空列表");
            return Collections.emptyList();
        }

        // 获取工厂（优先使用传入，否则默认）
        String plant = reqVO.getPlantOrDefault(defaultPlant);
        List<String> matnrList = reqVO.getMaterialNumbers().stream()
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .distinct()
                .collect(Collectors.toList());

        log.info("批量查询物料主数据，物料数: {}, 工厂: {}", matnrList.size(), plant);

        // 调用工具类查询（自动分批，工厂固定）
        List<Map<String, Object>> rawData = sapMaterialUtils.searchMaterials(matnrList, plant);

        // 转换为VO
        return convertToResultVO(rawData);
    }

    @Override
    public MaterialResultVO getMaterial(String matnr, String plant) {
        if (matnr == null || matnr.trim().isEmpty()) {
            return null;
        }
        String targetPlant = plant != null ? plant : defaultPlant;
        List<Map<String, Object>> rawData = sapMaterialUtils.searchMaterial(matnr, targetPlant);
        if (CollectionUtils.isEmpty(rawData)) {
            return null;
        }
        // 一个物料可能对应多个工厂/扩展记录，取第一条作为基本信息
        List<MaterialResultVO> vos = convertToResultVO(rawData);
        return vos.isEmpty() ? null : vos.get(0);
    }

    @Override
    public boolean checkMaterialExists(String matnr, String plant) {
        MaterialResultVO vo = getMaterial(matnr, plant);
        return vo != null;
    }

    @Override
    public int syncMaterialsFromMainPlan(List<String> materialNos) {
        if (CollectionUtils.isEmpty(materialNos)) {
            return 0;
        }

        // 1. 从 SAP 查询物料数据（复用已有的 searchMaterials 逻辑）
        SapMaterialQueryReqVO reqVO = new SapMaterialQueryReqVO();
        reqVO.setMaterialNumbers(materialNos);
        reqVO.setPlant("6400"); // 根据实际情况，可从配置或参数中获取
        List<MaterialResultVO> sapMaterials = this.searchMaterials(reqVO); // 假设已有此方法

        if (CollectionUtils.isEmpty(sapMaterials)) {
            return 0;
        }

        // 2. 将 MaterialResultVO 转换为 MasterImportDO
        List<MasterImportDO> importList = sapMaterials.stream()
                .map(this::convertToMasterImport)
                .collect(Collectors.toList());

        // 3. 调用 MasterImportService 执行删除 + 插入（事务内）
        log.info("【数据库操作】调用 masterImportService.syncMaterialData，待删除物料数={}, 待插入记录数={}",
                materialNos.size(), importList.size());
        masterImportService.syncMaterialData(materialNos, importList);
        log.info("【同步结束】成功处理 {} 条物料", importList.size());

        return importList.size();
    }

    /**
     * 转换方法：MaterialResultVO -> MasterImportDO
     * 字段映射需根据实际 VO 和 DO 的字段名调整
     */
    @DS("oracle")  // 关键：指定 Oracle 数据源
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean syncSingleMaterial(String matnr, String plant) {
        if (matnr == null || matnr.trim().isEmpty()) {
            log.warn("物料号为空，同步取消");
            return false;
        }

        String targetPlant = StringUtils.hasText(plant) ? plant : defaultPlant;

        // 1. 从 SAP 查询单个物料
        List<Map<String, Object>> rawData = sapMaterialUtils.searchMaterial(matnr, targetPlant);
        if (CollectionUtils.isEmpty(rawData)) {
            log.warn("SAP 未返回物料 {} 的数据", matnr);
            return false;
        }

        // 2. 转换为 VO
        List<MaterialResultVO> voList = convertToResultVO(rawData);
        if (voList.isEmpty()) {
            return false;
        }
        MaterialResultVO vo = voList.get(0); // 取第一条

        // 3. 转换为 DO
        MasterImportDO entity = convertToMasterImport(vo);

        // 4. 删除旧数据并插入新数据
        List<String> matnrList = Collections.singletonList(matnr);
        List<MasterImportDO> insertList = Collections.singletonList(entity);
        masterImportService.syncMaterialData(matnrList, insertList);

        log.info("单个物料同步成功：{}", matnr);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int syncMaterialsByDate(LocalDate startDate, LocalDate endDate) {
        log.info("开始按日期同步物料，日期范围: {} 至 {}", startDate, endDate);
        // 转换 LocalDate 为 Date
        Date fromDate = startDate != null ? java.sql.Date.valueOf(startDate) : null;
        Date toDate = endDate != null ? java.sql.Date.valueOf(endDate) : null;
        String plant = defaultPlant;

        // 调用工具类查询物料（按日期）
        List<Map<String, Object>> rawData = sapMaterialUtils.searchMaterialsByDate(fromDate, toDate, plant);
        if (CollectionUtils.isEmpty(rawData)) {
            log.info("未查询到日期范围内的物料");
            return 0;
        }

        // 转换为 MaterialResultVO
        List<MaterialResultVO> materialList = convertToResultVO(rawData);
        // 转换为 MasterImportDO
        List<MasterImportDO> importList = materialList.stream()
                .map(this::convertToMasterImport)
                .collect(Collectors.toList());

        // 提取物料号列表
        List<String> materialNos = materialList.stream()
                .map(MaterialResultVO::getMaterialNumber)
                .distinct()
                .collect(Collectors.toList());

        if (!importList.isEmpty()) {
            masterImportService.syncMaterialData(materialNos, importList);
        }
        log.info("按日期同步完成，共处理 {} 条物料", importList.size());
        return importList.size();
    }

    /**
     * 转换方法：MaterialResultVO -> MasterImportDO
     * 字段映射需根据实际 VO 和 DO 的字段名调整
     */
    private MasterImportDO convertToMasterImport(MaterialResultVO vo) {
        MasterImportDO master = new MasterImportDO();
        master.setMaterialNo(vo.getMaterialNumber());
        master.setMaterialDesc(vo.getMaterialDescCn());
        master.setMaterialType(vo.getMaterialType());
        master.setGrossWeight(vo.getGrossWeight());
        master.setNetWeight(vo.getNetWeight());
        master.setBaseUom(vo.getBaseUnit());
        master.setProcurementType(vo.getProcurementType());
        master.setProductionStorageLocation(vo.getProductionStorageLoc());
        master.setProductionScheduler(vo.getProductionScheduler());
        master.setMaterialCategory(vo.getMaterialCategory());
        master.setExternalProcurementStorage(vo.getExternalProcurementStorage());
        master.setPlannedDeliveryTime(vo.getPlannedDeliveryTime());
        master.setPriceControl(vo.getPriceControl());
        master.setNoCostEstimation(vo.getNoCostEstimation());
        master.setQsCostEstimate(vo.getQsCostEstimate());
        master.setSizeDimension(vo.getSizeDimension());
        master.setDistributionFlag(vo.getDistributionFlag());
        // 新增：评估类、采购组
        master.setValuationClass(vo.getValuationClass());
        master.setPurchasingGroup(vo.getPurchasingGroup());
        return master;
    }

    // ==================== 私有转换方法 ====================

    /**
     * 将SAP原始数据转换为VO列表
     */
    /**
     * 将 SAP RFC zfm_tl_search_matnr 返回的 ET_MARA 表数据转换为 MaterialResultVO 列表
     *
     * @param rawData SAP 返回的原始 Map 列表（key 为字段名大写）
     * @return MaterialResultVO 列表
     */
    private List<MaterialResultVO> convertToResultVO(List<Map<String, Object>> rawData) {
        if (CollectionUtils.isEmpty(rawData)) {
            return Collections.emptyList();
        }

        List<MaterialResultVO> resultList = new ArrayList<>();
        for (Map<String, Object> item : rawData) {
            MaterialResultVO vo = new MaterialResultVO();

            // MARA 基础表字段
            vo.setMaterialNumber(getString(item, "MATNR"));
            vo.setMaterialDescCn(getString(item, "MAKTX"));
            vo.setMaterialType(getString(item, "MTART"));
            vo.setBaseUnit(getString(item, "MEINS"));
            vo.setGrossWeight(toBigDecimal(item.get("BRGEW")));
            vo.setNetWeight(toBigDecimal(item.get("NTGEW")));
            vo.setSizeDimension(getString(item, "GROES"));

            // MARC 工厂数据字段
            vo.setPlant(getString(item, "WERKS"));
            vo.setProcurementType(getString(item, "BESKZ"));
            vo.setProductionStorageLoc(getString(item, "LGPRO"));
            vo.setProductionScheduler(getString(item, "FEVOR"));
            vo.setPurchasingGroup(getString(item, "EKGRP"));
            vo.setExternalProcurementStorage(getString(item, "LGFSB"));
            vo.setPlannedDeliveryTime(toInteger(item.get("PLIFZ")));
            vo.setNoCostEstimation(getString(item, "NCOST"));

            // MBEW 评估数据字段
            vo.setValuationClass(getString(item, "BKLAS"));
            vo.setPriceControl(getString(item, "VPRSV"));
            vo.setQsCostEstimate(getString(item, "EKALR"));

            // ZTLMMM0001 自定义表字段
            vo.setDistributionFlag(getString(item, "ZFIELD3"));
            vo.setMaterialCategory(getString(item, "ZFIELD4"));

            // 其他
            vo.setLastChangeDate(getString(item, "LAEDA"));

            resultList.add(vo);
        }
        return resultList;
    }

    private String getString(Map<String, Object> map, String key) {
        Object value = map.get(key);
        return value != null ? value.toString() : "";
    }

    private BigDecimal toBigDecimal(Object value) {
        if (value == null) {
            return BigDecimal.ZERO;
        }
        try {
            return new BigDecimal(value.toString());
        } catch (NumberFormatException e) {
            return BigDecimal.ZERO;
        }
    }

    private Integer toInteger(Object value) {
        if (value == null) return null;
        try {
            return Integer.valueOf(value.toString().trim());
        } catch (NumberFormatException e) {
            log.warn("无法转换为Integer: {}", value);
            return null;
        }
    }
}