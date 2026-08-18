package cn.iocoder.yudao.module.buyer.service.productiontransfer;

import com.baomidou.dynamic.datasource.annotation.DS;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import cn.iocoder.yudao.module.buyer.controller.admin.productiontransfer.vo.*;
import cn.iocoder.yudao.module.buyer.dal.dataobject.productiontransfer.ProductionTransferDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.buyer.dal.mysql.productiontransfer.ProductionTransferMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.buyer.enums.ErrorCodeConstants.*;
import org.springframework.util.StringUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import cn.iocoder.yudao.framework.common.exception.ServiceException;
import static cn.iocoder.yudao.module.buyer.enums.ErrorCodeConstants.PRODUCTION_TRANSFER_SYNC_ERROR;


/**
 * MES转序单信息 Service 实现类
 *
 * @author 柳文
 */
@Service
@DS("oracle") // 指定使用 Oracle 数据源
@Validated
@Slf4j
public class ProductionTransferServiceImpl implements ProductionTransferService {

    @Resource
    private ProductionTransferMapper productionTransferMapper;

    @Resource
    private RestTemplate restTemplate;

    private static final String MES_SYNC_URL = "http://mom-interplat-transfer.mesprd.zoomlion.com/plat/originalRequest/ZL/zvmes-ptd/zvmes-ptd-queryTrans/noroute";

    @Override
    public BigDecimal createProductionTransfer(ProductionTransferSaveReqVO createReqVO) {
        // 插入
        ProductionTransferDO productionTransfer = BeanUtils.toBean(createReqVO, ProductionTransferDO.class);
        productionTransferMapper.insert(productionTransfer);
        // 返回
        return productionTransfer.getId();
    }

    @Override
    public void updateProductionTransfer(ProductionTransferSaveReqVO updateReqVO) {
        // 校验存在
        validateProductionTransferExists(updateReqVO.getId());
        // 更新
        ProductionTransferDO updateObj = BeanUtils.toBean(updateReqVO, ProductionTransferDO.class);
        productionTransferMapper.updateById(updateObj);
    }

    @Override
    public void deleteProductionTransfer(BigDecimal id) {
        // 校验存在
        validateProductionTransferExists(id);
        // 删除
        productionTransferMapper.deleteById(id);
    }

    private void validateProductionTransferExists(BigDecimal id) {
        if (productionTransferMapper.selectById(id) == null) {
            throw exception(PRODUCTION_TRANSFER_NOT_EXISTS);
        }
    }

    @Override
    public ProductionTransferDO getProductionTransfer(BigDecimal id) {
        return productionTransferMapper.selectById(id);
    }

    @Override
    public PageResult<ProductionTransferDO> getProductionTransferPage(ProductionTransferPageReqVO pageReqVO) {
        return productionTransferMapper.selectPage(pageReqVO);
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int importProductionTransfer(List<ProductionTransferImportReqVO> importVOList) {
        if (CollectionUtils.isEmpty(importVOList)) {
            return 0;
        }

        // 1. 内部去重（按转序单号+计划批次），同时收集组合列表用于后续删除
        Set<String> keySet = new HashSet<>();
        List<ProductionTransferDO> saveList = new ArrayList<>();
        List<Map<String, String>> deleteKeys = new ArrayList<>();

        for (ProductionTransferImportReqVO vo : importVOList) {
            // 跳过关键字段为空
            if (!StringUtils.hasText(vo.getTransferNo()) || !StringUtils.hasText(vo.getBatchNo())) {
                log.warn("跳过转序单号或计划批次为空的记录: transferNo={}, batchNo={}", vo.getTransferNo(), vo.getBatchNo());
                continue;
            }
            String key = vo.getTransferNo() + "||" + vo.getBatchNo();
            if (keySet.contains(key)) {
                log.warn("重复记录已跳过: transferNo={}, batchNo={}", vo.getTransferNo(), vo.getBatchNo());
                continue;
            }
            keySet.add(key);

            // 记录需要删除的键
            Map<String, String> deleteKey = new HashMap<>();
            deleteKey.put("transferNo", vo.getTransferNo());
            deleteKey.put("batchNo", vo.getBatchNo());
            deleteKeys.add(deleteKey);

            ProductionTransferDO entity = BeanUtils.toBean(vo, ProductionTransferDO.class);
            saveList.add(entity);
        }

        if (saveList.isEmpty()) {
            return 0;
        }

        // 2. 物理删除这些转序单号+计划批次的现有数据
        int deletedCount = batchDeleteByTransferNosAndBatchNos(deleteKeys);
        log.info("已物理删除 {} 条旧数据", deletedCount);

        // 3. 设置创建时间和更新时间
        LocalDateTime now = LocalDateTime.now();
        for (ProductionTransferDO entity : saveList) {
            entity.setCreateTime(now);
            entity.setUpdateTime(now);
        }

        // 4. 分批插入
        int batchSize = 30;
        int totalInserted = 0;
        for (int i = 0; i < saveList.size(); i += batchSize) {
            int end = Math.min(i + batchSize, saveList.size());
            List<ProductionTransferDO> batchList = saveList.subList(i, end);
            productionTransferMapper.batchInsert(batchList);
            totalInserted += batchList.size();
        }

        log.info("MES转序单信息导入完成，共插入 {} 条，删除 {} 条", totalInserted, deletedCount);
        return totalInserted;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int syncFromMes(MesSyncReqVO syncReqVO) {
        log.info("开始同步MES数据，参数：{}", syncReqVO);

        // 1. 构造请求体
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("plantNo", syncReqVO.getPlantNo());
        requestBody.put("beginTime", syncReqVO.getBeginTime());
        requestBody.put("endTime", syncReqVO.getEndTime());
        requestBody.put("plannerName", StringUtils.hasText(syncReqVO.getPlannerName()) ? syncReqVO.getPlannerName() : "");
        requestBody.put("orderNo", StringUtils.hasText(syncReqVO.getOrderNo()) ? syncReqVO.getOrderNo() : "");

        // 2. 调用外部接口
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response;
        try {
            response = restTemplate.postForEntity(MES_SYNC_URL, entity, String.class);
        } catch (Exception e) {
            log.error("调用MES接口失败", e);
            throw exception(PRODUCTION_TRANSFER_SYNC_ERROR, "调用MES接口失败：" + e.getMessage());
        }

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw exception(PRODUCTION_TRANSFER_SYNC_ERROR, "MES接口返回异常，状态码：" + response.getStatusCode());
        }

        // 3. 解析响应数据
        String responseBody = response.getBody();
        JSONObject jsonObject = JSONObject.parseObject(responseBody);

        // 兼容两种返回格式：直接返回数组 或 包含 code/data 的结构
        JSONArray dataArray = null;
        if (jsonObject.containsKey("code") && jsonObject.containsKey("data")) {
            Integer code = jsonObject.getInteger("code");
            if (code == null || code != 200) {
                String msg = jsonObject.getString("msg");
                throw exception(PRODUCTION_TRANSFER_SYNC_ERROR, "MES接口业务错误：" + (msg != null ? msg : "未知错误"));
            }
            dataArray = jsonObject.getJSONArray("data");
        } else {
            dataArray = JSONObject.parseArray(responseBody);
        }

        if (dataArray == null || dataArray.isEmpty()) {
            log.info("MES接口返回数据为空");
            return 0;
        }

        // 4. 转换为 ImportReqVO 列表（字段映射）
        List<ProductionTransferImportReqVO> importList = new ArrayList<>();
        for (int i = 0; i < dataArray.size(); i++) {
            JSONObject item = dataArray.getJSONObject(i);
            ProductionTransferImportReqVO vo = new ProductionTransferImportReqVO();

            vo.setTransferNo(item.getString("transNo"));
            vo.setOrderNo(item.getString("orderNo"));
            vo.setMaterialCode(item.getString("matnr"));
            vo.setMaterialDesc(item.getString("maktx"));
            vo.setProductionScheduler(item.getString("plannerName"));
            vo.setTransferInitiator(item.getString("createName"));
            vo.setBatchNo(item.getString("batchNo"));
            vo.setSigner(item.getString("receiveName"));

            BigDecimal qty = item.getBigDecimal("qty");
            vo.setQuantity(qty);

            String createdAt = item.getString("createdAt");
            if (StringUtils.hasText(createdAt)) {
                try {
                    LocalDateTime initiatorDate = LocalDateTime.parse(createdAt, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    vo.setInitiatorDate(initiatorDate);
                } catch (DateTimeParseException e) {
                    log.warn("发起日期解析失败: {}", createdAt);
                }
            }

            String receiveTime = item.getString("receiveTime");
            if (StringUtils.hasText(receiveTime)) {
                try {
                    LocalDateTime signTime = LocalDateTime.parse(receiveTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    vo.setSignTime(signTime);
                } catch (DateTimeParseException e) {
                    log.warn("签收时间解析失败: {}", receiveTime);
                }
            }

            if (!StringUtils.hasText(vo.getTransferNo())) {
                log.warn("MES返回数据中转序单号为空，跳过：{}", item);
                continue;
            }
            importList.add(vo);
        }

        if (importList.isEmpty()) {
            log.info("有效数据为空");
            return 0;
        }

        // 5. 复用导入逻辑
        int insertedCount = this.importProductionTransfer(importList);
        log.info("同步MES完成，共插入 {} 条", insertedCount);
        return insertedCount;
    }

    /**
     * 根据转序单号列表批量物理删除
     */
    private int batchDeleteByTransferNos(Collection<String> transferNos) {
        if (CollectionUtils.isEmpty(transferNos)) {
            return 0;
        }
        List<String> list = new ArrayList<>(transferNos);
        int totalDeleted = 0;
        int batchSize = 30; // Oracle IN 子句限制
        for (int i = 0; i < list.size(); i += batchSize) {
            List<String> batch = list.subList(i, Math.min(i + batchSize, list.size()));
            totalDeleted += productionTransferMapper.deleteByTransferNos(batch);
        }
        return totalDeleted;
    }

    /**
     * 根据转序单号+计划批次列表批量物理删除
     */
    private int batchDeleteByTransferNosAndBatchNos(List<Map<String, String>> keys) {
        if (CollectionUtils.isEmpty(keys)) {
            return 0;
        }
        int totalDeleted = 0;
        int batchSize = 30; // Oracle IN 子句限制
        for (int i = 0; i < keys.size(); i += batchSize) {
            int end = Math.min(i + batchSize, keys.size());
            List<Map<String, String>> batch = keys.subList(i, end);
            totalDeleted += productionTransferMapper.deleteByTransferNosAndBatchNos(batch);
        }
        return totalDeleted;
    }

}