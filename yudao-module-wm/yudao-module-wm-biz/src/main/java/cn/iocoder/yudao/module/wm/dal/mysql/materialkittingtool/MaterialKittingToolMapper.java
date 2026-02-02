package cn.iocoder.yudao.module.wm.dal.mysql.materialkittingtool;

import cn.iocoder.yudao.module.wm.dal.dataobject.materialkittingtool.MaterialKittingToolDO;
import cn.iocoder.yudao.module.wm.controller.admin.materialkittingtool.vo.MaterialKittingToolPageReqVO;
import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MaterialKittingToolMapper {

    /**
     * 查询齐套分析数据
     */
    List<MaterialKittingToolDO> selectMaterialKittingTool();

    /**
     * 根据参数查询齐套分析数据
     */
    @InterceptorIgnore(tenantLine = "true")
    List<MaterialKittingToolDO> selectMaterialKittingToolByParams(@Param("params") MaterialKittingToolPageReqVO params);
}