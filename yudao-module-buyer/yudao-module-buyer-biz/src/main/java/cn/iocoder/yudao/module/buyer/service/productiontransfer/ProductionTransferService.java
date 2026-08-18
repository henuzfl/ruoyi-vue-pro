package cn.iocoder.yudao.module.buyer.service.productiontransfer;

import java.math.BigDecimal;
import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.buyer.controller.admin.productiontransfer.vo.*;
import cn.iocoder.yudao.module.buyer.dal.dataobject.productiontransfer.ProductionTransferDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * MES转序单信息 Service 接口
 *
 * @author 柳文
 */
public interface ProductionTransferService {

    /**
     * 创建MES转序单信息
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    BigDecimal createProductionTransfer(@Valid ProductionTransferSaveReqVO createReqVO);

    /**
     * 更新MES转序单信息
     *
     * @param updateReqVO 更新信息
     */
    void updateProductionTransfer(@Valid ProductionTransferSaveReqVO updateReqVO);

    /**
     * 删除MES转序单信息
     *
     * @param id 编号
     */
    void deleteProductionTransfer(BigDecimal id);

    /**
     * 获得MES转序单信息
     *
     * @param id 编号
     * @return MES转序单信息
     */
    ProductionTransferDO getProductionTransfer(BigDecimal id);

    /**
     * 获得MES转序单信息分页
     *
     * @param pageReqVO 分页查询
     * @return MES转序单信息分页
     */
    PageResult<ProductionTransferDO> getProductionTransferPage(ProductionTransferPageReqVO pageReqVO);

    // 在接口中增加
    /**
     * 批量导入MES转序单信息
     *
     * @param importVOList 导入数据列表
     * @return 成功导入条数
     */
    int importProductionTransfer(List<ProductionTransferImportReqVO> importVOList);

    /**
     * 从MES系统同步转序单数据
     * @param syncReqVO 同步请求参数
     * @return 同步成功条数
     */
    int syncFromMes(MesSyncReqVO syncReqVO);
}