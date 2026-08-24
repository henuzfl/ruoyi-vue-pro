package cn.iocoder.yudao.module.buyer.service.overseasinventory;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.buyer.controller.admin.overseasinventory.vo.*;
import cn.iocoder.yudao.module.buyer.dal.dataobject.overseasinventory.OverseasInventoryDO;
import cn.iocoder.yudao.module.buyer.dal.mysql.overseasinventory.OverseasInventoryMapper;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.buyer.enums.ErrorCodeConstants.OVERSEAS_INVENTORY_NOT_EXISTS;

@Service
@DS("oracle")
@Validated
@Slf4j
public class OverseasInventoryServiceImpl implements OverseasInventoryService {

    @Resource
    private OverseasInventoryMapper overseasInventoryMapper;

    @Override
    public BigDecimal createOverseasInventory(OverseasInventorySaveReqVO reqVO) {
        OverseasInventoryDO entity = BeanUtils.toBean(reqVO, OverseasInventoryDO.class);
        overseasInventoryMapper.insert(entity);
        return entity.getId();
    }

    @Override
    public void updateOverseasInventory(OverseasInventorySaveReqVO reqVO) {
        validateExists(reqVO.getId());
        overseasInventoryMapper.updateById(BeanUtils.toBean(reqVO, OverseasInventoryDO.class));
    }

    @Override
    public void deleteOverseasInventory(BigDecimal id) {
        validateExists(id);
        overseasInventoryMapper.deleteById(id);
    }

    private void validateExists(BigDecimal id) {
        if (id == null || overseasInventoryMapper.selectById(id) == null) {
            throw exception(OVERSEAS_INVENTORY_NOT_EXISTS);
        }
    }

    @Override
    public OverseasInventoryDO getOverseasInventory(BigDecimal id) {
        return overseasInventoryMapper.selectById(id);
    }

    @Override
    public PageResult<OverseasInventoryDO> getOverseasInventoryPage(OverseasInventoryPageReqVO reqVO) {
        return overseasInventoryMapper.selectPage(reqVO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int importOverseasInventory(List<OverseasInventoryImportReqVO> list) {
        if (list == null || list.isEmpty()) {
            throw new IllegalArgumentException("导入文件没有可导入的数据");
        }
        List<OverseasInventoryDO> entities = new ArrayList<>(list.size());
        LocalDateTime now = LocalDateTime.now();
        for (OverseasInventoryImportReqVO item : list) {
            OverseasInventoryDO entity = BeanUtils.toBean(item, OverseasInventoryDO.class);
            entity.setId(BigDecimal.valueOf(IdWorker.getId()));
            entity.setCreateTime(now);
            entity.setUpdateTime(now);
            entities.add(entity);
        }
        overseasInventoryMapper.deleteAllPhysical();
        int batchSize = 30;
        for (int i = 0; i < entities.size(); i += batchSize) {
            overseasInventoryMapper.batchInsert(entities.subList(i, Math.min(i + batchSize, entities.size())));
        }
        log.info("驻外库存全量导入完成，共 {} 条", entities.size());
        return entities.size();
    }
}
