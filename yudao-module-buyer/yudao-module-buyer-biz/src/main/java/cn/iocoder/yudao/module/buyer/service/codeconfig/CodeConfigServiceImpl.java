package cn.iocoder.yudao.module.buyer.service.codeconfig;

import com.baomidou.dynamic.datasource.annotation.DS;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import cn.iocoder.yudao.module.buyer.controller.admin.codeconfig.vo.*;
import cn.iocoder.yudao.module.buyer.dal.dataobject.codeconfig.CodeConfigDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.buyer.dal.mysql.codeconfig.CodeConfigMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.buyer.enums.ErrorCodeConstants.*;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 * 主机编码配置 Service 实现类
 *
 * @author 柳文
 */
@Service
@DS("oracle") // 指定使用 Oracle 数据源
@Validated
@Slf4j
public class CodeConfigServiceImpl implements CodeConfigService {

    @Resource
    private CodeConfigMapper codeConfigMapper;

    @Override
    public BigDecimal createCodeConfig(CodeConfigSaveReqVO createReqVO) {
        // 插入
        CodeConfigDO codeConfig = BeanUtils.toBean(createReqVO, CodeConfigDO.class);
        codeConfigMapper.insert(codeConfig);
        // 返回
        return codeConfig.getId();
    }

    @Override
    public void updateCodeConfig(CodeConfigSaveReqVO updateReqVO) {
        // 校验存在
        validateCodeConfigExists(updateReqVO.getId());
        // 更新
        CodeConfigDO updateObj = BeanUtils.toBean(updateReqVO, CodeConfigDO.class);
        codeConfigMapper.updateById(updateObj);
    }

    @Override
    public void deleteCodeConfig(BigDecimal id) {
        // 校验存在
        validateCodeConfigExists(id);
        // 删除
        codeConfigMapper.deleteById(id);
    }

    private void validateCodeConfigExists(BigDecimal id) {
        if (codeConfigMapper.selectById(id) == null) {
            throw exception(CODE_CONFIG_NOT_EXISTS);
        }
    }

    @Override
    public CodeConfigDO getCodeConfig(BigDecimal id) {
        return codeConfigMapper.selectById(id);
    }

    @Override
    public PageResult<CodeConfigDO> getCodeConfigPage(CodeConfigPageReqVO pageReqVO) {
        return codeConfigMapper.selectPage(pageReqVO);
    }

    /*导入
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int importCodeConfig(List<CodeConfigImportReqVO> importVOList) {
        if (CollectionUtils.isEmpty(importVOList)) {
            return 0;
        }

        // 1. 内部去重（按名称）
        Set<String> hostcodeSet = new HashSet<>();
        List<CodeConfigDO> saveList = new ArrayList<>();
        for (CodeConfigImportReqVO vo : importVOList) {
//            if (StringUtils.isEmpty(vo.getName())) {
//                log.warn("跳过名称为空的数据行");
//                continue;
//            }
            if (hostcodeSet.contains(vo.getHostCode())) {
                log.warn("重复名称已跳过：{}", vo.getHostCode());
                continue;
            }
            hostcodeSet.add(vo.getHostCode());

            CodeConfigDO entity = BeanUtils.toBean(vo, CodeConfigDO.class);

            saveList.add(entity);
        }

        if (saveList.isEmpty()) {
            return 0;
        }

        // 2. 物理删除这些名称的现有数据
        List<String> hostcode = new ArrayList<>(hostcodeSet);
        int deletedCount = batchDeleteByHostCodes(hostcodeSet);
        log.info("已物理删除 {} 条旧数据", deletedCount);

        // 3. 设置创建时间和更新时间
        LocalDateTime now = LocalDateTime.now();
        for (CodeConfigDO entity : saveList) {
            entity.setCreateTime(now);
            entity.setUpdateTime(now);
            // creator/updater 由自动填充处理
        }

        // 4. 分批插入（每批 500 条）
        int batchSize = 30;
        int totalInserted = 0;
        for (int i = 0; i < saveList.size(); i += batchSize) {
            int end = Math.min(i + batchSize, saveList.size());
            List<CodeConfigDO> batchList = saveList.subList(i, end);
            codeConfigMapper.batchInsert(batchList);
            totalInserted += batchList.size();
        }

        log.info("主机编码配置导入完成，共插入 {} 条，删除 {} 条", totalInserted, deletedCount);
        return totalInserted;
    }

    /**
     * 根据名称列表批量物理删除（已存在删除方法）
     */
    private int batchDeleteByHostCodes(Collection<String> hostcodeSet) {
        if (CollectionUtils.isEmpty(hostcodeSet)) {
            return 0;
        }
        List<String> list = new ArrayList<>(hostcodeSet);
        int totalDeleted = 0;
        int batchSize = 100; // Oracle IN 子句限制
        for (int i = 0; i < list.size(); i += batchSize) {
            List<String> batch = list.subList(i, Math.min(i + batchSize, list.size()));
            totalDeleted += codeConfigMapper.deleteByHostCodes(batch);
        }
        return totalDeleted;
    }

}