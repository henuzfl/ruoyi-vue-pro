package cn.iocoder.yudao.module.aps.controller.admin.dataimport.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 营销数据导入 Response VO")
@Data
@ExcelIgnoreUnannotated
public class DataImportRespVO {

    @Schema(description = "主键编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "9241")
    @ExcelProperty("主键编号")
    private Short id;

    @Schema(description = "主计划单号", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("主计划单号")
    private String danhao;

    @Schema(description = "总成图号", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("总成图号")
    private String productCode;

    @Schema(description = "总成数量", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("总成数量")
    private BigDecimal quantity;

    @Schema(description = "创建订单日期")
    @ExcelProperty("创建订单日期")
    private LocalDateTime orderDate;

    @Schema(description = "主计划完成日期", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("主计划完成日期")
    private LocalDateTime eventTime;

    @Schema(description = "状态（0=正常，1=停用）", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("状态（0=正常，1=停用）")
    private Short status;

    @Schema(description = "备注", example = "你说的对")
    @ExcelProperty("备注")
    private String remark;

    @Schema(description = "创建时间")
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}