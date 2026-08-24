package cn.iocoder.yudao.module.buyer.dal.mysql.monthlynetdemand;

import cn.iocoder.yudao.module.buyer.dal.dataobject.monthlynetdemand.MonthlyDemandCodeMappingRow;
import cn.iocoder.yudao.module.buyer.dal.dataobject.monthlynetdemand.MonthlyDemandConfigRow;
import cn.iocoder.yudao.module.buyer.dal.dataobject.monthlynetdemand.MonthlyDemandPlanRow;
import cn.iocoder.yudao.module.buyer.dal.dataobject.monthlynetdemand.MonthlyDemandQuantityRow;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Collection;

@Mapper
public interface MonthlyNetDemandMapper {
    /** 查询最新导入版本中归属于指定计划月份的主机计划。 */
    @InterceptorIgnore(tenantLine = "true")
    List<MonthlyDemandPlanRow> selectPlanRows(@Param("planMonth") String planMonth);

    /** 查询最新导入版本的车型配置，并在数据库端合并相同配置行。 */
    @InterceptorIgnore(tenantLine = "true")
    List<MonthlyDemandConfigRow> selectConfigRows();

    /** 查询车型配置缓存版本。 */
    @InterceptorIgnore(tenantLine = "true")
    String selectConfigVersion();

    /** 查询主机编码到特力物料编码的映射。 */
    @InterceptorIgnore(tenantLine = "true")
    List<MonthlyDemandCodeMappingRow> selectCodeMappings();

    /** 查询编码映射缓存版本。 */
    @InterceptorIgnore(tenantLine = "true")
    String selectCodeMappingVersion();

    /**
     * 仅汇总本次需求涉及物料的三类可抵扣数量，避免对三张业务表做全表聚合。
     */
    @InterceptorIgnore(tenantLine = "true")
    List<MonthlyDemandQuantityRow> selectInventoryQuantities(
            @Param("materialNos") Collection<String> materialNos);
}
