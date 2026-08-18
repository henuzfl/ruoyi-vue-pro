package cn.iocoder.yudao.module.aps.dal.mysql.purchasefeedback;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.aps.controller.admin.purchasefeedback.vo.PurchaseFeedbackPageReqVO;
import cn.iocoder.yudao.module.aps.dal.dataobject.purchasefeedback.PurchaseFeedbackDO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 采购反馈 Mapper
 *
 * @author 柳文
 */
@Mapper
public interface PurchaseFeedbackMapper extends BaseMapperX<PurchaseFeedbackDO> {

    default PageResult<PurchaseFeedbackDO> selectPage(PurchaseFeedbackPageReqVO reqVO) {
        LambdaQueryWrapperX<PurchaseFeedbackDO> wrapper = new LambdaQueryWrapperX<>();
        wrapper.eqIfPresent(PurchaseFeedbackDO::getOrderNo, reqVO.getOrderNo())
                .eqIfPresent(PurchaseFeedbackDO::getPurchaseMaterial, reqVO.getPurchaseMaterial())
                .likeIfPresent(PurchaseFeedbackDO::getFeedbackRemark, reqVO.getFeedbackRemark())
                .betweenIfPresent(PurchaseFeedbackDO::getScheduleTime, reqVO.getScheduleTime())
                .orderByDesc(PurchaseFeedbackDO::getCreateTime); // 按创建时间倒序
        return selectPage(reqVO, wrapper);
    }

    /**
     * 批量查询反馈（根据订单号、排产时间、采购物料组合）
     * @param keys 每个元素包含 orderNo, scheduleTime, purchaseMaterial
     * @return 反馈列表
     */
    default List<PurchaseFeedbackDO> selectListByKeys(List<Map<String, Object>> keys) {
        if (keys == null || keys.isEmpty()) {
            return Collections.emptyList();
        }
        LambdaQueryWrapper<PurchaseFeedbackDO> wrapper = new LambdaQueryWrapper<>();
        // 使用 OR 条件组合
        for (Map<String, Object> key : keys) {
            String orderNo = (String) key.get("orderNo");
            Date scheduleTime = (Date) key.get("scheduleTime");
            String purchaseMaterial = (String) key.get("purchaseMaterial");
            wrapper.or(w -> {
                w.eq(PurchaseFeedbackDO::getOrderNo, orderNo);
                if (scheduleTime != null) {
                    w.eq(PurchaseFeedbackDO::getScheduleTime, scheduleTime);
                }
                w.eq(PurchaseFeedbackDO::getPurchaseMaterial, purchaseMaterial);
            });
        }
        return this.selectList(wrapper);
    }


    /**
     * 批量获取序列生成的 ID（用于 Oracle 批量插入）
     */
    @Select("SELECT SEQ_APS_PURCHASE_FEEDBACK.NEXTVAL FROM DUAL CONNECT BY LEVEL <= #{count}")
    List<Long> selectNextIds(int count);

    void batchInsert(@Param("list") List<PurchaseFeedbackDO> list);


}