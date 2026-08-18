package cn.iocoder.yudao.module.buyer.service.codeconfig;

import java.math.BigDecimal;
import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.buyer.controller.admin.codeconfig.vo.*;
import cn.iocoder.yudao.module.buyer.dal.dataobject.codeconfig.CodeConfigDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 主机编码配置 Service 接口
 *
 * @author 柳文
 */
public interface CodeConfigService {

    /**
     * 创建主机编码配置
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    BigDecimal createCodeConfig(@Valid CodeConfigSaveReqVO createReqVO);

    /**
     * 更新主机编码配置
     *
     * @param updateReqVO 更新信息
     */
    void updateCodeConfig(@Valid CodeConfigSaveReqVO updateReqVO);

    /**
     * 删除主机编码配置
     *
     * @param id 编号
     */
    void deleteCodeConfig(BigDecimal id);

    /**
     * 获得主机编码配置
     *
     * @param id 编号
     * @return 主机编码配置
     */
    CodeConfigDO getCodeConfig(BigDecimal id);

    /**
     * 获得主机编码配置分页
     *
     * @param pageReqVO 分页查询
     * @return 主机编码配置分页
     */
    PageResult<CodeConfigDO> getCodeConfigPage(CodeConfigPageReqVO pageReqVO);

    /**
     * 获得主机编码配置分页
     *
     * @param
     * @return 主机编码配置分页
     */
    int importCodeConfig(List<CodeConfigImportReqVO> importVOList);


}