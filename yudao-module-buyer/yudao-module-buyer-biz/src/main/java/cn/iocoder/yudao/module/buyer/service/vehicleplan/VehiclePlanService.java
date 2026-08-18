package cn.iocoder.yudao.module.buyer.service.vehicleplan;

import java.math.BigDecimal;
import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.buyer.controller.admin.vehicleplan.vo.*;
import cn.iocoder.yudao.module.buyer.dal.dataobject.vehicleplan.VehiclePlanDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import org.apache.ibatis.annotations.Param;

/**
 * 买家车辆营销计划表（主机厂计划） Service 接口
 *
 * @author 柳文
 */
public interface VehiclePlanService {

    /**
     * 创建买家车辆营销计划表（主机厂计划）
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    BigDecimal createVehiclePlan(@Valid VehiclePlanSaveReqVO createReqVO);

    /**
     * 更新买家车辆营销计划表（主机厂计划）
     *
     * @param updateReqVO 更新信息
     */
    void updateVehiclePlan(@Valid VehiclePlanSaveReqVO updateReqVO);

    /**
     * 删除买家车辆营销计划表（主机厂计划）
     *
     * @param id 编号
     */
    void deleteVehiclePlan(String id);

    /**
     * 获得买家车辆营销计划表（主机厂计划）
     *
     * @param id 编号
     * @return 买家车辆营销计划表（主机厂计划）
     */
    VehiclePlanDO getVehiclePlan(BigDecimal id);

    /**
     * 获得买家车辆营销计划表（主机厂计划）分页
     *
     * @param pageReqVO 分页查询
     * @return 买家车辆营销计划表（主机厂计划）分页
     */
    PageResult<VehiclePlanDO> getVehiclePlanPage(VehiclePlanPageReqVO pageReqVO);

    /**
     * 批量导入买家车辆营销计划
     *
     * @param importVOList 导入数据列表
     * @return 成功导入条数
     */
    int importVehiclePlan(List<VehiclePlanImportReqVO> importVOList);

}