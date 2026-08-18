package cn.iocoder.yudao.module.marketing.service.scissorliftdplan;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.marketing.controller.admin.scissorliftdplan.vo.*;
import cn.iocoder.yudao.module.marketing.dal.dataobject.scissorliftdplan.ScissorLiftDplanDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 高机剪叉日计划 Service 接口
 *
 * @author 柳文
 */
public interface ScissorLiftDplanService {

    /**
     * 创建高机剪叉日计划
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createScissorLiftDplan(@Valid ScissorLiftDplanSaveReqVO createReqVO);

    /**
     * 更新高机剪叉日计划
     *
     * @param updateReqVO 更新信息
     */
    void updateScissorLiftDplan(@Valid ScissorLiftDplanSaveReqVO updateReqVO);

    /**
     * 删除高机剪叉日计划
     *
     * @param id 编号
     */
    void deleteScissorLiftDplan(Long id);

    /**
     * 获得高机剪叉日计划
     *
     * @param id 编号
     * @return 高机剪叉日计划
     */
    ScissorLiftDplanDO getScissorLiftDplan(Long id);

    /**
     * 获得高机剪叉日计划分页
     *
     * @param pageReqVO 分页查询
     * @return 高机剪叉日计划分页
     */
    PageResult<ScissorLiftDplanDO> getScissorLiftDplanPage(ScissorLiftDplanPageReqVO pageReqVO);

}