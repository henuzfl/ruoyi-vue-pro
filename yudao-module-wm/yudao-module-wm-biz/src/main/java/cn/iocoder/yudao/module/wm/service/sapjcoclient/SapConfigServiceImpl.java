package cn.iocoder.yudao.module.wm.service.sapjcoclient;

import com.baomidou.dynamic.datasource.annotation.DS;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import cn.iocoder.yudao.module.wm.dal.dataobject.sapjcoclient.AclBasecodeDO;
import cn.iocoder.yudao.module.wm.dal.mysql.sapjcoclient.AclBasecodeMapper;
import cn.iocoder.yudao.module.wm.service.sapjcoclient.ISapConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.util.List;


import java.util.HashMap;
import java.util.Map;

/**
 * 订单追溯需求 Service 实现类
 *
 * @author 柳文
 */
@Service
@DS("oracle") // 指定使用 Oracle 数据源
@Validated
public class SapConfigServiceImpl implements ISapConfigService {


    @Autowired
    private AclBasecodeMapper aclBasecodeMapper;

    /**
     * 直接对应老项目的 getBaseCodeByType 方法
     * 使用Spring Cache注解缓存结果，提升性能
     */
    @Override
    @Cacheable(value = "sapConfigCache", key = "#type")
    public String getBaseCodeByType(String type) {
        List<AclBasecodeDO> list = aclBasecodeMapper.selectByType(type);
        if (list != null && !list.isEmpty()) {
            return list.get(0).getCode(); // 假设每个type唯一
        }
        return null; // 或返回空字符串，根据老项目逻辑调整
    }
}