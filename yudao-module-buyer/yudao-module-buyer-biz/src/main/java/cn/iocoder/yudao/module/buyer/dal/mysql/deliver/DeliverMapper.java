package cn.iocoder.yudao.module.buyer.dal.mysql.deliver;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.buyer.dal.dataobject.deliver.DeliverDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.buyer.controller.admin.deliver.vo.*;
import org.apache.ibatis.annotations.Update;

/**
 * 配送与采购报表 Mapper
 *
 * @author 柳文
 */
@Mapper
public interface DeliverMapper extends BaseMapperX<DeliverDO> {

    default PageResult<DeliverDO> selectPage(DeliverPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<DeliverDO>()
                .eqIfPresent(DeliverDO::getPlant, reqVO.getPlant())
                .eqIfPresent(DeliverDO::getDeliveryOrderNo, reqVO.getDeliveryOrderNo())
                .betweenIfPresent(DeliverDO::getDeliveryDate, reqVO.getDeliveryDate())
                .betweenIfPresent(DeliverDO::getCreationDate, reqVO.getCreationDate())
                .betweenIfPresent(DeliverDO::getCreationTime, reqVO.getCreationTime())
                .eqIfPresent(DeliverDO::getCreatedBy, reqVO.getCreatedBy())
                .betweenIfPresent(DeliverDO::getLastUpdateDate, reqVO.getLastUpdateDate())
                .betweenIfPresent(DeliverDO::getLastUpdateTime, reqVO.getLastUpdateTime())
                .eqIfPresent(DeliverDO::getLastUpdatedBy, reqVO.getLastUpdatedBy())
                .eqIfPresent(DeliverDO::getPartOrderNo, reqVO.getPartOrderNo())
                .eqIfPresent(DeliverDO::getProductionWorkshop, reqVO.getProductionWorkshop())
                .eqIfPresent(DeliverDO::getPartMatCode, reqVO.getPartMatCode())
                .eqIfPresent(DeliverDO::getPartMatDesc, reqVO.getPartMatDesc())
                .eqIfPresent(DeliverDO::getReservationNo, reqVO.getReservationNo())
                .eqIfPresent(DeliverDO::getReservationItem, reqVO.getReservationItem())
                .eqIfPresent(DeliverDO::getPlannedIssueQty, reqVO.getPlannedIssueQty())
                .eqIfPresent(DeliverDO::getDeliveredQty, reqVO.getDeliveredQty())
                .eqIfPresent(DeliverDO::getUndeliveredQty, reqVO.getUndeliveredQty())
                .eqIfPresent(DeliverDO::getBuyerMaterialNo, reqVO.getBuyerMaterialNo())
                .eqIfPresent(DeliverDO::getOldMaterialNo, reqVO.getOldMaterialNo())
                .eqIfPresent(DeliverDO::getBuyerMaterialDesc, reqVO.getBuyerMaterialDesc())
                .eqIfPresent(DeliverDO::getDeliverySupplierCode, reqVO.getDeliverySupplierCode())
                .likeIfPresent(DeliverDO::getDeliverySupplierName, reqVO.getDeliverySupplierName())
                .eqIfPresent(DeliverDO::getStockSufficientFlag, reqVO.getStockSufficientFlag())
                .eqIfPresent(DeliverDO::getTotalStockQty, reqVO.getTotalStockQty())
                .eqIfPresent(DeliverDO::getCurrentStockConsumeQty, reqVO.getCurrentStockConsumeQty())
                .eqIfPresent(DeliverDO::getDeliveryStorageLoc, reqVO.getDeliveryStorageLoc())
                .eqIfPresent(DeliverDO::getPoSufficientFlag, reqVO.getPoSufficientFlag())
                .eqIfPresent(DeliverDO::getBuyerOrderNo, reqVO.getBuyerOrderNo())
                .eqIfPresent(DeliverDO::getLineItem, reqVO.getLineItem())
                .eqIfPresent(DeliverDO::getRequirementTrackingNo, reqVO.getRequirementTrackingNo())
                .eqIfPresent(DeliverDO::getOrderQty, reqVO.getOrderQty())
                .eqIfPresent(DeliverDO::getOpenQty, reqVO.getOpenQty())
                .eqIfPresent(DeliverDO::getReceivedQty, reqVO.getReceivedQty())
                .betweenIfPresent(DeliverDO::getPoDeliveryDate, reqVO.getPoDeliveryDate())
                .eqIfPresent(DeliverDO::getPoSupplierCode, reqVO.getPoSupplierCode())
                .eqIfPresent(DeliverDO::getSupplierDesc, reqVO.getSupplierDesc())
                .eqIfPresent(DeliverDO::getBuyerPurchasingGroup, reqVO.getBuyerPurchasingGroup())
                .eqIfPresent(DeliverDO::getOrderingBuyer, reqVO.getOrderingBuyer())
                .eqIfPresent(DeliverDO::getDeliveryBuyer, reqVO.getDeliveryBuyer())
                .betweenIfPresent(DeliverDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(DeliverDO::getId));
    }


    /**
     * 物理删除表中所有数据（不经过逻辑删除）
     */
    @Update("DELETE FROM buyer_deliver")
    void physicalDeleteAll();

}