package cn.iocoder.yudao.module.buyer.dal.mysql.vehicleplan;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.buyer.dal.dataobject.vehicleplan.VehiclePlanDO;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.buyer.controller.admin.vehicleplan.vo.*;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.util.StringUtils;

/**
 * 买家车辆营销计划表（主机厂计划） Mapper
 *
 * @author 柳文
 */
@Mapper
public interface VehiclePlanMapper extends BaseMapperX<VehiclePlanDO> {

    default PageResult<VehiclePlanDO> selectPage(VehiclePlanPageReqVO reqVO) {
        // 1. 创建 wrapper
        LambdaQueryWrapperX<VehiclePlanDO> wrapper = new LambdaQueryWrapperX<VehiclePlanDO>();

        // 2. 添加普通条件（使用 eqIfPresent，它们会返回 LambdaQueryWrapperX）
        wrapper.eqIfPresent(VehiclePlanDO::getProductLine, reqVO.getProductLine())
                .eqIfPresent(VehiclePlanDO::getProductModel, reqVO.getProductModel())
                .eqIfPresent(VehiclePlanDO::getVehicleCode, reqVO.getVehicleCode())
                .eqIfPresent(VehiclePlanDO::getSeqNo2025, reqVO.getSeqNo2025())
                .eqIfPresent(VehiclePlanDO::getSeqNo2026, reqVO.getSeqNo2026())
                .eqIfPresent(VehiclePlanDO::getBareMachineOrderNo, reqVO.getBareMachineOrderNo())
                .eqIfPresent(VehiclePlanDO::getDrivingUnitOrderNo, reqVO.getDrivingUnitOrderNo())
                .eqIfPresent(VehiclePlanDO::getTradeType, reqVO.getTradeType())
                .eqIfPresent(VehiclePlanDO::getUnitQuantity, reqVO.getUnitQuantity());

        // 3. 添加范围条件（注意：betweenIfPresent 也可能返回父类型，需强制转为 LambdaQueryWrapperX）
        // 方案：不连续链式，单独调用并重新赋值
        if (reqVO.getChassisOnlinePlanDate() != null && reqVO.getChassisOnlinePlanDate().length == 2) {
            wrapper.between(VehiclePlanDO::getChassisOnlinePlanDate,
                    reqVO.getChassisOnlinePlanDate()[0],
                    reqVO.getChassisOnlinePlanDate()[1]);
        }
        if (reqVO.getFinishedProductPlanDate() != null && reqVO.getFinishedProductPlanDate().length == 2) {
            wrapper.between(VehiclePlanDO::getFinishedProductPlanDate,
                    reqVO.getFinishedProductPlanDate()[0],
                    reqVO.getFinishedProductPlanDate()[1]);
        }

//        // 4. 处理创建时间（年月优先）
//        if (StringUtils.hasText(reqVO.getCreateTimeYearMonth())) {
//            YearMonth ym = YearMonth.parse(reqVO.getCreateTimeYearMonth(), DateTimeFormatter.ofPattern("yyyy-MM"));
//            wrapper.between(VehiclePlanDO::getCreateTime,
//                    ym.atDay(1).atStartOfDay(),
//                    ym.atEndOfMonth().atTime(23, 59, 59));
//        } else if (reqVO.getCreateTime() != null && reqVO.getCreateTime().length == 2) {
//            wrapper.between(VehiclePlanDO::getCreateTime,
//                    reqVO.getCreateTime()[0],
//                    reqVO.getCreateTime()[1]);
//        }

        // 5. 排序（orderByDesc/orderByAsc 应返回 LambdaQueryWrapperX，如果依然报错，同样拆开调用）
        wrapper.orderByDesc(VehiclePlanDO::getImportDate)
                .orderByAsc(VehiclePlanDO::getVehicleCode)
                .orderByAsc(VehiclePlanDO::getSeqNo2026);

        // 6. 执行分页查询
        return selectPage(reqVO, wrapper);
    }

    /**
     * 批量插入（忽略租户拦截）
     */
    @InterceptorIgnore(tenantLine = "true")
    void batchInsert(@Param("list") List<VehiclePlanDO> list);

    /**
     * 根据裸机订单号列表物理删除（忽略租户拦截）
     */
    @InterceptorIgnore(tenantLine = "true")
    int deleteByBareMachineOrderNos(@Param("orderNos") Collection<String> orderNos);
    /**
     * 根据导入日期物理删除数据
     */
    @InterceptorIgnore(tenantLine = "true")
    int deleteByImportDate(@Param("importDate") Date importDate);

    /**
     * 物理删除（根据 ID）
     */
    @InterceptorIgnore(tenantLine = "true")
    int physicalDeleteById(@Param("id") String id);

    // VehiclePlanMapper.java
    @Select("SELECT DISTINCT import_date FROM buyer_vehicle_plan ORDER BY import_date DESC")
    List<Date> selectDistinctImportDates();

}