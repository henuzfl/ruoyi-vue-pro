package cn.iocoder.yudao.module.buyer.dal.mysql.productiontransfer;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.buyer.dal.dataobject.productiontransfer.ProductionTransferDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.buyer.controller.admin.productiontransfer.vo.*;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import org.apache.ibatis.annotations.Param;

/**
 * MES转序单信息 Mapper
 *
 * @author 柳文
 */
@Mapper
public interface ProductionTransferMapper extends BaseMapperX<ProductionTransferDO> {

    default PageResult<ProductionTransferDO> selectPage(ProductionTransferPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ProductionTransferDO>()
                .eqIfPresent(ProductionTransferDO::getOrderNo, reqVO.getOrderNo())
                .eqIfPresent(ProductionTransferDO::getMaterialCode, reqVO.getMaterialCode())
                .eqIfPresent(ProductionTransferDO::getMaterialDesc, reqVO.getMaterialDesc())
                .eqIfPresent(ProductionTransferDO::getProductionScheduler, reqVO.getProductionScheduler())
                .eqIfPresent(ProductionTransferDO::getTransferInitiator, reqVO.getTransferInitiator())
                .betweenIfPresent(ProductionTransferDO::getInitiatorDate, reqVO.getInitiatorDate())
                .eqIfPresent(ProductionTransferDO::getQuantity, reqVO.getQuantity())
                .eqIfPresent(ProductionTransferDO::getTransferNo, reqVO.getTransferNo())
                .eqIfPresent(ProductionTransferDO::getBatchNo, reqVO.getBatchNo())
                .eqIfPresent(ProductionTransferDO::getSigner, reqVO.getSigner())
                .betweenIfPresent(ProductionTransferDO::getSignTime, reqVO.getSignTime())
                .orderByDesc(ProductionTransferDO::getId));
    }

    @InterceptorIgnore(tenantLine = "true")
    void batchInsert(@Param("list") List<ProductionTransferDO> list);

    @InterceptorIgnore(tenantLine = "true")
    int deleteByTransferNos(@Param("transferNos") Collection<String> transferNos);

    @InterceptorIgnore(tenantLine = "true")
    int deleteByTransferNosAndBatchNos(@Param("list") List<Map<String, String>> transferNoBatchNoList);
}