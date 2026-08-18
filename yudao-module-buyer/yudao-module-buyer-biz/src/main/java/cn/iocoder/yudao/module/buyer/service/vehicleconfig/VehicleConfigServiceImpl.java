package cn.iocoder.yudao.module.buyer.service.vehicleconfig;

import com.baomidou.dynamic.datasource.annotation.DS;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.*;
import cn.iocoder.yudao.module.buyer.controller.admin.vehicleconfig.vo.*;
import cn.iocoder.yudao.module.buyer.dal.dataobject.vehicleconfig.VehicleConfigDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.buyer.dal.mysql.vehicleconfig.VehicleConfigMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.buyer.enums.ErrorCodeConstants.*;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;



/**
 * 主机车型配置 Service 实现类
 *
 * @author 柳文
 */
@Service
@DS("oracle") // 指定使用 Oracle 数据源
@Validated
@Slf4j
public class VehicleConfigServiceImpl implements VehicleConfigService {

    @Resource
    private VehicleConfigMapper vehicleConfigMapper;

    @Override
    public BigDecimal createVehicleConfig(VehicleConfigSaveReqVO createReqVO) {
        // 插入
        VehicleConfigDO vehicleConfig = BeanUtils.toBean(createReqVO, VehicleConfigDO.class);
        vehicleConfigMapper.insert(vehicleConfig);
        // 返回
        return vehicleConfig.getId();
    }

    @Override
    public void updateVehicleConfig(VehicleConfigSaveReqVO updateReqVO) {
        // 校验存在
        validateVehicleConfigExists(updateReqVO.getId());
        // 更新
        VehicleConfigDO updateObj = BeanUtils.toBean(updateReqVO, VehicleConfigDO.class);
        vehicleConfigMapper.updateById(updateObj);
    }

    @Override
    public void deleteVehicleConfig(BigDecimal id) {
        // 校验存在
        validateVehicleConfigExists(id);
        // 删除
        vehicleConfigMapper.deleteById(id);
    }

    private void validateVehicleConfigExists(BigDecimal id) {
        if (vehicleConfigMapper.selectById(id) == null) {
            throw exception(VEHICLE_CONFIG_NOT_EXISTS);
        }
    }

    @Override
    public VehicleConfigDO getVehicleConfig(BigDecimal id) {
        return vehicleConfigMapper.selectById(id);
    }

    @Override
    public PageResult<VehicleConfigDO> getVehicleConfigPage(VehicleConfigPageReqVO pageReqVO) {
        return vehicleConfigMapper.selectPage(pageReqVO);
    }

    /**
     * 导入
     * @param importVOList
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int importVehicleConfig(List<VehicleConfigImportReqVO> importVOList) {
        if (CollectionUtils.isEmpty(importVOList)) {
            return 0;
        }

        // 1. 确定导入日期
        String firstImportDateStr = importVOList.get(0).getImportDate();
        Date importDate;
        if (StringUtils.hasText(firstImportDateStr)) {
            try {
                // 解析字符串为 LocalDate，再转为 java.sql.Date
                LocalDate localDate = LocalDate.parse(firstImportDateStr, DateTimeFormatter.ofPattern("yyyy-M-d"));
                importDate = java.sql.Date.valueOf(localDate);
            } catch (DateTimeParseException e) {
                log.error("导入日期格式错误: {}", firstImportDateStr, e);
                throw new IllegalArgumentException("导入日期格式应为 yyyy-M-d，例如 2026-3-15");
            }
        } else {
            // 为空则使用当前日期
            importDate = java.sql.Date.valueOf(LocalDate.now());
        }

        // 2. 删除该日期的旧数据
        int deletedCount = vehicleConfigMapper.deleteByImportDate(importDate);
        log.info("已删除导入日期 {} 的旧数据 {} 条", importDate, deletedCount);

        // 3. 去重并构建保存列表
        Set<String> uniqueKeySet = new HashSet<>();
        List<VehicleConfigDO> saveList = new ArrayList<>();
        for (VehicleConfigImportReqVO vo : importVOList) {
            String uniqueKey = vo.getVehicleModel() + "|" + vo.getSeqNo2026() + "|" + vo.getMaterialNo();
//            if (uniqueKeySet.contains(uniqueKey)) {
//                log.warn("重复的车型+顺序号+物料号已跳过：{}", uniqueKey);
//                continue;
//            }
            uniqueKeySet.add(uniqueKey);

            VehicleConfigDO entity = new VehicleConfigDO();
            // 使用统一的导入日期
            entity.setImportDate(importDate);
            entity.setOrderNo(vo.getOrderNo());
            entity.setVehicleModel(vo.getVehicleModel());
            entity.setSeqNo2025(vo.getSeqNo2025());
            entity.setSeqNo2026(vo.getSeqNo2026());
            entity.setRequiredArrivalTime(vo.getRequiredArrivalTime());
            entity.setMaterialDesc(vo.getMaterialDesc());
            entity.setQuota1(vo.getQuota1());
            entity.setQuota2(vo.getQuota2());
            entity.setMaterialNo(vo.getMaterialNo());
            entity.setFactory(vo.getFactory());
            entity.setRequiredQuantity(vo.getRequiredQuantity());
            entity.setDeliveredQuantity(vo.getDeliveredQuantity());

            saveList.add(entity);
        }

        if (saveList.isEmpty()) {
            return 0;
        }

        // 4. 设置创建/更新时间
        LocalDateTime now = LocalDateTime.now();
        for (VehicleConfigDO entity : saveList) {
            entity.setCreateTime(now);
            entity.setUpdateTime(now);
        }

        // 5. 分批插入
        int batchSize = 30;
        int totalInserted = 0;
        for (int i = 0; i < saveList.size(); i += batchSize) {
            int end = Math.min(i + batchSize, saveList.size());
            List<VehicleConfigDO> batchList = saveList.subList(i, end);
            vehicleConfigMapper.batchInsert(batchList);
            totalInserted += batchList.size();
        }

        log.info("主机车型配置导入完成，共插入 {} 条", totalInserted);
        return totalInserted;
    }
}