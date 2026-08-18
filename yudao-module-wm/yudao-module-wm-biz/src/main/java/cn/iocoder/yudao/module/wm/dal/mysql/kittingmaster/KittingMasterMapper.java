package cn.iocoder.yudao.module.wm.dal.mysql.kittingmaster;

import cn.iocoder.yudao.module.wm.dal.dataobject.kittingmaster.KittingMasterDO;
import cn.iocoder.yudao.module.wm.controller.admin.kittingmaster.vo.KittingMasterPageReqVO;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface KittingMasterMapper {

    /**
     * 查询齐套分析数据
     */
    List<KittingMasterDO> selectKittingMaster();

    /**
     * 根据参数查询齐套分析数据
     */
    @InterceptorIgnore(tenantLine = "true")
    List<KittingMasterDO> selectKittingMasterByParams(@Param("params") KittingMasterPageReqVO params);

    // 添加存储过程调用方法
    @InterceptorIgnore(tenantLine = "true")
    void callcompleProcedure();

    /**
     * 根据参数查询齐套分析数据-存储过程
     */
    @InterceptorIgnore(tenantLine = "true")
    List<KittingMasterDO> selectKittingcalculateByParams(@Param("params") KittingMasterPageReqVO params);

}