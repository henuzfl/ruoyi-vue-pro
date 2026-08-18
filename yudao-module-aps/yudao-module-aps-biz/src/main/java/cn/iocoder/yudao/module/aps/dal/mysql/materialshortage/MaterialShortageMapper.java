package cn.iocoder.yudao.module.aps.dal.mysql.materialshortage;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.aps.dal.dataobject.materialshortage.MaterialShortageComponentSummaryDO;
import cn.iocoder.yudao.module.aps.dal.dataobject.materialshortage.MaterialShortageDO;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface MaterialShortageMapper extends BaseMapperX<MaterialShortageDO> {

    /**
     * 查询某个成品的缺口明细
     */
    @InterceptorIgnore(tenantLine = "true")
    @Select("SELECT * FROM material_shortage_report " +
            "WHERE main_material_no = #{mainMaterialNo} AND deleted = 0 " +
            "ORDER BY shortage_qty DESC")
    List<MaterialShortageDO> selectDetailsByMainMaterial(@Param("mainMaterialNo") String mainMaterialNo);

    /**
     * 查询某个成品的总缺口
     */
    @InterceptorIgnore(tenantLine = "true")
    @Select("SELECT SUM(shortage_qty) FROM material_shortage_report " +
            "WHERE main_material_no = #{mainMaterialNo} AND deleted = 0")
    BigDecimal selectTotalShortageByMainMaterial(@Param("mainMaterialNo") String mainMaterialNo);

    /**
     * 查询所有组件的缺口汇总（按组件物料号分组）
     */
    @InterceptorIgnore(tenantLine = "true")
    @Select("WITH component_main_list AS ( " +
            "SELECT sub.component_material_no, " +
            "       XMLAGG(XMLELEMENT(e, sub.main_material_no || ',')).EXTRACT('//text()').GetClobVal() AS main_material_nos " +
            "FROM (SELECT DISTINCT component_material_no, main_material_no " +
            "      FROM material_shortage_report " +
            "      WHERE deleted = 0) sub " +
            "GROUP BY sub.component_material_no " +
            ") " +
            "SELECT agg.component_material_no, " +
            "       agg.component_desc, " +
            "       agg.total_requirement, " +
            "       agg.stock_quantity, " +
            "       agg.transit, " +
            "       agg.total_issue, " +
            "       agg.shortage_qty, " +
            "       agg.main_count, " +
            "       cml.main_material_nos " +
            "FROM ( " +
            "    SELECT main.component_material_no, " +
            "           main.component_desc, " +
            "           SUM(main.main_requirement * main.unit_usage) AS total_requirement, " +
            "           MAX(main.stock_quantity) AS stock_quantity, " +
            "           MAX(main.transit) AS transit, " +
            "           SUM(main.issue) AS total_issue, " +
            "           SUM(main.shortage_qty) AS shortage_qty, " +
            "           COUNT(DISTINCT main.main_material_no) AS main_count " +
            "    FROM material_shortage_report main " +
            "    WHERE main.deleted = 0 " +
            "    GROUP BY main.component_material_no, main.component_desc " +
            ") agg " +
            "LEFT JOIN component_main_list cml ON cml.component_material_no = agg.component_material_no " +
            "ORDER BY agg.shortage_qty DESC")
    List<MaterialShortageComponentSummaryDO> selectComponentSummary();

    /**
     * 查询所有组件的缺口汇总（带过滤条件）
     */
    @InterceptorIgnore(tenantLine = "true")
    @Select("<script>" +
            "WITH component_main_list AS ( " +
            "SELECT sub.component_material_no, " +
            "       XMLAGG(XMLELEMENT(e, sub.main_material_no || ',')).EXTRACT('//text()').GetClobVal() AS main_material_nos " +
            "FROM (SELECT DISTINCT component_material_no, main_material_no " +
            "      FROM material_shortage_report " +
            "      WHERE deleted = 0 AND tenant_id = 1 " +
            "      <if test='componentMaterialNo != null and componentMaterialNo != \"\"'>" +
            "        AND component_material_no LIKE '%' || #{componentMaterialNo} || '%' " +
            "      </if>" +
            "      <if test='componentDesc != null and componentDesc != \"\"'>" +
            "        AND component_desc LIKE '%' || #{componentDesc} || '%' " +
            "      </if>" +
            ") sub " +
            "GROUP BY sub.component_material_no " +
            ") " +
            "SELECT agg.component_material_no, " +
            "       agg.component_desc, " +
            "       agg.total_requirement, " +
            "       agg.stock_quantity, " +
            "       agg.transit, " +
            "       agg.total_issue, " +
            "       agg.shortage_qty, " +
            "       agg.main_count, " +
            "       cml.main_material_nos " +
            "FROM ( " +
            "    SELECT main.component_material_no, " +
            "           main.component_desc, " +
            "           SUM(main.main_requirement * main.unit_usage) AS total_requirement, " +
            "           MAX(main.stock_quantity) AS stock_quantity, " +
            "           MAX(main.transit) AS transit, " +
            "           SUM(main.issue) AS total_issue, " +
            "           SUM(main.shortage_qty) AS shortage_qty, " +
            "           COUNT(DISTINCT main.main_material_no) AS main_count " +
            "    FROM material_shortage_report main " +
            "    WHERE main.deleted = 0 AND main.tenant_id = 1 " +
            "    <if test='componentMaterialNo != null and componentMaterialNo != \"\"'>" +
            "      AND main.component_material_no LIKE '%' || #{componentMaterialNo} || '%' " +
            "    </if>" +
            "    <if test='componentDesc != null and componentDesc != \"\"'>" +
            "      AND main.component_desc LIKE '%' || #{componentDesc} || '%' " +
            "    </if>" +
            "    GROUP BY main.component_material_no, main.component_desc " +
            ") agg " +
            "LEFT JOIN component_main_list cml ON cml.component_material_no = agg.component_material_no " +
            "<if test='onlyShortage != null and onlyShortage == true'>" +
            "WHERE agg.shortage_qty > 0 " +
            "</if>" +
            "ORDER BY agg.shortage_qty DESC" +
            "</script>")
    List<MaterialShortageComponentSummaryDO> selectComponentSummaryWithFilter(
            @Param("componentMaterialNo") String componentMaterialNo,
            @Param("componentDesc") String componentDesc,
            @Param("onlyShortage") Boolean onlyShortage);
}