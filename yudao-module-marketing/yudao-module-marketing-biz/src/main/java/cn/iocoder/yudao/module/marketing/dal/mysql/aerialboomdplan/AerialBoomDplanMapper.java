package cn.iocoder.yudao.module.marketing.dal.mysql.aerialboomdplan;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.marketing.dal.dataobject.aerialboomdplan.AerialBoomDplanDO;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.marketing.controller.admin.aerialboomdplan.vo.*;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 高机臂式日计划 Mapper
 *
 * @author 柳文
 */
@Mapper
public interface AerialBoomDplanMapper extends BaseMapperX<AerialBoomDplanDO> {

    default PageResult<AerialBoomDplanDO> selectPage(AerialBoomDplanPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<AerialBoomDplanDO>()
                .eqIfPresent(AerialBoomDplanDO::getLineType, reqVO.getLineType())
                .eqIfPresent(AerialBoomDplanDO::getPreciseModel, reqVO.getPreciseModel())
                .eqIfPresent(AerialBoomDplanDO::getProductModel, reqVO.getProductModel())
                .eqIfPresent(AerialBoomDplanDO::getZpsModel, reqVO.getZpsModel())
                .eqIfPresent(AerialBoomDplanDO::getPreciseBom, reqVO.getPreciseBom())
                .eqIfPresent(AerialBoomDplanDO::getCarNo, reqVO.getCarNo())
                .eqIfPresent(AerialBoomDplanDO::getOrderNo, reqVO.getOrderNo())
                .eqIfPresent(AerialBoomDplanDO::getRemark, reqVO.getRemark())
                .eqIfPresent(AerialBoomDplanDO::getTradeVersion, reqVO.getTradeVersion())
                .eqIfPresent(AerialBoomDplanDO::getUnitCount, reqVO.getUnitCount())
                .eqIfPresent(AerialBoomDplanDO::getOnlinePlan, reqVO.getOnlinePlan())
                .eqIfPresent(AerialBoomDplanDO::getCompletePlan, reqVO.getCompletePlan())
                .betweenIfPresent(AerialBoomDplanDO::getReportDate, reqVO.getReportDate())
                .eqIfPresent(AerialBoomDplanDO::getCountry, reqVO.getCountry())
                .eqIfPresent(AerialBoomDplanDO::getContractNo, reqVO.getContractNo())
                .betweenIfPresent(AerialBoomDplanDO::getMarketingNoticeTime, reqVO.getMarketingNoticeTime())
                .betweenIfPresent(AerialBoomDplanDO::getOrderCreateTime, reqVO.getOrderCreateTime())
                .eqIfPresent(AerialBoomDplanDO::getPlate, "高机臂式")
                .betweenIfPresent(AerialBoomDplanDO::getImportTime, reqVO.getImportTime())
                .betweenIfPresent(AerialBoomDplanDO::getCreateTime, reqVO.getCreateTime())
                // ========== 以下为固定排序字段 ==========
                .orderByDesc(AerialBoomDplanDO::getImportTime)     // 1. 导入批次时间 倒序
                .orderByAsc(AerialBoomDplanDO::getPlate)           // 2. 板块
                .orderByAsc(AerialBoomDplanDO::getLineType)        // 3. 线别
                .orderByAsc(AerialBoomDplanDO::getPreciseModel)    // 4. 精准车型
                .orderByAsc(AerialBoomDplanDO::getProductModel)    // 5. 产品型号
                .orderByAsc(AerialBoomDplanDO::getZpsModel)        // 6. ZPS型号
                .orderByAsc(AerialBoomDplanDO::getPreciseBom)      // 7. 精准BOM
                .orderByAsc(AerialBoomDplanDO::getCarNo)           // 8. 车号
                .orderByAsc(AerialBoomDplanDO::getOnlinePlan)      // 9. 上线计划
                .orderByAsc(AerialBoomDplanDO::getCompletePlan)    // 10. 成台计划
                .orderByAsc(AerialBoomDplanDO::getReportDate)      // 11. 报缴日期
                .orderByAsc(AerialBoomDplanDO::getOrderNo));       // 12. 订单号
    }

    /**
     * 获取所有不同的导入批次日期（倒序）
     */
    @InterceptorIgnore(tenantLine = "true")
    List<String> selectDistinctImportDates(@Param("plate") String plate);

    default List<String> selectDistinctImportDates() {
        return selectDistinctImportDates(null);
    }

    @Select("SELECT DISTINCT plate FROM marketing_aerial_boom_dplan WHERE plate IS NOT NULL ORDER BY plate")
    @InterceptorIgnore(tenantLine = "true")
    List<String> selectDistinctPlates();
}