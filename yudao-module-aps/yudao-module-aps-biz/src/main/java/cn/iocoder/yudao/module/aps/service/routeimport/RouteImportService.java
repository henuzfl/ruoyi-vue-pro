package cn.iocoder.yudao.module.aps.service.routeimport;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.aps.controller.admin.routeimport.vo.*;
import cn.iocoder.yudao.module.aps.dal.dataobject.routeimport.RouteImportDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 工艺路线导入 Service 接口
 *
 * @author 柳文
 */
public interface RouteImportService {

    /**
     * 创建工艺路线导入
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createRouteImport(@Valid RouteImportSaveReqVO createReqVO);

    /**
     * 更新工艺路线导入
     *
     * @param updateReqVO 更新信息
     */
    void updateRouteImport(@Valid RouteImportSaveReqVO updateReqVO);

    /**
     * 删除工艺路线导入
     *
     * @param id 编号
     */
    void deleteRouteImport(Long id);

    /**
     * 获得工艺路线导入
     *
     * @param id 编号
     * @return 工艺路线导入
     */
    RouteImportDO getRouteImport(Long id);

    /**
     * 获得工艺路线导入分页
     *
     * @param pageReqVO 分页查询
     * @return 工艺路线导入分页
     */
    PageResult<RouteImportDO> getRouteImportPage(RouteImportPageReqVO pageReqVO);

}