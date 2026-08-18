package cn.iocoder.yudao.module.aps.controller.admin.assempart.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 总成与子件关联表管理 Response VO")
@Data
@ExcelIgnoreUnannotated
public class AssemPartRespVO {

    @Schema(description = "总成订单号")
    @ExcelProperty("总成订单号")
    private String orderNo;

    @Schema(description = "总成数量")
    @ExcelProperty("总成数量")
    private BigDecimal quantity;

    @Schema(description = "零部件订单号")
    @ExcelProperty("零部件订单号")
    private String componentOrder;

    @Schema(description = "零部件数量")
    @ExcelProperty("零部件数量")
    private BigDecimal allocQty;

    @Schema(description = "计划时间")
    @ExcelProperty("计划时间")
    private Date scheduleTime;

    @Schema(description = "创建时间")
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "4653")
    @ExcelProperty("编号")
    private Long id;
}