package cn.iocoder.yudao.module.buyer.controller.admin.monthlynetdemand;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.buyer.controller.admin.monthlynetdemand.vo.MonthlyNetDemandPageReqVO;
import cn.iocoder.yudao.module.buyer.controller.admin.monthlynetdemand.vo.MonthlyNetDemandRespVO;
import cn.iocoder.yudao.module.buyer.service.monthlynetdemand.MonthlyNetDemandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import javax.validation.Valid;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 月净需求")
@RestController
@RequestMapping("/buyer/monthly-net-demand")
@Validated
public class MonthlyNetDemandController {
    @Resource
    private MonthlyNetDemandService monthlyNetDemandService;

    @GetMapping("/page")
    @Operation(summary = "获得月净需求分页")
    @PreAuthorize("@ss.hasPermission('buyer:monthly-net-demand:query')")
    public CommonResult<PageResult<MonthlyNetDemandRespVO>> getPage(@Valid MonthlyNetDemandPageReqVO reqVO) {
        return success(monthlyNetDemandService.getPage(reqVO));
    }
}
