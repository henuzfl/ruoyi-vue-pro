package cn.iocoder.yudao.module.aps.service.assempart;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.io.IOException;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.context.AnalysisContext;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import cn.iocoder.yudao.module.aps.controller.admin.assempart.vo.*;
import cn.iocoder.yudao.module.aps.dal.dataobject.assempart.AssemPartDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.aps.dal.mysql.assempart.AssemPartMapper;
import org.springframework.web.multipart.MultipartFile;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.aps.enums.ErrorCodeConstants.*;

/**
 * 总成与子件关联表管理 Service 实现类
 *
 * @author 柳文
 */
@Service
@DS("oracle") // 指定使用 Oracle 数据源
@Validated
@Slf4j
public class AssemPartServiceImpl implements AssemPartService {

    @Resource
    private AssemPartMapper assemPartMapper;

    @Override
    public Long createAssemPart(AssemPartSaveReqVO createReqVO) {
        AssemPartDO assemPart = BeanUtils.toBean(createReqVO, AssemPartDO.class);
        // 补零
        assemPart.setOrderNo(padZero(assemPart.getOrderNo()));
        assemPart.setComponentOrder(padZero(assemPart.getComponentOrder()));
        assemPartMapper.insert(assemPart);
        return assemPart.getId();
    }

    @Override
    public void updateAssemPart(AssemPartSaveReqVO updateReqVO) {
        validateAssemPartExists(updateReqVO.getId());
        AssemPartDO updateObj = BeanUtils.toBean(updateReqVO, AssemPartDO.class);
        // 补零
        updateObj.setOrderNo(padZero(updateObj.getOrderNo()));
        updateObj.setComponentOrder(padZero(updateObj.getComponentOrder()));
        assemPartMapper.updateById(updateObj);
    }

    @Override
    public void deleteAssemPart(Long id) {
        // 校验存在
        validateAssemPartExists(id);
        // 删除
        assemPartMapper.deleteById(id);
    }

    private void validateAssemPartExists(Long id) {
        if (assemPartMapper.selectById(id) == null) {
            throw exception(ASSEM_PART_NOT_EXISTS);
        }
    }

    @Override
    public AssemPartDO getAssemPart(Long id) {
        return assemPartMapper.selectById(id);
    }

    @Override
    public PageResult<AssemPartDO> getAssemPartPage(AssemPartPageReqVO pageReqVO) {
        return assemPartMapper.selectPage(pageReqVO);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int importAssemPart(MultipartFile file) throws IOException {
        // 读取原始行
        List<Map<Integer, String>> rows = EasyExcel.read(file.getInputStream())
                .sheet(0)
                .headRowNumber(0)
                .doReadSync();

        if (rows == null || rows.size() <= 1) {
            log.warn("导入文件无数据或只有表头");
            return 0;
        }

        // 存储待导入的数据以及用于删除的组合键（订单号#计划日期）
        List<AssemPartDO> importList = new ArrayList<>();
        Set<String> deleteKeys = new HashSet<>();  // 去重，避免重复删除
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");

        for (int i = 1; i < rows.size(); i++) {
            Map<Integer, String> row = rows.get(i);
            String orderNo = row.get(0);        // 总成订单号
            String quantityStr = row.get(1);    // 订单数量
            String scheduleTimeStr = row.get(2); // 总成计划日期
            String componentOrder = row.get(3);  // 零部件订单号
            String allocQtyStr = row.get(4);     // 零部件数量

            if (orderNo == null || orderNo.trim().isEmpty()) {
                log.warn("第{}行总成订单号为空，跳过", i + 1);
                continue;
            }

            // 解析日期并生成组合键
            Date scheduleTime = null;
            String planDateStr = null;
            if (scheduleTimeStr != null && !scheduleTimeStr.trim().isEmpty()) {
                String dateStr = scheduleTimeStr.trim();
                try {
                    // 尝试完整格式 yyyy-MM-dd HH:mm:ss
                    SimpleDateFormat sdfFull = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    scheduleTime = sdfFull.parse(dateStr);
                    planDateStr = sdfDate.format(scheduleTime);
                } catch (ParseException e1) {
                    try {
                        // 尝试短格式 M/d，补年份
                        int currentYear = LocalDate.now().getYear();
                        String normalized = dateStr.replace('/', '-');
                        String fullDateStr = currentYear + "-" + normalized;
                        SimpleDateFormat sdfShort = new SimpleDateFormat("yyyy-M-d");
                        scheduleTime = sdfShort.parse(fullDateStr);
                        planDateStr = sdfDate.format(scheduleTime);
                    } catch (ParseException e2) {
                        log.warn("第{}行日期解析失败: {}", i + 1, dateStr);
                    }
                }
            }

            if (planDateStr != null) {
                String paddedOrderNo = padZero(orderNo.trim());
                deleteKeys.add(paddedOrderNo + "#" + planDateStr);
            }

            // 构建插入实体
            AssemPartDO entity = new AssemPartDO();
            entity.setOrderNo(padZero(orderNo.trim()));
            entity.setQuantity(parseBigDecimal(quantityStr));
            entity.setScheduleTime(scheduleTime);
            entity.setComponentOrder(padZero(componentOrder));
            entity.setAllocQty(parseBigDecimal(allocQtyStr));
            entity.setDeleted(false);          // 新数据未删除
            entity.setCreateTime(LocalDateTime.now());
            entity.setUpdateTime(LocalDateTime.now());

            importList.add(entity);
        }

        if (importList.isEmpty()) {
            log.warn("无有效数据可导入");
            return 0;
        }

        // 1. 物理删除旧数据（根据订单号 + 计划日期）
        int deletedCount = 0;
        for (String key : deleteKeys) {
            String[] parts = key.split("#");
            if (parts.length != 2) continue;
            String orderNo = parts[0];
            String dateStr = parts[1];
            try {
                Date dateOnly = sdfDate.parse(dateStr);
                int deleted = assemPartMapper.physicalDeleteByOrderNoAndDate(orderNo, dateOnly);
                deletedCount += deleted;
                log.info("删除订单号={}, 日期={} 的记录 {} 条", orderNo, dateStr, deleted);
            } catch (ParseException e) {
                log.warn("日期解析失败，无法删除: {}", key);
            }
        }
        log.info("共物理删除 {} 条旧记录", deletedCount);

        // 2. 批量插入新数据
        int insertedCount = 0;
        for (AssemPartDO entity : importList) {
            assemPartMapper.insert(entity);
            insertedCount++;
        }
        log.info("导入完成，共插入 {} 条记录", insertedCount);
        return insertedCount;
    }

    // 辅助方法：安全获取字符串
    private String getString(Object obj) {
        return obj == null ? null : obj.toString().trim();
    }

    // 辅助方法：安全解析 BigDecimal
    private BigDecimal parseBigDecimal(Object obj) {
        if (obj == null) return null;
        try {
            return new BigDecimal(obj.toString().trim());
        } catch (NumberFormatException e) {
            log.warn("数字解析失败: {}", obj);
            return null;
        }
    }

    /**
     * 将 Excel 单元格值安全转换为 java.util.Date
     * 支持 Date、LocalDateTime、String 三种格式
     */
    private Date parseDate(Object obj) {
        if (obj == null) return null;
        if (obj instanceof Date) {
            return (Date) obj;
        } else if (obj instanceof LocalDateTime) {
            return Date.from(((LocalDateTime) obj).atZone(ZoneId.systemDefault()).toInstant());
        } else if (obj instanceof String) {
            String str = ((String) obj).trim();
            if (str.isEmpty()) return null;
            // 支持 yyyy-MM-dd HH:mm:ss 或 yyyy-MM-dd
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                return sdf.parse(str);
            } catch (ParseException e1) {
                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    return sdf2.parse(str);
                } catch (ParseException e2) {
                    log.warn("日期解析失败: {}", str);
                    return null;
                }
            }
        }
        return null;
    }

    /**
     * 将字符串左侧补零至指定长度（默认12位）
     */
    /**
     * 将字符串左侧补零至12位，若输入为 null 则返回 null
     */
    private String padZero(String input) {
        if (input == null) {
            return null;
        }
        String trimmed = input.trim();
        if (trimmed.isEmpty()) {
            return trimmed;  // 或返回 null，根据业务需求
        }
        if (trimmed.length() >= 12) {
            return trimmed;
        }
        // 若为纯数字，按数字补零；否则按字符串左补空格再替换为0
        try {
            return String.format("%012d", new BigDecimal(trimmed).longValue());
        } catch (NumberFormatException e) {
            return String.format("%12s", trimmed).replace(' ', '0');
        }
    }

}