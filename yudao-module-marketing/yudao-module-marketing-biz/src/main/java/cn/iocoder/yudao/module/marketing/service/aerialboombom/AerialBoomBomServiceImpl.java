package cn.iocoder.yudao.module.marketing.service.aerialboombom;

import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.marketing.controller.admin.aerialboombom.vo.AerialBoomBomPageReqVO;
import cn.iocoder.yudao.module.marketing.controller.admin.aerialboombom.vo.AerialBoomBomSaveReqVO;
import cn.iocoder.yudao.module.marketing.dal.dataobject.aerialboombom.AerialBoomBomDO;
import cn.iocoder.yudao.module.marketing.dal.mysql.aerialboombom.AerialBoomBomMapper;
import com.alibaba.excel.EasyExcel;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.marketing.enums.ErrorCodeConstants.AERIAL_BOOM_BOM_NOT_EXISTS;

/**
 * 高机臂式/剪叉BOM物料清单 Service 实现类
 *
 * @author 柳文
 */
@Service
@DS("oracle") // 指定使用 Oracle 数据源
@Validated
@Slf4j
public class AerialBoomBomServiceImpl implements AerialBoomBomService {

    @Resource
    private AerialBoomBomMapper aerialBoomBomMapper;

    private static final Pattern PRODUCT_MODEL_PATTERN = Pattern.compile("^([A-Z0-9]+(?:[A-Z0-9\\-]*[A-Z0-9])?)");

    @Override
    public Long createAerialBoomBom(AerialBoomBomSaveReqVO createReqVO) {
        // 插入
        AerialBoomBomDO aerialBoomBom = BeanUtils.toBean(createReqVO, AerialBoomBomDO.class);
        aerialBoomBomMapper.insert(aerialBoomBom);
        // 返回
        return aerialBoomBom.getId();
    }

    @Override
    public void updateAerialBoomBom(AerialBoomBomSaveReqVO updateReqVO) {
        // 校验存在
        validateAerialBoomBomExists(updateReqVO.getId());
        // 更新
        AerialBoomBomDO updateObj = BeanUtils.toBean(updateReqVO, AerialBoomBomDO.class);
        aerialBoomBomMapper.updateById(updateObj);
    }

    @Override
    public void deleteAerialBoomBom(Long id) {
        // 校验存在
        validateAerialBoomBomExists(id);
        // 删除
        aerialBoomBomMapper.deleteById(id);
    }

    private void validateAerialBoomBomExists(Long id) {
        if (aerialBoomBomMapper.selectById(id) == null) {
            throw exception(AERIAL_BOOM_BOM_NOT_EXISTS);
        }
    }

    @Override
    public AerialBoomBomDO getAerialBoomBom(Long id) {
        return aerialBoomBomMapper.selectById(id);
    }

    @Override
    public PageResult<AerialBoomBomDO> getAerialBoomBomPage(AerialBoomBomPageReqVO pageReqVO) {
        return aerialBoomBomMapper.selectPage(pageReqVO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void importBomExcel(MultipartFile file, LocalDate importTime) throws IOException {
        log.info("开始导入BOM清单，文件：{}，批次时间：{}", file.getOriginalFilename(), importTime);
        ZipSecureFile.setMinInflateRatio(0.001);

        List<Map<Integer, String>> rows;
        try (InputStream is = file.getInputStream()) {
            rows = EasyExcel.read(is).sheet(0).doReadSync();
        }

        int headerRowIdx = findHeaderRow(rows);
        if (headerRowIdx == -1) {
            throw new ServiceException(400, "未找到包含“物料编码”的表头行，请检查模板格式");
        }
        Map<Integer, String> headerRow = rows.get(headerRowIdx);

        Map<String, Integer> fixedColMap = buildFixedColumnMap(headerRow);
        if (!fixedColMap.containsKey("物料编码")) {
            throw new ServiceException(400, "表头缺少“物料编码”列");
        }

        // 获取产品型号行（表头行的上一行）
        Map<Integer, String> productModelRow = null;
        if (headerRowIdx > 0) {
            productModelRow = rows.get(headerRowIdx - 1);
        }
        List<DynamicColumn> dynamicColumns = parseDynamicColumns(headerRow, fixedColMap, productModelRow);
        List<AerialBoomBomDO> bomList = new ArrayList<>();
        LocalDateTime batchTime = importTime.atStartOfDay();

        // 收集所有待导入的精准BOM，用于后续删除历史数据
        Set<String> preciseBomSet = new HashSet<>();

        for (int i = headerRowIdx + 1; i < rows.size(); i++) {
            Map<Integer, String> row = rows.get(i);
            String materialCode = row.get(fixedColMap.get("物料编码"));
            if (isBlank(materialCode)) continue;

            String materialDesc = row.get(fixedColMap.getOrDefault("物料描述", -1));
            String supplier = row.get(fixedColMap.getOrDefault("供应商", -1));
            String jitFlag = row.get(fixedColMap.getOrDefault("JIT标识(1)", -1));
            String colorManagement = row.get(fixedColMap.getOrDefault("是否颜色管理(X)", -1));
            String supplyOnDemand = row.get(fixedColMap.getOrDefault("是否按需供货(X)", -1));
            String applicableModel = row.get(fixedColMap.getOrDefault("适配机型", -1));
            // 截取过长的 applicableModel
            if (applicableModel != null && applicableModel.length() > 4000) {
                applicableModel = applicableModel.substring(0, 4000);
                log.warn("物料编码 {} 的适配机型内容过长（原长度{}），已截取至4000字符", materialCode, applicableModel.length());
            }

            for (DynamicColumn dc : dynamicColumns) {
                String qtyStr = row.get(dc.colIndex);
                if (isBlank(qtyStr)) continue;
                BigDecimal quantity = parseBigDecimal(qtyStr);
                if (quantity == null || quantity.compareTo(BigDecimal.ZERO) <= 0) continue;

                AerialBoomBomDO bomDO = AerialBoomBomDO.builder()
                        .materialCode(materialCode.trim())
                        .materialDesc(materialDesc)
                        .supplier(supplier)
                        .jitFlag(jitFlag)
                        .colorManagement(colorManagement)
                        .supplyOnDemand(supplyOnDemand)
                        .applicableModel(applicableModel)
                        .productModel(dc.productModel)
                        .preciseBom(dc.preciseBom)
                        .quantity(quantity)
                        .sourceCategory(null)   // 如果有来源分类列，请从 Excel 读取
                        .plate("高机臂式")
                        .importTime(batchTime)
                        .build();
                bomList.add(bomDO);
                preciseBomSet.add(dc.preciseBom);
            }
        }

        if (bomList.isEmpty()) {
            log.warn("未解析到有效的BOM数据，导入终止");
            return;
        }

        // 根据精准BOM删除历史数据（覆盖导入）
        if (batchTime != null) {
            int deleted = aerialBoomBomMapper.physicalDeleteByImportTime(batchTime);
            log.info("物理删除导入批次 {} 的旧数据 {} 条", batchTime, deleted);
        }

        // 批量插入
        saveBatchWithSnowflakeId(bomList);
        log.info("BOM导入完成，共插入 {} 条记录", bomList.size());
    }

    // ----------------------- 辅助方法 -----------------------
    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    private int findHeaderRow(List<Map<Integer, String>> rows) {
        for (int i = 0; i < rows.size(); i++) {
            Map<Integer, String> row = rows.get(i);
            if (row == null) continue;
            for (String cell : row.values()) {
                if (cell != null && cell.contains("物料编码")) return i;
            }
        }
        return -1;
    }

    private Map<String, Integer> buildFixedColumnMap(Map<Integer, String> headerRow) {
        Map<String, Integer> colMap = new HashMap<>();
        String[] fields = {"物料编码", "物料描述", "供应商", "JIT标识(1)", "是否颜色管理(X)", "是否按需供货(X)", "适配机型"};
        for (String field : fields) {
            for (Map.Entry<Integer, String> entry : headerRow.entrySet()) {
                if (entry.getValue() != null && entry.getValue().trim().contains(field)) {
                    colMap.put(field, entry.getKey());
                    break;
                }
            }
        }
        return colMap;
    }

    private List<DynamicColumn> parseDynamicColumns(Map<Integer, String> headerRow,
                                                    Map<String, Integer> fixedColMap,
                                                    Map<Integer, String> productModelRow) {
        List<DynamicColumn> result = new ArrayList<>();
        Integer adapterModelCol = fixedColMap.get("适配机型");
        if (adapterModelCol == null) {
            return result;
        }
        int startCol = adapterModelCol + 1;
        int totalCols = headerRow.size();
        int endCol = totalCols - 1;
        String lastColVal = headerRow.get(endCol);
        if (lastColVal != null && lastColVal.contains("总计")) {
            endCol = totalCols - 2;
        }
        for (int col = startCol; col <= endCol; col++) {
            String preciseBom = headerRow.get(col);
            if (isBlank(preciseBom)) continue;

            // 优先从产品型号行获取
            String productModel = null;
            if (productModelRow != null) {
                productModel = productModelRow.get(col);
            }
            // 如果获取不到或为空，回退到从精准BOM名称解析
            if (isBlank(productModel)) {
                productModel = extractProductModel(preciseBom);
            }
            if (productModel == null) {
                log.warn("无法获取产品型号，列索引: {}, 精准BOM: {}", col, preciseBom);
                continue;
            }
            result.add(new DynamicColumn(col, preciseBom, productModel));
        }
        return result;
    }

    private String extractProductModel(String preciseBom) {
        if (preciseBom == null) return null;
        int dashIdx = preciseBom.indexOf('-');
        if (dashIdx > 0) {
            String candidate = preciseBom.substring(0, dashIdx);
            if (candidate.matches("^[A-Z0-9]+.*")) return candidate;
        }
        Matcher m = PRODUCT_MODEL_PATTERN.matcher(preciseBom);
        if (m.find()) return m.group(1);
        return null;
    }

    private BigDecimal parseBigDecimal(String str) {
        if (str == null) return null;
        try {
            return new BigDecimal(str.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private void deleteByImportTime(LocalDateTime importTime) {
        LambdaQueryWrapper<AerialBoomBomDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AerialBoomBomDO::getImportTime, importTime);
        int deleted = aerialBoomBomMapper.delete(wrapper);
        log.info("删除批次 {} 的旧数据 {} 条", importTime, deleted);
    }

    /**
     * 批量插入，并为每个对象生成雪花ID（使用 MyBatis-Plus 的 IdWorker）
     * 注意：DO 上的 @TableId(type = IdType.ASSIGN_ID) 在批量 insert 时不会自动填充，
     * 因此需要手动设置 ID。
     */
    private void saveBatchWithSnowflakeId(List<AerialBoomBomDO> list) {
        for (AerialBoomBomDO entity : list) {
            if (entity.getId() == null) {
                entity.setId(IdWorker.getId());  // 生成Long类型的雪花ID
            }
            aerialBoomBomMapper.insert(entity);
        }
    }

    // 内部类
    @AllArgsConstructor
    private static class DynamicColumn {
        int colIndex;
        String preciseBom;
        String productModel;
    }
}