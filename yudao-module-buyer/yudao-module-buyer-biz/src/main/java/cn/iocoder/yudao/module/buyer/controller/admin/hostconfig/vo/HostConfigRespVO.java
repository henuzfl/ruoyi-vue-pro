package cn.iocoder.yudao.module.buyer.controller.admin.hostconfig.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 主机配置 Response VO")
@Data
@ExcelIgnoreUnannotated
public class HostConfigRespVO {

    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "20478")
    @ExcelProperty("主键ID")
    private BigDecimal id;

    @Schema(description = "车型", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("车型")
    private String vehicleModel;

    @Schema(description = "特力图号", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("特力图号")
    private String specialDrawingNo;

    @Schema(description = "配置数量", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("配置数量")
    private Long configQuantity;

    @Schema(description = "事业部", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("事业部")
    private String businessUnit;

    @Schema(description = "创建时间")
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}