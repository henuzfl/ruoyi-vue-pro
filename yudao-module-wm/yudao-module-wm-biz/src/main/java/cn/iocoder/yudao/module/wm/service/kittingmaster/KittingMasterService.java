package cn.iocoder.yudao.module.wm.service.kittingmaster;

import java.math.BigDecimal;
import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.wm.controller.admin.kittingmaster.vo.*;
import cn.iocoder.yudao.module.wm.dal.dataobject.kittingmaster.KittingMasterDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 主计划 Service 接口
 *
 * @author 柳文
 */
public interface KittingMasterService {


    /**
     * 获得主计划
     *
     * @param id 编号
     * @return 主计划
     */
    KittingMasterDO getKittingMaster(BigDecimal id); // 这个方法需要实现


    /**
     * 获得主计划分页
     *
     * @param pageReqVO 分页查询
     * @return 主计划分页
     */
    PageResult<KittingMasterDO> selectKittingMasterByParams(KittingMasterPageReqVO pageReqVO);

}