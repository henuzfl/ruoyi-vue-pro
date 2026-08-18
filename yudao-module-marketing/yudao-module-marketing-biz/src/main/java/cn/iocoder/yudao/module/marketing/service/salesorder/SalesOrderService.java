package cn.iocoder.yudao.module.marketing.service.salesorder;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.marketing.controller.admin.salesorder.vo.SalesOrderPageReqVO;
import cn.iocoder.yudao.module.marketing.controller.admin.salesorder.vo.SalesOrderSaveReqVO;
import cn.iocoder.yudao.module.marketing.dal.dataobject.salesorder.SalesOrderDO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface SalesOrderService {

    Long createSalesOrder(SalesOrderSaveReqVO createReqVO);

    void updateSalesOrder(SalesOrderSaveReqVO updateReqVO);

    void deleteSalesOrder(Long id);

    SalesOrderDO getSalesOrder(Long id);

    PageResult<SalesOrderDO> getSalesOrderPage(SalesOrderPageReqVO pageReqVO);

    void importExcel(MultipartFile file) throws IOException;

    List<SalesOrderDO> getExportList(SalesOrderPageReqVO reqVO);
}