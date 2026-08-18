package cn.iocoder.yudao.module.buyer.dal.mysql.codeconfig;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.buyer.dal.dataobject.codeconfig.CodeConfigDO;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import org.apache.ibatis.annotations.Mapper;
import cn.iocoder.yudao.module.buyer.controller.admin.codeconfig.vo.*;
import org.apache.ibatis.annotations.Param;

/**
 * 主机编码配置 Mapper
 *
 * @author 柳文
 */
@Mapper
public interface CodeConfigMapper extends BaseMapperX<CodeConfigDO> {

    default PageResult<CodeConfigDO> selectPage(CodeConfigPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<CodeConfigDO>()
                .likeIfPresent(CodeConfigDO::getName, reqVO.getName())
                .eqIfPresent(CodeConfigDO::getHostCode, reqVO.getHostCode())
                .eqIfPresent(CodeConfigDO::getTeliCode, reqVO.getTeliCode())
                .betweenIfPresent(CodeConfigDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(CodeConfigDO::getId));
    }

    @InterceptorIgnore(tenantLine = "true")
    int deleteByHostCodes(@Param("hostcodes") Collection<String> hostcodes);

    @InterceptorIgnore(tenantLine = "true")
    void batchInsert(@Param("list") List<CodeConfigDO> list);
}