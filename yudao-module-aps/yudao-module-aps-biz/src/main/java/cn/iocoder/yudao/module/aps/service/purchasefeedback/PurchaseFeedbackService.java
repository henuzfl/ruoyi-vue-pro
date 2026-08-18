package cn.iocoder.yudao.module.aps.service.purchasefeedback;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.aps.controller.admin.purchasefeedback.vo.PurchaseFeedbackImportVO;
import cn.iocoder.yudao.module.aps.controller.admin.purchasefeedback.vo.PurchaseFeedbackPageReqVO;
import cn.iocoder.yudao.module.aps.controller.admin.purchasefeedback.vo.PurchaseFeedbackSaveReqVO;
import cn.iocoder.yudao.module.aps.dal.dataobject.purchasefeedback.PurchaseFeedbackDO;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * 采购反馈 Service 接口
 *
 * @author 柳文
 */
public interface PurchaseFeedbackService {

    /**
     * 创建采购反馈
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createPurchaseFeedback(@Valid PurchaseFeedbackSaveReqVO createReqVO);

    /**
     * 更新采购反馈
     *
     * @param updateReqVO 更新信息
     */
    void updatePurchaseFeedback(@Valid PurchaseFeedbackSaveReqVO updateReqVO);

    /**
     * 删除采购反馈
     *
     * @param id 编号
     */
    void deletePurchaseFeedback(Long id);

    /**
     * 获得采购反馈
     *
     * @param id 编号
     * @return 采购反馈
     */
    PurchaseFeedbackDO getPurchaseFeedback(Long id);

    /**
     * 获得采购反馈分页
     *
     * @param pageReqVO 分页查询
     * @return 采购反馈分页
     */
    PageResult<PurchaseFeedbackDO> getPurchaseFeedbackPage(PurchaseFeedbackPageReqVO pageReqVO);

    /**
     * 导入采购反馈
     *
     * @param importList 导入数据列表
     * @return 导入结果（成功条数）
     */
    Integer importPurchaseFeedback(List<PurchaseFeedbackImportVO> importList);

    /**
     * 根据订单号、排产时间、采购物料批量查询反馈
     * @param keys 每个元素包含 orderNo, scheduleTime, purchaseMaterial
     * @return 反馈列表
     */
    List<PurchaseFeedbackDO> listByKeys(List<Map<String, Object>> keys);
}