package cn.iocoder.yudao.module.marketing.dal.mysql.salesorder;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.marketing.controller.admin.salesorder.vo.SalesOrderPageReqVO;
import cn.iocoder.yudao.module.marketing.dal.dataobject.salesorder.SalesOrderDO;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SalesOrderMapper extends BaseMapperX<SalesOrderDO> {

    default PageResult<SalesOrderDO> selectPage(SalesOrderPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<SalesOrderDO>()
                .eqIfPresent(SalesOrderDO::getOrderNumber, reqVO.getOrderNumber())
                .likeIfPresent(SalesOrderDO::getMaterialCode, reqVO.getMaterialCode())
                .eqIfPresent(SalesOrderDO::getSoldToParty, reqVO.getSoldToParty())
                .eqIfPresent(SalesOrderDO::getSalesOrganization, reqVO.getSalesOrganization())
                .eqIfPresent(SalesOrderDO::getPlant, reqVO.getPlant())
                .eqIfPresent(SalesOrderDO::getDeliveryStatus, reqVO.getDeliveryStatus())
                .betweenIfPresent(SalesOrderDO::getOrderDate, reqVO.getOrderDate())
                .betweenIfPresent(SalesOrderDO::getEarliestDeliveryDate, reqVO.getEarliestDeliveryDate())
                .eqIfPresent(SalesOrderDO::getOrderQuantity, reqVO.getOrderQuantity())
                .orderByDesc(SalesOrderDO::getId)
        );
    }

    @InterceptorIgnore(tenantLine = "true")
    void insertBatch(@Param("list") List<SalesOrderDO> list);
}