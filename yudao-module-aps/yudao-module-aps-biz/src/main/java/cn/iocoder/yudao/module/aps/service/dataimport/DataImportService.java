package cn.iocoder.yudao.module.aps.service.dataimport;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.aps.controller.admin.dataimport.vo.*;
import cn.iocoder.yudao.module.aps.dal.dataobject.dataimport.DataImportDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 营销数据导入 Service 接口
 *
 * @author 柳文
 */
public interface DataImportService {

    /**
     * 创建营销数据导入
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Short createDataImport(@Valid DataImportSaveReqVO createReqVO);

    /**
     * 更新营销数据导入
     *
     * @param updateReqVO 更新信息
     */
    void updateDataImport(@Valid DataImportSaveReqVO updateReqVO);

    /**
     * 删除营销数据导入
     *
     * @param id 编号
     */
    void deleteDataImport(Short id);

    /**
     * 获得营销数据导入
     *
     * @param id 编号
     * @return 营销数据导入
     */
    DataImportDO getDataImport(Short id);

    /**
     * 获得营销数据导入分页
     *
     * @param pageReqVO 分页查询
     * @return 营销数据导入分页
     */
    PageResult<DataImportDO> getDataImportPage(DataImportPageReqVO pageReqVO);

}