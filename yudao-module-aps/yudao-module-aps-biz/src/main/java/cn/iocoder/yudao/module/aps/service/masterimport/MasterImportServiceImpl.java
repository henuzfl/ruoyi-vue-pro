package cn.iocoder.yudao.module.aps.service.masterimport;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.aps.controller.admin.masterimport.vo.*;
import cn.iocoder.yudao.module.aps.dal.dataobject.masterimport.MasterImportDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.aps.dal.mysql.masterimport.MasterImportMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.aps.enums.ErrorCodeConstants.*;

/**
 * 物料主数据导入 Service 实现类
 *
 * @author 柳文
 */
@Service
@Validated
public class MasterImportServiceImpl implements MasterImportService {

    @Resource
    private MasterImportMapper masterImportMapper;

    @Override
    public Long createMasterImport(MasterImportSaveReqVO createReqVO) {
        // 插入
        MasterImportDO masterImport = BeanUtils.toBean(createReqVO, MasterImportDO.class);
        masterImportMapper.insert(masterImport);
        // 返回
        return masterImport.getId();
    }

    @Override
    public void updateMasterImport(MasterImportSaveReqVO updateReqVO) {
        // 校验存在
        validateMasterImportExists(updateReqVO.getId());
        // 更新
        MasterImportDO updateObj = BeanUtils.toBean(updateReqVO, MasterImportDO.class);
        masterImportMapper.updateById(updateObj);
    }

    @Override
    public void deleteMasterImport(Long id) {
        // 校验存在
        validateMasterImportExists(id);
        // 删除
        masterImportMapper.deleteById(id);
    }

    private void validateMasterImportExists(Long id) {
        if (masterImportMapper.selectById(id) == null) {
            throw exception(MASTER_IMPORT_NOT_EXISTS);
        }
    }

    @Override
    public MasterImportDO getMasterImport(Long id) {
        return masterImportMapper.selectById(id);
    }

    @Override
    public PageResult<MasterImportDO> getMasterImportPage(MasterImportPageReqVO pageReqVO) {
        return masterImportMapper.selectPage(pageReqVO);
    }

}