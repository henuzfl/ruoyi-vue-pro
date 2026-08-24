package cn.iocoder.yudao.module.buyer.dal.mysql.overseasinventory;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.buyer.controller.admin.overseasinventory.vo.OverseasInventoryPageReqVO;
import cn.iocoder.yudao.module.buyer.dal.dataobject.overseasinventory.OverseasInventoryDO;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface OverseasInventoryMapper extends BaseMapperX<OverseasInventoryDO> {

    default PageResult<OverseasInventoryDO> selectPage(OverseasInventoryPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<OverseasInventoryDO>()
                .eqIfPresent(OverseasInventoryDO::getOwnerCode, reqVO.getOwnerCode())
                .eqIfPresent(OverseasInventoryDO::getSupplierCode, reqVO.getSupplierCode())
                .eqIfPresent(OverseasInventoryDO::getItemCode, reqVO.getItemCode())
                .likeIfPresent(OverseasInventoryDO::getItemName, reqVO.getItemName())
                .orderByAsc(OverseasInventoryDO::getOwnerCode)
                .orderByAsc(OverseasInventoryDO::getItemCode));
    }

    @InterceptorIgnore(tenantLine = "true")
    int deleteAllPhysical();

    @InterceptorIgnore(tenantLine = "true")
    void batchInsert(@Param("list") List<OverseasInventoryDO> list);
}
