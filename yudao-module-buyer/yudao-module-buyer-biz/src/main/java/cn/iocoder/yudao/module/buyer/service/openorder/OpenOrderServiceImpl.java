package cn.iocoder.yudao.module.buyer.service.openorder;

import com.baomidou.dynamic.datasource.annotation.DS;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import cn.iocoder.yudao.module.buyer.controller.admin.openorder.vo.*;
import cn.iocoder.yudao.module.buyer.dal.dataobject.openorder.OpenOrderDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.buyer.dal.mysql.openorder.OpenOrderMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.buyer.enums.ErrorCodeConstants.*;

/**
 * 采购未清订单 Service 实现类
 *
 * @author 柳文
 */
@Service
@DS("oracle") // 指定使用 Oracle 数据源
@Validated
@Slf4j
public class OpenOrderServiceImpl implements OpenOrderService {

    @Resource
    private OpenOrderMapper openOrderMapper;

    @Override
    public BigDecimal createOpenOrder(OpenOrderSaveReqVO createReqVO) {
        // 插入
        OpenOrderDO openOrder = BeanUtils.toBean(createReqVO, OpenOrderDO.class);
        openOrderMapper.insert(openOrder);
        // 返回
        return openOrder.getId();
    }

    @Override
    public void updateOpenOrder(OpenOrderSaveReqVO updateReqVO) {
        // 校验存在
        validateOpenOrderExists(updateReqVO.getId());
        // 更新
        OpenOrderDO updateObj = BeanUtils.toBean(updateReqVO, OpenOrderDO.class);
        openOrderMapper.updateById(updateObj);
    }

    @Override
    public void deleteOpenOrder(BigDecimal id) {
        // 校验存在
        validateOpenOrderExists(id);
        // 删除
        openOrderMapper.deleteById(id);
    }

    private void validateOpenOrderExists(BigDecimal id) {
        if (openOrderMapper.selectById(id) == null) {
            throw exception(OPEN_ORDER_NOT_EXISTS);
        }
    }

    @Override
    public OpenOrderDO getOpenOrder(BigDecimal id) {
        return openOrderMapper.selectById(id);
    }

//    @Override
//    public PageResult<OpenOrderDO> getOpenOrderPage(OpenOrderPageReqVO pageReqVO) {
//        log.info("pageNo: {}, pageSize: {}", pageReqVO.getPageNo(), pageReqVO.getPageSize());
//        PageResult<OpenOrderDO> pageResult = openOrderMapper.selectPage(pageReqVO);
//        log.info("查询结果 total: {}", pageResult.getTotal());
//        return pageResult;
//    }
    @Override
    public PageResult<OpenOrderDO> getOpenOrderPage(OpenOrderPageReqVO pageReqVO) {
        int pageNo = pageReqVO.getPageNo();
        int pageSize = pageReqVO.getPageSize();
        int offset = (pageNo - 1) * pageSize;

        // 查询总数
        long total = openOrderMapper.selectPageCount(pageReqVO);
        if (total == 0) {
            return new PageResult<>(Collections.emptyList(), 0L);
        }

        // 查询列表
        List<OpenOrderDO> list = openOrderMapper.selectPageList(offset, pageSize, pageReqVO);
        return new PageResult<>(list, total);
    }

}