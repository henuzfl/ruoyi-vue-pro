package cn.iocoder.yudao.module.aps.service.assemblyorderprogress;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.aps.controller.admin.assemblyorderprogress.vo.AssemblyOrderProgressPageReqVO;
import cn.iocoder.yudao.module.aps.controller.admin.assemblyorderprogress.vo.AssemblyOrderProgressRespVO;
import cn.iocoder.yudao.module.aps.controller.admin.assemblyorderprogress.vo.AssemblyOrderShortageRespVO;

import java.util.List;

public interface AssemblyOrderProgressService {
    PageResult<AssemblyOrderProgressRespVO> getPage(AssemblyOrderProgressPageReqVO reqVO);
    List<AssemblyOrderShortageRespVO> getShortages(String materialCode, String scheduleTime);
    List<AssemblyOrderProgressRespVO> getExportList(AssemblyOrderProgressPageReqVO reqVO);
}