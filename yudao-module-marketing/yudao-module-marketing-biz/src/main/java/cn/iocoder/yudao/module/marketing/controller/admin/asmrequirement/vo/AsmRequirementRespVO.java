package cn.iocoder.yudao.module.marketing.controller.admin.asmrequirement.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 营销总成需求 Response VO")
@Data
@ExcelIgnoreUnannotated
public class AsmRequirementRespVO {

    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "26395")
    @ExcelProperty("主键ID")
    private Long id;

    @Schema(description = "主机单位")
    @ExcelProperty("主机单位")
    private String hostUnit;

    @Schema(description = "车型")
    @ExcelProperty("车型")
    private String vehicleModel;

    @Schema(description = "总成物料编码", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("总成物料编码")
    private String assemblyMaterialNo;

    @Schema(description = "总成物料名称")
    @ExcelProperty("总成物料名称")
    private String mainMaterialDesc;

    @Schema(description = "需求数量")
    @ExcelProperty("需求数量")
    private BigDecimal requireQuantity;

    @Schema(description = "需求日期", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("需求日期")
    private LocalDateTime requireDate;

    @Schema(description = "创建时间")
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}