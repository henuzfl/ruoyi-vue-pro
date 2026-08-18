package cn.iocoder.yudao.module.wm.controller.admin.kittingmaster.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 主计划导出 Request VO")
@Data
public class KittingMasterExportReqVO {

    @Schema(description = "生产订单号", example = "MO20240209001")
    private String productionOrderNo;

    @Schema(description = "总成物料号", example = "MAT-1001")
    private String assemblyMaterialNo;

    @Schema(description = "组件物料号", example = "MAT-1002")
    private String componentMaterialNo;

    @Schema(description = "主物料描述", example = "液压油缸总成")
    private String mainMaterialDesc;

    @Schema(description = "生产车间", example = "Y01")
    private String productionWorkshop;

    @Schema(description = "排产日期范围")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime[] scheduledDate;

    @Schema(description = "齐套状态", example = "齐套")
    private String kittingStatus;

    @Schema(description = "总成齐套状态", example = "齐套")
    private String assemblyKittingStatus;

    @Schema(description = "排产数量最小值", example = "100")
    private Integer scheduledQuantityMin;

    @Schema(description = "排产数量最大值", example = "1000")
    private Integer scheduledQuantityMax;

    @Schema(description = "组件描述", example = "液压缸")
    private String componentDesc;

    @Schema(description = "单位用量最小值", example = "1.0")
    private Double unitUsageMin;

    @Schema(description = "单位用量最大值", example = "10.0")
    private Double unitUsageMax;

    @Schema(description = "需求数量最小值", example = "100.0")
    private Double requiredQtyMin;

    @Schema(description = "需求数量最大值", example = "1000.0")
    private Double requiredQtyMax;

    @Schema(description = "明日库存最小值", example = "0.0")
    private Double stockTomorrowMin;

    @Schema(description = "明日库存最大值", example = "1000.0")
    private Double stockTomorrowMax;

    @Schema(description = "后日库存最小值", example = "0.0")
    private Double stockDayAfterTomorrowMin;

    @Schema(description = "后日库存最大值", example = "1000.0")
    private Double stockDayAfterTomorrowMax;

    @Schema(description = "第三日库存最小值", example = "0.0")
    private Double stockThirdDayMin;

    @Schema(description = "第三日库存最大值", example = "1000.0")
    private Double stockThirdDayMax;

    @Schema(description = "当前库存最小值", example = "0.0")
    private Double stockQuantityMin;

    @Schema(description = "当前库存最大值", example = "10000.0")
    private Double stockQuantityMax;

    @Schema(description = "缺料数量最小值", example = "0.0")
    private Double shortageQtyMin;

    @Schema(description = "缺料数量最大值", example = "1000.0")
    private Double shortageQtyMax;

    @Schema(description = "创建时间范围")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime[] createTime;
}