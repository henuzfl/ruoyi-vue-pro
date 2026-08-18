package cn.iocoder.yudao.module.aps.service.materialshortage;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.aps.controller.admin.materialshortage.vo.*;

import java.util.List;

public interface MaterialShortageService {

    /**
     * 分页查询物料缺口汇总
     */
    PageResult<MaterialShortageSummaryRespVO> getShortageSummaryPage(MaterialShortagePageReqVO pageReqVO);

    /**
     * 查询某个成品的缺口明细
     */
    List<MaterialShortageDetailRespVO> getShortageDetails(String mainMaterialNo);

    /**
     * 刷新缺口数据（调用存储过程）
     */
    void refreshShortageData();

    /**
     * 导出缺口汇总数据
     */
    List<MaterialShortageSummaryRespVO> exportShortageSummary(MaterialShortagePageReqVO reqVO);

    /**
     * 分页查询组件缺口汇总
     */
    PageResult<MaterialShortageComponentSummaryRespVO> getComponentShortagePage(MaterialShortageComponentPageReqVO reqVO);

    /**
     * 导出组件缺口汇总
     */
    List<MaterialShortageComponentSummaryRespVO> exportComponentShortage(MaterialShortageComponentPageReqVO reqVO);
}