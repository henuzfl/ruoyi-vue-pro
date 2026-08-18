package cn.iocoder.yudao.module.aps.service.assemblyplan;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.aps.controller.admin.assemblyplan.vo.*;
import cn.iocoder.yudao.module.aps.dal.dataobject.assemblyplan.AssemblyPlanDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.web.multipart.MultipartFile;

/**
 * 各车间开装计划 Service 接口
 *
 * @author 柳文
 */
public interface AssemblyPlanService {

    /**
     * 创建各车间开装计划
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createAssemblyPlan(@Valid AssemblyPlanSaveReqVO createReqVO);

    /**
     * 更新各车间开装计划
     *
     * @param updateReqVO 更新信息
     */
    void updateAssemblyPlan(@Valid AssemblyPlanSaveReqVO updateReqVO);

    /**
     * 删除各车间开装计划
     *
     * @param id 编号
     */
    void deleteAssemblyPlan(Long id);

    /**
     * 获得各车间开装计划
     *
     * @param id 编号
     * @return 各车间开装计划
     */
    AssemblyPlanDO getAssemblyPlan(Long id);

    /**
     * 获得各车间开装计划分页
     *
     * @param pageReqVO 分页查询
     * @return 各车间开装计划分页
     */
    PageResult<AssemblyPlanDO> getAssemblyPlanPage(AssemblyPlanPageReqVO pageReqVO);


    /**
     * 导入各车间开装计划
     * @param file Excel文件
     * @param importTime 导入批次时间
     */
    void importExcel(MultipartFile file, LocalDate importTime) throws IOException;

}