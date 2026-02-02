package cn.iocoder.yudao.module.buyer.service.materialforecast;

import java.math.BigDecimal;
import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.buyer.controller.admin.materialforecast.vo.*;
import cn.iocoder.yudao.module.buyer.dal.dataobject.materialforecast.MaterialForecastDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 营销材料备料预测 Service 接口
 *
 * @author 柳文
 */
public interface MaterialForecastService {

    /**
     * 创建营销材料备料预测
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    BigDecimal createMaterialForecast(@Valid MaterialForecastSaveReqVO createReqVO);

    /**
     * 更新营销材料备料预测
     *
     * @param updateReqVO 更新信息
     */
    void updateMaterialForecast(@Valid MaterialForecastSaveReqVO updateReqVO);

    /**
     * 删除营销材料备料预测
     *
     * @param id 编号
     */
    void deleteMaterialForecast(BigDecimal id);

    /**
     * 获得营销材料备料预测
     *
     * @param id 编号
     * @return 营销材料备料预测
     */
    MaterialForecastDO getMaterialForecast(BigDecimal id);

    /**
     * 获得营销材料备料预测分页
     *
     * @param pageReqVO 分页查询
     * @return 营销材料备料预测分页
     */
    PageResult<MaterialForecastDO> getMaterialForecastPage(MaterialForecastPageReqVO pageReqVO);

}