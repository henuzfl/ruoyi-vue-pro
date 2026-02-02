package cn.iocoder.yudao.module.aps.service.dataimport;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.aps.controller.admin.dataimport.vo.*;
import cn.iocoder.yudao.module.aps.dal.dataobject.dataimport.DataImportDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.aps.dal.mysql.dataimport.DataImportMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.aps.enums.ErrorCodeConstants.*;

/**
 * 营销数据导入 Service 实现类
 *
 * @author 柳文
 */
@Service
@DS("oracle") // 指定使用 Oracle 数据源
@Validated
public class DataImportServiceImpl implements DataImportService {

    @Resource
    private DataImportMapper dataImportMapper;

    @Override
    public Short createDataImport(DataImportSaveReqVO createReqVO) {
        // 插入
        DataImportDO dataImport = BeanUtils.toBean(createReqVO, DataImportDO.class);
        dataImportMapper.insert(dataImport);
        // 返回
        return dataImport.getId();
    }

    @Override
    public void updateDataImport(DataImportSaveReqVO updateReqVO) {
        // 校验存在
        validateDataImportExists(updateReqVO.getId());
        // 更新
        DataImportDO updateObj = BeanUtils.toBean(updateReqVO, DataImportDO.class);
        dataImportMapper.updateById(updateObj);
    }

    @Override
    public void deleteDataImport(Short id) {
        // 校验存在
        validateDataImportExists(id);
        // 删除
        dataImportMapper.deleteById(id);
    }

    private void validateDataImportExists(Short id) {
        if (dataImportMapper.selectById(id) == null) {
            throw exception(DATA_IMPORT_NOT_EXISTS);
        }
    }

    @Override
    public DataImportDO getDataImport(Short id) {
        return dataImportMapper.selectById(id);
    }

    @Override
    public PageResult<DataImportDO> getDataImportPage(DataImportPageReqVO pageReqVO) {
        return dataImportMapper.selectPage(pageReqVO);
    }

}