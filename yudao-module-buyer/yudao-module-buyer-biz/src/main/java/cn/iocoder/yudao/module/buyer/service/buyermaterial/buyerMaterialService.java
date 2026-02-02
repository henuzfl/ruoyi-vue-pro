package cn.iocoder.yudao.module.buyer.service.buyermaterial;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.buyer.controller.admin.buyermaterial.vo.*;
import cn.iocoder.yudao.module.buyer.dal.dataobject.buyermaterial.buyerMaterialDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 营销数据导入 Service 接口
 *
 * @author 柳文
 */
public interface buyerMaterialService {


    /**
     * 获得营销数据导入分页
     *
     * @param pageReqVO 分页查询
     * @return 营销数据导入分页
     */
    PageResult<buyerMaterialDO> getDataImportPage(buyerMaterialPageReqVO pageReqVO);

}