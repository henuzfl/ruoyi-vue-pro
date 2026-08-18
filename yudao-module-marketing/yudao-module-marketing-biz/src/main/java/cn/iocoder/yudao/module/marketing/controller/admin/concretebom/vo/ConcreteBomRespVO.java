package cn.iocoder.yudao.module.marketing.controller.admin.concretebom.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 混凝土BOM Response VO")
@Data
@ExcelIgnoreUnannotated
public class ConcreteBomRespVO {

    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "12380")
    @ExcelProperty("主键ID")
    private Long id;

    @Schema(description = "车型（物料编码或车型名称）", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("车型（物料编码或车型名称）")
    private String vehicleModel;

    @Schema(description = "分解油缸（部件名称及路径）", requiredMode = Schema.RequiredMode.REQUIRED, example = "李四")
    @ExcelProperty("分解油缸（部件名称及路径）")
    private String cylinderName;

    @Schema(description = "SBP编码")
    @ExcelProperty("SBP编码")
    private String sbpCode;

    @Schema(description = "配置（如数量等）")
    @ExcelProperty("配置（如数量等）")
    private String config;

    @Schema(description = "导入时间")
    @ExcelProperty("导入时间")
    private LocalDateTime importTime;

    @Schema(description = "创建时间")
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}