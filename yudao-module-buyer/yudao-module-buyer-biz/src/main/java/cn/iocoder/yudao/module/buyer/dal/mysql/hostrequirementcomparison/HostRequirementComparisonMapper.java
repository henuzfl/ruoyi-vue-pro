package cn.iocoder.yudao.module.buyer.dal.mysql.hostrequirementcomparison;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.buyer.controller.admin.hostrequirementcomparison.vo.HostRequirementComparisonPageReqVO;
import cn.iocoder.yudao.module.buyer.controller.admin.hostrequirementcomparison.vo.HostRequirementComparisonRespVO;
import cn.iocoder.yudao.module.buyer.controller.admin.hostrequirementcomparisondiff.vo.HostRequirementComparisonDiffRespVO;
import cn.iocoder.yudao.module.buyer.controller.admin.hostrequirementcomparisondiff.vo.HostRequirementComparisonDiffVO;
import cn.iocoder.yudao.module.buyer.dal.dataobject.hostrequirementcomparison.HostRequirementComparisonDO;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface HostRequirementComparisonMapper extends BaseMapperX<HostRequirementComparisonDO> {

//    default PageResult<HostRequirementComparisonDO> selectPage(HostRequirementComparisonPageReqVO reqVO) {
//        return selectPage(reqVO, new LambdaQueryWrapperX<HostRequirementComparisonDO>()
//                .eqIfPresent(HostRequirementComparisonDO::getProductModel, reqVO.getProductModel())
//                .eqIfPresent(HostRequirementComparisonDO::getSeqNo2026, reqVO.getSeqNo2026())
//                .eqIfPresent(HostRequirementComparisonDO::getBareMachineOrderNo, reqVO.getBareMachineOrderNo())
//                .eqIfPresent(HostRequirementComparisonDO::getMaterialNo, reqVO.getMaterialNo())
//                .betweenIfPresent(HostRequirementComparisonDO::getChassisOnlinePlanDate, reqVO.getChassisOnlinePlanDate())
//                .betweenIfPresent(HostRequirementComparisonDO::getCreateTime, reqVO.getCreateTime())
//                .orderByDesc(HostRequirementComparisonDO::getId));
//    }

    /**
     * 分页查询主机需求对比数据（基于实时SQL）
     *
     * @param
     * @param
     * @return 分页结果
     */

    @InterceptorIgnore(tenantLine = "true")
    List<HostRequirementComparisonRespVO> selectComparisonPage(
            @Param("pageNo") Long pageNo,
            @Param("pageSize") Long pageSize,
            @Param("currentDate") String currentDate,
            @Param("compareDate") String compareDate,
            @Param("productModel") String productModel,
            @Param("seqNo2026") String seqNo2026,
            @Param("bareMachineOrderNo") String bareMachineOrderNo,
            @Param("materialNo") String materialNo,
            @Param("fallbackMatched") Integer fallbackMatched
    );

    /**
     * 查询主机需求对比总数
     */
    @InterceptorIgnore(tenantLine = "true")
    Long selectComparisonCount(
            @Param("currentDate") String currentDate,
            @Param("compareDate") String compareDate,
            @Param("productModel") String productModel,
            @Param("seqNo2026") String seqNo2026,
            @Param("bareMachineOrderNo") String bareMachineOrderNo,
            @Param("materialNo") String materialNo,
            @Param("fallbackMatched") Integer fallbackMatched
    );

    /**
     * 查询主机需求对比两个月对比
     */
    @InterceptorIgnore(tenantLine = "true")
    List<HostRequirementComparisonRespVO> selectByMonth(
            @Param("month") String month,          // 格式：yyyy-MM
            @Param("productModel") String productModel,
            @Param("seqNo2026") String seqNo2026,
            @Param("bareMachineOrderNo") String bareMachineOrderNo,
            @Param("materialNo") String materialNo
    );

    // 分页查询对比数据
    @InterceptorIgnore(tenantLine = "true")
    List<HostRequirementComparisonDiffVO> selectDiffPage(
            @Param("pageNo") Long pageNo,
            @Param("pageSize") Long pageSize,
            @Param("currentDate") String currentDate,
            @Param("compareDate") String compareDate,
            @Param("productModel") String productModel,
            @Param("seqNo2026") String seqNo2026,
            @Param("bareMachineOrderNo") String bareMachineOrderNo,
            @Param("materialNo") String materialNo
    );

    // 查询对比数据总数
    @InterceptorIgnore(tenantLine = "true")
    Long selectDiffCount(
            @Param("currentDate") String currentDate,
            @Param("compareDate") String compareDate,
            @Param("productModel") String productModel,
            @Param("seqNo2026") String seqNo2026,
            @Param("bareMachineOrderNo") String bareMachineOrderNo,
            @Param("materialNo") String materialNo
    );

    // 导出EXCEL
    @InterceptorIgnore(tenantLine = "true")
    List<HostRequirementComparisonRespVO> selectAllForExport(
            @Param("currentDate") String currentDate,
            @Param("compareDate") String compareDate,
            @Param("productModel") String productModel,
            @Param("seqNo2026") String seqNo2026,
            @Param("bareMachineOrderNo") String bareMachineOrderNo,
            @Param("materialNo") String materialNo,
            @Param("fallbackMatched") Integer fallbackMatched
    );

    @InterceptorIgnore(tenantLine = "true")
    List<HostRequirementComparisonDiffVO> selectDiffList(
            @Param("currentDate") String currentDate,
            @Param("compareDate") String compareDate,
            @Param("productModel") String productModel,
            @Param("seqNo2026") String seqNo2026,
            @Param("bareMachineOrderNo") String bareMachineOrderNo,
            @Param("materialNo") String materialNo
    );
}