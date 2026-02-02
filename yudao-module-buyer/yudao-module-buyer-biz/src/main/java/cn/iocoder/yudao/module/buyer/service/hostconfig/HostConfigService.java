package cn.iocoder.yudao.module.buyer.service.hostconfig;

import java.math.BigDecimal;
import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.buyer.controller.admin.hostconfig.vo.*;
import cn.iocoder.yudao.module.buyer.dal.dataobject.hostconfig.HostConfigDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 主机配置 Service 接口
 *
 * @author 柳文
 */
public interface HostConfigService {

    /**
     * 创建主机配置
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    BigDecimal createHostConfig(@Valid HostConfigSaveReqVO createReqVO);

    /**
     * 更新主机配置
     *
     * @param updateReqVO 更新信息
     */
    void updateHostConfig(@Valid HostConfigSaveReqVO updateReqVO);

    /**
     * 删除主机配置
     *
     * @param id 编号
     */
    void deleteHostConfig(BigDecimal id);

    /**
     * 获得主机配置
     *
     * @param id 编号
     * @return 主机配置
     */
    HostConfigDO getHostConfig(BigDecimal id);

    /**
     * 获得主机配置分页
     *
     * @param pageReqVO 分页查询
     * @return 主机配置分页
     */
    PageResult<HostConfigDO> getHostConfigPage(HostConfigPageReqVO pageReqVO);

}