package cn.iocoder.yudao.module.wm.service.bom;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.aps.dal.dataobject.bomimport.BomImportDO;
import cn.iocoder.yudao.module.aps.dal.dataobject.masterimport.MasterImportDO;
import cn.iocoder.yudao.module.aps.dal.mysql.masterimport.MasterImportMapper;
import cn.iocoder.yudao.module.aps.service.bomimport.BomImportService;
import cn.iocoder.yudao.module.aps.service.masterimport.MasterImportService;
import cn.iocoder.yudao.module.wm.controller.admin.bom.vo.BomPageReqVO;
import cn.iocoder.yudao.module.wm.controller.admin.bom.vo.BomSaveReqVO;
import cn.iocoder.yudao.module.wm.controller.admin.bom.vo.SyncBomReqVO;
import cn.iocoder.yudao.module.wm.dal.dataobject.bom.BomDO;
import cn.iocoder.yudao.module.wm.dal.mysql.bom.BomMapper;
import cn.iocoder.yudao.module.wm.util.SapBomUtils;
import com.alibaba.druid.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class BomServiceImpl implements BomService {

    @Autowired
    private SapBomUtils sapBomUtils;

    @Resource
    private BomMapper bomMapper;

    @Resource
    private BomImportService bomImportService;

    @Resource
    private MasterImportMapper masterImportMapper;
    @Resource
    private MasterImportService masterImportService;

    // ============ SAP BOM查询方法 ============

    /**
     * 从SAP获取BOM数据（新版）- 需要在BomService接口中声明
     */
    @Override
    public List<Map<String, Object>> getBomFromSap(String materialNumber, String plant, String date) {
        try {
            log.info("从SAP获取BOM数据: 物料={}, 工厂={}, 日期={}", materialNumber, plant, date);
            List<Map<String, Object>> rawList = sapBomUtils.getBomComponentsByFunction(materialNumber, plant, date);
            // 过滤：只保留 ANNAM = "JT_TC_RFC" 的组件
//            rawList.stream()
//                    .filter(item -> "JT_TC_RFC".equals(item.get("ANNAM")))
//                    .collect(Collectors.toList());
            // 应用外购件子件过滤
            List<Map<String, Object>> filteredList = filterSapBomData(rawList);
            log.info("原始数据 {} 条，过滤后保留 {} 条", rawList.size(), filteredList.size());
            return filteredList;
        } catch (Exception e) {
            log.error("获取BOM数据失败: 物料={}, 工厂={}", materialNumber, plant, e);
            throw new RuntimeException("获取BOM数据失败: " + e.getMessage(), e);
        }
    }

    /**
     * 兼容旧的接口 - 已经在BomService接口中声明
     */
    @Override
    public List<Map<String, Object>> getBomFromSap(Map<String, Object> conditions) {
        String materialNumber = (String) conditions.get("param4");
        String plant = (String) conditions.get("param5");
        String date = (String) conditions.get("param2"); // 可选的日期参数

        if (StringUtils.isEmpty(materialNumber) || StringUtils.isEmpty(plant)) {
            throw new IllegalArgumentException("物料号和工厂不能为空");
        }

        return getBomFromSap(materialNumber, plant, date);
    }

    /**
     * 根据物料和工厂获取BOM - 已经在BomService接口中声明
     */
    @Override
    public List<Map<String, Object>> getBomByMaterial(String materialNumber, String plant) {
        return getBomFromSap(materialNumber, plant, null);
    }

    /**
     * 获取简化的BOM数据 - 可选方法，如果在接口中声明了需要@Override
     */
    @Override
    public List<Map<String, Object>> getSimpleBom(String materialNumber, String plant) {
        try {
            return sapBomUtils.getSimpleBom(materialNumber, plant);
        } catch (Exception e) {
            log.error("获取简化BOM失败: 物料={}, 工厂={}", materialNumber, plant, e);
            throw new RuntimeException("获取简化BOM失败: " + e.getMessage(), e);
        }
    }

    /**
     * 获取分层的BOM结构 - 可选方法，如果在接口中声明了需要@Override
     */
    @Override
    public Map<String, List<Map<String, Object>>> getGroupedBom(String materialNumber, String plant) {
        try {
            return sapBomUtils.getGroupedBomByLevel(materialNumber, plant);
        } catch (Exception e) {
            log.error("获取分层BOM失败: 物料={}, 工厂={}", materialNumber, plant, e);
            throw new RuntimeException("获取分层BOM失败: " + e.getMessage(), e);
        }
    }

    // ============ 本地数据库操作实现 ============

    @Override
    public Long createBom(BomSaveReqVO createReqVO) {
        // 参数校验
        if (createReqVO == null) {
            throw new IllegalArgumentException("BOM数据不能为空");
        }

        // 检查BOM是否已存在（根据工厂和物料号）
        BomDO existingBom = bomMapper.selectOne(new LambdaQueryWrapperX<BomDO>()
                .eq(BomDO::getWerks, createReqVO.getWerks())
                .eq(BomDO::getIdnrk, createReqVO.getIdnrk()));

        if (existingBom != null) {
            throw new RuntimeException("BOM已存在，工厂: " + createReqVO.getWerks() +
                    ", 物料号: " + createReqVO.getIdnrk());
        }

        // 转换为DO对象
        BomDO bom = BeanUtils.toBean(createReqVO, BomDO.class);

        // 设置默认值
        if (bom.getStatus() == null) {
            bom.setStatus(1); // 默认有效
        }

        // 如果没有物料描述，使用物料号
        if (StringUtils.isEmpty(bom.getOjtxp())) {
            bom.setOjtxp(bom.getIdnrk());
        }

        // 插入数据库
        int result = bomMapper.insert(bom);
        if (result > 0) {
            log.info("创建BOM成功: ID={}, 物料={}, 工厂={}", bom.getId(), bom.getIdnrk(), bom.getWerks());
            return bom.getId();
        } else {
            throw new RuntimeException("创建BOM失败");
        }
    }

    @Override
    public void updateBom(BomSaveReqVO updateReqVO) {
        // 参数校验
        if (updateReqVO == null || updateReqVO.getId() == null) {
            throw new IllegalArgumentException("BOM ID不能为空");
        }

        // 检查BOM是否存在
        BomDO existingBom = bomMapper.selectById(updateReqVO.getId());
        if (existingBom == null) {
            throw new RuntimeException("BOM不存在，ID: " + updateReqVO.getId());
        }

        // 转换为DO对象
        BomDO bom = BeanUtils.toBean(updateReqVO, BomDO.class);

        // 更新数据库
        int result = bomMapper.updateById(bom);
        if (result == 0) {
            throw new RuntimeException("更新BOM失败，ID: " + updateReqVO.getId());
        }

        log.info("更新BOM成功: ID={}", updateReqVO.getId());
    }

    @Override
    public void deleteBom(Long id) {
        // 参数校验
        if (id == null) {
            throw new IllegalArgumentException("BOM ID不能为空");
        }

        // 检查BOM是否存在
        BomDO existingBom = bomMapper.selectById(id);
        if (existingBom == null) {
            throw new RuntimeException("BOM不存在，ID: " + id);
        }

        // 删除BOM
        int result = bomMapper.deleteById(id);
        if (result == 0) {
            throw new RuntimeException("删除BOM失败，ID: " + id);
        }

        log.info("删除BOM成功，ID: {}", id);
    }

    @Override
    public BomDO getBom(Long id) {
        // 参数校验
        if (id == null) {
            throw new IllegalArgumentException("BOM ID不能为空");
        }

        // 查询BOM
        BomDO bom = bomMapper.selectById(id);
        if (bom == null) {
            throw new RuntimeException("BOM不存在，ID: " + id);
        }

        return bom;
    }

    @Override
    public PageResult<BomDO> getBomPage(BomPageReqVO pageReqVO) {
        // 使用BaseMapperX的selectPage方法
        return bomMapper.selectPage(pageReqVO, new LambdaQueryWrapperX<BomDO>()
                .likeIfPresent(BomDO::getWerks, pageReqVO.getWerks())
                .likeIfPresent(BomDO::getIdnrk, pageReqVO.getIdnrk())
                .likeIfPresent(BomDO::getOjtxp, pageReqVO.getOjtxp())
                .eqIfPresent(BomDO::getStatus, pageReqVO.getStatus())
                .orderByDesc(BomDO::getId));
    }

    @Override
    public void syncBomFromSap(SyncBomReqVO syncReqVO) {
        // 参数校验
        if (syncReqVO == null ||
                syncReqVO.getMaterialNumber() == null || syncReqVO.getMaterialNumber().isEmpty() ||
                syncReqVO.getPlant() == null || syncReqVO.getPlant().isEmpty()) {
            throw new IllegalArgumentException("物料号和工厂不能为空");
        }

        log.info("开始从SAP同步BOM数据，物料: {}, 工厂: {}, 强制同步: {}",
                syncReqVO.getMaterialNumber(), syncReqVO.getPlant(), syncReqVO.getForceSync());

        try {
            // 1. 从SAP获取BOM数据（使用新方法）
            List<Map<String, Object>> sapBomList = sapBomUtils.getBomComponentsByFunction(
                    syncReqVO.getMaterialNumber(),
                    syncReqVO.getPlant(),
                    null // 使用当前日期
            );

            if (sapBomList == null || sapBomList.isEmpty()) {
                log.warn("从SAP获取的BOM数据为空，物料: {}, 工厂: {}",
                        syncReqVO.getMaterialNumber(), syncReqVO.getPlant());
                return;
            }

            log.info("从SAP获取到 {} 条BOM记录（原始）", sapBomList.size());

            // 2. 过滤外购件的子件
            List<Map<String, Object>> filteredList = filterSapBomData(sapBomList);
            log.info("过滤后保留 {} 条BOM记录", filteredList.size());

            // 3. 后续处理循环使用 filteredList  根据同步策略处理数据

            int successCount = 0;
            int skipCount = 0;
            int errorCount = 0;

            for (Map<String, Object> sapItem : filteredList) {
                try {
                    // 转换SAP数据为BomDO对象
                    BomDO bomDO = convertSapDataToBomDO(sapItem);

                    // 设置父物料号（如果是顶层物料，父物料号为传入的物料号）
                    String stufe = bomDO.getStufe();
                    if ("0".equals(stufe) || StringUtils.isEmpty(stufe)) {
                        bomDO.setParentIdnrk(syncReqVO.getMaterialNumber());
                    }

                    // 设置版本和状态
                    bomDO.setVersion("1.0");
                    bomDO.setStatus(1); // 默认有效

                    // 设置同步时间（如果有syncTime字段）
                    // SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    // bomDO.setSyncTime(sdf.format(new Date()));

                    // 检查是否已存在（使用工厂、物料号和项目号作为唯一标识）
                    // 注意：需要确保BomDO有posnr字段或者使用extField1存储posnr
                    BomDO existingBom = bomMapper.selectOne(new LambdaQueryWrapperX<BomDO>()
                            .eq(BomDO::getWerks, bomDO.getWerks())
                            .eq(BomDO::getIdnrk, bomDO.getIdnrk())
                            .eq(BomDO::getExtField1, bomDO.getExtField1())); // 假设extField1存储POSNR

                    if (existingBom != null) {
                        // 已存在的情况
                        if (Boolean.TRUE.equals(syncReqVO.getForceSync())) {
                            // 强制同步：更新现有记录
                            bomDO.setId(existingBom.getId());
                            bomMapper.updateById(bomDO);
                            log.debug("更新BOM记录: 工厂={}, 物料号={}, 项目号={}",
                                    bomDO.getWerks(), bomDO.getIdnrk(), bomDO.getExtField1());
                            successCount++;
                        } else {
                            // 非强制同步：跳过已存在记录
                            log.debug("跳过已存在的BOM记录: 工厂={}, 物料号={}, 项目号={}",
                                    bomDO.getWerks(), bomDO.getIdnrk(), bomDO.getExtField1());
                            skipCount++;
                        }
                    } else {
                        // 不存在：插入新记录
                        bomMapper.insert(bomDO);
                        log.debug("插入新BOM记录: 工厂={}, 物料号={}, 项目号={}",
                                bomDO.getWerks(), bomDO.getIdnrk(), bomDO.getExtField1());
                        successCount++;
                    }

                } catch (Exception e) {
                    log.error("处理BOM记录失败: {}", sapItem, e);
                    errorCount++;
                }
            }

            log.info("BOM同步完成，物料: {}, 工厂: {}, 成功: {}, 跳过: {}, 失败: {}",
                    syncReqVO.getMaterialNumber(), syncReqVO.getPlant(),
                    successCount, skipCount, errorCount);

            if (errorCount > 0) {
                throw new RuntimeException(String.format(
                        "BOM同步完成但有错误，成功: %d, 跳过: %d, 失败: %d",
                        successCount, skipCount, errorCount));
            }

        } catch (Exception e) {
            log.error("从SAP同步BOM数据失败，物料: {}, 工厂: {}",
                    syncReqVO.getMaterialNumber(), syncReqVO.getPlant(), e);
            throw new RuntimeException("从SAP同步BOM数据失败: " + e.getMessage(), e);
        }
    }

    /**
     * 定时任务调用方案
     * @param materialNumber
     * @param plant
     */
    @Override
    public void syncBomForMaterial(String materialNumber, String plant) {
        log.info("开始同步物料BOM: 物料={}, 工厂={}", materialNumber, plant);
        // 1. 删除该物料在BOM导入表中的历史数据
        bomImportService.clearBomImportData(materialNumber, plant);

        // 2. 从SAP获取BOM数据
        List<Map<String, Object>> sapBomList = getBomFromSap(materialNumber, plant, null);
        if (sapBomList == null || sapBomList.isEmpty()) {
            log.info("物料 {} 在SAP中无BOM数据", materialNumber);
            return;
        }

        // === 增加过滤 ===
        List<Map<String, Object>> filteredList = filterSapBomData(sapBomList);
        log.info("物料 {} 原始记录 {} 条，过滤后 {} 条", materialNumber, sapBomList.size(), filteredList.size());

        // 3. 导入到BOM导入表
        bomImportService.importBomFromSapData(filteredList, materialNumber, plant);
        log.info("物料 {} BOM同步完成，导入 {} 条记录", materialNumber, filteredList.size());
    }

    /**
     * 将SAP返回的Map数据转换为BomDO对象
     */
    private BomDO convertSapDataToBomDO(Map<String, Object> sapItem) {
        BomDO bomDO = new BomDO();

        try {
            // 设置基本字段
            bomDO.setWerks(getStringValue(sapItem, "WERKS"));
            bomDO.setStufe(getStringValue(sapItem, "STUFE"));
            bomDO.setWegxx(getStringValue(sapItem, "WEGXX"));
            bomDO.setBmtyp(getStringValue(sapItem, "BMTYP"));
            bomDO.setVwegx(getStringValue(sapItem, "VWEGX"));
            bomDO.setOjtxb(getStringValue(sapItem, "OJTXB"));
            bomDO.setOjtxp(getStringValue(sapItem, "OJTXP"));
            bomDO.setMtart(getStringValue(sapItem, "MTART"));
            bomDO.setMeins(getStringValue(sapItem, "MEINS"));
            bomDO.setIdnrk(getStringValue(sapItem, "IDNRK"));

            // 如果没有posnr字段，使用extField1存储
            bomDO.setExtField1(getStringValue(sapItem, "POSNR"));
            bomDO.setExtField2(getStringValue(sapItem, "POSTP"));

            // 处理数量（从字符串转换为BigDecimal）
            String mengeStr = getStringValue(sapItem, "MENGE");
            if (mengeStr != null && !mengeStr.isEmpty()) {
                try {
                    bomDO.setMenge(new BigDecimal(mengeStr));
                } catch (NumberFormatException e) {
                    log.warn("数量格式错误: {}, 使用默认值1", mengeStr);
                    bomDO.setMenge(BigDecimal.ONE);
                }
            } else {
                bomDO.setMenge(BigDecimal.ONE);
            }

            // 处理基本数量
            String bmengStr = getStringValue(sapItem, "BMENG");
            if (bmengStr != null && !bmengStr.isEmpty()) {
                try {
                    bomDO.setBmeng(new BigDecimal(bmengStr));
                } catch (NumberFormatException e) {
                    log.warn("基本数量格式错误: {}", bmengStr);
                }
            }

            // 设置默认描述
            if (StringUtils.isEmpty(bomDO.getOjtxp())) {
                bomDO.setOjtxp(bomDO.getIdnrk());
            }

        } catch (Exception e) {
            log.error("转换SAP数据到BomDO失败: {}", sapItem, e);
            throw new RuntimeException("转换SAP数据失败: " + e.getMessage(), e);
        }

        return bomDO;
    }

    /**
     * 根据物料号和工厂获取采购类型（PROCUREMENT_TYPE）
     */
    private void pruneExternalProcurement(List<BomNode> nodes) {
        for (BomNode node : nodes) {
            if ("F".equals(node.procurementType)) {
                if (!node.children.isEmpty()) {
                    log.info("[剪枝] 物料 {} 为外购件，清空其 {} 个子节点", node.idnrk, node.children.size());
                    for (BomNode child : node.children) {
                        log.info("  移除子件: 物料={}, 层级={}",
                                child.idnrk, parseStufe(getStringValue(child.rawData, "STUFE")));
                    }
                }
                node.children.clear();
            } else {
                pruneExternalProcurement(node.children);
            }
        }
    }

    /**
     * BOM行数据内部类，用于过滤处理
     */
    private static class BomRow {
        Map<String, Object> rawData;
        String bomIdx;       // BOM序号（IDENT）
        int level;           // 层级（STUFE）
        String posnr;        // 行号（POSNR）
        String idnrk;        // 组件物料号
        String werks;        // 工厂
        String procurementType; // 采购类型（PROCUREMENT_TYPE）

        public String getBomIdx() { return bomIdx; }
        public int getLevel() { return level; }
        public String getPosnr() { return posnr; }
    }

    // ========== 内部类：BOM树节点 ==========
    private class BomNode {
        Map<String, Object> rawData;
        String stlkn;
        String stvkn;
        String idnrk;
        String procurementType;
        List<BomNode> children = new ArrayList<>();

        BomNode(Map<String, Object> raw) {
            this.rawData = raw;
            this.stlkn = getStringValue(raw, "STLKN");
            this.stvkn = getStringValue(raw, "STVKN");
            this.idnrk = getStringValue(raw, "IDNRK");
        }

        void collect(List<Map<String, Object>> result) {
            result.add(rawData);
            for (BomNode child : children) {
                child.collect(result);
            }
        }
    }

    // ========== 安全获取字符串值 ==========
    private static String getStringValue(Map<String, Object> map, String key) {
        Object value = map.get(key);
        return value != null ? value.toString().trim() : "";
    }

    // ========== 安全解析层级 ==========
    private int parseStufe(String stufe) {
        try {
            return Integer.parseInt(stufe.trim());
        } catch (Exception e) {
            return 0;
        }
    }

    // ========== 查询采购类型 ==========
    private String getProcurementType(String materialNo, String plant) {
        try {
            MasterImportDO material = masterImportService.getByMaterialNo(materialNo);
            return material != null ? material.getProcurementType() : "";
        } catch (Exception e) {
            log.error("查询物料主数据失败: materialNo={}", materialNo, e);
            return "";
        }
    }


    // ========== 核心过滤方法 ==========
    private List<Map<String, Object>> filterSapBomData(List<Map<String, Object>> sapBomList) {
        if (sapBomList == null || sapBomList.isEmpty()) {
            return Collections.emptyList();
        }

        log.info("=== BOM过滤开始（全局栈方法），原始数据 {} 条 ===", sapBomList.size());

        // 不分组、不排序，直接使用原始顺序构建整棵树
        List<BomNode> roots = buildTreeGlobal(sapBomList);

        // 剪枝
        pruneExternalProcurement(roots);

        // 收集保留节点
        List<Map<String, Object>> result = new ArrayList<>();
        for (BomNode root : roots) {
            root.collect(result);
        }

        // 最终排序（按 IDENT + POSNR，仅影响展示顺序）
        result.sort(Comparator
                .comparing((Map<String, Object> m) -> getStringValue(m, "IDENT"))
                .thenComparingInt(m -> {
                    try {
                        return Integer.parseInt(getStringValue(m, "POSNR"));
                    } catch (NumberFormatException e) {
                        return 0;
                    }
                }));

        log.info("过滤完成：原始{}条，保留{}条", sapBomList.size(), result.size());
        return result;
    }

    private List<BomNode> buildTreeGlobal(List<Map<String, Object>> rows) {
        List<BomNode> roots = new ArrayList<>();
        Deque<BomNode> stack = new ArrayDeque<>();

        log.info("=== 全局栈构建树，共 {} 行 ===", rows.size());
        for (Map<String, Object> row : rows) {
            BomNode node = new BomNode(row);
            node.procurementType = getProcurementType(node.idnrk, getStringValue(row, "WERKS"));
            int level = parseStufe(getStringValue(row, "STUFE"));

            // 回退栈：弹出所有层级 >= 当前层级的节点（找到父节点）
            while (!stack.isEmpty()) {
                BomNode top = stack.peek();
                int topLevel = parseStufe(getStringValue(top.rawData, "STUFE"));
                if (topLevel >= level) {
                    stack.pop();
                } else {
                    break;
                }
            }

            if (stack.isEmpty() || level == 0) {
                roots.add(node);
                log.debug("根节点: 物料={}, 层级={}", node.idnrk, level);
            } else {
                BomNode parent = stack.peek();
                parent.children.add(node);
                log.debug("挂载: 子物料={} -> 父物料={}", node.idnrk, parent.idnrk);
            }

            stack.push(node);
        }

        log.info("全局树构建完成，根节点数: {}", roots.size());
        return roots;
    }

    // ========== 基于栈构建树（正确处理深度优先遍历顺序） ==========
    private List<BomNode> buildTreeFromFlatRows(List<Map<String, Object>> rows) {
        List<BomNode> roots = new ArrayList<>();
        Map<String, BomNode> nodeMap = new LinkedHashMap<>();

        log.info("=== 诊断模式：开始构建树，共 {} 行 ===", rows.size());

        // ----- 打印所有行的关键字段 -----
        log.info("===== SAP原始数据（STUFE,WEGXX,VWEGX,物料号） =====");
        Map<String, Integer> keyCount = new HashMap<>();
        for (Map<String, Object> row : rows) {
            String stufe = getStringValue(row, "STUFE");
            String wegxx = getStringValue(row, "WEGXX");
            String vwegx = getStringValue(row, "VWEGX");
            String idnrk = getStringValue(row, "IDNRK");

            log.info("行: STUFE={}, WEGXX={}, VWEGX={}, 物料={}",
                    stufe, wegxx, vwegx, idnrk);

            // 检查 (STUFE, WEGXX) 组合的唯一性
            String key = stufe + "|" + wegxx;
            keyCount.put(key, keyCount.getOrDefault(key, 0) + 1);

            BomNode node = new BomNode(row);
            node.procurementType = getProcurementType(node.idnrk, getStringValue(row, "WERKS"));
            nodeMap.put(key, node);
        }

        // 输出重复的 key（如果有）
        boolean hasDuplicate = false;
        for (Map.Entry<String, Integer> entry : keyCount.entrySet()) {
            if (entry.getValue() > 1) {
                log.warn("重复的键: '{}' 出现了 {} 次！", entry.getKey(), entry.getValue());
                hasDuplicate = true;
            }
        }
        if (!hasDuplicate) {
            log.info("所有 (STUFE|WEGXX) 组合均唯一。");
        }
        log.info("===== 注册完成，唯一键数量: {} =====", nodeMap.size());

        // ----- 父子关系挂载 -----
        log.info("===== 父子关系挂载过程 =====");
        for (BomNode node : nodeMap.values()) {
            String stufe = getStringValue(node.rawData, "STUFE");
            String wegxx = getStringValue(node.rawData, "WEGXX");
            String vwegx = getStringValue(node.rawData, "VWEGX");
            String idnrk = node.idnrk;
            int level = parseStufe(stufe);

            log.info("处理物料: {} (STUFE={}, WEGXX={}, VWEGX='{}')",
                    idnrk, stufe, wegxx, vwegx);

            if (vwegx.isEmpty() || "0".equals(vwegx) || "000000".equals(vwegx) || level == 0) {
                roots.add(node);
                log.info("  -> 作为根节点（VWEGX为空/0 或 STUFE=0）");
            } else {
                int parentLevel = level - 1;
                String parentKey = parentLevel + "|" + vwegx;
                BomNode parent = nodeMap.get(parentKey);
                if (parent != null) {
                    parent.children.add(node);
                    log.info("  -> 挂载到父物料: {} (父键='{}')", parent.idnrk, parentKey);
                } else {
                    log.warn("  -> 找不到父节点！期望 parentKey='{}'，映射表中无此键。", parentKey);
                    // 尝试列出与 vwegx 相关的可能匹配项
                    log.info("      当前映射表中的所有键: {}", nodeMap.keySet());
                    roots.add(node);
                }
            }
        }

        log.info("===== 树构建完成，根节点数量: {} =====", roots.size());
        return roots;
    }

    // ========== 辅助方法：计算节点数 ==========
    private int countNodes(List<BomNode> nodes) {
        int count = 0;
        for (BomNode node : nodes) {
            count += 1 + countNodes(node.children);
        }
        return count;
    }


    /**
     * 对同一IDENT下的行列表进行层级跳跃过滤
     */
    private List<Map<String, Object>> filterRowsByStufe(List<Map<String, Object>> rows) {
        List<Map<String, Object>> result = new ArrayList<>();
        int i = 0;
        while (i < rows.size()) {
            Map<String, Object> currentRow = rows.get(i);
            result.add(currentRow);

            String stufe = getStringValue(currentRow, "STUFE");
            String idnrk = getStringValue(currentRow, "IDNRK");
            String procurementType = getProcurementType(idnrk, getStringValue(currentRow, "WERKS"));

            // 打印每一行的决策信息
            log.info("[过滤决策] IDENT={}, 物料={}, 层级={}, 采购类型={}, 操作={}",
                    getStringValue(currentRow, "IDENT"),
                    idnrk,
                    stufe,
                    procurementType,
                    "F".equals(procurementType) ? "保留并跳过子件" : "保留");

            if ("F".equals(procurementType)) {
                // 外购件：获取当前层级
                int currentLevel = parseStufe(getStringValue(currentRow, "STUFE"));
                log.debug("跳过外购件[{}]的子件，层级={}", idnrk, currentLevel);

                // 跳过后续所有层级 > currentLevel 的行
                int j = i + 1;
                while (j < rows.size()) {
                    Map<String, Object> nextRow = rows.get(j);
                    int nextLevel = parseStufe(getStringValue(nextRow, "STUFE"));
                    if (nextLevel > currentLevel) {
                        log.info("[跳过子件] 父件={}, 子件={}, 子件层级={}",
                                idnrk, getStringValue(nextRow, "IDNRK"), nextLevel);
                        j++;
                    } else {
                        break;
                    }
                }
                i = j;  // 跳到下一个非子件行
            } else {
                i++;
            }
        }
        return result;
    }

    /**
     * 从SAP同步BOM数据并导入到BOM导入表
     */
    public String syncAndImportBomFromSap(String materialNumber, String plant) {
        try {
            log.info("开始同步并导入BOM数据: 物料={}, 工厂={}", materialNumber, plant);

            // 1. 从SAP获取BOM数据
            List<Map<String, Object>> sapBomList = getBomFromSap(materialNumber, plant, null);

            if (sapBomList == null || sapBomList.isEmpty()) {
                return "从SAP获取的BOM数据为空";
            }

            log.info("从SAP获取到 {} 条BOM记录", sapBomList.size());

            // === 增加过滤 ===
            List<Map<String, Object>> filteredList = filterSapBomData(sapBomList);

            // 2. 导入到BOM导入表
            String importResult = bomImportService.importBomFromSapData(
                    filteredList,
                    materialNumber,
                    plant
            );

            log.info("BOM数据导入结果: {}", importResult);

            return importResult;

        } catch (Exception e) {
            log.error("同步并导入BOM数据失败", e);
            throw new RuntimeException("同步并导入BOM数据失败: " + e.getMessage(), e);
        }
    }

    /**
     * 查询BOM导入数据
     */
    public List<BomImportDO> getBomImportList(String mainMaterialNo, String plant) {
        return bomImportService.getBomImportList(mainMaterialNo, plant);
    }

    /**
     * 清空BOM导入数据
     */
    public void clearBomImport(String mainMaterialNo, String plant) {
        bomImportService.clearBomImportData(mainMaterialNo, plant);
    }
}