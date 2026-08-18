package cn.iocoder.yudao.module.marketing.controller.admin.scissorliftwplan.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 高机剪叉周计划新增/修改 Request VO")
@Data
public class ScissorLiftWplanSaveReqVO {

    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "18666")
    private Long id;

    @Schema(description = "产品线")
    private String productLine;

    @Schema(description = "精准车型")
    private String preciseModel;

    @Schema(description = "产品型号")
    private String productModel;

    @Schema(description = "精准BOM")
    private String preciseBom;

    @Schema(description = "生产日期", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "生产日期不能为空")
    private LocalDateTime planDate;

    @Schema(description = "周次")
    private String weekNo;

    @Schema(description = "周起始日期")
    private LocalDateTime weekStartDate;

    @Schema(description = "周结束日期")
    private LocalDateTime weekEndDate;

    @Schema(description = "当日数量")
    private Integer dailyQuantity;

    @Schema(description = "车号范围")
    private String carNumberRange;

    @Schema(description = "生产线条类型", example = "2")
    private String productionLineType;

    @Schema(description = "板块")
    private String plate;

    @Schema(description = "导入批次时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "导入批次时间不能为空")
    private LocalDateTime importTime;

}