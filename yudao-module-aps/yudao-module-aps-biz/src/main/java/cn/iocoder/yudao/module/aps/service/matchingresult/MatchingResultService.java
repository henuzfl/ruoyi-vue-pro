package cn.iocoder.yudao.module.aps.service.matchingresult;

import java.math.BigDecimal;
import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.aps.controller.admin.matchingresult.vo.*;
import cn.iocoder.yudao.module.aps.dal.dataobject.matchingresult.MatchingResultDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.module.aps.dal.dataobject.purchasefeedback.PurchaseFeedbackDO;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * 主计划物料需求匹配 Service 接口
 *
 * @author 柳文
 */
public interface MatchingResultService {

    /**
     * 创建主计划物料需求匹配
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    BigDecimal createMatchingResult(@Valid MatchingResultSaveReqVO createReqVO);

    /**
     * 更新主计划物料需求匹配
     *
     * @param updateReqVO 更新信息
     */
    void updateMatchingResult(@Valid MatchingResultSaveReqVO updateReqVO);

    /**
     * 删除主计划物料需求匹配
     *
     * @param id 编号
     */
    void deleteMatchingResult(BigDecimal id);

    /**
     * 获得主计划物料需求匹配
     *
     * @param id 编号
     * @return 主计划物料需求匹配
     */
    MatchingResultDO getMatchingResult(BigDecimal id);

    /**
     * 获得主计划物料需求匹配分页
     *
     * @param pageReqVO 分页查询
     * @return 主计划物料需求匹配分页
     */
    PageResult<MatchingResultDO> getMatchingResultPage(MatchingResultPageReqVO pageReqVO);

    /**
     * 执行主计划物料分配存储过程
     */
    void runMasterAllocationProcedure();

    /**
     * 根据订单号、排产时间、采购物料批量查询反馈
     * @param keys 每个元素包含 orderNo, scheduleTime, purchaseMaterial
     * @return 反馈列表
     */
    List<PurchaseFeedbackDO> listByKeys(List<Map<String, Object>> keys);

    /**
     * 分页查询导出数据（已包含反馈备注）
     * @param pageParam 分页参数
     * @param reqVO 查询条件
     * @return 分页结果
     */
    IPage<MatchingResultRespVO> getExportPage(PageParam pageParam, MatchingResultPageReqVO reqVO);

    // Service 接口
    String getLatestDataDay();
}