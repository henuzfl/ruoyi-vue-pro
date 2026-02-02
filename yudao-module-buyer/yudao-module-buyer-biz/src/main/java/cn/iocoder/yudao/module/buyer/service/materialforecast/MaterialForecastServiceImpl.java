package cn.iocoder.yudao.module.buyer.service.materialforecast;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import cn.iocoder.yudao.module.buyer.controller.admin.materialforecast.vo.*;
import cn.iocoder.yudao.module.buyer.dal.dataobject.materialforecast.MaterialForecastDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.buyer.dal.mysql.materialforecast.MaterialForecastMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.buyer.enums.ErrorCodeConstants.*;

/**
 * 营销材料备料预测 Service 实现类
 *
 * @author 柳文
 */
@Service
@DS("oracle") // 指定使用 Oracle 数据源
@Validated
public class MaterialForecastServiceImpl implements MaterialForecastService {

    @Resource
    private MaterialForecastMapper materialForecastMapper;

    @Override
    public BigDecimal createMaterialForecast(MaterialForecastSaveReqVO createReqVO) {
        // 插入
        MaterialForecastDO materialForecast = BeanUtils.toBean(createReqVO, MaterialForecastDO.class);
        materialForecastMapper.insert(materialForecast);
        // 返回
        return materialForecast.getId();
    }

    @Override
    public void updateMaterialForecast(MaterialForecastSaveReqVO updateReqVO) {
        // 校验存在
        validateMaterialForecastExists(updateReqVO.getId());
        // 更新
        MaterialForecastDO updateObj = BeanUtils.toBean(updateReqVO, MaterialForecastDO.class);
        materialForecastMapper.updateById(updateObj);
    }

    @Override
    public void deleteMaterialForecast(BigDecimal id) {
        // 校验存在
        validateMaterialForecastExists(id);
        // 删除
        materialForecastMapper.deleteById(id);
    }

    private void validateMaterialForecastExists(BigDecimal id) {
        if (materialForecastMapper.selectById(id) == null) {
            throw exception(MATERIAL_FORECAST_NOT_EXISTS);
        }
    }

    @Override
    public MaterialForecastDO getMaterialForecast(BigDecimal id) {
        return materialForecastMapper.selectById(id);
    }

    @Override
    public PageResult<MaterialForecastDO> getMaterialForecastPage(MaterialForecastPageReqVO pageReqVO) {
        return materialForecastMapper.selectPage(pageReqVO);
    }

}