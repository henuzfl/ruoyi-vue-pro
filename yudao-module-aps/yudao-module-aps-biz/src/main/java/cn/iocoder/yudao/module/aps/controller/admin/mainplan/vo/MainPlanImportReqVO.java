package cn.iocoder.yudao.module.aps.controller.admin.mainplan.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 主计划导入 Excel 行数据")
@Data
public class MainPlanImportReqVO {

    @Schema(description = "生产订单号", required = true)
    @NotEmpty(message = "生产订单号不能为空")
    @ExcelProperty(index = 0)   // 对应 Excel 第一列
    private String productionOrderNo;

    @Schema(description = "总成物料号", required = true)
    @NotEmpty(message = "总成物料号不能为空")
    @ExcelProperty(index = 1)   // 对应 Excel 第二列
    private String assemblyMaterialNo;

    @Schema(description = "主物料描述")
    @ExcelProperty(index = 2)   // 对应 Excel 第三列
    private String mainMaterialDesc;

    @Schema(description = "排产时间", required = true)
    @NotNull(message = "排产时间不能为空")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    @ExcelProperty(index = 3)   // 对应 Excel 第四列
    private LocalDateTime scheduledDate;

    @Schema(description = "排产数量")
    @ExcelProperty(index = 4)   // 对应 Excel 第五列
    private BigDecimal scheduledQuantity;

    @Schema(description = "生产车间")
    @ExcelProperty(index = 5)   // 对应 Excel 第六列
    private String productionWorkshop;

    @Schema(description = "已完成数量")
    private BigDecimal completedQuantity;
}