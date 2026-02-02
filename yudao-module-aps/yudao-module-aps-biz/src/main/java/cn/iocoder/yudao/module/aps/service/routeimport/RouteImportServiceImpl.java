package cn.iocoder.yudao.module.aps.service.routeimport;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.module.aps.controller.admin.routeimport.vo.*;
import cn.iocoder.yudao.module.aps.dal.dataobject.routeimport.RouteImportDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import cn.iocoder.yudao.module.aps.dal.mysql.routeimport.RouteImportMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.aps.enums.ErrorCodeConstants.*;

/**
 * 工艺路线导入 Service 实现类
 *
 * @author 柳文
 */
@Service
@DS("oracle") // 指定使用 Oracle 数据源
@Validated
public class RouteImportServiceImpl implements RouteImportService {

    @Resource
    private RouteImportMapper routeImportMapper;

    @Override
    public Long createRouteImport(RouteImportSaveReqVO createReqVO) {
        // 插入
        RouteImportDO routeImport = BeanUtils.toBean(createReqVO, RouteImportDO.class);
        routeImportMapper.insert(routeImport);
        // 返回
        return routeImport.getId();
    }

    @Override
    public void updateRouteImport(RouteImportSaveReqVO updateReqVO) {
        // 校验存在
        validateRouteImportExists(updateReqVO.getId());
        // 更新
        RouteImportDO updateObj = BeanUtils.toBean(updateReqVO, RouteImportDO.class);
        routeImportMapper.updateById(updateObj);
    }

    @Override
    public void deleteRouteImport(Long id) {
        // 校验存在
        validateRouteImportExists(id);
        // 删除
        routeImportMapper.deleteById(id);
    }

    private void validateRouteImportExists(Long id) {
        if (routeImportMapper.selectById(id) == null) {
            throw exception(ROUTE_IMPORT_NOT_EXISTS);
        }
    }

    @Override
    public RouteImportDO getRouteImport(Long id) {
        return routeImportMapper.selectById(id);
    }

    @Override
    public PageResult<RouteImportDO> getRouteImportPage(RouteImportPageReqVO pageReqVO) {
        return routeImportMapper.selectPage(pageReqVO);
    }

}