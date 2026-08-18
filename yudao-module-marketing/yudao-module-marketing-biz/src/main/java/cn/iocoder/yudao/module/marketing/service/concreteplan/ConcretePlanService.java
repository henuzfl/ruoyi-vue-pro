package cn.iocoder.yudao.module.marketing.service.concreteplan;

import javax.validation.*;
import cn.iocoder.yudao.module.marketing.controller.admin.concreteplan.vo.*;
import cn.iocoder.yudao.module.marketing.dal.dataobject.concreteplan.ConcretePlanDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 混凝土计划需求 Service 接口
 *
 * @author 管理员
 */
public interface ConcretePlanService {

    /**
     * 创建混凝土计划需求
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createConcretePlan(@Valid ConcretePlanSaveReqVO createReqVO);

    /**
     * 更新混凝土计划需求
     *
     * @param updateReqVO 更新信息
     */
    void updateConcretePlan(@Valid ConcretePlanSaveReqVO updateReqVO);

    /**
     * 删除混凝土计划需求
     *
     * @param id 编号
     */
    void deleteConcretePlan(Long id);

    /**
     * 获得混凝土计划需求
     *
     * @param id 编号
     * @return 混凝土计划需求
     */
    ConcretePlanDO getConcretePlan(Long id);

    /**
     * 获得混凝土计划需求分页
     *
     * @param pageReqVO 分页查询
     * @return 混凝土计划需求分页
     */
    PageResult<ConcretePlanDO> getConcretePlanPage(ConcretePlanPageReqVO pageReqVO);

    /**
     * 导入混凝土计划需求 Excel
     *
     * @param file       上传的 Excel 文件
     * @param importTime 导入批次时间（用户指定）
     */
    void importExcel(MultipartFile file, LocalDate importTime) throws IOException;



}