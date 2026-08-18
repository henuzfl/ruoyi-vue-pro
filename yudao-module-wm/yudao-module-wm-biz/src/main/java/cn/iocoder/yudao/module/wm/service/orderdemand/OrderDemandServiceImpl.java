package cn.iocoder.yudao.module.wm.service.orderdemand;

import cn.iocoder.yudao.module.wm.util.sap.SapResbUtils;
import com.baomidou.dynamic.datasource.annotation.DS;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import cn.iocoder.yudao.module.wm.controller.admin.orderdemand.vo.*;
import cn.iocoder.yudao.module.wm.dal.dataobject.orderdemand.OrderDemandDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.wm.dal.mysql.orderdemand.OrderDemandMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.wm.enums.ErrorCodeConstants.*;
import cn.iocoder.yudao.module.wm.controller.admin.orderdemand.vo.OrderDemandKey;


/**
 * 订单追溯需求 Service 实现类
 *
 * @author 柳文
 */
@Service
@DS("oracle") // 指定使用 Oracle 数据源
@Validated
@Slf4j
public class OrderDemandServiceImpl implements OrderDemandService {

    @Resource
    private OrderDemandMapper orderDemandMapper;

    @Autowired
    private SapResbUtils sapResbUtils;

    @Value("${sap.default-plant:6400}")
    private String defaultPlant;

    @Override
    public BigDecimal createOrderDemand(OrderDemandSaveReqVO createReqVO) {
        // 插入
        OrderDemandDO orderDemand = BeanUtils.toBean(createReqVO, OrderDemandDO.class);
        orderDemandMapper.insert(orderDemand);
        // 返回
        return orderDemand.getId();
    }

    @Override
    public void updateOrderDemand(OrderDemandSaveReqVO updateReqVO) {
        // 校验存在
        validateOrderDemandExists(updateReqVO.getId());
        // 更新
        OrderDemandDO updateObj = BeanUtils.toBean(updateReqVO, OrderDemandDO.class);
        orderDemandMapper.updateById(updateObj);
    }

    @Override
    public void deleteOrderDemand(BigDecimal id) {
        // 校验存在
        validateOrderDemandExists(id);
        // 删除
        orderDemandMapper.deleteById(id);
    }

    private void validateOrderDemandExists(BigDecimal id) {
        if (orderDemandMapper.selectById(id) == null) {
            throw exception(ORDER_DEMAND_NOT_EXISTS);
        }
    }

    @Override
    public OrderDemandDO getOrderDemand(BigDecimal id) {
        return orderDemandMapper.selectById(id);
    }

    @Override
    public PageResult<OrderDemandDO> getOrderDemandPage(OrderDemandPageReqVO pageReqVO) {
        return orderDemandMapper.selectPage(pageReqVO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int importOrderDemand(List<OrderDemandImportReqVO> importVOList) {
        if (CollectionUtils.isEmpty(importVOList)) {
            return 0;
        }

        // 1. 内部去重（按订单号+物料号），同时收集键列表用于删除
        Set<String> uniqueKeys = new HashSet<>();
        List<OrderDemandDO> saveList = new ArrayList<>();
        List<OrderDemandKey> deleteKeys = new ArrayList<>(); // 用于删除的键

        for (OrderDemandImportReqVO vo : importVOList) {
            if (StringUtils.isEmpty(vo.getOrderNo()) || StringUtils.isEmpty(vo.getMaterialNo())) {
                log.warn("跳过订单号或物料号为空的数据行");
                continue;
            }
            String key = vo.getOrderNo() + "_" + vo.getMaterialNo();
//            if (uniqueKeys.contains(key)) {
//                log.warn("重复键已跳过：{}", key);
//                continue;
//            }
            uniqueKeys.add(key);
            deleteKeys.add(new OrderDemandKey(vo.getOrderNo(), vo.getMaterialNo()));

            OrderDemandDO entity = BeanUtils.toBean(vo, OrderDemandDO.class);
            saveList.add(entity);
        }

        if (saveList.isEmpty()) {
            return 0;
        }

        // 2. 先物理删除这些订单号+物料号的现有数据
        // 2. 先物理删除这些订单号+物料号的现有数据
        if (!deleteKeys.isEmpty()) {
            int deletedCount = 0;
            int deleteBatchSize = 1000; // 每批最多1000个键
            for (int i = 0; i < deleteKeys.size(); i += deleteBatchSize) {
                int end = Math.min(i + deleteBatchSize, deleteKeys.size());
                List<OrderDemandKey> batchKeys = deleteKeys.subList(i, end);
                deletedCount += orderDemandMapper.deleteByOrderNoAndMaterialNos(batchKeys);
            }
            log.info("已物理删除 {} 条旧数据", deletedCount);
        }

        // 3. 设置审计字段（ID 由雪花算法自动生成）
        LocalDateTime now = LocalDateTime.now();
        for (OrderDemandDO entity : saveList) {
            entity.setCreateTime(now);
            entity.setUpdateTime(now);
            // creator/updater 由 MyBatis-Plus 自动填充，无需手动设置
        }

        // 4. 分批插入（每批 500 条）
        int batchSize = 30; // 30 × 16 = 480 < 1000
        int totalInserted = 0;
        for (int i = 0; i < saveList.size(); i += batchSize) {
            int end = Math.min(i + batchSize, saveList.size());
            List<OrderDemandDO> batchList = saveList.subList(i, end);
            orderDemandMapper.batchInsert(batchList);
            totalInserted += batchList.size();
        }

        log.info("订单追溯需求导入完成，共插入 {} 条", totalInserted);
        return totalInserted;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int syncOrderDemandFromSap(SapResbQueryReqVO reqVO) {
        // 参数校验
        if (reqVO == null || !StringUtils.hasText(reqVO.getPlant())) {
            reqVO = new SapResbQueryReqVO();
            reqVO.setPlant(defaultPlant);
        }

        // 1. 调用 SAP 查询
        List<Map<String, Object>> rawData = sapResbUtils.searchResb(
                reqVO.getPlant(),
                reqVO.getAufnr(),
                reqVO.getRsnum(),
                reqVO.getDateRange()
        );

        if (CollectionUtils.isEmpty(rawData)) {
            log.info("SAP 未返回任何预留数据");
            return 0;
        }

        // 2. 转换为 OrderDemandDO 列表
        List<OrderDemandDO> demandList = convertResbToOrderDemand(rawData);
        if (demandList.isEmpty()) {
            return 0;
        }

        // 3. 构建删除键（按订单号+物料号）
        List<OrderDemandKey> deleteKeys = new ArrayList<>();
        Set<String> uniqueKeys = new HashSet<>();
        for (OrderDemandDO entity : demandList) {
            String key = entity.getOrderNo() + "_" + entity.getMaterialNo();
            if (!uniqueKeys.contains(key)) {
                uniqueKeys.add(key);
                deleteKeys.add(new OrderDemandKey(entity.getOrderNo(), entity.getMaterialNo()));
            }
        }

        // 4. 删除现有数据
        if (!deleteKeys.isEmpty()) {
            int deletedCount = orderDemandMapper.deleteByOrderNoAndMaterialNos(deleteKeys);
            log.info("同步前删除旧数据 {} 条", deletedCount);
        }

        // 5. 批量插入新数据
        if (!demandList.isEmpty()) {
            LocalDateTime now = LocalDateTime.now();
            for (OrderDemandDO entity : demandList) {
                entity.setCreateTime(now);
                entity.setUpdateTime(now);
            }
            orderDemandMapper.batchInsert(demandList);
        }

        log.info("从 SAP 同步订单需求完成，共插入 {} 条", demandList.size());
        return demandList.size();
    }

    @Override
    public List<OrderDemandFromSapVO> searchResbFromSap(SapResbQueryReqVO reqVO) {
        if (reqVO == null || !StringUtils.hasText(reqVO.getPlant())) {
            reqVO = new SapResbQueryReqVO();
            reqVO.setPlant(defaultPlant);
        }
        List<Map<String, Object>> rawData = sapResbUtils.searchResb(
                reqVO.getPlant(),
                reqVO.getAufnr(),
                reqVO.getRsnum(),
                reqVO.getDateRange()
        );
        return convertMapToSapResbVO(rawData);
    }
    // ==================== 私有转换方法 ====================

    /**
     * 将 SAP 预留数据转换为 OrderDemandDO
     */
    private List<OrderDemandDO> convertResbToOrderDemand(List<Map<String, Object>> rawData) {
        List<OrderDemandDO> result = new ArrayList<>();
        if (CollectionUtils.isEmpty(rawData)) return result;

        for (Map<String, Object> item : rawData) {
            if ("X".equalsIgnoreCase(getString(item, "DUMPS"))) {
                continue;
            }

            String materialNo = getString(item, "MATNR");
            // 新增：过滤物料号以 'A' 开头的记录（不区分大小写）
            if (materialNo != null && !materialNo.isEmpty() && materialNo.toUpperCase().startsWith("A")) {
                log.debug("跳过物料号以A开头的预留记录，物料号: {}", materialNo);
                continue;
            }

            OrderDemandDO entity = new OrderDemandDO();

            // 订单号
            entity.setOrderNo(getString(item, "AUFNR"));
            // 追溯需求号（可留空或使用预留号）
            entity.setTraceDemandNo(getString(item, "RSNUM"));
            // 物料编码
            entity.setMaterialNo(getString(item, "MATNR"));
            // 物料描述
            entity.setMaterialDescription(getString(item, "MAKTX"));

            // 需求数量：取 BDMNG（需求数量） - ENMNG（已删除数量）
            BigDecimal bdmng = toBigDecimal(item.get("BDMNG"));
            BigDecimal enmng = toBigDecimal(item.get("ENMNG"));
            entity.setDemandQuantity(bdmng);

            // 出库累计数（预留表无此字段，默认0）
            entity.setOutboundAccumulated(enmng);
            // 未清数量（暂等于需求数量）
            entity.setOpenQuantity(bdmng.subtract(enmng));

            // 状态：预留状态 RSSTA 可转换为状态码（示例）
            String rssta = getString(item, "RSSTA");
            if ("B".equalsIgnoreCase(rssta)) {
                entity.setStatus((short) 0); // 待处理
            } else if ("F".equalsIgnoreCase(rssta)) {
                entity.setStatus((short) 2); // 已完成
            } else {
                entity.setStatus((short) 1); // 部分完成/其他
            }

            // 备注：可拼接订单号+预留号
            entity.setRemark("SAP同步，预留号:" + getString(item, "RSNUM"));

            result.add(entity);
        }
        return result;
    }

    /**
     * 将 SAP 原始数据转换为 OrderDemandFromSapVO
     */
    private List<OrderDemandFromSapVO> convertMapToSapResbVO(List<Map<String, Object>> rawData) {
        List<OrderDemandFromSapVO> list = new ArrayList<>();
        if (CollectionUtils.isEmpty(rawData)) return list;

        for (Map<String, Object> item : rawData) {
            if ("X".equalsIgnoreCase(getString(item, "DUMPS"))) {
                continue; // 跳过逻辑删除的预留行
            }

            String materialNo = getString(item, "MATNR");
            // 新增：过滤物料号以 'A' 开头的记录
            if (materialNo != null && !materialNo.isEmpty() && materialNo.toUpperCase().startsWith("A")) {
                log.debug("查询结果中跳过物料号以A开头的记录，物料号: {}", materialNo);
                continue;
            }


            OrderDemandFromSapVO vo = new OrderDemandFromSapVO();
            vo.setRsnum(getString(item, "RSNUM"));
            vo.setBdter(getString(item, "BDTER"));
            vo.setAufnr(getString(item, "AUFNR"));
            vo.setRspos(getString(item, "RSPOS"));
            vo.setMatnr(getString(item, "MATNR"));
            vo.setMaktx(getString(item, "MAKTX"));
            vo.setBdmng(toBigDecimal(item.get("BDMNG")));
            vo.setEnmng(toBigDecimal(item.get("ENMNG")));
            vo.setMeins(getString(item, "MEINS"));
            vo.setXloek(getString(item, "XLOEK"));
            vo.setXwaok(getString(item, "XWAOK"));
            vo.setBwart(getString(item, "BWART"));
            vo.setLgort(getString(item, "LGORT"));
            vo.setSchgt(getString(item, "SCHGT"));
            vo.setDumps(getString(item, "DUMPS"));
            vo.setRssta(getString(item, "RSSTA"));
            vo.setAenam(getString(item, "AENAM"));
            vo.setSanka(getString(item, "SANKA"));
            vo.setAedat(getString(item, "AEDAT"));
            vo.setWerks(getString(item, "WERKS"));
            vo.setSortf(getString(item, "SORTF"));
            list.add(vo);
        }
        return list;
    }

    private String getString(Map<String, Object> map, String key) {
        Object value = map.get(key);
        return value != null ? value.toString().trim() : "";
    }

    private BigDecimal toBigDecimal(Object value) {
        if (value == null) return BigDecimal.ZERO;
        try {
            return new BigDecimal(value.toString());
        } catch (NumberFormatException e) {
            log.warn("无法转换为BigDecimal: {}", value);
            return BigDecimal.ZERO;
        }
    }

}