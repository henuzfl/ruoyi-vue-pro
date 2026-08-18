package cn.iocoder.yudao.module.marketing.dal.mysql.concretebom;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.marketing.dal.dataobject.concretebom.ConcreteBomDO;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.marketing.controller.admin.concretebom.vo.*;
import org.apache.ibatis.annotations.Select;

/**
 * 混凝土BOM Mapper
 *
 * @author 柳文
 */
@Mapper
public interface ConcreteBomMapper extends BaseMapperX<ConcreteBomDO> {

    default PageResult<ConcreteBomDO> selectPage(ConcreteBomPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ConcreteBomDO>()
                .eqIfPresent(ConcreteBomDO::getVehicleModel, reqVO.getVehicleModel())
                .likeIfPresent(ConcreteBomDO::getCylinderName, reqVO.getCylinderName())
                .eqIfPresent(ConcreteBomDO::getSbpCode, reqVO.getSbpCode())
                .eqIfPresent(ConcreteBomDO::getConfig, reqVO.getConfig())
                .betweenIfPresent(ConcreteBomDO::getImportTime, reqVO.getImportTime())
                .betweenIfPresent(ConcreteBomDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(ConcreteBomDO::getId));
    }

    /**
     * 对比最新批次与上一批次的配置差异
     * @return 差异列表
     */
    @Select("" +
            "SELECT a.VEHICLE_MODEL,a.SBP_CODE,a.config as CURRENT_CONFIG," +
            "b.config as PREVIOUS_CONFIG,(CASE WHEN b.config IS NULL THEN '新增' WHEN TO_NUMBER(a.config) = TO_NUMBER(b.config) THEN '无差异' ELSE '存在差异' END) AS STATE " +
            " FROM concrete_bom a left join concrete_bom b " +
            " on a.vehicle_model = b.vehicle_model" +
            " AND a.sbp_code = b.sbp_code" +
            "   and b.deleted = 1" +
            "   left join (SELECT max(IMPORT_TIME) as IMPORT_TIME FROM concrete_bom) c" +
            " on b.IMPORT_TIME < c.IMPORT_TIME" +
            " WHERE a.IMPORT_TIME = (SELECT max(IMPORT_TIME) FROM concrete_bom)" +
            " ORDER BY a.vehicle_model, a.sbp_code" +
            "  ")
    @InterceptorIgnore(tenantLine = "true")
    List<Map<String, Object>> compareLatestWithPrevious();

}