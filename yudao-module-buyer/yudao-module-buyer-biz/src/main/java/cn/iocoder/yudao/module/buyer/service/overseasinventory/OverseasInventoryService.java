package cn.iocoder.yudao.module.buyer.service.overseasinventory;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.buyer.controller.admin.overseasinventory.vo.*;
import cn.iocoder.yudao.module.buyer.dal.dataobject.overseasinventory.OverseasInventoryDO;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

public interface OverseasInventoryService {
    BigDecimal createOverseasInventory(@Valid OverseasInventorySaveReqVO reqVO);
    void updateOverseasInventory(@Valid OverseasInventorySaveReqVO reqVO);
    void deleteOverseasInventory(BigDecimal id);
    OverseasInventoryDO getOverseasInventory(BigDecimal id);
    PageResult<OverseasInventoryDO> getOverseasInventoryPage(OverseasInventoryPageReqVO reqVO);
    int importOverseasInventory(List<OverseasInventoryImportReqVO> list);
}
