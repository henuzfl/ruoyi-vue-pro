package cn.iocoder.yudao.module.aps.service.bomimport;

import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

import java.math.BigDecimal;
import java.util.*;
import cn.iocoder.yudao.module.aps.controller.admin.bomimport.vo.*;
import cn.iocoder.yudao.module.aps.dal.dataobject.bomimport.BomImportDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.aps.dal.mysql.bomimport.BomImportMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.aps.enums.ErrorCodeConstants.*;

/**
 * 物料BOM导入 Service 实现类
 *
 * @author 柳文
 */
@Service
@DS("oracle") // 指定使用 Oracle 数据源
@Validated
@Slf4j
public class BomImportServiceImpl implements BomImportService {

    @Resource
    private BomImportMapper bomImportMapper;

    @Override
    public Long createBomImport(BomImportSaveReqVO createReqVO) {
        // 插入
        BomImportDO bomImport = BeanUtils.toBean(createReqVO, BomImportDO.class);
        bomImportMapper.insert(bomImport);
        // 返回
        return bomImport.getId();
    }

    @Override
    public void updateBomImport(BomImportSaveReqVO updateReqVO) {
        // 校验存在
        validateBomImportExists(updateReqVO.getId());
        // 更新
        BomImportDO updateObj = BeanUtils.toBean(updateReqVO, BomImportDO.class);
        bomImportMapper.updateById(updateObj);
    }

    @Override
    public void deleteBomImport(Long id) {
        // 校验存在
        validateBomImportExists(id);
        // 删除
        bomImportMapper.deleteById(id);
    }

    private void validateBomImportExists(Long id) {
        if (bomImportMapper.selectById(id) == null) {
            throw exception(BOM_IMPORT_NOT_EXISTS);
        }
    }

    @Override
    public BomImportDO getBomImport(Long id) {
        return bomImportMapper.selectById(id);
    }

    @Override
    public PageResult<BomImportDO> getBomImportPage(BomImportPageReqVO pageReqVO) {
        return bomImportMapper.selectPage(pageReqVO);
    }
    // ============ 新增的BOM导入方法 ============

    @Override
    public String importBomFromExcel(byte[] fileData, String fileName, String mainMaterialNo, String plant) {
        // 实现Excel导入逻辑
        // 这里需要将文件数据转换为MultipartFile或直接处理
        return "Excel导入功能待实现";
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String importBomFromSapData(List<Map<String, Object>> sapBomList, String mainMaterialNo, String plant) {
        log.info("开始导入SAP BOM数据到BOM导入表: 主物料号={}, 工厂={}, 数据量={}",
                mainMaterialNo, plant, sapBomList != null ? sapBomList.size() : 0);

        if (sapBomList == null || sapBomList.isEmpty()) {
            return "SAP BOM数据为空，无需导入";
        }

        // 先清空现有数据
        clearBomImportData(mainMaterialNo, plant);

        int successCount = 0;
        int errorCount = 0;
        List<String> errorMessages = new ArrayList<>();

        for (int i = 0; i < sapBomList.size(); i++) {
            Map<String, Object> sapItem = sapBomList.get(i);
            try {
                // 转换SAP数据为BomImportDO
                BomImportDO bomImportDO = convertSapDataToBomImportDO(sapItem, mainMaterialNo, plant, i + 1);
                if (bomImportDO != null) {
                    bomImportMapper.insert(bomImportDO);
                    successCount++;
                }
            } catch (Exception e) {
                errorCount++;
                String errorMsg = String.format("第%d条SAP数据导入失败: %s", i + 1, e.getMessage());
                errorMessages.add(errorMsg);
                log.error(errorMsg, e);
            }
        }

        log.info("SAP BOM数据导入完成: 成功={}, 失败={}", successCount, errorCount);

        // 构建返回消息
        StringBuilder resultMsg = new StringBuilder();
        resultMsg.append(String.format("导入完成: 成功 %d 条, 失败 %d 条", successCount, errorCount));
        if (!errorMessages.isEmpty()) {
            resultMsg.append("\n失败详情:");
            for (int i = 0; i < Math.min(errorMessages.size(), 10); i++) {
                resultMsg.append("\n").append(errorMessages.get(i));
            }
        }

        return resultMsg.toString();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void clearBomImportData(String mainMaterialNo, String plant) {
        // 使用逻辑删除或物理删除，根据你的业务需求
        // 这里使用物理删除
        int deletedCount = bomImportMapper.physicalDeleteByMaterialAndPlant(mainMaterialNo, plant);
        log.info("清空BOM导入数据: 主物料号={}, 工厂={}, 删除{}条",
                mainMaterialNo, plant, deletedCount);
    }

    @Override
    public List<BomImportDO> getBomImportList(String mainMaterialNo, String plant) {
        return bomImportMapper.selectList(
                new LambdaQueryWrapper<BomImportDO>()
                        .eq(BomImportDO::getMainMaterialNo, mainMaterialNo)
                        .eq(BomImportDO::getPlant, plant)
                        .orderByAsc(BomImportDO::getLevelNo, BomImportDO::getLineNo)
        );
    }

    /**
     * 转换SAP数据为BomImportDO
     */
    private BomImportDO convertSapDataToBomImportDO(
            Map<String, Object> sapItem,
            String mainMaterialNo,
            String plant,
            int lineNumber) {

        BomImportDO bomImport = new BomImportDO();

        try {
            // 设置行号
            //bomImport.setLineNo(String.valueOf(lineNumber));
            bomImport.setLineNo(getStringValue(sapItem, "WEGXX"));
            // 设置父节点行号 - 对应VWEGX字段
            bomImport.setParentLineNo(getStringValue(sapItem, "VWEGX"));

            // 设置层级 - 使用Long类型，对应STUFE字段
            String stufe = getStringValue(sapItem, "STUFE");
            if (stufe != null && !stufe.isEmpty()) {
                try {
                    bomImport.setLevelNo(Long.parseLong(stufe));
                } catch (NumberFormatException e) {
                    bomImport.setLevelNo(0L);
                }
            } else {
                bomImport.setLevelNo(0L);
            }

            // 工厂 - 对应PLANT/WERKS字段
            // 优先使用sapItem中的WERKS，如果没有则使用传入的plant参数
            String werks = getStringValue(sapItem, "WERKS");
            bomImport.setPlant(werks != null && !werks.isEmpty() ? werks : plant);

            // 主物料号 - 对应MAIN_MATERIAL_NO字段
            bomImport.setMainMaterialNo(mainMaterialNo);

            // 组件物料号 - 对应COMPONENT_MATERIAL_NO/IDNRK字段
            bomImport.setComponentMaterialNo(getStringValue(sapItem, "IDNRK"));

            // 组件物料描述 - 对应COMPONENT_DESC/OJTXP字段
            bomImport.setComponentDesc(getStringValue(sapItem, "OJTXP"));

            // 规格型号 - 对应SPEC_MODEL字段，从SAP数据中获取
            // 注意：这里假设SPEC_MODEL在SAP数据中直接提供，如果没有可以设为null
            bomImport.setSpecModel(getStringValue(sapItem, "SPEC_MODEL"));

            // 组件数量 - 对应COMPONENT_QTY/MENGE字段
            String mengeStr = getStringValue(sapItem, "MENGE");
            if (mengeStr != null && !mengeStr.isEmpty()) {
                try {
                    bomImport.setComponentQty(new BigDecimal(mengeStr));
                } catch (NumberFormatException e) {
                    // 如果转换失败，设置为0
                    bomImport.setComponentQty(BigDecimal.ZERO);
                }
            } else {
                bomImport.setComponentQty(BigDecimal.ZERO);
            }

            // 单台用量 - 对应UNIT_USAGE/MNGLG字段
            String mnglgStr = getStringValue(sapItem, "MNGLG");
            if (mnglgStr != null && !mnglgStr.isEmpty()) {
                try {
                    bomImport.setUnitUsage(new BigDecimal(mnglgStr));
                } catch (NumberFormatException e) {
                    // 如果转换失败，使用组件数量
                    bomImport.setUnitUsage(bomImport.getComponentQty());
                }
            } else {
                // 如果没有MNGLG字段，使用组件数量
                bomImport.setUnitUsage(bomImport.getComponentQty());
            }

            // 物料类型 - 对应MATERIAL_TYPE/MTART字段
            bomImport.setMaterialType(getStringValue(sapItem, "MTART"));

            // 特殊采购类型 - 对应SPECIAL_PROCUREMENT_TYPE/SOBSL字段
            // 注意：之前使用的是SOBKZ，现在改为SOBSL
            bomImport.setSpecialProcurementType(getStringValue(sapItem, "SOBSL"));

            // 库存地点 - 对应STORAGE_LOCATION/LGORT字段
            bomImport.setStorageLocation(getStringValue(sapItem, "LGORT"));

            // 单位 - 对应UNIT/MMEIN字段
            // 注意：之前使用的是MEINS，现在改为MMEIN
            bomImport.setUnit(getStringValue(sapItem, "MMEIN"));

            // 采购类型 - 对应PROCUREMENT_TYPE/ZEINR字段
            // 注意：之前使用的是BMTYP，现在改为ZEINR
            bomImport.setProcurementType(getStringValue(sapItem, "ZEINR"));

            // 采购组 - 对应PURCHASING_GROUP/EKGRP字段
            bomImport.setPurchasingGroup(getStringValue(sapItem, "EKGRP"));

            // 导入时间 - 对应IMPORT_DATE，设置为当前日期
            bomImport.setImportDate(LocalDateTime.now());

            // 毛重和净重字段在映射中没有对应，保持为null或根据业务需求设置
            // bomImport.setGrossWeight(...);
            // bomImport.setNetWeight(...);

            return bomImport;

        } catch (Exception e) {
            log.error("转换SAP数据为BOM导入对象失败: {}", sapItem, e);
            throw new RuntimeException("转换SAP数据失败: " + e.getMessage(), e);
        }
    }

    /**
     * 安全获取Map中的字符串值
     */
    private String getStringValue(Map<String, Object> map, String key) {
        if (map == null) {
            return "";
        }

        Object value = map.get(key);
        if (value == null) {
            return "";
        }

        // 处理不同类型
        if (value instanceof String) {
            return ((String) value).trim();
        } else if (value instanceof Number) {
            return String.valueOf(value);
        } else {
            return value.toString().trim();
        }
    }
}