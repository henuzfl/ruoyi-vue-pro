package cn.iocoder.yudao.module.aps.service.bomimport;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.aps.controller.admin.bomimport.vo.*;
import cn.iocoder.yudao.module.aps.dal.dataobject.bomimport.BomImportDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.aps.dal.mysql.bomimport.BomImportMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.aps.enums.ErrorCodeConstants.*;

/**
 * 物料BOM导入 Service 实现类
 *
 * @author 柳文
 */
@Service
@DS("oracle") // 指定使用 Oracle 数据源
@Validated
public class BomImportServiceImpl implements BomImportService {

    @Resource
    private BomImportMapper bomImportMapper;

    @Override
    public Long createBomImport(BomImportSaveReqVO createReqVO) {
        // 插入
        BomImportDO bomImport = BeanUtils.toBean(createReqVO, BomImportDO.class);
        bomImportMapper.insert(bomImport);
        // 返回
        return bomImport.getId();
    }

    @Override
    public void updateBomImport(BomImportSaveReqVO updateReqVO) {
        // 校验存在
        validateBomImportExists(updateReqVO.getId());
        // 更新
        BomImportDO updateObj = BeanUtils.toBean(updateReqVO, BomImportDO.class);
        bomImportMapper.updateById(updateObj);
    }

    @Override
    public void deleteBomImport(Long id) {
        // 校验存在
        validateBomImportExists(id);
        // 删除
        bomImportMapper.deleteById(id);
    }

    private void validateBomImportExists(Long id) {
        if (bomImportMapper.selectById(id) == null) {
            throw exception(BOM_IMPORT_NOT_EXISTS);
        }
    }

    @Override
    public BomImportDO getBomImport(Long id) {
        return bomImportMapper.selectById(id);
    }

    @Override
    public PageResult<BomImportDO> getBomImportPage(BomImportPageReqVO pageReqVO) {
        return bomImportMapper.selectPage(pageReqVO);
    }

}