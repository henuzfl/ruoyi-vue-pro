package cn.iocoder.yudao.module.buyer.service.hostconfig;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import cn.iocoder.yudao.module.buyer.controller.admin.hostconfig.vo.*;
import cn.iocoder.yudao.module.buyer.dal.dataobject.hostconfig.HostConfigDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.buyer.dal.mysql.hostconfig.HostConfigMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.buyer.enums.ErrorCodeConstants.*;

/**
 * 主机配置 Service 实现类
 *
 * @author 柳文
 */
@Service
@DS("oracle") // 指定使用 Oracle 数据源
@Validated
public class HostConfigServiceImpl implements HostConfigService {

    @Resource
    private HostConfigMapper hostConfigMapper;

    @Override
    public BigDecimal createHostConfig(HostConfigSaveReqVO createReqVO) {
        // 插入
        HostConfigDO hostConfig = BeanUtils.toBean(createReqVO, HostConfigDO.class);
        hostConfigMapper.insert(hostConfig);
        // 返回
        return hostConfig.getId();
    }

    @Override
    public void updateHostConfig(HostConfigSaveReqVO updateReqVO) {
        // 校验存在
        validateHostConfigExists(updateReqVO.getId());
        // 更新
        HostConfigDO updateObj = BeanUtils.toBean(updateReqVO, HostConfigDO.class);
        hostConfigMapper.updateById(updateObj);
    }

    @Override
    public void deleteHostConfig(BigDecimal id) {
        // 校验存在
        validateHostConfigExists(id);
        // 删除
        hostConfigMapper.deleteById(id);
    }

    private void validateHostConfigExists(BigDecimal id) {
        if (hostConfigMapper.selectById(id) == null) {
            throw exception(HOST_CONFIG_NOT_EXISTS);
        }
    }

    @Override
    public HostConfigDO getHostConfig(BigDecimal id) {
        return hostConfigMapper.selectById(id);
    }

    @Override
    public PageResult<HostConfigDO> getHostConfigPage(HostConfigPageReqVO pageReqVO) {
        return hostConfigMapper.selectPage(pageReqVO);
    }

}