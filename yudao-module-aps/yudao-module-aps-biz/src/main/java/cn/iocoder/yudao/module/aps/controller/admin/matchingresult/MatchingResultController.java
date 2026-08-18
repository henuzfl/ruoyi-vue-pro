package cn.iocoder.yudao.module.aps.controller.admin.matchingresult;

import cn.iocoder.yudao.module.aps.dal.dataobject.purchasefeedback.PurchaseFeedbackDO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;

import javax.validation.constraints.*;
import javax.validation.*;
import javax.servlet.http.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.io.IOException;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;

import cn.iocoder.yudao.framework.apilog.core.annotation.ApiAccessLog;
import static cn.iocoder.yudao.framework.apilog.core.enums.OperateTypeEnum.*;

import cn.iocoder.yudao.module.aps.controller.admin.matchingresult.vo.*;
import cn.iocoder.yudao.module.aps.dal.dataobject.matchingresult.MatchingResultDO;
import cn.iocoder.yudao.module.aps.service.matchingresult.MatchingResultService;
import cn.iocoder.yudao.module.aps.dal.dataobject.purchasefeedback.PurchaseFeedbackDO;
import cn.iocoder.yudao.module.aps.service.purchasefeedback.PurchaseFeedbackService;
import java.util.stream.Collectors;

@Tag(name = "管理后台 - 主计划物料需求匹配")
@RestController
@RequestMapping("/aps/matching-result")
@Validated
@Slf4j
public class MatchingResultController {

    @Resource
    private MatchingResultService matchingResultService;

    @Resource
    private PurchaseFeedbackService purchaseFeedbackService;

    @PostMapping("/create")
    @Operation(summary = "创建主计划物料需求匹配")
    @PreAuthorize("@ss.hasPermission('aps:matching-result:create')")
    public CommonResult<BigDecimal> createMatchingResult(@Valid @RequestBody MatchingResultSaveReqVO createReqVO) {
        return success(matchingResultService.createMatchingResult(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新主计划物料需求匹配")
    @PreAuthorize("@ss.hasPermission('aps:matching-result:update')")
    public CommonResult<Boolean> updateMatchingResult(@Valid @RequestBody MatchingResultSaveReqVO updateReqVO) {
        matchingResultService.updateMatchingResult(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除主计划物料需求匹配")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('aps:matching-result:delete')")
    public CommonResult<Boolean> deleteMatchingResult(@RequestParam("id") BigDecimal id) {
        matchingResultService.deleteMatchingResult(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得主计划物料需求匹配")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('aps:matching-result:query')")
    public CommonResult<MatchingResultRespVO> getMatchingResult(@RequestParam("id") BigDecimal id) {
        MatchingResultDO matchingResult = matchingResultService.getMatchingResult(id);
        return success(BeanUtils.toBean(matchingResult, MatchingResultRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得主计划物料需求匹配分页")
    @PreAuthorize("@ss.hasPermission('aps:matching-result:query')")
    public CommonResult<PageResult<MatchingResultRespVO>> getMatchingResultPage(@Valid MatchingResultPageReqVO pageReqVO) {
        // 1. 获取原始分页数据
        PageResult<MatchingResultDO> pageResult = matchingResultService.getMatchingResultPage(pageReqVO);

        // 2. 转换为响应 VO
        List<MatchingResultRespVO> respList = BeanUtils.toBean(pageResult.getList(), MatchingResultRespVO.class);

        // 3. 批量填充反馈备注
        if (!respList.isEmpty()) {
            // 提取条件
            List<Map<String, Object>> keyList = respList.stream()
                    .map(vo -> {
                        Map<String, Object> map = new HashMap<>();
                        map.put("orderNo", vo.getOrderNo());
                        map.put("scheduleTime", vo.getScheduleTime());
                        map.put("purchaseMaterial", vo.getPurchaseMaterial());
                        return map;
                    })
                    .collect(Collectors.toList());

            // 批量查询反馈
            List<PurchaseFeedbackDO> feedbackList = purchaseFeedbackService.listByKeys(keyList); // 新增方法

            // 合并备注
            Map<String, String> feedbackMap = feedbackList.stream()
                    .collect(Collectors.groupingBy(
                            fb -> buildKey(fb.getOrderNo(), fb.getScheduleTime(), fb.getPurchaseMaterial()),
                            Collectors.mapping(PurchaseFeedbackDO::getFeedbackRemark,
                                    Collectors.joining("; "))
                    ));

            // 填充到响应 VO
            respList.forEach(vo -> {
                String key = buildKey(vo.getOrderNo(), vo.getScheduleTime(), vo.getPurchaseMaterial());
                vo.setFeedbackRemarks(feedbackMap.getOrDefault(key, ""));
            });
        }

        // 返回分页结果
        PageResult<MatchingResultRespVO> result = new PageResult<>(respList, pageResult.getTotal());
        return success(result);
    }

    // 构建组合键
    private String buildKey(String orderNo, Date scheduleTime, String purchaseMaterial) {
        return orderNo + "_" + (scheduleTime != null ? scheduleTime.getTime() : "") + "_" + purchaseMaterial;
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出主计划物料需求匹配 Excel")
    @PreAuthorize("@ss.hasPermission('aps:matching-result:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportMatchingResultExcel(@Valid MatchingResultPageReqVO pageReqVO,
                                          HttpServletResponse response) throws IOException {
        // 1. 自动设置最新一天（复用现有逻辑）
        if (pageReqVO.getCreateTime() == null || pageReqVO.getCreateTime().length != 2 ||
                (pageReqVO.getCreateTime()[0] == null && pageReqVO.getCreateTime()[1] == null)) {
            String latestDay = matchingResultService.getLatestDataDay();
            if (latestDay != null && !latestDay.isEmpty()) {
                LocalDate date = LocalDate.parse(latestDay);
                Date start = Date.from(date.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
                Date end = Date.from(date.atTime(23, 59, 59).atZone(ZoneId.systemDefault()).toInstant());
                pageReqVO.setCreateTime(new Date[]{start, end});
            }
        }

        long start = System.currentTimeMillis();
        log.info("导出开始，查询条件：{}", pageReqVO);

        // 2. 分页循环导出（直接调用现有分页查询）
        int pageSize = 500;
        int currentPage = 1;
        List<MatchingResultRespVO> allData = new ArrayList<>();

        while (true) {
            pageReqVO.setPageNo(currentPage);
            pageReqVO.setPageSize(pageSize);

            // ★ 调用现有的分页查询（返回 DO）
            PageResult<MatchingResultDO> doPage = matchingResultService.getMatchingResultPage(pageReqVO);
            if (doPage.getList().isEmpty()) {
                break;
            }

            // ★ 转换为 VO（复用 /page 接口的填充逻辑）
            List<MatchingResultRespVO> respList = BeanUtils.toBean(doPage.getList(), MatchingResultRespVO.class);

            // ★ 填充反馈备注（完全复用 /page 接口的代码）
            if (!respList.isEmpty()) {
                List<Map<String, Object>> keyList = respList.stream()
                        .map(vo -> {
                            Map<String, Object> map = new HashMap<>();
                            map.put("orderNo", vo.getOrderNo());
                            map.put("scheduleTime", vo.getScheduleTime());
                            map.put("purchaseMaterial", vo.getPurchaseMaterial());
                            return map;
                        })
                        .collect(Collectors.toList());

                // 分批查询反馈（防止 OR 过多）
                List<PurchaseFeedbackDO> feedbackList = new ArrayList<>();
                int batchSize = 500;
                for (int i = 0; i < keyList.size(); i += batchSize) {
                    int end = Math.min(i + batchSize, keyList.size());
                    feedbackList.addAll(purchaseFeedbackService.listByKeys(keyList.subList(i, end)));
                }

                Map<String, String> feedbackMap = feedbackList.stream()
                        .collect(Collectors.groupingBy(
                                fb -> buildKey(fb.getOrderNo(), fb.getScheduleTime(), fb.getPurchaseMaterial()),
                                Collectors.mapping(PurchaseFeedbackDO::getFeedbackRemark,
                                        Collectors.joining("; "))
                        ));

                respList.forEach(vo -> {
                    String key = buildKey(vo.getOrderNo(), vo.getScheduleTime(), vo.getPurchaseMaterial());
                    vo.setFeedbackRemarks(feedbackMap.getOrDefault(key, ""));
                });
            }

            allData.addAll(respList);

            // 判断是否最后一页
            if (respList.size() < pageSize) {
                break;
            }
            currentPage++;

            if (allData.size() >= 50000) {
                log.warn("导出数据已达 50000 条上限，已截断");
                break;
            }
            log.info("已导出 {} 条记录", allData.size());
        }

        log.info("导出完成，总记录数：{}，总耗时：{} ms", allData.size(), System.currentTimeMillis() - start);
        ExcelUtils.write(response, "主计划物料需求匹配.xls", "数据", MatchingResultRespVO.class, allData);
    }

    @PostMapping("/run-procedure")
    @Operation(summary = "运行主计划物料分配存储过程")
    @PreAuthorize("@ss.hasPermission('aps:matching-result:run-procedure')")
    public CommonResult<Boolean> runProcedure() {
        matchingResultService.runMasterAllocationProcedure();
        return success(true);
    }

}