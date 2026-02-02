package cn.iocoder.yudao.module.buyer.service.buyermaterial;

import cn.iocoder.yudao.module.buyer.controller.admin.buyermaterial.vo.buyerMaterialPageReqVO;
import com.baomidou.dynamic.datasource.annotation.DS;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.buyer.controller.admin.buyermaterial.vo.*;
import cn.iocoder.yudao.module.buyer.dal.dataobject.buyermaterial.buyerMaterialDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.buyer.dal.mysql.buyermaterial.buyerMaterialMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.buyer.enums.ErrorCodeConstants.*;

/**
 * 营销数据导入 Service 实现类
 *
 * @author 柳文
 */
@Service
@DS("oracle") // 指定使用 Oracle 数据源
@Validated
public class buyerMaterialServiceImpl implements buyerMaterialService {

    @Resource
    private buyerMaterialMapper BuyerMaterialMapper;

    @Override
    public PageResult<buyerMaterialDO> getDataImportPage(buyerMaterialPageReqVO pageReqVO) {
        return BuyerMaterialMapper.selectPage(pageReqVO);
    }

}