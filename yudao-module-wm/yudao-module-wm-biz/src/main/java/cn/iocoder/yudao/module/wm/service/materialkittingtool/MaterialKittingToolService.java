package cn.iocoder.yudao.module.wm.service.materialkittingtool;

import java.math.BigDecimal;
import java.util.*;
import javax.validation.*;

import cn.iocoder.yudao.module.wm.controller.admin.kittingmaster.vo.KittingMasterPageReqVO;
import cn.iocoder.yudao.module.wm.controller.admin.materialkittingtool.vo.*;
import cn.iocoder.yudao.module.wm.dal.dataobject.kittingmaster.KittingMasterDO;
import cn.iocoder.yudao.module.wm.dal.dataobject.materialkittingtool.MaterialKittingToolDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;

/**
 * 主计划 Service 接口
 *
 * @author 柳文
 */
public interface MaterialKittingToolService {


    /**
     * 获得主计划
     *
     * @param id 编号
     * @return 主计划
     */
    MaterialKittingToolDO getMaterialKittingTool(BigDecimal id); // 这个方法需要实现


    /**
     * 获得主计划分页
     *
     * @param pageReqVO 分页查询
     * @return 主计划分页
     */
    @InterceptorIgnore(tenantLine = "true")
    PageResult<MaterialKittingToolDO> selectMaterialKittingToolByParams(MaterialKittingToolPageReqVO pageReqVO);
    /**
     * 获得主计划导出数据
     *
     * @param exportReqVO 导出查询条件
     * @return 主计划列表
     */
    List<MaterialKittingToolDO> selectMasterKittingToolForExport(MaterialKittingToolPageReqVO exportReqVO);

    /**
     * 获得主计划分页
     *
     * @param pageReqVO 分页查询
     * @return 主计划分页
     */
    @InterceptorIgnore(tenantLine = "true")
    PageResult<MaterialKittingToolDO> selectCalculateKittingToolByParams(MaterialKittingToolPageReqVO pageReqVO);

    /**
     * 获得主计划导出数据
     *
     * @param exportReqVO 导出查询条件
     * @return 主计划列表
     */
    List<MaterialKittingToolDO> selectCalculateKittingToolForExport(MaterialKittingToolPageReqVO exportReqVO);


}