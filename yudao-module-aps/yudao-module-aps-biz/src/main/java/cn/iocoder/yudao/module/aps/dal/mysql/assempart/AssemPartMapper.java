package cn.iocoder.yudao.module.aps.dal.mysql.assempart;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.aps.dal.dataobject.assempart.AssemPartDO;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.aps.controller.admin.assempart.vo.*;
import org.apache.ibatis.annotations.Param;

/**
 * 总成与子件关联表管理 Mapper
 *
 * @author 柳文
 */
@Mapper
public interface AssemPartMapper extends BaseMapperX<AssemPartDO> {

    default PageResult<AssemPartDO> selectPage(AssemPartPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<AssemPartDO>()
                .eqIfPresent(AssemPartDO::getOrderNo, reqVO.getOrderNo())
                .eqIfPresent(AssemPartDO::getQuantity, reqVO.getQuantity())
                .eqIfPresent(AssemPartDO::getComponentOrder, reqVO.getComponentOrder())
                .eqIfPresent(AssemPartDO::getAllocQty, reqVO.getAllocQty())
                .betweenIfPresent(AssemPartDO::getCreateTime, reqVO.getCreateTime())
                .betweenIfPresent(AssemPartDO::getScheduleTime, reqVO.getScheduleTime())
                .orderByDesc(AssemPartDO::getId));
    }

    // 物理删除：根据总成订单号和计划日期（只比较年月日）
    @Delete("DELETE FROM aps_assem_part WHERE order_no = #{orderNo} AND TRUNC(schedule_time) = #{dateOnly}")
    @InterceptorIgnore(tenantLine = "true")
    int physicalDeleteByOrderNoAndDate(@Param("orderNo") String orderNo, @Param("dateOnly") Date dateOnly);

}