package cn.iocoder.yudao.module.buyer.service.materialplanmrp;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import java.util.*;
import cn.iocoder.yudao.module.buyer.controller.admin.materialplanmrp.vo.*;
import cn.iocoder.yudao.module.buyer.dal.dataobject.materialplanmrp.MaterialPlanMrpDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

import cn.iocoder.yudao.module.buyer.dal.mysql.materialplanmrp.MaterialPlanMrpMapper;

/**
 * 买家需求预测 Service 实现类
 *
 * @author 柳文
 */
@Service
@DS("oracle") // 指定使用 Oracle 数据源
@Validated
public class MaterialPlanMrpServiceImpl implements MaterialPlanMrpService {

    @Resource
    private MaterialPlanMrpMapper materialPlanMrpMapper;

    @Override
    public PageResult<MaterialPlanMrpDO> getMaterialPlanMrpPage(MaterialPlanMrpPageReqVO pageReqVO) {
        return materialPlanMrpMapper.selectPage(pageReqVO);
    }

    @Override
    public List<MaterialPlanMrpDO> getMaterialPlanMrpList(MaterialPlanMrpPageReqVO pageReqVO) {
        return materialPlanMrpMapper.selectList(pageReqVO);
    }

    @Override
    public List<MaterialPlanMrpDO> getMaterialPlanMrpExport(MaterialPlanMrpPageReqVO pageReqVO) {
        return materialPlanMrpMapper.selectList(pageReqVO);
    }
}