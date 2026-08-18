package cn.iocoder.yudao.module.buyer.dal.mysql.openorder;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.buyer.dal.dataobject.openorder.OpenOrderDO;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.buyer.controller.admin.openorder.vo.*;
import org.apache.ibatis.annotations.Param;

/**
 * 采购未清订单 Mapper
 *
 * @author 柳文
 */
@Mapper
public interface OpenOrderMapper extends BaseMapperX<OpenOrderDO> {

    default PageResult<OpenOrderDO> selectPage(OpenOrderPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<OpenOrderDO>()
                .betweenIfPresent(OpenOrderDO::getOrderDate, reqVO.getOrderDate())
                .eqIfPresent(OpenOrderDO::getBuyerOrderNo, reqVO.getBuyerOrderNo())
                .eqIfPresent(OpenOrderDO::getLineItem, reqVO.getLineItem())
                .eqIfPresent(OpenOrderDO::getMaterialNo, reqVO.getMaterialNo())
                .eqIfPresent(OpenOrderDO::getMaterialDesc, reqVO.getMaterialDesc())
                .eqIfPresent(OpenOrderDO::getOrderQty, reqVO.getOrderQty())
                .eqIfPresent(OpenOrderDO::getReceivedQty, reqVO.getReceivedQty())
                .eqIfPresent(OpenOrderDO::getOpenQty, reqVO.getOpenQty())
                .eqIfPresent(OpenOrderDO::getUnit, reqVO.getUnit())
                .betweenIfPresent(OpenOrderDO::getRequiredArrivalDate, reqVO.getRequiredArrivalDate())
                .betweenIfPresent(OpenOrderDO::getActualArrivalDate, reqVO.getActualArrivalDate())
                .eqIfPresent(OpenOrderDO::getSupplierDesc, reqVO.getSupplierDesc())
                .eqIfPresent(OpenOrderDO::getCustomer, reqVO.getCustomer())
                .eqIfPresent(OpenOrderDO::getBuyerGroup, reqVO.getBuyerGroup())
                .eqIfPresent(OpenOrderDO::getDocumentType, reqVO.getDocumentType())
                .eqIfPresent(OpenOrderDO::getProductionOrderNo, reqVO.getProductionOrderNo())
                .eqIfPresent(OpenOrderDO::getBrandInfo, reqVO.getBrandInfo())
                .eqIfPresent(OpenOrderDO::getUnitPrice, reqVO.getUnitPrice())
                .eqIfPresent(OpenOrderDO::getSupplierCode, reqVO.getSupplierCode())
                .eqIfPresent(OpenOrderDO::getReceivingWarehouse, reqVO.getReceivingWarehouse())
                .eqIfPresent(OpenOrderDO::getTotalAmount, reqVO.getTotalAmount())
                .eqIfPresent(OpenOrderDO::getBuyerReqNo, reqVO.getBuyerReqNo())
                .betweenIfPresent(OpenOrderDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(OpenOrderDO::getId));
    }

    /**
     * 分页查询数据列表
     * @param offset 起始行（从0开始）
     * @param limit  每页条数
     * @param reqVO  查询条件
     * @return 列表
     */
    @InterceptorIgnore(tenantLine = "true")
    List<OpenOrderDO> selectPageList(@Param("offset") int offset,
                                     @Param("limit") int limit,
                                     @Param("reqVO") OpenOrderPageReqVO reqVO);

    /**
     * 查询总条数
     * @param reqVO 查询条件
     * @return 总数
     */
    @InterceptorIgnore(tenantLine = "true")
    long selectPageCount(@Param("reqVO") OpenOrderPageReqVO reqVO);

}