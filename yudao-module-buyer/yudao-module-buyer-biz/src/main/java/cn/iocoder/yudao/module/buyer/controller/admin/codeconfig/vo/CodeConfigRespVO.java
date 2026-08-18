package cn.iocoder.yudao.module.buyer.controller.admin.codeconfig.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 主机编码配置 Response VO")
@Data
@ExcelIgnoreUnannotated
public class CodeConfigRespVO {

    @Schema(description = "主键ID（雪花算法）", requiredMode = Schema.RequiredMode.REQUIRED, example = "136")
    @ExcelProperty("主键ID（雪花算法）")
    private BigDecimal id;

    @Schema(description = "名称", example = "李四")
    @ExcelProperty("名称")
    private String name;

    @Schema(description = "主机编码")
    @ExcelProperty("主机编码")
    private String hostCode;

    @Schema(description = "特力编码")
    @ExcelProperty("特力编码")
    private String teliCode;

    @Schema(description = "创建时间")
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}