package cn.iocoder.yudao.module.aps.service.bomimport;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.aps.controller.admin.bomimport.vo.*;
import cn.iocoder.yudao.module.aps.dal.dataobject.bomimport.BomImportDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 物料BOM导入 Service 接口
 *
 * @author 柳文
 */
public interface BomImportService {

    /**
     * 创建物料BOM导入
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createBomImport(@Valid BomImportSaveReqVO createReqVO);

    /**
     * 更新物料BOM导入
     *
     * @param updateReqVO 更新信息
     */
    void updateBomImport(@Valid BomImportSaveReqVO updateReqVO);

    /**
     * 删除物料BOM导入
     *
     * @param id 编号
     */
    void deleteBomImport(Long id);

    /**
     * 获得物料BOM导入
     *
     * @param id 编号
     * @return 物料BOM导入
     */
    BomImportDO getBomImport(Long id);

    /**
     * 获得物料BOM导入分页
     *
     * @param pageReqVO 分页查询
     * @return 物料BOM导入分页
     */
    PageResult<BomImportDO> getBomImportPage(BomImportPageReqVO pageReqVO);
    /**
     * 导入Excel BOM数据到BOM导入表
     */
    String importBomFromExcel(byte[] fileData, String fileName, String mainMaterialNo, String plant);

    /**
     * 从SAP数据导入BOM到BOM导入表
     */
    String importBomFromSapData(List<Map<String, Object>> sapBomList, String mainMaterialNo, String plant);

    /**
     * 清空指定物料的BOM导入数据
     */
    void clearBomImportData(String mainMaterialNo, String plant);

    /**
     * 根据主物料和工厂查询BOM导入数据
     */
    List<BomImportDO> getBomImportList(String mainMaterialNo, String plant);
}