package cn.iocoder.yudao.module.marketing.controller.admin.concretebom.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 混凝土BOM新增/修改 Request VO")
@Data
public class ConcreteBomSaveReqVO {

    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "12380")
    private Long id;

    @Schema(description = "车型（物料编码或车型名称）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "车型（物料编码或车型名称）不能为空")
    private String vehicleModel;

    @Schema(description = "分解油缸（部件名称及路径）", requiredMode = Schema.RequiredMode.REQUIRED, example = "李四")
    @NotEmpty(message = "分解油缸（部件名称及路径）不能为空")
    private String cylinderName;

    @Schema(description = "SBP编码")
    private String sbpCode;

    @Schema(description = "配置（如数量等）")
    private String config;

    @Schema(description = "导入时间")
    private LocalDateTime importTime;

}