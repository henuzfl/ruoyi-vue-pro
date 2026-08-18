package cn.iocoder.yudao.module.marketing.service.concreteplan;

import cn.iocoder.yudao.framework.common.exception.ServiceException;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.read.builder.ExcelReaderSheetBuilder;
import com.alibaba.excel.read.metadata.ReadSheet;
import com.baomidou.dynamic.datasource.annotation.DS;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import cn.iocoder.yudao.module.marketing.controller.admin.concreteplan.vo.*;
import cn.iocoder.yudao.module.marketing.dal.dataobject.concreteplan.ConcretePlanDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.marketing.dal.mysql.concreteplan.ConcretePlanMapper;
import org.springframework.web.multipart.MultipartFile;
import com.alibaba.excel.EasyExcel;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import cn.hutool.core.util.IdUtil; // 或 com.baomidou.mybatisplus.core.toolkit.IdWorker
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import cn.iocoder.yudao.framework.tenant.core.context.TenantContextHolder;
import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.Arrays;



import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.marketing.enums.ErrorCodeConstants.*;

/**
 * 混凝土计划需求 Service 实现类
 *
 * @author 管理员
 */
@Service
@DS("oracle") // 指定使用 Oracle 数据源
@Validated
@Slf4j
public class ConcretePlanServiceImpl implements ConcretePlanService {

    @Resource
    private ConcretePlanMapper concretePlanMapper;

    @Override
    public Long createConcretePlan(ConcretePlanSaveReqVO createReqVO) {
        // 插入
        ConcretePlanDO concretePlan = BeanUtils.toBean(createReqVO, ConcretePlanDO.class);
        concretePlanMapper.insert(concretePlan);
        // 返回
        return concretePlan.getId();
    }

    @Override
    public void updateConcretePlan(ConcretePlanSaveReqVO updateReqVO) {
        // 校验存在
        validateConcretePlanExists(updateReqVO.getId());
        // 更新
        ConcretePlanDO updateObj = BeanUtils.toBean(updateReqVO, ConcretePlanDO.class);
        concretePlanMapper.updateById(updateObj);
    }

    @Override
    public void deleteConcretePlan(Long id) {
        // 校验存在
        validateConcretePlanExists(id);
        // 删除
        concretePlanMapper.deleteById(id);
    }

    private void validateConcretePlanExists(Long id) {
        if (concretePlanMapper.selectById(id) == null) {
            throw exception(CONCRETE_PLAN_NOT_EXISTS);
        }
    }

    @Override
    public ConcretePlanDO getConcretePlan(Long id) {
        return concretePlanMapper.selectById(id);
    }

    @Override
    public PageResult<ConcretePlanDO> getConcretePlanPage(ConcretePlanPageReqVO pageReqVO) {
        return concretePlanMapper.selectPage(pageReqVO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void importExcel(MultipartFile file, LocalDate importTime) throws IOException {
        log.info("开始导入混凝土计划需求，文件：{}，导入时间：{}", file.getOriginalFilename(), importTime);

        Set<String> allowedSheetNames = new HashSet<>(Arrays.asList(
                "泵车排产", "布料机排产", "消防臂架", "车载泵", "砼泵", "湿喷机"
        ));

        byte[] fileBytes = file.getBytes();
        List<String> sheetNames = getSheetNames(fileBytes);
        if (sheetNames.isEmpty()) {
            throw new ServiceException(400, "Excel 文件没有找到任何 Sheet");
        }

        LocalDateTime importDateTime = importTime.atStartOfDay();

        // 1. 先删除该导入时间下的所有旧数据（一次性删除）
        int deleted = concretePlanMapper.deleteByImportTime(importDateTime);
        log.info("删除导入时间 {} 的旧数据 {} 条", importDateTime, deleted);

        int totalRecords = 0;
        for (String sheetName : sheetNames) {
            if (!allowedSheetNames.contains(sheetName)) {
                log.info("跳过不在白名单的 Sheet: {}", sheetName);
                continue;
            }
            log.info("处理 Sheet: {}", sheetName);

            // 2. 解析当前 sheet 数据
            List<ConcretePlanDO> sheetData = parseSheet(fileBytes, sheetName, importTime);
            if (sheetData.isEmpty()) {
                log.warn("Sheet {} 无有效数据，跳过", sheetName);
                continue;
            }

            // 3. 保存新数据
            saveBatch(sheetData);
            totalRecords += sheetData.size();
            log.info("Sheet {} 导入完成，共 {} 条", sheetName, sheetData.size());
        }
        log.info("全部导入完成，总记录数：{}", totalRecords);
    }

     /**
     * 获取 Excel 中所有 Sheet 名称
     */
    private List<String> getSheetNames(byte[] fileBytes) throws IOException {
        List<String> names = new ArrayList<>();
        try (InputStream is = new ByteArrayInputStream(fileBytes);
             ExcelReader reader = EasyExcel.read(is).build()) {
            for (ReadSheet sheet : reader.excelExecutor().sheetList()) {
                names.add(sheet.getSheetName());
            }
        }
        return names;
    }

    /**
     * 解析单个 Sheet
     */
    private List<ConcretePlanDO> parseSheet(byte[] fileBytes, String sheetName, LocalDate importTime) throws IOException {
        ConcretePlanImportListener listener = new ConcretePlanImportListener(importTime.atStartOfDay(), sheetName);
        try (InputStream is = new ByteArrayInputStream(fileBytes)) {
            ExcelReaderSheetBuilder sheetBuilder = EasyExcel.read(is, listener).sheet(sheetName);
            // 根据 sheet 名称设置表头行号（1-based）
            int headRowNumber;
            switch (sheetName) {
                case "泵车排产":
                case "布料机排产":
                case "消防臂架":
                    headRowNumber = 2;   // 第2行为表头
                    break;
                case "车载泵":
                case "砼泵":
                    headRowNumber = 3;   // 第3行为表头
                    break;
                case "湿喷机":
                    headRowNumber = 1;   // 第1行为表头
                    break;
                default:
                    headRowNumber = 1;
            }
            sheetBuilder.headRowNumber(headRowNumber).doRead();
            return listener.getDataList();
        }
    }

    /**
     * 批量保存（可继续使用原有的 saveBatch 方法）
     */
    private void saveBatch(List<ConcretePlanDO> batch) {
        if (batch == null || batch.isEmpty()) return;
        // 移除这行：batch.removeIf(entity -> !hasBusinessData(entity));
        String currentUserId = SecurityFrameworkUtils.getLoginUser() != null
                ? String.valueOf(SecurityFrameworkUtils.getLoginUser().getId())
                : "system";
        LocalDateTime now = LocalDateTime.now();
        for (ConcretePlanDO entity : batch) {
            if (entity.getId() == null) {
                entity.setId(IdUtil.getSnowflakeNextId());
            }
            entity.setCreateTime(now);
            entity.setCreator(currentUserId);
            entity.setUpdateTime(now);
            entity.setUpdater(currentUserId);
        }
        concretePlanMapper.insertBatchSomeColumn(batch);
    }


    /**
     * 判断实体是否包含有效业务数据
     * 根据 ConcretePlanDO 的关键业务字段检查
     */
    private boolean hasBusinessData(ConcretePlanDO entity) {
        // 列举您认为必须的业务字段（至少一个非空）
        return entity.getPlanNo() != null
                || entity.getSeqNo() != null
                || entity.getMeter() != null
                || entity.getModelName() != null
                || entity.getMaterialName() != null
                || entity.getMaterialCode() != null
                || entity.getProdNo() != null
                || entity.getOrderNo() != null
                || entity.getBatchNo() != null
                || entity.getStructSerialNo() != null
                || entity.getQuantity() != null;
    }
}