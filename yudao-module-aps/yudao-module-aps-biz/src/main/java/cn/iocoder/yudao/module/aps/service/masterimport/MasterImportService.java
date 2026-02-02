package cn.iocoder.yudao.module.aps.service.masterimport;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.aps.controller.admin.masterimport.vo.*;
import cn.iocoder.yudao.module.aps.dal.dataobject.masterimport.MasterImportDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 物料主数据导入 Service 接口
 *
 * @author 柳文
 */
public interface MasterImportService {

    /**
     * 创建物料主数据导入
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createMasterImport(@Valid MasterImportSaveReqVO createReqVO);

    /**
     * 更新物料主数据导入
     *
     * @param updateReqVO 更新信息
     */
    void updateMasterImport(@Valid MasterImportSaveReqVO updateReqVO);

    /**
     * 删除物料主数据导入
     *
     * @param id 编号
     */
    void deleteMasterImport(Long id);

    /**
     * 获得物料主数据导入
     *
     * @param id 编号
     * @return 物料主数据导入
     */
    MasterImportDO getMasterImport(Long id);

    /**
     * 获得物料主数据导入分页
     *
     * @param pageReqVO 分页查询
     * @return 物料主数据导入分页
     */
    PageResult<MasterImportDO> getMasterImportPage(MasterImportPageReqVO pageReqVO);

}