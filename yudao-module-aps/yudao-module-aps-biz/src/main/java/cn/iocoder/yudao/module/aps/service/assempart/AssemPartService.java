package cn.iocoder.yudao.module.aps.service.assempart;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.aps.controller.admin.assempart.vo.*;
import cn.iocoder.yudao.module.aps.dal.dataobject.assempart.AssemPartDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.web.multipart.MultipartFile;

/**
 * 总成与子件关联表管理 Service 接口
 *
 * @author 柳文
 */
public interface AssemPartService {

    /**
     * 创建总成与子件关联表管理
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createAssemPart(@Valid AssemPartSaveReqVO createReqVO);

    /**
     * 更新总成与子件关联表管理
     *
     * @param updateReqVO 更新信息
     */
    void updateAssemPart(@Valid AssemPartSaveReqVO updateReqVO);

    /**
     * 删除总成与子件关联表管理
     *
     * @param id 编号
     */
    void deleteAssemPart(Long id);

    /**
     * 获得总成与子件关联表管理
     *
     * @param id 编号
     * @return 总成与子件关联表管理
     */
    AssemPartDO getAssemPart(Long id);

    /**
     * 获得总成与子件关联表管理分页
     *
     * @param pageReqVO 分页查询
     * @return 总成与子件关联表管理分页
     */
    PageResult<AssemPartDO> getAssemPartPage(AssemPartPageReqVO pageReqVO);

    /**
     * 导入
     *
     * @param file Excel 文件
     * @return 导入数量
     */
    int importAssemPart(MultipartFile file) throws IOException;

}