package cn.iocoder.yudao.module.wm.dal.mysql.orderdemand;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.wm.dal.dataobject.orderdemand.OrderDemandDO;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.wm.controller.admin.orderdemand.vo.*;
import cn.iocoder.yudao.module.wm.controller.admin.orderdemand.vo.OrderDemandKey;
import org.apache.ibatis.annotations.Param;

/**
 * 订单追溯需求 Mapper
 *
 * @author 柳文
 */
@Mapper
public interface OrderDemandMapper extends BaseMapperX<OrderDemandDO> {

    default PageResult<OrderDemandDO> selectPage(OrderDemandPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<OrderDemandDO>()
                .eqIfPresent(OrderDemandDO::getOrderNo, reqVO.getOrderNo())
                .eqIfPresent(OrderDemandDO::getTraceDemandNo, reqVO.getTraceDemandNo())
                .eqIfPresent(OrderDemandDO::getMaterialNo, reqVO.getMaterialNo())
                .eqIfPresent(OrderDemandDO::getMaterialDescription, reqVO.getMaterialDescription())
                .eqIfPresent(OrderDemandDO::getDemandQuantity, reqVO.getDemandQuantity())
                .eqIfPresent(OrderDemandDO::getOutboundAccumulated, reqVO.getOutboundAccumulated())
                .eqIfPresent(OrderDemandDO::getOpenQuantity, reqVO.getOpenQuantity())
                .eqIfPresent(OrderDemandDO::getStatus, reqVO.getStatus())
                .eqIfPresent(OrderDemandDO::getRemark, reqVO.getRemark())
                .betweenIfPresent(OrderDemandDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(OrderDemandDO::getId));
    }
    /**
     * 根据订单号组合批量物理删除
     * @param keys 键列表
     * @return 删除行数
     */
    int deleteByOrderNoAndMaterialNos(@Param("keys") List<OrderDemandKey> keys);

    /**
     * 批量插入
     * @param list 数据列表
     */
    @InterceptorIgnore(tenantLine = "true")
    void batchInsert(@Param("list") List<OrderDemandDO> list);


}