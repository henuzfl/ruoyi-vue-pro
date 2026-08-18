package cn.iocoder.yudao.module.marketing.service.concretebom;

import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.alibaba.excel.EasyExcel;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


import cn.iocoder.yudao.module.marketing.controller.admin.concretebom.vo.*;
import cn.iocoder.yudao.module.marketing.dal.dataobject.concretebom.ConcreteBomDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.marketing.dal.mysql.concretebom.ConcreteBomMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.marketing.enums.ErrorCodeConstants.*;

/**
 * 混凝土BOM Service 实现类
 */
@Service
@DS("oracle")
@Validated
@Slf4j
public class ConcreteBomServiceImpl extends ServiceImpl<ConcreteBomMapper, ConcreteBomDO>
        implements ConcreteBomService {

    // 使用 baseMapper 继承自 ServiceImpl，无需再手动注入

    @Override
    public Long createConcreteBom(ConcreteBomSaveReqVO createReqVO) {
        ConcreteBomDO concreteBom = BeanUtils.toBean(createReqVO, ConcreteBomDO.class);
        baseMapper.insert(concreteBom);
        return concreteBom.getId();
    }

    @Override
    public void updateConcreteBom(ConcreteBomSaveReqVO updateReqVO) {
        validateConcreteBomExists(updateReqVO.getId());
        ConcreteBomDO updateObj = BeanUtils.toBean(updateReqVO, ConcreteBomDO.class);
        baseMapper.updateById(updateObj);
    }

    @Override
    public void deleteConcreteBom(Long id) {
        validateConcreteBomExists(id);
        baseMapper.deleteById(id);
    }

    private void validateConcreteBomExists(Long id) {
        if (baseMapper.selectById(id) == null) {
            throw exception(CONCRETE_BOM_NOT_EXISTS);
        }
    }

    @Override
    public ConcreteBomDO getConcreteBom(Long id) {
        return baseMapper.selectById(id);
    }

    @Override
    public PageResult<ConcreteBomDO> getConcreteBomPage(ConcreteBomPageReqVO pageReqVO) {
        return baseMapper.selectPage(pageReqVO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void importExcel(MultipartFile file, LocalDate importTime) throws IOException {
        log.info("开始导入混凝土BOM，文件：{}，导入时间：{}", file.getOriginalFilename(), importTime);
        ZipSecureFile.setMinInflateRatio(0.001);

        List<Map<Integer, String>> rows = EasyExcel.read(file.getInputStream())
                .sheet(0)
                .headRowNumber(0)
                .doReadSync();

        if (rows == null || rows.size() <= 1) {
            log.warn("导入文件无数据");
            return;
        }

        // 解析表头判断模板类型
        Map<Integer, String> headerRow = rows.get(0);
        boolean isNewTemplate = headerRow.containsValue("顶层物料号");
        boolean isOldTemplate = headerRow.containsValue("车型");
        if (!isNewTemplate && !isOldTemplate) {
            throw new ServiceException(400, "无法识别的模板格式");
        }

        // 收集导入文件中的所有车型（去重）
        Set<String> vehicleModelsInFile = new HashSet<>();
        List<ConcreteBomDO> importList = new ArrayList<>();
        LocalDateTime batchTime = importTime.atStartOfDay();

        for (int i = 1; i < rows.size(); i++) {
            Map<Integer, String> row = rows.get(i);
            String vehicleModel = null;
            String cylinderName = null;
            String sbpCode = null;
            String config = null;

            if (isNewTemplate) {
                vehicleModel = row.get(3);
                cylinderName = row.get(2);
                sbpCode = row.get(1);
                config = row.get(5);
            } else {
                vehicleModel = row.get(0);
                cylinderName = row.get(1);
                sbpCode = row.get(2);
                config = row.get(3);
            }

            if (vehicleModel == null || vehicleModel.trim().isEmpty() ||
                    cylinderName == null || cylinderName.trim().isEmpty()) {
                log.warn("跳过无效行：{}", row);
                continue;
            }

            vehicleModelsInFile.add(vehicleModel.trim());

            ConcreteBomDO entity = new ConcreteBomDO();
            entity.setVehicleModel(vehicleModel.trim());
            entity.setCylinderName(cylinderName.trim());
            entity.setSbpCode(sbpCode != null ? sbpCode.trim() : null);
            entity.setConfig(config != null ? config.trim() : null);
            entity.setImportTime(batchTime);
            entity.setDeleted(false);   // 新数据未删除
            importList.add(entity);
        }

        if (importList.isEmpty()) {
            log.info("无有效数据可导入");
            return;
        }

        // 1. 对于导入文件中出现的每个车型，先将该车型下所有未删除的记录标记为删除（delete_flag=1）
        for (String vehicleModel : vehicleModelsInFile) {
            LambdaUpdateWrapper<ConcreteBomDO> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(ConcreteBomDO::getVehicleModel, vehicleModel)
                    .eq(ConcreteBomDO::getDeleted, false)
                    .set(ConcreteBomDO::getDeleted, true);
            baseMapper.update(null, updateWrapper);
        }
        log.info("已标记 {} 个车型的历史记录为删除", vehicleModelsInFile.size());

        // 2. 批量插入新数据（delete_flag=0）
        saveBatch(importList, 500);
        log.info("导入完成，共插入 {} 条记录", importList.size());
    }

    @Override
    public List<ConcreteBomCompareRespVO> compareDifference() {
        List<Map<String, Object>> rows = baseMapper.compareLatestWithPrevious();
        if (rows == null) {
            return Collections.emptyList();
        }

        // 可选：打印第一行的 key 用于调试
        if (!rows.isEmpty()) {
            log.info("对比返回的第一行字段: {}", rows.get(0).keySet());
        }

        return rows.stream().map(row -> {
            ConcreteBomCompareRespVO vo = new ConcreteBomCompareRespVO();
            vo.setVehicleModel((String) row.get("VEHICLE_MODEL"));
            vo.setSbpCode((String) row.get("SBP_CODE"));
            vo.setCurrentConfig((String) row.get("CURRENT_CONFIG"));
            vo.setPreviousConfig((String) row.get("PREVIOUS_CONFIG"));
            vo.setState((String) row.get("STATE"));
            return vo;
        }).collect(Collectors.toList());
    }
}