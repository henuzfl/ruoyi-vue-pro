package cn.iocoder.yudao.module.buyer.service.buyerstock;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.buyer.controller.admin.buyerstock.vo.*;
import cn.iocoder.yudao.module.buyer.dal.dataobject.buyerstock.buyerStockDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 供应商库存 Service 接口
 *
 * @author 柳文
 */
public interface buyerStockService {

    /**
     * 创建供应商库存
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createbuyerStock(@Valid buyerStockSaveReqVO createReqVO);

    /**
     * 更新供应商库存
     *
     * @param updateReqVO 更新信息
     */
    void updatebuyerStock(@Valid buyerStockSaveReqVO updateReqVO);

    /**
     * 删除供应商库存
     *
     * @param id 编号
     */
    void deletebuyerStock(Long id);

    /**
     * 获得供应商库存
     *
     * @param id 编号
     * @return 供应商库存
     */
    buyerStockDO getbuyerStock(Long id);

    /**
     * 获得供应商库存分页
     *
     * @param pageReqVO 分页查询
     * @return 供应商库存分页
     */
    PageResult<buyerStockDO> getbuyerStockPage(buyerStockPageReqVO pageReqVO);

}