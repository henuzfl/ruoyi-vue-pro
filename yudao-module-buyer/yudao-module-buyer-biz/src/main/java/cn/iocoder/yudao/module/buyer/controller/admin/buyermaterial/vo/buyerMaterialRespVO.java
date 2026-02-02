package cn.iocoder.yudao.module.buyer.controller.admin.buyermaterial.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 需求输入 Response VO")
@Data
@ExcelIgnoreUnannotated
public class buyerInputRespVO {

    @Schema(description = "主键编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "5730")
    @ExcelProperty("主键编号")
    private Long id;

    @Schema(description = "客户", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("客户")
    private String customer;

    @Schema(description = "车型")
    @ExcelProperty("车型")
    private String vehicleModel;

    @Schema(description = "总成物料", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("总成物料")
    private String assemblyMaterial;

    @Schema(description = "总成数量", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("总成数量")
    private BigDecimal assemblyQuantity;

    @Schema(description = "需求月份")
    @ExcelProperty("需求月份")
    private String demandMonth;

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