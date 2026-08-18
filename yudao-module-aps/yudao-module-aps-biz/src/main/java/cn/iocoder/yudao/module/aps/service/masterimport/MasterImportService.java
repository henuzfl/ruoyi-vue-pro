package cn.iocoder.yudao.module.aps.service.masterimport;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.aps.controller.admin.masterimport.vo.*;
import cn.iocoder.yudao.module.aps.dal.dataobject.masterimport.MasterImportDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.data.repository.query.Param;

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
    /**
     * 同步物料数据：先物理删除指定物料号的数据，再批量插入新数据（事务内）
     * @param materialNos 物料号列表（用于删除）
     * @param importList  待插入的物料数据
     */
    void syncMaterialData(@Param("materialNos") List<String> materialNos,
                          @Param("importList") List<MasterImportDO> importList);

    /**
     * 批量导入物料主数据
     * @param importVOList 导入数据列表
     * @return 成功导入条数
     */
    int importMasterImport(List<MasterImportImportReqVO> importVOList);

    /**
     * 根据物料号查询
     */
    MasterImportDO getByMaterialNo(String materialNo);
}