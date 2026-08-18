package cn.iocoder.yudao.module.wm.controller.admin.kittingmaster.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 主计划 Response VO")
@Data
@ExcelIgnoreUnannotated
public class KittingMasterRespVO {

    @Schema(description = "生产订单号", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("生产订单号")
    private String productionOrderNo;

    @Schema(description = "总成物料号", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("总成物料号")
    private String assemblyMaterialNo;

    @Schema(description = "主物料描述")
    @ExcelProperty("主物料描述")
    private String mainMaterialDesc;

    @Schema(description = "订单类型")
    @ExcelProperty("订单类型")
    private String orderMessage;

    @Schema(description = "父订单")
    @ExcelProperty("父订单")
    private String parentOrderNo;

    @Schema(description = "排产数量")
    @ExcelProperty("排产数量")
    private BigDecimal scheduledQuantity;

    @Schema(description = "生产车间")
    @ExcelProperty("生产车间")
    private String productionWorkshop;

    // 新增字段
    @Schema(description = "排产时间")
    @ExcelProperty("排产时间")
    private LocalDateTime scheduledDate;

    @Schema(description = "总成是否齐套")
    @ExcelProperty("总成是否齐套")
    private String assemblyKittingStatus;
    //private String kittingStatus;

    @Schema(description = "是否全部出库")
    @ExcelProperty("是否全部出库")
    private String openFlag;

}