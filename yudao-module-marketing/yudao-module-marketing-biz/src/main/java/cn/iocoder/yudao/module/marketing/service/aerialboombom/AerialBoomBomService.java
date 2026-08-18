package cn.iocoder.yudao.module.marketing.service.aerialboombom;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.marketing.controller.admin.aerialboombom.vo.*;
import cn.iocoder.yudao.module.marketing.dal.dataobject.aerialboombom.AerialBoomBomDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.web.multipart.MultipartFile;

/**
 * 高机臂式/剪叉BOM物料清单 Service 接口
 *
 * @author 柳文
 */
public interface AerialBoomBomService {

    /**
     * 创建高机臂式/剪叉BOM物料清单
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createAerialBoomBom(@Valid AerialBoomBomSaveReqVO createReqVO);

    /**
     * 更新高机臂式/剪叉BOM物料清单
     *
     * @param updateReqVO 更新信息
     */
    void updateAerialBoomBom(@Valid AerialBoomBomSaveReqVO updateReqVO);

    /**
     * 删除高机臂式/剪叉BOM物料清单
     *
     * @param id 编号
     */
    void deleteAerialBoomBom(Long id);

    /**
     * 获得高机臂式/剪叉BOM物料清单
     *
     * @param id 编号
     * @return 高机臂式/剪叉BOM物料清单
     */
    AerialBoomBomDO getAerialBoomBom(Long id);

    /**
     * 获得高机臂式/剪叉BOM物料清单分页
     *
     * @param pageReqVO 分页查询
     * @return 高机臂式/剪叉BOM物料清单分页
     */
    PageResult<AerialBoomBomDO> getAerialBoomBomPage(AerialBoomBomPageReqVO pageReqVO);

    void importBomExcel(MultipartFile file, LocalDate importTime) throws IOException;



}