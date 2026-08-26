package cn.iocoder.yudao.module.aps.dal.mysql.productionmaterialsupply;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.aps.controller.admin.productionmaterialsupply.vo.ProductionMaterialSupplyPageReqVO;
import cn.iocoder.yudao.module.aps.dal.dataobject.productionmaterialsupply.ProductionMaterialAvailableRow;
import cn.iocoder.yudao.module.aps.dal.dataobject.productionmaterialsupply.ProductionMaterialDemandRow;
import cn.iocoder.yudao.module.aps.dal.dataobject.productionmaterialsupply.ProductionMaterialPurchaseRow;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ProductionMaterialSupplyMapper extends BaseMapperX<Object> {

    @InterceptorIgnore(tenantLine = "true")
    List<ProductionMaterialDemandRow> selectDemandRows(
            @Param("reqVO") ProductionMaterialSupplyPageReqVO reqVO);

    @InterceptorIgnore(tenantLine = "true")
    List<ProductionMaterialAvailableRow> selectAvailableRows(
            @Param("materialNos") List<String> materialNos);

    @InterceptorIgnore(tenantLine = "true")
    List<ProductionMaterialPurchaseRow> selectPurchaseRows(
            @Param("keys") List<ProductionMaterialDemandRow> keys);
}
