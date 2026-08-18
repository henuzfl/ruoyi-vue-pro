package cn.iocoder.yudao.module.aps.dal.mysql.matchingresult;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.aps.dal.dataobject.matchingresult.MatchingResultDO;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.aps.controller.admin.matchingresult.vo.*;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
 * 主计划物料需求匹配 Mapper
 *
 * @author 柳文
 */
@Mapper
public interface MatchingResultMapper extends BaseMapperX<MatchingResultDO> {

    default PageResult<MatchingResultDO> selectPage(MatchingResultPageReqVO reqVO) {
        // 自动设置 createTime 为最新数据日（保持原逻辑）
        if (reqVO.getCreateTime() == null || reqVO.getCreateTime().length != 2 ||
                (reqVO.getCreateTime()[0] == null && reqVO.getCreateTime()[1] == null)) {
            String latestDay = selectLatestDataDay();
            if (latestDay != null && !latestDay.isEmpty()) {
                LocalDate date = LocalDate.parse(latestDay);
                Date start = Date.from(date.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
                Date end = Date.from(date.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant());
                reqVO.setCreateTime(new Date[]{start, end});
            }
        }

        LambdaQueryWrapperX<MatchingResultDO> wrapper = new LambdaQueryWrapperX<>();

        // 订单号多值 IN 查询
        if (reqVO.getOrderNos() != null && !reqVO.getOrderNos().isEmpty()) {
            wrapper.in(MatchingResultDO::getOrderNo, reqVO.getOrderNos());
        }

        // ---------- 仅处理 Date 类型的日期范围字段 ----------
        handleDateRange(wrapper, MatchingResultDO::getScheduleTime, reqVO.getScheduleTime());
        handleDateRange(wrapper, MatchingResultDO::getBasicStartDate, reqVO.getBasicStartDate());
        handleDateRange(wrapper, MatchingResultDO::getOrderDate, reqVO.getOrderDate());
        handleDateRange(wrapper, MatchingResultDO::getRequiredDeliveryDate, reqVO.getRequiredDeliveryDate());
        handleDateRange(wrapper, MatchingResultDO::getActualArrivalDate, reqVO.getActualArrivalDate());

        // ---------- createTime 使用原有的 betweenIfPresent，因其类型为 LocalDateTime ----------
        wrapper.betweenIfPresent(MatchingResultDO::getCreateTime, reqVO.getCreateTime());

        // ---------- 其他等值条件（保持不变） ----------
        wrapper.eqIfPresent(MatchingResultDO::getMaterialCode, reqVO.getMaterialCode())
                .eqIfPresent(MatchingResultDO::getMaterialDesc, reqVO.getMaterialDesc())
                .eqIfPresent(MatchingResultDO::getWorkshop, reqVO.getWorkshop())
                .eqIfPresent(MatchingResultDO::getQuantity, reqVO.getQuantity())
                .eqIfPresent(MatchingResultDO::getCompletedQuantity, reqVO.getCompletedQuantity())
                .eqIfPresent(MatchingResultDO::getStock, reqVO.getStock())
                .eqIfPresent(MatchingResultDO::getTransferOrder, reqVO.getTransferOrder())
                .eqIfPresent(MatchingResultDO::getComponentOrder, reqVO.getComponentOrder())
                .eqIfPresent(MatchingResultDO::getComponentCode, reqVO.getComponentCode())
                .eqIfPresent(MatchingResultDO::getComponentDesc, reqVO.getComponentDesc())
                .eqIfPresent(MatchingResultDO::getComponentWorkshop, reqVO.getComponentWorkshop())
                .eqIfPresent(MatchingResultDO::getRequiredQuantity, reqVO.getRequiredQuantity())
                .eqIfPresent(MatchingResultDO::getUnfinishedQuantity, reqVO.getUnfinishedQuantity())
                .eqIfPresent(MatchingResultDO::getPurchaseMaterial, reqVO.getPurchaseMaterial())
                .eqIfPresent(MatchingResultDO::getPurchaseMaterialDesc, reqVO.getPurchaseMaterialDesc())
                .eqIfPresent(MatchingResultDO::getPurchaseRequiredQty, reqVO.getPurchaseRequiredQty())
                .eqIfPresent(MatchingResultDO::getDeliveredQuantity, reqVO.getDeliveredQuantity())
                .eqIfPresent(MatchingResultDO::getToDeliverQuantity, reqVO.getToDeliverQuantity())
                .eqIfPresent(MatchingResultDO::getKitQtySingle, reqVO.getKitQtySingle())
                .eqIfPresent(MatchingResultDO::getPurchaseOrder, reqVO.getPurchaseOrder())
                .eqIfPresent(MatchingResultDO::getLineNumber, reqVO.getLineNumber())
                .eqIfPresent(MatchingResultDO::getSizeDimension, reqVO.getSizeDimension())
                .likeIfPresent(MatchingResultDO::getSupplierName, reqVO.getSupplierName())
                .eqIfPresent(MatchingResultDO::getOpenOrderQuantity, reqVO.getOpenOrderQuantity())
                .orderByAsc(MatchingResultDO::getScheduleTime)
                .orderByAsc(MatchingResultDO::getWorkshop)
                .orderByAsc(MatchingResultDO::getOrderNo);

        return selectPage(reqVO, wrapper);
    }

    /**
     * 安全处理日期范围（仅适用于 java.util.Date 类型字段）
     * 分别添加 ge 和 le 条件，避免数组内 null 元素导致 JDBC 类型异常
     */
    default void handleDateRange(LambdaQueryWrapperX<MatchingResultDO> wrapper,
                                 SFunction<MatchingResultDO, Date> column,
                                 Date[] dateRange) {
        if (dateRange == null || dateRange.length < 2) {
            return;
        }
        Date start = dateRange[0];
        Date end = dateRange[1];
        if (start != null) {
            wrapper.ge(column, start);
        }
        if (end != null) {
            wrapper.le(column, end);
        }
    }

    @Select("SELECT TO_CHAR(MAX(create_time), 'YYYY-MM-DD') FROM aps_matching_result WHERE deleted = 0")
    String selectLatestDataDay();

    @Update("{ CALL PRC_MASTER_ALLOCATION() }")
    @DS("oracle")
    @InterceptorIgnore(tenantLine = "true")
    void callMasterAllocationProcedure();

    /**
     * 分页查询导出数据（直接关联反馈表，合并备注）
     * @param page 分页对象
     * @param reqVO 查询条件
     * @return 分页结果，包含反馈备注
     */
    IPage<MatchingResultRespVO> selectExportPage(Page<?> page, @Param("reqVO") MatchingResultPageReqVO reqVO);
}