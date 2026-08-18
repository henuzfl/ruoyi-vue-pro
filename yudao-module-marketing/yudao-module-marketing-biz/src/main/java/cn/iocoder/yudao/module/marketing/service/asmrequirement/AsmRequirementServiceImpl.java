package cn.iocoder.yudao.module.marketing.service.asmrequirement;

import cn.iocoder.yudao.framework.common.exception.ServiceException;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;
import java.time.ZoneId;


import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import cn.iocoder.yudao.module.marketing.controller.admin.asmrequirement.vo.*;
import cn.iocoder.yudao.module.marketing.dal.dataobject.asmrequirement.AsmRequirementDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.marketing.dal.mysql.asmrequirement.AsmRequirementMapper;
import org.springframework.web.multipart.MultipartFile;
import com.alibaba.excel.EasyExcel;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;



import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.marketing.enums.ErrorCodeConstants.*;

/**
 * 营销总成需求 Service 实现类
 *
 * @author 柳文
 */
@Service
@DS("oracle") // 指定使用 Oracle 数据源
@Validated
@Slf4j
public class AsmRequirementServiceImpl implements AsmRequirementService {

    @Resource
    private AsmRequirementMapper asmRequirementMapper;

    @Override
    public Long createAsmRequirement(AsmRequirementSaveReqVO createReqVO) {
        // 插入
        AsmRequirementDO asmRequirement = BeanUtils.toBean(createReqVO, AsmRequirementDO.class);
        asmRequirementMapper.insert(asmRequirement);
        // 返回
        return asmRequirement.getId();
    }

    @Override
    public void updateAsmRequirement(AsmRequirementSaveReqVO updateReqVO) {
        // 校验存在
        validateAsmRequirementExists(updateReqVO.getId());
        // 更新
        AsmRequirementDO updateObj = BeanUtils.toBean(updateReqVO, AsmRequirementDO.class);
        asmRequirementMapper.updateById(updateObj);
    }

    @Override
    public void deleteAsmRequirement(Long id) {
        // 校验存在
        validateAsmRequirementExists(id);
        // 删除
        asmRequirementMapper.deleteById(id);
    }

    private void validateAsmRequirementExists(Long id) {
        if (asmRequirementMapper.selectById(id) == null) {
            throw exception(ASM_REQUIREMENT_NOT_EXISTS);
        }
    }

    @Override
    public AsmRequirementDO getAsmRequirement(Long id) {
        return asmRequirementMapper.selectById(id);
    }

    @Override
    public PageResult<AsmRequirementDO> getAsmRequirementPage(AsmRequirementPageReqVO pageReqVO) {
        return asmRequirementMapper.selectPage(pageReqVO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void importExcel(MultipartFile file) throws IOException {
        log.info("开始导入营销总成需求，文件：{}", file.getOriginalFilename());
        ZipSecureFile.setMinInflateRatio(0.001);

        // 读取所有行（包括表头）为 List<Map<Integer, Object>>
        List<Map<Integer, Object>> rows;
        try (InputStream is = file.getInputStream()) {
            rows = EasyExcel.read(is)
                    .headRowNumber(0)
                    .sheet(0)
                    .doReadSync();
        }

        if (rows == null || rows.size() < 2) {
            throw new ServiceException(400, "导入文件无数据");
        }

        // 第一行是表头，从第二行开始解析
        List<AsmRequirementDO> doList = new ArrayList<>();
        for (int i = 1; i < rows.size(); i++) {
            Map<Integer, Object> row = rows.get(i);
            // 列索引: 0=主机单位, 1=车型, 2=总成物料编码, 3=总成物料名称, 4=需求数量, 5=需求日期
            if (row.size() < 6) {
                throw new ServiceException(400, "第 " + (i + 1) + " 行列数不足，请检查格式");
            }

            String hostUnit = row.get(0) == null ? null : row.get(0).toString().trim();
            String vehicleModel = row.get(1) == null ? null : row.get(1).toString().trim();
            String assemblyMaterialNo = row.get(2) == null ? null : row.get(2).toString().trim();
            String mainMaterialDesc = row.get(3) == null ? null : row.get(3).toString().trim();
            String quantityStr = row.get(4) == null ? null : row.get(4).toString().trim();
            Object dateObj = row.get(5);

            // 校验物料编码
            if (assemblyMaterialNo == null || assemblyMaterialNo.isEmpty()) {
                throw new ServiceException(400, "第 " + (i + 1) + " 行总成物料编码不能为空");
            }

            // 校验数量
            BigDecimal quantity;
            try {
                quantity = new BigDecimal(quantityStr);
            } catch (Exception e) {
                throw new ServiceException(400, "第 " + (i + 1) + " 行需求数量格式错误: " + quantityStr);
            }

            // ---------- 日期解析（增强版） ----------
            LocalDate requireDate = null;
            if (dateObj == null) {
                throw new ServiceException(400, "第 " + (i + 1) + " 行需求日期不能为空");
            }

            try {
                // 1. 如果已经是 LocalDate 或 Date，直接转换
                if (dateObj instanceof LocalDate) {
                    requireDate = (LocalDate) dateObj;
                } else if (dateObj instanceof Date) {
                    requireDate = ((Date) dateObj).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                } else {
                    // 2. 转为字符串并处理
                    String dateStr = dateObj.toString().trim();
                    // 去除可能的前后非法字符（如 BOM）
                    dateStr = dateStr.replaceAll("^[\\s\\uFEFF]+|[\\s\\uFEFF]+$", "");
                    log.info("第 {} 行日期字符串：'{}'", i + 1, dateStr);

                    // 尝试多种格式
                    DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy/M/d");
                    DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    DateTimeFormatter formatter3 = DateTimeFormatter.ofPattern("yyyy/M/dd");
                    DateTimeFormatter formatter4 = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                    try {
                        requireDate = LocalDate.parse(dateStr, formatter1);
                    } catch (DateTimeParseException e1) {
                        try {
                            requireDate = LocalDate.parse(dateStr, formatter2);
                        } catch (DateTimeParseException e2) {
                            try {
                                requireDate = LocalDate.parse(dateStr, formatter3);
                            } catch (DateTimeParseException e3) {
                                requireDate = LocalDate.parse(dateStr, formatter4);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                log.error("日期解析失败，原始值：{}", dateObj, e);
                throw new ServiceException(400, "第 " + (i + 1) + " 行需求日期格式错误: " + dateObj);
            }

            // ---------- 组装实体 ----------
            AsmRequirementDO entity = new AsmRequirementDO();
            entity.setId(IdWorker.getId());
            entity.setHostUnit(hostUnit);
            entity.setVehicleModel(vehicleModel);
            entity.setAssemblyMaterialNo(assemblyMaterialNo);
            entity.setMainMaterialDesc(mainMaterialDesc);
            entity.setRequireQuantity(quantity);
            entity.setRequireDate(requireDate.atStartOfDay());

            doList.add(entity);
        }

        // 批量插入（每批 40 条）
        int batchSize = 40;
        for (int i = 0; i < doList.size(); i += batchSize) {
            int end = Math.min(i + batchSize, doList.size());
            List<AsmRequirementDO> subList = doList.subList(i, end);
            asmRequirementMapper.insertBatch(subList);
        }
        log.info("营销总成需求导入完成，共 {} 条", doList.size());
    }

}