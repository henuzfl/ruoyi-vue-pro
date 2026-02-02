package cn.iocoder.yudao.module.aps.controller.admin.dataimport.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 营销数据导入新增/修改 Request VO")
@Data
public class DataImportSaveReqVO {

    @Schema(description = "主键编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "9241")
    private Short id;

    @Schema(description = "主计划单号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "主计划单号不能为空")
    private String danhao;

    @Schema(description = "总成图号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "总成图号不能为空")
    private String productCode;

    @Schema(description = "总成数量", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "总成数量不能为空")
    private BigDecimal quantity;

    @Schema(description = "创建订单日期")
    private LocalDateTime orderDate;

    @Schema(description = "主计划完成日期", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "主计划完成日期不能为空")
    private LocalDateTime eventTime;

    @Schema(description = "状态（0=正常，1=停用）", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "状态（0=正常，1=停用）不能为空")
    private Short status;

    @Schema(description = "备注", example = "你说的对")
    private String remark;

}