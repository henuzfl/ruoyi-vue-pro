package cn.iocoder.yudao.module.marketing.service.concretebom;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.marketing.controller.admin.concretebom.vo.*;
import cn.iocoder.yudao.module.marketing.dal.dataobject.concretebom.ConcreteBomDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.web.multipart.MultipartFile;

/**
 * 混凝土BOM Service 接口
 *
 * @author 柳文
 */
public interface ConcreteBomService {

    /**
     * 创建混凝土BOM
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createConcreteBom(@Valid ConcreteBomSaveReqVO createReqVO);

    /**
     * 更新混凝土BOM
     *
     * @param updateReqVO 更新信息
     */
    void updateConcreteBom(@Valid ConcreteBomSaveReqVO updateReqVO);

    /**
     * 删除混凝土BOM
     *
     * @param id 编号
     */
    void deleteConcreteBom(Long id);

    /**
     * 获得混凝土BOM
     *
     * @param id 编号
     * @return 混凝土BOM
     */
    ConcreteBomDO getConcreteBom(Long id);

    /**
     * 获得混凝土BOM分页
     *
     * @param pageReqVO 分页查询
     * @return 混凝土BOM分页
     */
    PageResult<ConcreteBomDO> getConcreteBomPage(ConcreteBomPageReqVO pageReqVO);

    /**
     * 导入混凝土BOM数据
     *
     * @param file       Excel文件
     * @param importTime 导入批次时间（用户选择）
     * @throws IOException IO异常
     */
    void importExcel(MultipartFile file, LocalDate importTime) throws IOException;

    /**
     * 对比最新批次与上一批次的配置差异
     * @return 差异列表
     */
    List<ConcreteBomCompareRespVO> compareDifference();
}