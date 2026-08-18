package cn.iocoder.yudao.module.buyer.service.deliver;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.buyer.controller.admin.deliver.vo.*;
import cn.iocoder.yudao.module.buyer.dal.dataobject.deliver.DeliverDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.apache.ibatis.annotations.Update;
import org.springframework.web.multipart.MultipartFile;

/**
 * 配送与采购报表 Service 接口
 *
 * @author 柳文
 */
public interface DeliverService {

    /**
     * 创建配送与采购报表
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createDeliver(@Valid DeliverSaveReqVO createReqVO);

    /**
     * 更新配送与采购报表
     *
     * @param updateReqVO 更新信息
     */
    void updateDeliver(@Valid DeliverSaveReqVO updateReqVO);

    /**
     * 删除配送与采购报表
     *
     * @param id 编号
     */
    void deleteDeliver(Long id);

    /**
     * 获得配送与采购报表
     *
     * @param id 编号
     * @return 配送与采购报表
     */
    DeliverDO getDeliver(Long id);

    /**
     * 获得配送与采购报表分页
     *
     * @param pageReqVO 分页查询
     * @return 配送与采购报表分页
     */
    PageResult<DeliverDO> getDeliverPage(DeliverPageReqVO pageReqVO);

    /**
     * 导入
     */
    int importDeliver(MultipartFile file) throws IOException;


}