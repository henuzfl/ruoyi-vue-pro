package cn.iocoder.yudao.module.aps.service.masterimport;

import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.security.core.LoginUser;
import com.baomidou.dynamic.datasource.annotation.DS;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import cn.iocoder.yudao.module.aps.controller.admin.masterimport.vo.*;
import cn.iocoder.yudao.module.aps.dal.dataobject.masterimport.MasterImportDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.aps.dal.mysql.masterimport.MasterImportMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.aps.enums.ErrorCodeConstants.*;
import org.springframework.util.StringUtils;
import org.springframework.util.CollectionUtils;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;


/**
 * 物料主数据导入 Service 实现类
 *
 * @author 柳文
 */
@Service
@DS("oracle") // 指定使用 Oracle 数据源
@Validated
@Slf4j
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

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void syncMaterialData(List<String> materialNos, List<MasterImportDO> importList) {
        log.info("【同步数据】开始执行删除+插入操作");
        // 1. 物理删除（已分批）
        if (materialNos != null && !materialNos.isEmpty()) {
            log.debug("【删除】物料号列表：{}", materialNos);
            int deleted = batchDeleteByMaterialNos(materialNos);
            log.info("【删除】成功删除 {} 条记录", deleted);
        } else {
            log.warn("【删除】物料号列表为空，跳过删除");
        }

        // 2. 批量插入（分批，每批 30 条，避免 ORA-24335）
        if (importList != null && !importList.isEmpty()) {
            int batchSize = 30;  // 与导入方法保持一致
            int totalInserted = 0;
            for (int i = 0; i < importList.size(); i += batchSize) {
                int end = Math.min(i + batchSize, importList.size());
                List<MasterImportDO> batchList = importList.subList(i, end);
                masterImportMapper.batchInsert(batchList);
                totalInserted += batchList.size();
            }
            log.info("【插入】成功插入 {} 条记录", totalInserted);
        }
    }

    /**
     *
     * @param importVOList 导入数据列表
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int importMasterImport(List<MasterImportImportReqVO> importVOList) {
        if (CollectionUtils.isEmpty(importVOList)) {
            return 0;
        }

        // 1. 内部去重（按物料号），同时收集物料号列表用于后续删除
        Set<String> materialNoSet = new HashSet<>();
        List<MasterImportDO> saveList = new ArrayList<>();
        for (MasterImportImportReqVO vo : importVOList) {
            if (StringUtils.isEmpty(vo.getMaterialNo())) {
                log.warn("跳过物料号为空的数据行");
                continue;
            }
            if (materialNoSet.contains(vo.getMaterialNo())) {
                log.warn("重复物料号已跳过：{}", vo.getMaterialNo());
                continue;
            }
            materialNoSet.add(vo.getMaterialNo());

            MasterImportDO entity = BeanUtils.toBean(vo, MasterImportDO.class);
            saveList.add(entity);
        }

        if (saveList.isEmpty()) {
            return 0;
        }

        // 2. 先物理删除这些物料号的现有数据
        List<String> materialNos = new ArrayList<>(materialNoSet);
        int deletedCount = batchDeleteByMaterialNos(materialNos);
        log.info("已物理删除 {} 条旧数据", deletedCount);

        // 3. 设置创建时间和更新时间（ID 由雪花算法自动生成，creator/updater 由自动填充处理）
        LocalDateTime now = LocalDateTime.now();
        for (MasterImportDO entity : saveList) {
            entity.setCreateTime(now);
            entity.setUpdateTime(now);
            // 不手动设置 creator/updater，让 MyBatis-Plus 自动填充处理
        }

        // 4. 分批插入（每批 500 条）
        int batchSize = 30;
        int totalInserted = 0;
        for (int i = 0; i < saveList.size(); i += batchSize) {
            int end = Math.min(i + batchSize, saveList.size());
            List<MasterImportDO> batchList = saveList.subList(i, end);
            masterImportMapper.batchInsert(batchList);
            totalInserted += batchList.size();
        }

        log.info("物料主数据导入完成，共插入 {} 条，删除 {} 条", totalInserted, deletedCount);
        return totalInserted;
    }

    @Override
    public MasterImportDO getByMaterialNo(String materialNo) {
        return masterImportMapper.selectOne(new LambdaQueryWrapperX<MasterImportDO>()
                .eq(MasterImportDO::getMaterialNo, materialNo));
    }

    //删除
    private int batchDeleteByMaterialNos(Collection<String> materialNos) {
        log.info("【数据源标记】当前数据源: {}", DynamicDataSourceContextHolder.peek());
        if (CollectionUtils.isEmpty(materialNos)) {
            return 0;
        }
        List<String> list = new ArrayList<>(materialNos);
        int totalDeleted = 0;
        int batchSize = 30; // Oracle 限制最大值
        for (int i = 0; i < list.size(); i += batchSize) {
            List<String> batch = list.subList(i, Math.min(i + batchSize, list.size()));
            totalDeleted += masterImportMapper.deleteByMaterialNos(batch);
        }
        return totalDeleted;
    }

}