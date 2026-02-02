package cn.iocoder.yudao.module.buyer.service.supplierbuyer;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.buyer.controller.admin.supplierbuyer.vo.*;
import cn.iocoder.yudao.module.buyer.dal.dataobject.supplierbuyer.buyerSupplierBuyerDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 物料供应商采购员对应 Service 接口
 *
 * @author 芋道源码
 */
public interface buyerSupplierBuyerService {

    /**
     * 创建物料供应商采购员对应
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createbuyerSupplierBuyer(@Valid buyerSupplierBuyerSaveReqVO createReqVO);

    /**
     * 更新物料供应商采购员对应
     *
     * @param updateReqVO 更新信息
     */
    void updatebuyerSupplierBuyer(@Valid buyerSupplierBuyerSaveReqVO updateReqVO);

    /**
     * 删除物料供应商采购员对应
     *
     * @param id 编号
     */
    void deletebuyerSupplierBuyer(Long id);

    /**
     * 获得物料供应商采购员对应
     *
     * @param id 编号
     * @return 物料供应商采购员对应
     */
    buyerSupplierBuyerDO getbuyerSupplierBuyer(Long id);

    /**
     * 获得物料供应商采购员对应分页
     *
     * @param pageReqVO 分页查询
     * @return 物料供应商采购员对应分页
     */
    PageResult<buyerSupplierBuyerDO> getbuyerSupplierBuyerPage(buyerSupplierBuyerPageReqVO pageReqVO);

}