package cn.iocoder.yudao.module.buyer.service.buyertimestock;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.buyer.controller.admin.buyertimestock.vo.*;
import cn.iocoder.yudao.module.buyer.dal.dataobject.buyertimestock.buyerTimeStockDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 实时库存 Service 接口
 *
 * @author 柳文
 */
public interface buyerTimeStockService {

    /**
     * 创建实时库存
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createbuyerTimeStock(@Valid buyerTimeStockSaveReqVO createReqVO);

    /**
     * 更新实时库存
     *
     * @param updateReqVO 更新信息
     */
    void updatebuyerTimeStock(@Valid buyerTimeStockSaveReqVO updateReqVO);

    /**
     * 删除实时库存
     *
     * @param id 编号
     */
    void deletebuyerTimeStock(Long id);

    /**
     * 获得实时库存
     *
     * @param id 编号
     * @return 实时库存
     */
    buyerTimeStockDO getbuyerTimeStock(Long id);

    /**
     * 获得实时库存分页
     *
     * @param pageReqVO 分页查询
     * @return 实时库存分页
     */
    PageResult<buyerTimeStockDO> getbuyerTimeStockPage(buyerTimeStockPageReqVO pageReqVO);


    /**
     * 批量插入库存数据
     * @param stockList 库存数据列表
     * @return 处理结果信息
     */
    String batchInsertOrUpdateStock(List<buyerTimeStockDO> stockList);

}