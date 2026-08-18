package cn.iocoder.yudao.module.marketing.controller.admin.aerialboomdplan.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 高机臂式日计划 Response VO")
@Data
@ExcelIgnoreUnannotated
public class AerialBoomDplanRespVO {

    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "96")
    @ExcelProperty("主键ID")
    private Long id;

    @Schema(description = "线别", example = "1")
    @ExcelProperty("线别")
    private String lineType;

    @Schema(description = "精准车型")
    @ExcelProperty("精准车型")
    private String preciseModel;

    @Schema(description = "产品型号")
    @ExcelProperty("产品型号")
    private String productModel;

    @Schema(description = "ZPS型号")
    @ExcelProperty("ZPS型号")
    private String zpsModel;

    @Schema(description = "精准BOM")
    @ExcelProperty("精准BOM")
    private String preciseBom;

    @Schema(description = "车号")
    @ExcelProperty("车号")
    private String carNo;

    @Schema(description = "订单号")
    @ExcelProperty("订单号")
    private String orderNo;

    @Schema(description = "备注", example = "随便")
    @ExcelProperty("备注")
    private String remark;

    @Schema(description = "内外贸版本")
    @ExcelProperty("内外贸版本")
    private String tradeVersion;

    @Schema(description = "台份", example = "25434")
    @ExcelProperty("台份")
    private Integer unitCount;

    @Schema(description = "上线计划")
    @ExcelProperty("上线计划")
    private LocalDateTime onlinePlan;

    @Schema(description = "成台计划")
    @ExcelProperty("成台计划")
    private LocalDateTime completePlan;

    @Schema(description = "报缴日期")
    @ExcelProperty("报缴日期")
    private LocalDateTime reportDate;

    @Schema(description = "国家")
    @ExcelProperty("国家")
    private String country;

    @Schema(description = "合同号")
    @ExcelProperty("合同号")
    private String contractNo;

    @Schema(description = "营销通知时间")
    @ExcelProperty("营销通知时间")
    private String marketingNoticeTime;

    @Schema(description = "订单开立时间")
    @ExcelProperty("订单开立时间")
    private LocalDateTime orderCreateTime;

    @Schema(description = "板块")
    @ExcelProperty("板块")
    private String plate;

    @Schema(description = "导入批次时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("导入批次时间")
    private LocalDateTime importTime;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "发货时间")
    @ExcelProperty("发货时间")
    private String deliveryTime;

}