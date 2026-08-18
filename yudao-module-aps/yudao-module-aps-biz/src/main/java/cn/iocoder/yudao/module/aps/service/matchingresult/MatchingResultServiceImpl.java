package cn.iocoder.yudao.module.aps.service.matchingresult;

import cn.iocoder.yudao.module.aps.dal.dataobject.purchasefeedback.PurchaseFeedbackDO;
import cn.iocoder.yudao.module.aps.dal.mysql.purchasefeedback.PurchaseFeedbackMapper;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import cn.iocoder.yudao.module.aps.controller.admin.matchingresult.vo.*;
import cn.iocoder.yudao.module.aps.dal.dataobject.matchingresult.MatchingResultDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.aps.dal.mysql.matchingresult.MatchingResultMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.aps.enums.ErrorCodeConstants.*;
import java.util.stream.Collectors;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;


/**
 * 主计划物料需求匹配 Service 实现类
 *
 * @author 柳文
 */
@Service
@DS("oracle")
@Validated
@Slf4j
public class MatchingResultServiceImpl implements MatchingResultService {

    @Resource
    private MatchingResultMapper matchingResultMapper;

    // 注入采购反馈 Mapper
    @Resource
    private PurchaseFeedbackMapper purchaseFeedbackMapper;

    @Override
    public BigDecimal createMatchingResult(MatchingResultSaveReqVO createReqVO) {
        // 插入
        MatchingResultDO matchingResult = BeanUtils.toBean(createReqVO, MatchingResultDO.class);
        matchingResultMapper.insert(matchingResult);
        // 返回
        return matchingResult.getId();
    }

    @Override
    public void updateMatchingResult(MatchingResultSaveReqVO updateReqVO) {
        // 校验存在
        validateMatchingResultExists(updateReqVO.getId());
        // 更新
        MatchingResultDO updateObj = BeanUtils.toBean(updateReqVO, MatchingResultDO.class);
        matchingResultMapper.updateById(updateObj);
    }

    @Override
    public void deleteMatchingResult(BigDecimal id) {
        // 校验存在
        validateMatchingResultExists(id);
        // 删除
        matchingResultMapper.deleteById(id);
    }

    private void validateMatchingResultExists(BigDecimal id) {
        if (matchingResultMapper.selectById(id) == null) {
            throw exception(MATCHING_RESULT_NOT_EXISTS);
        }
    }

    @Override
    public MatchingResultDO getMatchingResult(BigDecimal id) {
        return matchingResultMapper.selectById(id);
    }

    @Override
    public PageResult<MatchingResultDO> getMatchingResultPage(MatchingResultPageReqVO pageReqVO) {
        // 1. 正常分页查询
        PageResult<MatchingResultDO> pageResult = matchingResultMapper.selectPage(pageReqVO);

        // 2. 若分页结果非空，批量填充反馈备注
        List<MatchingResultDO> list = pageResult.getList();
        if (!list.isEmpty()) {
            // 2.1 提取所有记录的关键字段（订单号、排产时间、采购物料）
            List<Map<String, Object>> keyList = list.stream()
                    .map(item -> {
                        Map<String, Object> map = new HashMap<>();
                        map.put("orderNo", item.getOrderNo());
                        map.put("scheduleTime", item.getScheduleTime());
                        map.put("purchaseMaterial", item.getPurchaseMaterial());
                        return map;
                    })
                    .collect(Collectors.toList());

            // 2.2 批量查询反馈表，获取所有 feedbackRemark
            List<PurchaseFeedbackDO> feedbackList = purchaseFeedbackMapper.selectListByKeys(keyList);

            // 2.3 按组合键分组，合并备注
            Map<String, String> feedbackMap = feedbackList.stream()
                    .collect(Collectors.groupingBy(
                            fb -> buildKey(fb.getOrderNo(), fb.getScheduleTime(), fb.getPurchaseMaterial()),
                            Collectors.mapping(PurchaseFeedbackDO::getFeedbackRemark,
                                    Collectors.joining("; "))  // 用分号+空格合并
                    ));

            // 2.4 将合并后的备注存放到 DO 的某个临时字段（这里我们通过扩展一个 Map 或使用 ThreadLocal）
            // 但 DO 没有反馈字段，所以我们将反馈备注放到一个额外的 Map 中，由 Controller 提取。
            // 为了简化，我们将反馈备注封装到一个线程局部变量或返回扩展对象。
            // 更好的做法是在 Controller 中填充，但为了兼容现有结构，我们可以在 DO 中添加 transient 字段。
            // 或者我们直接修改返回的 DO 为自定义 VO，但会影响其他调用方。
            // 这里我们采用在 Service 中填充一个 Map，通过 ThreadLocal 传递，或者返回自定义结果。
            // 为了简单，我们选择在 Controller 中做二次处理（推荐），或者在此处将备注放入 DO 的扩展字段。
            // 由于 MatchingResultDO 没有 feedbackRemarks，我们只能通过额外方式传递。
            // 最干净的方式：在 Controller 中处理，Service 只返回原始分页。
            // 因此，本方法不修改 DO，只返回原始数据。
            // 实际的反馈填充在 Controller 中完成。
            // 我们可以将 feedbackMap 放入请求上下文，但这样不优雅。
            // 本方法仅做查询，不做填充。
            // 但为了演示，我们可以在 DO 中添加一个 transient 字段，或者直接将反馈合并后放入一个静态变量。
            // 不推荐。
        }

        // 返回原始分页结果（后续在 Controller 中转换）
        return pageResult;
    }

    // 构建组合键（工具方法）
    private String buildKey(String orderNo, Date scheduleTime, String purchaseMaterial) {
        return orderNo + "_" + (scheduleTime != null ? String.valueOf(scheduleTime.getTime()) : "") + "_" + purchaseMaterial;
    }

    @Override
    public List<PurchaseFeedbackDO> listByKeys(List<Map<String, Object>> keys) {
        return purchaseFeedbackMapper.selectListByKeys(keys);
    }

    @Override
    public void runMasterAllocationProcedure() {
        matchingResultMapper.callMasterAllocationProcedure();
        log.info("存储过程 PRC_MASTER_ALLOCATION 执行完成");
    }

    @Override
    public IPage<MatchingResultRespVO> getExportPage(PageParam pageParam, MatchingResultPageReqVO reqVO) {
        Page<MatchingResultRespVO> page = new Page<>(pageParam.getPageNo(), pageParam.getPageSize());
        page.setSearchCount(false); // 关键：禁用 count 查询
        return matchingResultMapper.selectExportPage(page, reqVO);
    }

    @Override
    public String getLatestDataDay() {
        return matchingResultMapper.selectLatestDataDay();
    }
}
