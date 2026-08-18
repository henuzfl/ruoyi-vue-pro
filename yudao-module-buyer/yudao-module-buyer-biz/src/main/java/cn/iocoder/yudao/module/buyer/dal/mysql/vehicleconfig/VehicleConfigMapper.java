package cn.iocoder.yudao.module.buyer.dal.mysql.vehicleconfig;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.buyer.controller.admin.vehicleconfig.vo.VehicleConfigPageReqVO;
import cn.iocoder.yudao.module.buyer.dal.dataobject.vehicleconfig.VehicleConfigDO;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Mapper
public interface VehicleConfigMapper extends BaseMapperX<VehicleConfigDO> {

    default PageResult<VehicleConfigDO> selectPage(VehicleConfigPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<VehicleConfigDO>()
                .eqIfPresent(VehicleConfigDO::getVehicleModel, reqVO.getVehicleModel())
                .eqIfPresent(VehicleConfigDO::getSeqNo2026, reqVO.getSeqNo2026())
                .eqIfPresent(VehicleConfigDO::getMaterialNo, reqVO.getMaterialNo())
                .eqIfPresent(VehicleConfigDO::getOrderNo, reqVO.getOrderNo())
                .eqIfPresent(VehicleConfigDO::getFactory, reqVO.getFactory())
                .eqIfPresent(VehicleConfigDO::getImportDate, reqVO.getImportDate())
                .betweenIfPresent(VehicleConfigDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(VehicleConfigDO::getImportDate)      // 导入日期降序
                .orderByAsc(VehicleConfigDO::getVehicleModel)     // 车型升序
                .orderByAsc(VehicleConfigDO::getSeqNo2026));      // 顺序号升序
    }

    /**
     * 根据车型+2026顺序号组合批量物理删除
     */
    @InterceptorIgnore(tenantLine = "true")
    int deleteByModelAndSeq(@Param("keys") List<Map<String, Object>> keys);

    /**
     * 批量插入
     */
    @InterceptorIgnore(tenantLine = "true")
    void batchInsert(@Param("list") List<VehicleConfigDO> list);

    /**
     * 根据导入日期物理删除数据
     */
    @InterceptorIgnore(tenantLine = "true")
    int deleteByImportDate(@Param("importDate") Date importDate);


    @Select("SELECT DISTINCT import_date FROM buyer_vehicle_config ORDER BY import_date DESC")
    List<Date> selectDistinctImportDates();
}