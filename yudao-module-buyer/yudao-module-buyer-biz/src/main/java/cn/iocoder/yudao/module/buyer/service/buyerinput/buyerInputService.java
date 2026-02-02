package cn.iocoder.yudao.module.buyer.service.buyerinput;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.buyer.controller.admin.buyerinput.vo.*;
import cn.iocoder.yudao.module.buyer.dal.dataobject.buyerinput.buyerInputDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 需求输入 Service 接口
 *
 * @author 柳文
 */
public interface buyerInputService {

    /**
     * 创建需求输入
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createbuyerInput(@Valid buyerInputSaveReqVO createReqVO);

    /**
     * 更新需求输入
     *
     * @param updateReqVO 更新信息
     */
    void updatebuyerInput(@Valid buyerInputSaveReqVO updateReqVO);

    /**
     * 删除需求输入
     *
     * @param id 编号
     */
    void deletebuyerInput(Long id);

    /**
     * 获得需求输入
     *
     * @param id 编号
     * @return 需求输入
     */
    buyerInputDO getbuyerInput(Long id);

    /**
     * 获得需求输入分页
     *
     * @param pageReqVO 分页查询
     * @return 需求输入分页
     */
    PageResult<buyerInputDO> getbuyerInputPage(buyerInputPageReqVO pageReqVO);

}