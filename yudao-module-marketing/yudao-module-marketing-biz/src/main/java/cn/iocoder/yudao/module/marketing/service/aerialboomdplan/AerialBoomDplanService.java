package cn.iocoder.yudao.module.marketing.service.aerialboomdplan;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.marketing.controller.admin.aerialboomdplan.vo.*;
import cn.iocoder.yudao.module.marketing.dal.dataobject.aerialboomdplan.AerialBoomDplanDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 高机臂式日计划 Service 接口
 *
 * @author 柳文
 */
public interface AerialBoomDplanService {

    /**
     * 创建高机臂式日计划
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createAerialBoomDplan(@Valid AerialBoomDplanSaveReqVO createReqVO);

    /**
     * 更新高机臂式日计划
     *
     * @param updateReqVO 更新信息
     */
    void updateAerialBoomDplan(@Valid AerialBoomDplanSaveReqVO updateReqVO);

    /**
     * 删除高机臂式日计划
     *
     * @param id 编号
     */
    void deleteAerialBoomDplan(Long id);

    /**
     * 获得高机臂式日计划
     *
     * @param id 编号
     * @return 高机臂式日计划
     */
    AerialBoomDplanDO getAerialBoomDplan(Long id);

    /**
     * 获得高机臂式日计划分页
     *
     * @param pageReqVO 分页查询
     * @return 高机臂式日计划分页
     */
    PageResult<AerialBoomDplanDO> getAerialBoomDplanPage(AerialBoomDplanPageReqVO pageReqVO);

    void saveBatch(List<AerialBoomDplanDO> list);
    void insertAerialBoomDplan(AerialBoomDplanDO entity);


}