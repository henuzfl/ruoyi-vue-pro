package cn.iocoder.yudao.module.wm.controller.admin.bom.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - BOM Response VO")
@Data
@ExcelIgnoreUnannotated
public class BomRespVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("ID")
    private Long id;

    @Schema(description = "工厂代码", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("工厂代码")
    private String werks;

    @Schema(description = "BOM等级")
    @ExcelProperty("BOM等级")
    private String stufe;

    @Schema(description = "路径标识")
    @ExcelProperty("路径标识")
    private String wegxx;

    @Schema(description = "BOM类型")
    @ExcelProperty("BOM类型")
    private String bmtyp;

    @Schema(description = "可选的BOM")
    @ExcelProperty("可选的BOM")
    private String vwegx;

    @Schema(description = "组件描述")
    @ExcelProperty("组件描述")
    private String ojtxb;

    @Schema(description = "物料描述")
    @ExcelProperty("物料描述")
    private String ojtxp;

    @Schema(description = "物料类型")
    @ExcelProperty("物料类型")
    private String mtart;

    @Schema(description = "数量")
    @ExcelProperty("数量")
    private BigDecimal menge;

    @Schema(description = "单位")
    @ExcelProperty("单位")
    private String meins;

    @Schema(description = "组件物料号")
    @ExcelProperty("组件物料号")
    private String idnrk;

    @Schema(description = "父物料号")
    @ExcelProperty("父物料号")
    private String parentIdnrk;

    @Schema(description = "BOM版本")
    @ExcelProperty("BOM版本")
    private String version;

    @Schema(description = "生效日期")
    @ExcelProperty("生效日期")
    private LocalDateTime validFrom;

    @Schema(description = "失效日期")
    @ExcelProperty("失效日期")
    private LocalDateTime validTo;

    @Schema(description = "状态：0-无效，1-有效")
    @ExcelProperty("状态")
    private Integer status;

    @Schema(description = "创建时间")
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "备注")
    @ExcelProperty("备注")
    private String remark;
}