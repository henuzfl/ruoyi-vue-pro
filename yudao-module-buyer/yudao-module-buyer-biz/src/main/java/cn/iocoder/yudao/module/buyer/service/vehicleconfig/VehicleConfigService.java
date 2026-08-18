package cn.iocoder.yudao.module.buyer.service.vehicleconfig;

import java.math.BigDecimal;
import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.buyer.controller.admin.vehicleconfig.vo.*;
import cn.iocoder.yudao.module.buyer.dal.dataobject.vehicleconfig.VehicleConfigDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import org.apache.ibatis.annotations.Param;

/**
 * 主机车型配置 Service 接口
 *
 * @author 柳文
 */
public interface VehicleConfigService {

    /**
     * 创建主机车型配置
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    BigDecimal createVehicleConfig(@Valid VehicleConfigSaveReqVO createReqVO);

    /**
     * 更新主机车型配置
     *
     * @param updateReqVO 更新信息
     */
    void updateVehicleConfig(@Valid VehicleConfigSaveReqVO updateReqVO);

    /**
     * 删除主机车型配置
     *
     * @param id 编号
     */
    void deleteVehicleConfig(BigDecimal id);

    /**
     * 获得主机车型配置
     *
     * @param id 编号
     * @return 主机车型配置
     */
    VehicleConfigDO getVehicleConfig(BigDecimal id);

    /**
     * 获得主机车型配置分页
     *
     * @param pageReqVO 分页查询
     * @return 主机车型配置分页
     */
    PageResult<VehicleConfigDO> getVehicleConfigPage(VehicleConfigPageReqVO pageReqVO);

    /**
     * 批量导入主机车型配置
     * @param importVOList 导入数据列表
     * @return 成功导入条数
     */
    int importVehicleConfig(List<VehicleConfigImportReqVO> importVOList);
}