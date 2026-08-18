package cn.iocoder.yudao.module.buyer.service.vehicleplan;

import com.baomidou.dynamic.datasource.annotation.DS;
import lombok.extern.slf4j.Slf4j;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import cn.iocoder.yudao.module.buyer.controller.admin.vehicleplan.vo.*;
import cn.iocoder.yudao.module.buyer.dal.dataobject.vehicleplan.VehiclePlanDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.buyer.dal.mysql.vehicleplan.VehiclePlanMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.buyer.enums.ErrorCodeConstants.*;
import org.springframework.util.StringUtils;
import org.springframework.util.CollectionUtils;

/**
 * 买家车辆营销计划表（主机厂计划） Service 实现类
 *
 * @author 柳文
 */
@Service
@DS("oracle") // 指定使用 Oracle 数据源
@Validated
@Slf4j
public class VehiclePlanServiceImpl implements VehiclePlanService {

    @Resource
    private VehiclePlanMapper vehiclePlanMapper;

    @Override
    public BigDecimal createVehiclePlan(VehiclePlanSaveReqVO createReqVO) {
        // 插入
        VehiclePlanDO vehiclePlan = BeanUtils.toBean(createReqVO, VehiclePlanDO.class);
        vehiclePlanMapper.insert(vehiclePlan);
        // 返回
        return vehiclePlan.getId();
    }

    @Override
    public void updateVehiclePlan(VehiclePlanSaveReqVO updateReqVO) {
        // 校验存在
        validateVehiclePlanExists(updateReqVO.getId());
        // 更新
        VehiclePlanDO updateObj = BeanUtils.toBean(updateReqVO, VehiclePlanDO.class);
        vehiclePlanMapper.updateById(updateObj);
    }

    @Override
    public void deleteVehiclePlan(String id) {
        // 校验存在（这里注意：如果已经逻辑删除，selectById 会查不到，所以要允许查询逻辑删除的记录，或者直接物理删除不校验）
        // 方案一：不校验直接物理删除（如果 id 不存在，影响行数为 0，不报错）
        // 方案二：先查询（包含已逻辑删除的记录），存在再物理删除
        // 推荐方案二，保持业务语义：如果记录不存在或已逻辑删除，都抛异常

        // 自定义查询（忽略逻辑删除条件）
        VehiclePlanDO vehiclePlan = vehiclePlanMapper.selectById(id); // 默认查询 deleted=0
        if (vehiclePlan == null) {
            throw exception(VEHICLE_PLAN_NOT_EXISTS);
        }
        // 物理删除
        int rows = vehiclePlanMapper.physicalDeleteById(id);
        if (rows == 0) {
            throw exception(VEHICLE_PLAN_NOT_EXISTS);
        }
    }

    private void validateVehiclePlanExists(BigDecimal id) {
        if (vehiclePlanMapper.selectById(id) == null) {
            throw exception(VEHICLE_PLAN_NOT_EXISTS);
        }
    }

    @Override
    public VehiclePlanDO getVehiclePlan(BigDecimal id) {
        return vehiclePlanMapper.selectById(id);
    }

    @Override
    public PageResult<VehiclePlanDO> getVehiclePlanPage(VehiclePlanPageReqVO pageReqVO) {
        return vehiclePlanMapper.selectPage(pageReqVO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int importVehiclePlan(List<VehiclePlanImportReqVO> importVOList) {
        if (CollectionUtils.isEmpty(importVOList)) {
            return 0;
        }

        // 1. 确定导入日期（年月日，零点）
        String firstImportDateStr = importVOList.get(0).getImportDate();
        Date importDate;
        if (StringUtils.hasText(firstImportDateStr)) {
            try {
                LocalDate localDate = parseImportDate(firstImportDateStr);
                if (localDate == null) {
                    localDate = LocalDate.now(); // 如果为空则使用当前日期
                }
                importDate = java.sql.Date.valueOf(localDate);
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("导入日期格式错误，应为 yyyy-MM-dd，例如 2026-04-02");
            }
        } else {
            importDate = java.sql.Date.valueOf(LocalDate.now());  // 当前日期零点
        }

        // 2. 删除该导入日期的所有旧数据
        int deletedCount = vehiclePlanMapper.deleteByImportDate(importDate);  // 需要 Mapper 中新增此方法
        log.info("已删除导入日期 {} 的旧数据 {} 条", importDate, deletedCount);

        // 3. 内部去重（按裸机订单号），同时生成 ID 并构建保存列表
        Set<String> orderNoSet = new HashSet<>();
        List<VehiclePlanDO> saveList = new ArrayList<>();
        for (VehiclePlanImportReqVO vo : importVOList) {
            // 跳过裸机订单号为空的记录（根据需要）
//            if (!StringUtils.hasText(vo.getBareMachineOrderNo())) {
//                log.warn("跳过裸机订单号为空的记录");
//                continue;
//            }
//            if (orderNoSet.contains(vo.getBareMachineOrderNo())) {
//                log.warn("重复裸机订单号已跳过：{}", vo.getBareMachineOrderNo());
//                continue;
//            }
            orderNoSet.add(vo.getBareMachineOrderNo());

            VehiclePlanDO entity = new VehiclePlanDO();
            entity.setImportDate(importDate);  // 统一使用确定的导入日期
            entity.setProductLine(vo.getProductLine());
            entity.setProductModel(vo.getProductModel());
            entity.setVehicleCode(vo.getVehicleCode());
            entity.setSeqNo2025(vo.getSeqNo2025());
            entity.setSeqNo2026(vo.getSeqNo2026());
            entity.setVin(vo.getVin());
            entity.setBareMachineOrderNo(vo.getBareMachineOrderNo());
            entity.setDrivingUnitOrderNo(vo.getDrivingUnitOrderNo());
            entity.setTradeType(vo.getTradeType());
            entity.setUnitQuantity(vo.getUnitQuantity());
            entity.setBlankingPlanDate(vo.getBlankingPlanDate());
            entity.setBoomLegPlanDate(vo.getBoomLegPlanDate());
            entity.setBoomTopBottomPlanDate(vo.getBoomTopBottomPlanDate());
            entity.setTurntablePlanDate(vo.getTurntablePlanDate());
            entity.setFramePlanDate(vo.getFramePlanDate());
            entity.setChassisOnlinePlanDate(vo.getChassisOnlinePlanDate());
            entity.setFinishedProductPlanDate(vo.getFinishedProductPlanDate());

            saveList.add(entity);
        }

        if (saveList.isEmpty()) {
            return 0;
        }

        // 4. 设置创建/更新时间（自动填充也可，这里显式设置）
        LocalDateTime now = LocalDateTime.now();
        for (VehiclePlanDO entity : saveList) {
            entity.setCreateTime(now);
            entity.setUpdateTime(now);
            // creator / updater 可由自动填充或手动设置，此处略
        }

        // 5. 分批插入
        int batchSize = 30;
        int totalInserted = 0;
        for (int i = 0; i < saveList.size(); i += batchSize) {
            int end = Math.min(i + batchSize, saveList.size());
            List<VehiclePlanDO> batchList = saveList.subList(i, end);
            vehiclePlanMapper.batchInsert(batchList);
            totalInserted += batchList.size();
        }

        log.info("买家车辆营销计划导入完成，共插入 {} 条，删除 {} 条", totalInserted, deletedCount);
        return totalInserted;
    }

    /**
     * 根据裸机订单号列表批量物理删除
     */
    private int batchDeleteByBareMachineOrderNos(Collection<String> orderNos) {
        if (CollectionUtils.isEmpty(orderNos)) {
            return 0;
        }
        List<String> list = new ArrayList<>(orderNos);
        int totalDeleted = 0;
        int batchSize = 100; // Oracle IN 子句限制
        for (int i = 0; i < list.size(); i += batchSize) {
            List<String> batch = list.subList(i, Math.min(i + batchSize, list.size()));
            totalDeleted += vehiclePlanMapper.deleteByBareMachineOrderNos(batch);
        }
        return totalDeleted;
    }

    private LocalDate parseImportDate(String dateStr) {
        if (!StringUtils.hasText(dateStr)) return null;
        // 定义支持的日期格式列表（按常见程度排序）
        DateTimeFormatter[] formatters = {
                DateTimeFormatter.ofPattern("yyyy-MM-dd"),
                DateTimeFormatter.ofPattern("yyyy/M/d"),
                DateTimeFormatter.ofPattern("yyyy-M-d"),
                DateTimeFormatter.ofPattern("yyyy/MM/dd"),
                DateTimeFormatter.ofPattern("yyyy年M月d日")
        };
        for (DateTimeFormatter formatter : formatters) {
            try {
                return LocalDate.parse(dateStr.trim(), formatter);
            } catch (DateTimeParseException ignored) {}
        }
        throw new IllegalArgumentException("导入日期格式错误，支持格式：yyyy-MM-dd、yyyy/M/d、yyyy-M-d 等，例如 2026-04-02 或 2026/4/2");
    }
}