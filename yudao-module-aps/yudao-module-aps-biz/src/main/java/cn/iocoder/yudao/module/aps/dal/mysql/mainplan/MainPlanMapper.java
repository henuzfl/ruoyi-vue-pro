package cn.iocoder.yudao.module.aps.dal.mysql.mainplan;

import java.math.BigDecimal;
import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.aps.dal.dataobject.mainplan.MainPlanDO;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.aps.controller.admin.mainplan.vo.*;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 主计划 Mapper
 *
 * @author 柳文
 */
@Mapper
public interface MainPlanMapper extends BaseMapperX<MainPlanDO> {

    default PageResult<MainPlanDO> selectPage(MainPlanPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<MainPlanDO>()
                .eqIfPresent(MainPlanDO::getProductionOrderNo, reqVO.getProductionOrderNo())
                .eqIfPresent(MainPlanDO::getAssemblyMaterialNo, reqVO.getAssemblyMaterialNo())
                .eqIfPresent(MainPlanDO::getMainMaterialDesc, reqVO.getMainMaterialDesc())
                .betweenIfPresent(MainPlanDO::getScheduledDate, reqVO.getScheduledDate())
                .eqIfPresent(MainPlanDO::getScheduledQuantity, reqVO.getScheduledQuantity())
                .eqIfPresent(MainPlanDO::getProductionWorkshop, reqVO.getProductionWorkshop())
                .betweenIfPresent(MainPlanDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(MainPlanDO::getId));
    }

//    @Select("SELECT DISTINCT ASSEMBLY_MATERIAL_NO FROM APS_MAIN_PLAN " +
//            "WHERE ASSEMBLY_MATERIAL_NO IS NOT NULL AND SCHEDULED_DATE IN (TRUNC(SYSDATE) + 1, TRUNC(SYSDATE) + 2, TRUNC(SYSDATE) + 3)")

    @InterceptorIgnore(tenantLine = "true")
    //@Select("select '01126602200990000' from dual")
    //@Select("SELECT DISTINCT ASSEMBLY_MATERIAL_NO FROM APS_MAIN_PLAN WHERE ASSEMBLY_MATERIAL_NO IS NOT NULL AND SCHEDULED_DATE <= TRUNC(SYSDATE) + 6")
    @Select("SELECT DISTINCT ASSEMBLY_MATERIAL_NO FROM APS_MAIN_PLAN WHERE ASSEMBLY_MATERIAL_NO IS NOT NULL AND SCHEDULED_DATE > TRUNC(ADD_MONTHS(SYSDATE, -1), 'MM') AND SCHEDULED_DATE < TRUNC(ADD_MONTHS(SYSDATE, 1), 'MM') UNION SELECT DISTINCT ASSEMBLY_MATERIAL_NO FROM marketing_asm_requirement")
    List<String> selectDistinctAssemblyMaterialNo();

//    @Select("SELECT DISTINCT b.component_material_no FROM (SELECT DISTINCT ASSEMBLY_MATERIAL_NO FROM APS_MAIN_PLAN WHERE ASSEMBLY_MATERIAL_NO IS NOT NULL AND SCHEDULED_DATE IN (TRUNC(SYSDATE) + 1, TRUNC(SYSDATE) + 2, TRUNC(SYSDATE) + 3)) a LEFT JOIN MATERIAL_BOM_IMPORT  b on a.ASSEMBLY_MATERIAL_NO = b.main_material_no LEFT JOIN MATERIAL_MASTER_IMPORT d on d.material_no =  b.component_material_no WHERE d.procurement_type = 'E' AND B.LEVEL_NO = 1\n")
    //@Select("SELECT DISTINCT b.component_material_no FROM (SELECT DISTINCT ASSEMBLY_MATERIAL_NO FROM APS_MAIN_PLAN WHERE ASSEMBLY_MATERIAL_NO IS NOT NULL AND SCHEDULED_DATE <= TRUNC(SYSDATE) + 6) a LEFT JOIN MATERIAL_BOM_IMPORT  b on a.ASSEMBLY_MATERIAL_NO = b.main_material_no LEFT JOIN MATERIAL_MASTER_IMPORT d on d.material_no =  b.component_material_no WHERE d.procurement_type = 'E' AND B.LEVEL_NO = 1")
    @InterceptorIgnore(tenantLine = "true")
    @Select("SELECT DISTINCT b.component_material_no FROM (SELECT DISTINCT ASSEMBLY_MATERIAL_NO FROM APS_MAIN_PLAN WHERE ASSEMBLY_MATERIAL_NO IS NOT NULL AND SCHEDULED_DATE <= TRUNC(SYSDATE) + 6) a LEFT JOIN MATERIAL_BOM_IMPORT  b on a.ASSEMBLY_MATERIAL_NO = b.main_material_no LEFT JOIN MATERIAL_MASTER_IMPORT d on d.material_no =  b.component_material_no WHERE d.procurement_type = 'E' AND B.LEVEL_NO = 1")
    //@Select("select '011299945A1840000' from dual")
    List<String> selectDistinctComponentMaterialNo();

    /**
     * 批量插入主计划
     * @param list 数据列表
     */
    @InterceptorIgnore(tenantLine = "true")
    void batchInsert(@Param("list") List<MainPlanDO> list);

    @Select("SELECT aps_main_plan_seq.NEXTVAL FROM DUAL") // 请替换为实际的序列名
    BigDecimal selectNextId();

    /**
     * 批量获取指定数量的序列下一个值
     * @param count 需要的序列值个数
     * @return 序列值列表
     */
    @Select("SELECT aps_main_plan_seq.NEXTVAL FROM DUAL CONNECT BY LEVEL <= #{count}")
    List<BigDecimal> selectNextIds(@Param("count") int count);

    /**
     * 物理删除，按订单号+车间+排产日期（日期部分）
     */
    int physicalDeleteByOrderKeys(@Param("list") List<MainPlanDO> keys);

    /**
     * 物理删除，
     */
    int physicalDeleteById(@Param("id") BigDecimal id);

    int physicalDeleteAll();


}