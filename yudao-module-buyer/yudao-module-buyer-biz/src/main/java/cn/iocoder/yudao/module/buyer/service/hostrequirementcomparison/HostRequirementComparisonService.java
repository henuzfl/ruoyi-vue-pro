package cn.iocoder.yudao.module.buyer.service.hostrequirementcomparison;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.buyer.controller.admin.hostrequirementcomparison.vo.HostRequirementComparisonPageReqVO;
import cn.iocoder.yudao.module.buyer.controller.admin.hostrequirementcomparison.vo.HostRequirementComparisonRespVO;
import cn.iocoder.yudao.module.buyer.controller.admin.hostrequirementcomparisondiff.vo.HostRequirementComparisonDiffReqVO;
import cn.iocoder.yudao.module.buyer.controller.admin.hostrequirementcomparisondiff.vo.HostRequirementComparisonDiffRespVO;
import cn.iocoder.yudao.module.buyer.controller.admin.hostrequirementcomparisondiff.vo.HostRequirementComparisonDiffVO;

import java.util.List;

public interface HostRequirementComparisonService {

    /**
     * 分页查询主机需求对比（无差异）
     *
     * @param pageReqVO 分页参数
     * @return 分页结果
     */
    PageResult<HostRequirementComparisonRespVO> getComparisonPage(HostRequirementComparisonPageReqVO pageReqVO);

    // HostRequirementComparisonService 接口添加方法
    List<String> getAvailableImportDates();

    /**
     * 分页查询主机需求对比差异（两个月对比）
     *
     * @param diffReqVO 对比请求参数（包含显示月份、对比月份、分页等）
     * @return 分页差异结果
     */
    PageResult<HostRequirementComparisonDiffVO> getComparisonDiff(HostRequirementComparisonDiffReqVO diffReqVO);

    /**
     * 查询所有主机需求对比数据（不分页），用于导出
     *
     * @param pageReqVO 查询参数（复用分页请求VO，但仅使用过滤条件）
     * @return 所有符合条件的数据列表
     */
    List<HostRequirementComparisonRespVO> getAllForExport(HostRequirementComparisonPageReqVO pageReqVO);

    /*
        导出
     */

    List<HostRequirementComparisonDiffVO> getAllDiffForExport(HostRequirementComparisonDiffReqVO reqVO);



}