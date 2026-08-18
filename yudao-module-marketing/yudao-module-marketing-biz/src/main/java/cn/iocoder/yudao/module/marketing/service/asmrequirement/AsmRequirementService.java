package cn.iocoder.yudao.module.marketing.service.asmrequirement;

import java.io.IOException;
import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.module.marketing.controller.admin.asmrequirement.vo.*;
import cn.iocoder.yudao.module.marketing.dal.dataobject.asmrequirement.AsmRequirementDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.web.multipart.MultipartFile;

/**
 * 营销总成需求 Service 接口
 *
 * @author 柳文
 */
public interface AsmRequirementService {

    /**
     * 创建营销总成需求
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createAsmRequirement(@Valid AsmRequirementSaveReqVO createReqVO);

    /**
     * 更新营销总成需求
     *
     * @param updateReqVO 更新信息
     */
    void updateAsmRequirement(@Valid AsmRequirementSaveReqVO updateReqVO);

    /**
     * 删除营销总成需求
     *
     * @param id 编号
     */
    void deleteAsmRequirement(Long id);

    /**
     * 获得营销总成需求
     *
     * @param id 编号
     * @return 营销总成需求
     */
    AsmRequirementDO getAsmRequirement(Long id);

    /**
     * 获得营销总成需求分页
     *
     * @param pageReqVO 分页查询
     * @return 营销总成需求分页
     */
    PageResult<AsmRequirementDO> getAsmRequirementPage(AsmRequirementPageReqVO pageReqVO);

    /**
     * 导入营销总成需求 Excel
     * @param file Excel 文件
     * @throws IOException
     */
    void importExcel(MultipartFile file) throws IOException;

}