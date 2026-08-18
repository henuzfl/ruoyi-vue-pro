package cn.iocoder.yudao.module.marketing.service.scissorliftwplan;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.marketing.controller.admin.scissorliftwplan.vo.*;
import cn.iocoder.yudao.module.marketing.dal.dataobject.scissorliftwplan.ScissorLiftWplanDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.web.multipart.MultipartFile;

/**
 * 高机剪叉周计划 Service 接口
 *
 * @author 柳文
 */
public interface ScissorLiftWplanService {

    /**
     * 创建高机剪叉周计划
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createScissorLiftWplan(@Valid ScissorLiftWplanSaveReqVO createReqVO);

    /**
     * 更新高机剪叉周计划
     *
     * @param updateReqVO 更新信息
     */
    void updateScissorLiftWplan(@Valid ScissorLiftWplanSaveReqVO updateReqVO);

    /**
     * 删除高机剪叉周计划
     *
     * @param id 编号
     */
    void deleteScissorLiftWplan(Long id);

    /**
     * 获得高机剪叉周计划
     *
     * @param id 编号
     * @return 高机剪叉周计划
     */
    ScissorLiftWplanDO getScissorLiftWplan(Long id);

    /**
     * 获得高机剪叉周计划分页
     *
     * @param pageReqVO 分页查询
     * @return 高机剪叉周计划分页
     */
    PageResult<ScissorLiftWplanDO> getScissorLiftWplanPage(ScissorLiftWplanPageReqVO pageReqVO);


    /**
     * 导入剪叉周计划（包含周计划+日计划）
     * @param file Excel文件
     * @param importTime 导入批次时间
     */
    void importExcel(MultipartFile file, LocalDate importTime) throws IOException;
}