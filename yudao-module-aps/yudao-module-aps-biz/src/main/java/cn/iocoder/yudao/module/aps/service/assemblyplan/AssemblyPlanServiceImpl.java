package cn.iocoder.yudao.module.aps.service.assemblyplan;

import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.aps.controller.admin.assemblyplan.vo.AssemblyPlanPageReqVO;
import cn.iocoder.yudao.module.aps.controller.admin.assemblyplan.vo.AssemblyPlanSaveReqVO;
import cn.iocoder.yudao.module.aps.dal.dataobject.assemblyplan.AssemblyPlanDO;
import cn.iocoder.yudao.module.aps.dal.mysql.assemblyplan.AssemblyPlanMapper;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAccessor;
import java.util.*;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.aps.enums.ErrorCodeConstants.ASSEMBLY_PLAN_NOT_EXISTS;

@Service
@DS("oracle")
@Validated
@Slf4j
public class AssemblyPlanServiceImpl implements AssemblyPlanService {

    private static final Validator VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();

    @Resource
    private AssemblyPlanMapper assemblyPlanMapper;   // 移到类顶部，确保注入

    // ========== 导入功能 ==========

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void importExcel(MultipartFile file, LocalDate importTime) throws IOException {
        log.info("开始导入各车间开装计划，文件：{}，导入时间：{}", file.getOriginalFilename(), importTime);

        List<Map<Integer, String>> rows;
        try (InputStream is = file.getInputStream()) {
            rows = EasyExcel.read(is).sheet(0).doReadSync();
//            log.info("读取到的总行数（包含表头）：{}", rows.size());
//            log.info("表头行内容：{}", rows.get(0));
        }


        if (rows.size() < 2) {
            throw new ServiceException(400, "Excel 至少需要表头行和一行数据");
        }

        // 假设第一行为表头行
        // 动态查找表头行（包含“订单号”或“物料编号”关键字的行）
        int headerRowIdx = -1;
        for (int i = 0; i < Math.min(rows.size(), 10); i++) {
            Map<Integer, String> row = rows.get(i);
            if (row.values().stream().anyMatch(v -> v != null &&
                    (v.contains("订单号") || v.contains("物料编号") || v.contains("排产时间")))) {
                headerRowIdx = i;
                log.info("找到表头行，索引：{}", headerRowIdx);
                break;
            }
        }
        if (headerRowIdx == -1) {
            // 没有找到表头行，则认为第一行就是数据行
            headerRowIdx = -1;
            log.warn("未找到表头行，将第一行视为数据行");
        }
        List<AssemblyPlanDO> successList = new ArrayList<>();
        List<Map<String, Object>> deleteKeys = new ArrayList<>();
        List<String> errorMessages = new ArrayList<>();

        int startRow = headerRowIdx == -1 ? 0 : headerRowIdx + 1;
        if (startRow >= rows.size()) {
            throw new ServiceException(400, "没有数据行");
        }
        for (int i = startRow; i < rows.size(); i++) {   // 注意：不再 +1

            Map<Integer, String> row = rows.get(i);
//            log.info("第 {} 行原始数据（列索引->值）：{}", i+1, row);
            int rowNum = i + 1;

            if (row.values().stream().allMatch(v -> v == null || v.trim().isEmpty())) {
                log.warn("第 {} 行被判定为空行，原始数据：{}", i+1, row);
                continue;
            }

//            log.info("正在处理第 {} 行", rowNum);
            String orderNo = getCellValue(row, 0);
            String materialCode = getCellValue(row, 1);
            String materialDesc = getCellValue(row, 2);
            String assemblyQuantityStr = getCellValue(row, 3);
            String assembledQuantityStr = getCellValue(row, 4);
            String scheduleTimeStr = getCellValue(row, 5);
            String workshop = getCellValue(row, 6);

//            log.info("第 {} 行提取数据：订单号={}, 物料编号={}, 物料描述={}, 装配数量={}, 已装配数量={}, 排产时间={}, 车间={}",
//                    rowNum, orderNo, materialCode, materialDesc, assemblyQuantityStr, assembledQuantityStr, scheduleTimeStr, workshop);

            List<String> rowErrors = new ArrayList<>();

            // 必填校验
            if (isBlank(orderNo)) rowErrors.add("订单号不能为空");
            if (isBlank(materialCode)) rowErrors.add("物料编号不能为空");
            if (isBlank(assemblyQuantityStr)) rowErrors.add("装配数量不能为空");
            if (isBlank(scheduleTimeStr)) rowErrors.add("排产时间不能为空");

            // 声明变量并赋予默认值，解决编译未初始化问题
            Long assemblyQuantity = null;
            Long assembledQuantity = null;
            LocalDateTime scheduleTime = null;
            if (!isBlank(scheduleTimeStr)) {
                String str = scheduleTimeStr.trim();
                // 定义多种可能的日期格式
                List<DateTimeFormatter> formatters = Arrays.asList(
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),
                        DateTimeFormatter.ofPattern("yyyy-MM-dd"),
                        DateTimeFormatter.ofPattern("yyyy/M/d HH:mm:ss"),
                        DateTimeFormatter.ofPattern("yyyy/M/d"),
                        DateTimeFormatter.ofPattern("yyyy/MM/dd"),
                        DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"),
                        DateTimeFormatter.ofPattern("yyyy-M-d"),
                        DateTimeFormatter.ofPattern("yyyy-M-d H:m:s")
                );
                boolean parsed = false;
                for (DateTimeFormatter fmt : formatters) {
                    try {
                        TemporalAccessor temporal = fmt.parseBest(str, LocalDateTime::from, LocalDate::from);
                        if (temporal instanceof LocalDateTime) {
                            scheduleTime = (LocalDateTime) temporal;
                        } else if (temporal instanceof LocalDate) {
                            scheduleTime = ((LocalDate) temporal).atStartOfDay();
                        }
                        parsed = true;
                        break;
                    } catch (DateTimeParseException ignored) {
                    }
                }
                if (!parsed) {
                    rowErrors.add("排产时间格式错误，支持格式：yyyy-MM-dd HH:mm:ss, yyyy-MM-dd, yyyy/M/d 等");
                }
            }

            // 解析装配数量
            if (!isBlank(assemblyQuantityStr)) {
                try {
                    assemblyQuantity = Long.parseLong(assemblyQuantityStr.trim());
                    if (assemblyQuantity <= 0) {
                        rowErrors.add("装配数量必须大于0");
                    }
                } catch (NumberFormatException e) {
                    rowErrors.add("装配数量必须是整数");
                }
            }

            // 解析已装配数量（可选）
            if (!isBlank(assembledQuantityStr)) {
                try {
                    assembledQuantity = Long.parseLong(assembledQuantityStr.trim());
                    if (assembledQuantity < 0) {
                        rowErrors.add("已装配数量不能为负数");
                    }
                } catch (NumberFormatException e) {
                    rowErrors.add("已装配数量必须是整数");
                }
            } else {
                assembledQuantity = 0L;  // 默认0
            }



            // 如果必填的 assemblyQuantity 因解析失败而仍为 null，补充错误
            if (assemblyQuantity == null && !rowErrors.stream().anyMatch(e -> e.contains("装配数量"))) {
                rowErrors.add("装配数量解析失败");
            }
            if (scheduleTime == null && !rowErrors.stream().anyMatch(e -> e.contains("排产时间"))) {
                rowErrors.add("排产时间解析失败");
            }

            if (!rowErrors.isEmpty()) {
//                log.warn("第 {} 行校验失败：{}", rowNum, rowErrors);
                errorMessages.add(String.format("第%d行：%s", rowNum, String.join(",", rowErrors)));
                continue;
            } else {
            log.info("第 {} 行解析成功，订单号：{}", rowNum, orderNo);
        }

            // 订单号前补零到12位
            if (StringUtils.isNotBlank(orderNo) && orderNo.length() < 12) {
                orderNo = StringUtils.leftPad(orderNo, 12, '0');
            }
            // 构建 DO
            AssemblyPlanDO plan = new AssemblyPlanDO();
            plan.setId(IdWorker.getId());
            plan.setOrderNo(orderNo);
            plan.setMaterialCode(materialCode);
            plan.setMaterialDesc(materialDesc);
            plan.setAssemblyQuantity(assemblyQuantity);
            plan.setAssembledQuantity(assembledQuantity);
            plan.setScheduleTime(scheduleTime);
            plan.setWorkshop(workshop);
            plan.setImportTime(importTime.atStartOfDay());

            successList.add(plan);

            // 记录唯一键（用于删除旧数据）
            Map<String, Object> key = new HashMap<>();
            key.put("orderNo", orderNo);
            key.put("scheduleTime", scheduleTime);
            key.put("workshop", workshop);
            deleteKeys.add(key);

            log.info("第 {} 行解析成功，订单号：{}", rowNum, orderNo);
        }

        // 先根据唯一键删除数据库中已存在的旧记录
        if (!deleteKeys.isEmpty()) {
            log.info("开始删除 {} 条重复记录（基于订单号+排产时间+车间）", deleteKeys.size());
            for (Map<String, Object> key : deleteKeys) {
                String orderNo = (String) key.get("orderNo");
                LocalDateTime scheduleTime = (LocalDateTime) key.get("scheduleTime");
                String workshop = (String) key.get("workshop");
                int deleted = assemblyPlanMapper.deleteByUniqueKey(orderNo, scheduleTime, workshop);
                if (deleted > 0) {
                    log.debug("删除记录：订单号={}, 排产时间={}, 车间={}", orderNo, scheduleTime, workshop);
                }
            }
            log.info("删除完成");
        }

        if (!successList.isEmpty()) {
            assemblyPlanMapper.insertBatch(successList);
            log.info("成功导入{}条记录", successList.size());
        }
        if (!errorMessages.isEmpty()) {
            throw new ServiceException(400, "导入失败：" + String.join("；", errorMessages));
        }
    }

    private String getCellValue(Map<Integer, String> row, int index) {
        String value = row.get(index);
        return value == null ? null : value.trim();
    }

    private boolean isBlank(String str) {
        return str == null || str.isEmpty();
    }

    /**
     * Excel 导入专用 VO
     */
//    @Data
//    public static class AssemblyPlanImportVO {
//        @ExcelProperty(index = 0)  // 第1列：订单号
//        @NotBlank(message = "订单号不能为空")
//        private String orderNo;
//
//        @ExcelProperty(index = 1)  // 第2列：物料编号
//        @NotBlank(message = "物料编号不能为空")
//        private String materialCode;
//
//        @ExcelProperty(index = 2)  // 第3列：物料描述
//        private String materialDesc;
//
//        @ExcelProperty(index = 3)  // 第4列：装配数量
//        @NotNull(message = "装配数量不能为空")
//        @Min(value = 1, message = "装配数量必须大于0")
//        private Long assemblyQuantity;
//
//        @ExcelProperty(index = 4)  // 第5列：已装配数量
//        @Min(value = 0, message = "已装配数量不能为负数")
//        private Long assembledQuantity;
//
//        @ExcelProperty(index = 5)  // 第6列：排产时间
//        @NotBlank(message = "排产时间不能为空")
//        private String scheduleTimeStr;   // 先以字符串接收
//
//        @ExcelProperty(index = 6)  // 第7列：车间
//        private String workshop;
//    }

    @Override
    public Long createAssemblyPlan(AssemblyPlanSaveReqVO createReqVO) {
        // 插入
        AssemblyPlanDO assemblyPlan = BeanUtils.toBean(createReqVO, AssemblyPlanDO.class);
        assemblyPlanMapper.insert(assemblyPlan);
        // 返回
        return assemblyPlan.getId();
    }

    @Override
    public void updateAssemblyPlan(AssemblyPlanSaveReqVO updateReqVO) {
        // 校验存在
        validateAssemblyPlanExists(updateReqVO.getId());
        // 更新
        AssemblyPlanDO updateObj = BeanUtils.toBean(updateReqVO, AssemblyPlanDO.class);
        assemblyPlanMapper.updateById(updateObj);
    }

    @Override
    public void deleteAssemblyPlan(Long id) {
        // 校验存在
        validateAssemblyPlanExists(id);
        // 删除
        assemblyPlanMapper.deleteById(id);
    }

    private void validateAssemblyPlanExists(Long id) {
        if (assemblyPlanMapper.selectById(id) == null) {
            throw exception(ASSEMBLY_PLAN_NOT_EXISTS);
        }
    }

    @Override
    public AssemblyPlanDO getAssemblyPlan(Long id) {
        return assemblyPlanMapper.selectById(id);
    }

    @Override
    public PageResult<AssemblyPlanDO> getAssemblyPlanPage(AssemblyPlanPageReqVO pageReqVO) {
        return assemblyPlanMapper.selectPage(pageReqVO);
    }

}