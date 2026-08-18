package cn.iocoder.yudao.module.aps.controller.admin.purchasefeedback;

import cn.iocoder.yudao.framework.apilog.core.annotation.ApiAccessLog;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.module.aps.controller.admin.purchasefeedback.vo.PurchaseFeedbackImportVO;
import cn.iocoder.yudao.module.aps.controller.admin.purchasefeedback.vo.PurchaseFeedbackPageReqVO;
import cn.iocoder.yudao.module.aps.controller.admin.purchasefeedback.vo.PurchaseFeedbackRespVO;
import cn.iocoder.yudao.module.aps.controller.admin.purchasefeedback.vo.PurchaseFeedbackSaveReqVO;
import cn.iocoder.yudao.module.aps.dal.dataobject.purchasefeedback.PurchaseFeedbackDO;
import cn.iocoder.yudao.module.aps.service.purchasefeedback.PurchaseFeedbackService;
import com.alibaba.excel.EasyExcel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.text.SimpleDateFormat;

import static cn.iocoder.yudao.framework.apilog.core.enums.OperateTypeEnum.EXPORT;
import static cn.iocoder.yudao.framework.apilog.core.enums.OperateTypeEnum.IMPORT;   // 新增导入
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 采购反馈")
@RestController
@RequestMapping("/aps/purchase-feedback")
@Validated
@Slf4j
public class PurchaseFeedbackController {

    @Resource
    private PurchaseFeedbackService purchaseFeedbackService;

    @PostMapping("/create")
    @Operation(summary = "创建采购反馈")
    @PreAuthorize("@ss.hasPermission('aps:purchase-feedback:create')")
    public CommonResult<Long> createPurchaseFeedback(@Valid @RequestBody PurchaseFeedbackSaveReqVO createReqVO) {
        return success(purchaseFeedbackService.createPurchaseFeedback(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新采购反馈")
    @PreAuthorize("@ss.hasPermission('aps:purchase-feedback:update')")
    public CommonResult<Boolean> updatePurchaseFeedback(@Valid @RequestBody PurchaseFeedbackSaveReqVO updateReqVO) {
        purchaseFeedbackService.updatePurchaseFeedback(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除采购反馈")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('aps:purchase-feedback:delete')")
    public CommonResult<Boolean> deletePurchaseFeedback(@RequestParam("id") Long id) {
        purchaseFeedbackService.deletePurchaseFeedback(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得采购反馈")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('aps:purchase-feedback:query')")
    public CommonResult<PurchaseFeedbackRespVO> getPurchaseFeedback(@RequestParam("id") Long id) {
        PurchaseFeedbackDO feedback = purchaseFeedbackService.getPurchaseFeedback(id);
        return success(BeanUtils.toBean(feedback, PurchaseFeedbackRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得采购反馈分页")
    @PreAuthorize("@ss.hasPermission('aps:purchase-feedback:query')")
    public CommonResult<PageResult<PurchaseFeedbackRespVO>> getPurchaseFeedbackPage(@Valid PurchaseFeedbackPageReqVO pageReqVO) {
        PageResult<PurchaseFeedbackDO> pageResult = purchaseFeedbackService.getPurchaseFeedbackPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, PurchaseFeedbackRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出采购反馈 Excel")
    @PreAuthorize("@ss.hasPermission('aps:purchase-feedback:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportPurchaseFeedbackExcel(@Valid PurchaseFeedbackPageReqVO pageReqVO,
                                            HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(-1); // 不分页，导出全部
        List<PurchaseFeedbackDO> list = purchaseFeedbackService.getPurchaseFeedbackPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "采购反馈.xls", "数据", PurchaseFeedbackRespVO.class,
                BeanUtils.toBean(list, PurchaseFeedbackRespVO.class));
    }

    @PostMapping("/import-excel")
    @Operation(summary = "导入采购反馈")
    @PreAuthorize("@ss.hasPermission('aps:purchase-feedback:import')")
    @ApiAccessLog(operateType = IMPORT)
    public CommonResult<Integer> importPurchaseFeedback(@RequestParam("file") MultipartFile file) throws IOException {
        // 使用 EasyExcel 原生读取，不指定 head 类，直接读取为 List<Map<Integer, Object>>
        List<Map<Integer, Object>> dataList = EasyExcel.read(file.getInputStream()).sheet().doReadSync();
        log.info("Excel 总行数：{}", dataList.size());

        List<PurchaseFeedbackImportVO> importList = new ArrayList<>();
        // 从第 1 行开始（索引 0 可能是标题，但如果有标题行需跳过）
        // 假设第 0 行是标题，从第 1 行开始读取数据
        for (int i = 0; i < dataList.size(); i++) {
            Map<Integer, Object> row = dataList.get(i);
            if (row == null || row.isEmpty()) continue;

            // 手动取值
            String orderNo = getCellStringValue(row.get(0));
            Date scheduleTime = parseDateFromCell(row.get(1));
            String purchaseMaterial = getCellStringValue(row.get(2));
            String feedbackRemark = getCellStringValue(row.get(3));

            // 跳过空行（订单号为空即视为无效行）
            if (orderNo == null || orderNo.trim().isEmpty()) {
                continue;
            }

            PurchaseFeedbackImportVO vo = new PurchaseFeedbackImportVO();
            vo.setOrderNo(orderNo);
            vo.setScheduleTime(scheduleTime);
            vo.setPurchaseMaterial(purchaseMaterial);
            vo.setFeedbackRemark(feedbackRemark);
            importList.add(vo);
        }

        // 打印样例验证
        if (!importList.isEmpty()) {
            PurchaseFeedbackImportVO first = importList.get(0);
            log.info("导入样例：订单号={}, 排产时间={}, 采购物料={}, 反馈备注={}",
                    first.getOrderNo(), first.getScheduleTime(),
                    first.getPurchaseMaterial(), first.getFeedbackRemark());
        }

        Integer count = purchaseFeedbackService.importPurchaseFeedback(importList);
        return success(count);
    }

    /**
     * 获取单元格字符串值（处理 null、Number 等）
     */
    private String getCellStringValue(Object cell) {
        if (cell == null) return null;
        if (cell instanceof String) return (String) cell;
        if (cell instanceof Number) return String.valueOf(cell);
        if (cell instanceof Date) return cell.toString(); // 备用
        return cell.toString();
    }

    /**
     * 从单元格解析日期（支持 yyyy-MM-dd 和 yyyy-MM-dd HH:mm:ss）
     */
    private Date parseDateFromCell(Object cell) {
        if (cell == null) return null;
        if (cell instanceof Date) return (Date) cell;
        if (cell instanceof String) {
            String str = (String) cell;
            try {
                if (str.contains(" ")) {
                    return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(str);
                } else {
                    return new SimpleDateFormat("yyyy-MM-dd").parse(str);
                }
            } catch (ParseException e) {
                log.warn("日期解析失败：{}", str);
                return null;
            }
        }
        return null;
    }

}