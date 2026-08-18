package cn.iocoder.yudao.module.marketing.dal.mysql.aerialboomwplan;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.marketing.dal.dataobject.aerialboomwplan.AerialBoomWplanDO;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.marketing.controller.admin.aerialboomwplan.vo.*;

/**
 * 高机臂式周计划 Mapper
 *
 * @author 柳文
 */
@Mapper
public interface AerialBoomWplanMapper extends BaseMapperX<AerialBoomWplanDO> {

    default PageResult<AerialBoomWplanDO> selectPage(AerialBoomWplanPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<AerialBoomWplanDO>()
                .eqIfPresent(AerialBoomWplanDO::getProductLine, reqVO.getProductLine())
                .eqIfPresent(AerialBoomWplanDO::getPreciseModel, reqVO.getPreciseModel())
                .eqIfPresent(AerialBoomWplanDO::getProductModel, reqVO.getProductModel())
                .eqIfPresent(AerialBoomWplanDO::getPlate, "高机臂式")
                .eqIfPresent(AerialBoomWplanDO::getPreciseBom, reqVO.getPreciseBom())
                .betweenIfPresent(AerialBoomWplanDO::getPlanDate, reqVO.getPlanDate())
                .eqIfPresent(AerialBoomWplanDO::getWeekNo, reqVO.getWeekNo())
                .betweenIfPresent(AerialBoomWplanDO::getWeekStartDate, reqVO.getWeekStartDate())
                .betweenIfPresent(AerialBoomWplanDO::getWeekEndDate, reqVO.getWeekEndDate())
                .eqIfPresent(AerialBoomWplanDO::getDailyQuantity, reqVO.getDailyQuantity())
                .eqIfPresent(AerialBoomWplanDO::getCarNumberRange, reqVO.getCarNumberRange())
                .eqIfPresent(AerialBoomWplanDO::getProductionLineType, reqVO.getProductionLineType())
                .eqIfPresent(AerialBoomWplanDO::getPlate, reqVO.getPlate())
                .betweenIfPresent(AerialBoomWplanDO::getImportTime, reqVO.getImportTime())
                .betweenIfPresent(AerialBoomWplanDO::getCreateTime, reqVO.getCreateTime())
                // ----- 固定排序（导入批次时间倒序，其余升序） -----
                .orderByDesc(AerialBoomWplanDO::getImportTime)   // 1. 导入批次时间 倒序
                .orderByAsc(AerialBoomWplanDO::getPlate)         // 2. 板块
                .orderByAsc(AerialBoomWplanDO::getProductLine)   // 3. 产品线
                .orderByAsc(AerialBoomWplanDO::getPreciseModel)  // 4. 精准车型
                .orderByAsc(AerialBoomWplanDO::getProductModel)  // 5. 产品型号
                .orderByAsc(AerialBoomWplanDO::getPreciseBom)    // 6. 精准BOM
                .orderByAsc(AerialBoomWplanDO::getWeekNo)        // 7. 周次
                .orderByAsc(AerialBoomWplanDO::getPlanDate)      // 8. 生产日期
        );
    }

    /**
     * 批量插入（适用于 Oracle，使用 insert all 或逐条，但 MyBatis-Plus 有性能问题）
     * 推荐使用 MyBatis-Plus 的 saveBatch 方法，它会自动分批。
     * 如果性能要求高，可自定义 SQL：
     * <insert id="insertBatch">
     *   INSERT INTO marketing_aerial_boom_wplan (...) VALUES
     *   <foreach collection="list" item="item" separator=",">
     *       (#{item.productLine}, ...)
     *   </foreach>
     * </insert>
     */
    int insertBatchSomeColumn(List<AerialBoomWplanDO> list);

}