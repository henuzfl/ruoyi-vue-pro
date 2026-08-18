package cn.iocoder.yudao.module.wm.dal.mysql.sapjcoclient;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.wm.controller.admin.orderdemand.vo.OrderDemandPageReqVO;
import cn.iocoder.yudao.module.wm.dal.dataobject.sapjcoclient.AclBasecodeDO;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 订单追溯需求 Mapper
 *
 * @author 柳文
 */
@Mapper
public interface AclBasecodeMapper extends BaseMapperX<AclBasecodeDO> {

    @InterceptorIgnore(tenantLine = "true")
    @DS("oracle") // 指定使用 Oracle 数据源
    List<AclBasecodeDO> selectByType(String type);

}