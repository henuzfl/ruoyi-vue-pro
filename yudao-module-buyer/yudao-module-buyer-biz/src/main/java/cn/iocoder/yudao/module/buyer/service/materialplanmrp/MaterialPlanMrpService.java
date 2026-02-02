package cn.iocoder.yudao.module.buyer.service.materialplanmrp;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.buyer.controller.admin.materialplanmrp.vo.*;
import cn.iocoder.yudao.module.buyer.dal.dataobject.materialplanmrp.MaterialPlanMrpDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 买家需求预测 Service 接口
 *
 * @author 柳文
 */
public interface MaterialPlanMrpService {

    /**
     * 获得买家需求预测分页
     *
     * @param pageReqVO 分页查询
     * @return 买家需求预测分页
     */
    PageResult<MaterialPlanMrpDO> getMaterialPlanMrpPage(MaterialPlanMrpPageReqVO pageReqVO);

    /**
     * 获得买家需求预测列表
     *
     * @param pageReqVO 查询条件
     * @return 买家需求预测列表
     */
    List<MaterialPlanMrpDO> getMaterialPlanMrpList(MaterialPlanMrpPageReqVO pageReqVO);

    /**
     * 导出买家需求预测数据
     *
     * @param pageReqVO 查询条件
     * @return 买家需求预测列表
     */
    List<MaterialPlanMrpDO> getMaterialPlanMrpExport(MaterialPlanMrpPageReqVO pageReqVO);
}