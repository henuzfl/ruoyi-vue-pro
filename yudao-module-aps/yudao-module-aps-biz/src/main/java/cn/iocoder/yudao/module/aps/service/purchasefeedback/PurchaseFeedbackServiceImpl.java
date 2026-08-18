package cn.iocoder.yudao.module.aps.service.purchasefeedback;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.aps.controller.admin.purchasefeedback.vo.PurchaseFeedbackImportVO;
import cn.iocoder.yudao.module.aps.controller.admin.purchasefeedback.vo.PurchaseFeedbackPageReqVO;
import cn.iocoder.yudao.module.aps.controller.admin.purchasefeedback.vo.PurchaseFeedbackSaveReqVO;
import cn.iocoder.yudao.module.aps.dal.dataobject.purchasefeedback.PurchaseFeedbackDO;
import cn.iocoder.yudao.module.aps.dal.mysql.purchasefeedback.PurchaseFeedbackMapper;
import com.baomidou.dynamic.datasource.annotation.DS;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;


import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import cn.hutool.core.util.IdUtil;


import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.aps.enums.ErrorCodeConstants.PURCHASE_FEEDBACK_NOT_EXISTS;

/**
 * 采购反馈 Service 实现类
 *
 * @author 柳文
 */
@Service
@DS("oracle")
@Validated
@Slf4j
public class PurchaseFeedbackServiceImpl implements PurchaseFeedbackService {

    @Resource
    private PurchaseFeedbackMapper purchaseFeedbackMapper;

    @Override
    public Long createPurchaseFeedback(PurchaseFeedbackSaveReqVO createReqVO) {
        PurchaseFeedbackDO feedback = BeanUtils.toBean(createReqVO, PurchaseFeedbackDO.class);
        purchaseFeedbackMapper.insert(feedback);
        return feedback.getId();
    }

    @Override
    public void updatePurchaseFeedback(PurchaseFeedbackSaveReqVO updateReqVO) {
        validateExists(updateReqVO.getId());
        PurchaseFeedbackDO updateObj = BeanUtils.toBean(updateReqVO, PurchaseFeedbackDO.class);
        purchaseFeedbackMapper.updateById(updateObj);
    }

    @Override
    public void deletePurchaseFeedback(Long id) {
        validateExists(id);
        purchaseFeedbackMapper.deleteById(id);
    }

    private void validateExists(Long id) {
        if (purchaseFeedbackMapper.selectById(id) == null) {
            throw exception(PURCHASE_FEEDBACK_NOT_EXISTS);
        }
    }

    @Override
    public PurchaseFeedbackDO getPurchaseFeedback(Long id) {
        return purchaseFeedbackMapper.selectById(id);
    }

    @Override
    public PageResult<PurchaseFeedbackDO> getPurchaseFeedbackPage(PurchaseFeedbackPageReqVO pageReqVO) {
        return purchaseFeedbackMapper.selectPage(pageReqVO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Integer importPurchaseFeedback(List<PurchaseFeedbackImportVO> importList) {
        if (CollectionUtils.isEmpty(importList)) {
            return 0;
        }

        LocalDateTime now = LocalDateTime.now();
        List<PurchaseFeedbackDO> insertList = new ArrayList<>();

        for (PurchaseFeedbackImportVO vo : importList) {
            PurchaseFeedbackDO feedback = new PurchaseFeedbackDO();
            // 不手动设置 ID，先留空
            feedback.setOrderNo(vo.getOrderNo());
            feedback.setScheduleTime(vo.getScheduleTime());
            feedback.setPurchaseMaterial(vo.getPurchaseMaterial());
            feedback.setFeedbackRemark(vo.getFeedbackRemark());
            feedback.setCreateTime(now);
            feedback.setUpdateTime(now);
            feedback.setCreator("1");
            feedback.setUpdater("1");
            insertList.add(feedback);
        }

        // 批量获取序列 ID（与主计划逻辑一致）
        int totalSize = insertList.size();
        List<Long> nextIds = purchaseFeedbackMapper.selectNextIds(totalSize);
        if (nextIds.size() != totalSize) {
            throw new RuntimeException("批量获取序列值失败，期望获取 " + totalSize + " 个，实际获取 " + nextIds.size() + " 个");
        }
        for (int j = 0; j < totalSize; j++) {
            insertList.get(j).setId(nextIds.get(j));
        }

        // 分批批量插入
        int batchSize = 50;
        int totalInserted = 0;
        for (int i = 0; i < totalSize; i += batchSize) {
            int end = Math.min(i + batchSize, totalSize);
            List<PurchaseFeedbackDO> batch = insertList.subList(i, end);
            purchaseFeedbackMapper.batchInsert(batch);
            totalInserted += batch.size();
        }
        return totalInserted;
    }

    @Override
    public List<PurchaseFeedbackDO> listByKeys(List<Map<String, Object>> keys) {
        return purchaseFeedbackMapper.selectListByKeys(keys);
    }

}