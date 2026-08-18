package cn.iocoder.yudao.module.aps.service.mainplan;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import cn.iocoder.yudao.module.aps.controller.admin.mainplan.vo.*;
import cn.iocoder.yudao.module.aps.dal.dataobject.mainplan.MainPlanDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.aps.dal.mysql.mainplan.MainPlanMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.aps.enums.ErrorCodeConstants.*;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils; // 确保导入
import org.apache.ibatis.session.SqlSessionFactory;
import java.util.stream.Collectors;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.ExecutorType;

/**
 * 主计划 Service 实现类
 *
 * @author 柳文
 */
@Service
@DS("oracle") // 指定使用 Oracle 数据源
@Validated
@Slf4j
public class MainPlanServiceImpl implements MainPlanService {

    @Resource
    private MainPlanMapper mainPlanMapper;

    @Resource
    private SqlSessionFactory sqlSessionFactory;

    @Override
    public BigDecimal createMainPlan(MainPlanSaveReqVO createReqVO) {
        // 插入
        MainPlanDO mainPlan = BeanUtils.toBean(createReqVO, MainPlanDO.class);
        mainPlanMapper.insert(mainPlan);
        // 返回
        return mainPlan.getId();
    }

    @Override
    public void updateMainPlan(MainPlanSaveReqVO updateReqVO) {
        // 校验存在
        validateMainPlanExists(updateReqVO.getId());
        // 更新
        MainPlanDO updateObj = BeanUtils.toBean(updateReqVO, MainPlanDO.class);
        mainPlanMapper.updateById(updateObj);
    }

    @Override
    public void deleteMainPlan(BigDecimal id) {
        // 校验存在
        validateMainPlanExists(id);
        // 删除
        mainPlanMapper.physicalDeleteById(id);
    }

    private void validateMainPlanExists(BigDecimal id) {
        if (mainPlanMapper.selectById(id) == null) {
            throw exception(MAIN_PLAN_NOT_EXISTS);
        }
    }

    @Override
    public MainPlanDO getMainPlan(BigDecimal id) {
        return mainPlanMapper.selectById(id);
    }

    @Override
    public PageResult<MainPlanDO> getMainPlanPage(MainPlanPageReqVO pageReqVO) {
        return mainPlanMapper.selectPage(pageReqVO);
    }
    /*
        查询总成物料号
     */
    @Override
    public List<String> getDistinctAssemblyMaterialNo() {   // 方法名明确
        return mainPlanMapper.selectDistinctAssemblyMaterialNo(); // 通过实例调用
    }

    /*
       查询总成物料号
    */
    @Override
    public List<String> getDistinctComponentMaterialNo() {   // 方法名明确
        return mainPlanMapper.selectDistinctComponentMaterialNo(); // 通过实例调用
    }

    @Override
    public void clearAllMainPlan() {
        // 物理删除当前租户下的全部主计划数据
        mainPlanMapper.physicalDeleteAll();
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public int importMainPlan(List<MainPlanImportReqVO> importVOList) {
        if (CollectionUtils.isEmpty(importVOList)) {
            return 0;
        }

        // 1. 数据清洗（补0、去重等）
        Set<String> existKeys = new HashSet<>();
        List<MainPlanDO> saveList = new ArrayList<>();

        for (MainPlanImportReqVO vo : importVOList) {
            // 确保订单号以固定长度12位（左侧补0）
            if (vo.getProductionOrderNo() != null && !vo.getProductionOrderNo().isEmpty()) {
                vo.setProductionOrderNo(String.format("%012d", new BigDecimal(vo.getProductionOrderNo()).longValue()));
            }
            if (vo.getAssemblyMaterialNo() != null && !vo.getAssemblyMaterialNo().isEmpty()) {
                // 总成物料号也按需补0？ 如需固定长度可同样处理，此处略
            }

            if (StringUtils.isEmpty(vo.getProductionOrderNo()) || StringUtils.isEmpty(vo.getAssemblyMaterialNo())) {
                log.warn("跳过空数据行：生产订单号或总成物料号为空");
                continue;
            }
            String key = vo.getProductionOrderNo() + "_" + vo.getAssemblyMaterialNo();
//            if (!existKeys.add(key)) {
//                log.warn("重复数据已跳过：{}", key);
//                continue;
//            }

            MainPlanDO entity = BeanUtils.toBean(vo, MainPlanDO.class);
            saveList.add(entity);
        }

        if (saveList.isEmpty()) {
            return 0;
        }

        // 2. 物理删除：订单号+车间+排产日期（日期部分）完全相同的旧数据
//        Set<String> deleteKeySet = new HashSet<>();
//        List<MainPlanDO> deleteKeys = new ArrayList<>();
//        for (MainPlanDO item : saveList) {
//            // 使用 uniqueKey 去重，避免重复 OR 条件
//            String uniqueKey = item.getProductionOrderNo() + "_"
//                    + item.getProductionWorkshop() + "_"
//                    + (item.getScheduledDate() != null ? item.getScheduledDate().toLocalDate() : "");
//            if (deleteKeySet.add(uniqueKey)) {
//                MainPlanDO key = new MainPlanDO();
//                key.setProductionOrderNo(item.getProductionOrderNo());
//                key.setProductionWorkshop(item.getProductionWorkshop());
//                key.setScheduledDate(item.getScheduledDate());
//                deleteKeys.add(key);
//            }
//        }
//        if (!deleteKeys.isEmpty()) {
//            mainPlanMapper.physicalDeleteByOrderKeys(deleteKeys);
//        }

        // 3. 批量插入（原有逻辑不变）
        LocalDateTime now = LocalDateTime.now();
        saveList.forEach(item -> {
            item.setCreateTime(now);
            item.setCompletedQuantity(item.getCompletedQuantity() == null ? BigDecimal.ZERO : item.getCompletedQuantity());
        });

        int totalSize = saveList.size();
        List<BigDecimal> nextIds = mainPlanMapper.selectNextIds(totalSize);
        if (nextIds.size() != totalSize) {
            throw new RuntimeException("批量获取序列值失败，期望获取 " + totalSize + " 个，实际获取 " + nextIds.size() + " 个");
        }
        for (int j = 0; j < totalSize; j++) {
            saveList.get(j).setId(nextIds.get(j));
        }

        int batchSize = 50;
        int totalInserted = 0;
        for (int i = 0; i < totalSize; i += batchSize) {
            int end = Math.min(i + batchSize, totalSize);
            List<MainPlanDO> batchList = saveList.subList(i, end);
            mainPlanMapper.batchInsert(batchList);
            totalInserted += batchList.size();
        }

        return totalInserted;
    }
//        for (int i = 0; i < saveList.size(); i += batchSize) {
//            a = a.add(BigDecimal.valueOf(i));
//            int end = Math.min(i + batchSize, saveList.size());
//            List<MainPlanDO> batchList = saveList.subList(i, end);
//            log.debug("待插入数据: productionOrderNo={}, assemblyMaterialNo={}, scheduledDate={}, scheduledQuantity={}",
//                    batchList.get(i).getId(),
//                    batchList.get(i).getProductionOrderNo(), batchList.get(i).getAssemblyMaterialNo(),
//                    batchList.get(i).getScheduledDate(), batchList.get(i).getScheduledQuantity());
//            batchList.get(i).setId(a);
//            mainPlanMapper.batchInsert(batchList);   // 直接调用批量插入
//            totalInserted += batchList.size();
//        }

        // 2. 分批批量插入（每批不超过 990 条，可根据数据库限制调整）
//        int batchSize = 990;
//        int totalInserted = 0;
//        for (int i = 0; i < saveList.size(); i += batchSize) {
//            int end = Math.min(i + batchSize, saveList.size());
//            List<MainPlanDO> batchList = saveList.subList(i, end);
//            // 开启批处理模式的 SqlSession
//            try (SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false)) {
//                MainPlanMapper batchMapper = sqlSession.getMapper(MainPlanMapper.class);
//                for (MainPlanDO entity : batchList) {
//                    entity.setId(mainPlanMapper.selectNextId());
//                    batchMapper.insert(entity);  // 循环执行单条 insert
//                }
//                sqlSession.flushStatements();  // 将批处理发送到数据库，但不提交事务
//                // 事务由 Spring 的 @Transactional 统一提交
//            }
//            totalInserted += batchList.size();
//            log.info("已批量插入 {} 条记录", batchList.size());
//        }

}