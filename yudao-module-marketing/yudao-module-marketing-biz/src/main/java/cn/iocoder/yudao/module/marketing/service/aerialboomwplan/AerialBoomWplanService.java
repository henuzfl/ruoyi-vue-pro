package cn.iocoder.yudao.module.marketing.service.aerialboomwplan;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.marketing.controller.admin.aerialboomwplan.vo.*;
import cn.iocoder.yudao.module.marketing.dal.dataobject.aerialboomwplan.AerialBoomWplanDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.web.multipart.MultipartFile;

/**
 * 高机臂式周计划 Service 接口
 *
 * @author 柳文
 */
public interface AerialBoomWplanService {

    /**
     * 创建高机臂式周计划
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createAerialBoomWplan(@Valid AerialBoomWplanSaveReqVO createReqVO);

    /**
     * 更新高机臂式周计划
     *
     * @param updateReqVO 更新信息
     */
    void updateAerialBoomWplan(@Valid AerialBoomWplanSaveReqVO updateReqVO);

    /**
     * 删除高机臂式周计划
     *
     * @param id 编号
     */
    void deleteAerialBoomWplan(Long id);

    /**
     * 获得高机臂式周计划
     *
     * @param id 编号
     * @return 高机臂式周计划
     */
    AerialBoomWplanDO getAerialBoomWplan(Long id);

    /**
     * 获得高机臂式周计划分页
     *
     * @param pageReqVO 分页查询
     * @return 高机臂式周计划分页
     */
    PageResult<AerialBoomWplanDO> getAerialBoomWplanPage(AerialBoomWplanPageReqVO pageReqVO);

    /**
     * 导入
     *
     * @param
     * @return
     */
    void importExcel(MultipartFile file, LocalDate importTime) throws IOException;


}