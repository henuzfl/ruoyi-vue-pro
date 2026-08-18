package cn.iocoder.yudao.module.wm.dal.mysql.bom;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.wm.controller.admin.bom.vo.BomPageReqVO;
import cn.iocoder.yudao.module.wm.dal.dataobject.bom.BomDO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Mapper
public interface BomMapper extends BaseMapperX<BomDO> {

    /**
     * 根据工厂和物料号查询BOM
     */
    default BomDO selectByWerksAndIdnrk(String werks, String idnrk) {
        return selectOne(new LambdaQueryWrapper<BomDO>()
                .eq(BomDO::getWerks, werks)
                .eq(BomDO::getIdnrk, idnrk));
    }

    /**
     * 根据工厂、物料号和项目号查询BOM（唯一标识）
     */
    default BomDO selectByWerksAndIdnrkAndPosnr(String werks, String idnrk, String posnr) {
        return selectOne(new LambdaQueryWrapper<BomDO>()
                .eq(BomDO::getWerks, werks)
                .eq(BomDO::getIdnrk, idnrk)
                .eq(BomDO::getPosnr, posnr));
    }
    /**
     * 分页查询
     */
    default PageResult<BomDO> selectPage(BomPageReqVO pageReqVO) {
        return selectPage(pageReqVO, new LambdaQueryWrapperX<BomDO>()
                .likeIfPresent(BomDO::getWerks, pageReqVO.getWerks())
                .likeIfPresent(BomDO::getIdnrk, pageReqVO.getIdnrk())
                .likeIfPresent(BomDO::getOjtxp, pageReqVO.getOjtxp())
                .eqIfPresent(BomDO::getStatus, pageReqVO.getStatus())
                .orderByDesc(BomDO::getId));
    }

    /**
     * 根据父物料号查询子BOM
     */
    default List<BomDO> selectByParentIdnrk(String parentIdnrk) {
        return selectList(new LambdaQueryWrapper<BomDO>()
                .eq(BomDO::getParentIdnrk, parentIdnrk)
                .orderByAsc(BomDO::getPosnr));
    }

    /**
     * 根据工厂和BOM编号查询
     */
    default List<BomDO> selectByWerksAndStlnr(String werks, String stlnr) {
        return selectList(new LambdaQueryWrapper<BomDO>()
                .eq(BomDO::getWerks, werks)
                .eq(BomDO::getStlnr, stlnr)
                .orderByAsc(BomDO::getStufe, BomDO::getPosnr));
    }
}