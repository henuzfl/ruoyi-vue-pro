package cn.iocoder.yudao.module.aps.controller.admin.assemblyorderprogress.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Schema(description = "管理后台 - 总成进度 Response VO")
@Data
public class AssemblyOrderProgressRespVO {

    @Schema(description = "总成物料编码")
    @ExcelProperty("图号")
    private String materialCode;

    @Schema(description = "总成物料描述")
    @ExcelProperty("名称")
    private String materialDesc;

    @Schema(description = "时间维度（日/周/月）")
    @ExcelProperty("时间")
    private String timeDimension; // 如 '2025-03-10', '2025-10', '2025-W10'

    @Schema(description = "计划数量")
    @ExcelProperty("计划数量")
    private BigDecimal planQty;

    @Schema(description = "完成数量")
    @ExcelProperty("完成数量")
    private BigDecimal completedQty;

    @Schema(description = "库存数量")
    @ExcelProperty("库存数量")
    private BigDecimal stockQty;

    @Schema(description = "缺料零件种数")
    private Integer shortageCount; // 用于前端显示
}