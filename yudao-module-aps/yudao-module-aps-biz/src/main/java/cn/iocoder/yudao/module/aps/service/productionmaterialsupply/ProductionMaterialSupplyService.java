package cn.iocoder.yudao.module.aps.service.productionmaterialsupply;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.aps.controller.admin.productionmaterialsupply.vo.ProductionMaterialSupplyPageReqVO;
import cn.iocoder.yudao.module.aps.controller.admin.productionmaterialsupply.vo.ProductionMaterialSupplyRespVO;

public interface ProductionMaterialSupplyService {

    PageResult<ProductionMaterialSupplyRespVO> getPage(ProductionMaterialSupplyPageReqVO pageReqVO);
}
