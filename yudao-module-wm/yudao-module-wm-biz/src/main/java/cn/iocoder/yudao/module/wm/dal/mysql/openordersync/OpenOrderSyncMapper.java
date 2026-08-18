package cn.iocoder.yudao.module.wm.dal.mysql.openordersync;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.wm.controller.admin.openordersync.vo.OpenOrderPageReqVO;
import cn.iocoder.yudao.module.wm.dal.dataobject.openordersync.SyncOpenOrderDO;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 采购未清订单 Mapper
 *
 * @author 柳文
 */
@Mapper
public interface OpenOrderSyncMapper extends BaseMapperX<SyncOpenOrderDO> {

    default PageResult<SyncOpenOrderDO> selectPage(OpenOrderPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<SyncOpenOrderDO>()
                .betweenIfPresent(SyncOpenOrderDO::getOrderDate, reqVO.getOrderDate())
                .eqIfPresent(SyncOpenOrderDO::getBuyerOrderNo, reqVO.getBuyerOrderNo())
                .eqIfPresent(SyncOpenOrderDO::getLineItem, reqVO.getLineItem())
                .eqIfPresent(SyncOpenOrderDO::getMaterialNo, reqVO.getMaterialNo())
                .eqIfPresent(SyncOpenOrderDO::getMaterialDesc, reqVO.getMaterialDesc())
                .eqIfPresent(SyncOpenOrderDO::getOrderQty, reqVO.getOrderQty())
                .eqIfPresent(SyncOpenOrderDO::getReceivedQty, reqVO.getReceivedQty())
                .eqIfPresent(SyncOpenOrderDO::getOpenQty, reqVO.getOpenQty())
                .eqIfPresent(SyncOpenOrderDO::getUnit, reqVO.getUnit())
                .betweenIfPresent(SyncOpenOrderDO::getRequiredArrivalDate, reqVO.getRequiredArrivalDate())
                .betweenIfPresent(SyncOpenOrderDO::getActualArrivalDate, reqVO.getActualArrivalDate())
                .eqIfPresent(SyncOpenOrderDO::getSupplierDesc, reqVO.getSupplierDesc())
                .eqIfPresent(SyncOpenOrderDO::getCustomer, reqVO.getCustomer())
                .eqIfPresent(SyncOpenOrderDO::getBuyerGroup, reqVO.getBuyerGroup())
                .eqIfPresent(SyncOpenOrderDO::getDocumentType, reqVO.getDocumentType())
                .eqIfPresent(SyncOpenOrderDO::getProductionOrderNo, reqVO.getProductionOrderNo())
                .eqIfPresent(SyncOpenOrderDO::getBrandInfo, reqVO.getBrandInfo())
                .eqIfPresent(SyncOpenOrderDO::getUnitPrice, reqVO.getUnitPrice())
                .eqIfPresent(SyncOpenOrderDO::getSupplierCode, reqVO.getSupplierCode())
                .eqIfPresent(SyncOpenOrderDO::getReceivingWarehouse, reqVO.getReceivingWarehouse())
                .eqIfPresent(SyncOpenOrderDO::getTotalAmount, reqVO.getTotalAmount())
                .eqIfPresent(SyncOpenOrderDO::getBuyerReqNo, reqVO.getBuyerReqNo())
                .betweenIfPresent(SyncOpenOrderDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(SyncOpenOrderDO::getId));
    }

    /**
     * 分页查询数据列表
     * @param offset 起始行（从0开始）
     * @param limit  每页条数
     * @param reqVO  查询条件
     * @return 列表
     */
    @InterceptorIgnore(tenantLine = "true")
    List<SyncOpenOrderDO> selectPageList(@Param("offset") int offset,
                                         @Param("limit") int limit,
                                         @Param("reqVO") OpenOrderPageReqVO reqVO);

    /**
     * 查询总条数
     * @param reqVO 查询条件
     * @return 总数
     */
    @InterceptorIgnore(tenantLine = "true")
    long selectPageCount(@Param("reqVO") OpenOrderPageReqVO reqVO);

    /**
     * 物理删除全表数据
     * @return 删除条数
     */
    @InterceptorIgnore(tenantLine = "true")
    int physicalDeleteAll();

    /**
     * 批量插入（Oracle 专用，使用 INSERT ALL）
     * @param list 数据列表
     */
    @InterceptorIgnore(tenantLine = "true")   // 跳过租户拦截器
    void batchInsert(@Param("list") List<SyncOpenOrderDO> list);

    @Select("SELECT DISTINCT COMPONENT_MATERIAL_NO " +
            "FROM APS_MAIN_PLAN a " +
            "LEFT JOIN MATERIAL_BOM_IMPORT b ON a.ASSEMBLY_MATERIAL_NO = b.main_material_no " +
            "LEFT JOIN MATERIAL_MASTER_IMPORT d ON d.material_no = b.component_material_no " +
            "WHERE d.procurement_type = 'F' AND to_char(scheduled_date,'yyyy-mm') = to_char(sysdate,'yyyy-mm')" +
            "AND (b.PURCHASING_GROUP IS NULL OR b.PURCHASING_GROUP NOT IN ('400', '410'))")
    //@Select("SELECT '1081007425' AS COMPONENT_MATERIAL_NO FROM DUAL")
    @InterceptorIgnore(tenantLine = "true")   // 跳过租户拦截器
    @DS("oracle")
    List<String> selectComponentMaterialNos();

}