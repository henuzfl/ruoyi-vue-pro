package cn.iocoder.yudao.module.wm.dal.mysql.sapordersync;

import java.util.*;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.aps.dal.dataobject.order.OrderDO;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * 订单表 - SAP订单信息 Mapper
 *
 * @author 柳文
 */
@Mapper
@Repository("wmSapOrderSyncMapper")
public interface SapOrderSyncMapper extends BaseMapperX<OrderDO> {

    // 新增方法
    int deleteByProductionOrderNos(@Param("orderNos") List<String> orderNos);

    @InterceptorIgnore(tenantLine = "true")
    void batchInsert(@Param("list") List<OrderDO> list);


    /**
     * 查询所有需要同步的生产订单号（去重）
     */
    @DS("oracle")
//    @Select("SELECT ASSEMBLY_MATERIAL_NO FROM marketing_asm_requirement WHERE TO_CHAR(REQUIRE_DATE,'YYYY-MM') = '2027-01'")
    @Select("SELECT DISTINCT production_order_no FROM (" +
            "SELECT DISTINCT production_order_no FROM APS_MAIN_PLAN " +
            "WHERE to_char(scheduled_date,'yyyy-mm') = to_char(sysdate,'yyyy-mm')" +
            " UNION ALL " +
            "SELECT DISTINCT component_order FROM APS_MAIN_PLAN A " +
            "LEFT JOIN APS_ASSEM_PART B ON A.production_order_no = B.order_no" +
            " WHERE to_char(scheduled_date,'yyyy-mm') = to_char(sysdate,'yyyy-mm')" +
            ") tmp")   // 添加别名
    @InterceptorIgnore(tenantLine = "true")
    List<String> selectDistinctProductionOrderNos();

}