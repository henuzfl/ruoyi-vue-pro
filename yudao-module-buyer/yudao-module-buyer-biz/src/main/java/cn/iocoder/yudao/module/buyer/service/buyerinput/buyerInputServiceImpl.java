package cn.iocoder.yudao.module.buyer.service.buyerinput;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.buyer.controller.admin.buyerinput.vo.*;
import cn.iocoder.yudao.module.buyer.dal.dataobject.buyerinput.buyerInputDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.buyer.dal.mysql.buyerinput.buyerInputMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.buyer.enums.ErrorCodeConstants.*;

/**
 * 需求输入 Service 实现类
 *
 * @author 柳文
 */
@Service
@Validated
public class buyerInputServiceImpl implements buyerInputService {

    @Resource
    private buyerInputMapper buyerInputMapper;

    @Override
    public Long createbuyerInput(buyerInputSaveReqVO createReqVO) {
        // 插入
        buyerInputDO buyerInput = BeanUtils.toBean(createReqVO, buyerInputDO.class);
        buyerInputMapper.insert(buyerInput);
        // 返回
        return buyerInput.getId();
    }

    @Override
    public void updatebuyerInput(buyerInputSaveReqVO updateReqVO) {
        // 校验存在
        validatebuyerInputExists(updateReqVO.getId());
        // 更新
        buyerInputDO updateObj = BeanUtils.toBean(updateReqVO, buyerInputDO.class);
        buyerInputMapper.updateById(updateObj);
    }

    @Override
    public void deletebuyerInput(Long id) {
        // 校验存在
        validatebuyerInputExists(id);
        // 删除
        buyerInputMapper.deleteById(id);
    }

    private void validatebuyerInputExists(Long id) {
        if (buyerInputMapper.selectById(id) == null) {
            throw exception(BUYER_INPUT_NOT_EXISTS);
        }
    }

    @Override
    public buyerInputDO getbuyerInput(Long id) {
        return buyerInputMapper.selectById(id);
    }

    @Override
    public PageResult<buyerInputDO> getbuyerInputPage(buyerInputPageReqVO pageReqVO) {
        return buyerInputMapper.selectPage(pageReqVO);
    }

}