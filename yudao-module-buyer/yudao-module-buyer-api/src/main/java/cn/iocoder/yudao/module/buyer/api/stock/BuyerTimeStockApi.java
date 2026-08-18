package cn.iocoder.yudao.module.buyer.api.stock;

import cn.iocoder.yudao.module.buyer.api.stock.dto.BuyerTimeStockRespDTO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.buyer.api.stock.dto.BuyerTimeStockQueryReqDTO;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * 买家实时库存 API（供 wm 模块调用）
 */
public interface BuyerTimeStockApi {

    /**
     * 分页查询库存
     */
    PageResult<BuyerTimeStockRespDTO> getStockPage(@Valid BuyerTimeStockQueryReqDTO reqDTO);

    /**
     * 根据物料号查询库存
     */
    BuyerTimeStockRespDTO getStockByMaterialNo(@NotNull String materialNo);
}