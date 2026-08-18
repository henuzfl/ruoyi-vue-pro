package cn.iocoder.yudao.module.buyer.controller.admin.vehicleplan.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 买家车辆营销计划表（主机厂计划） Response VO")
@Data
@ExcelIgnoreUnannotated
public class VehiclePlanRespVO {

    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "26324")
    @ExcelProperty("主键ID")
    private String id;

    @Schema(description = "导入日期（年月日）")
    private Date importDate;

    @Schema(description = "产品线")
    @ExcelProperty("产品线")
    private String productLine;

    @Schema(description = "产品机型")
    @ExcelProperty("产品机型")
    private String productModel;

    @Schema(description = "车型代码")
    @ExcelProperty("车型代码")
    private String vehicleCode;

    @Schema(description = "2025年度顺序号/车号")
    @ExcelProperty("2025年度顺序号/车号")
    private String seqNo2025;

    @Schema(description = "2026年度顺序号/车号")
    @ExcelProperty("2026年度顺序号/车号")
    private String seqNo2026;

    @Schema(description = "裸机订单号")
    @ExcelProperty("裸机订单号")
    private String bareMachineOrderNo;

    @Schema(description = "行驶单元订单号")
    @ExcelProperty("行驶单元订单号")
    private String drivingUnitOrderNo;

    @Schema(description = "内外贸（内贸/外贸）", example = "1")
    @ExcelProperty("内外贸（内贸/外贸）")
    private String tradeType;

    @Schema(description = "台份数量")
    @ExcelProperty("台份数量")
    private BigDecimal unitQuantity;

    @Schema(description = "底盘上线计划日期")
    @ExcelProperty("底盘上线计划日期")
    private LocalDateTime chassisOnlinePlanDate;

    @Schema(description = "成台完工计划日期")
    @ExcelProperty("成台完工计划日期")
    private LocalDateTime finishedProductPlanDate;

    @Schema(description = "创建时间")
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "vin")
    @ExcelProperty("vin")
    private String vin;

    @Schema(description = "下料完工计划")
    @ExcelProperty("下料完工计划")
    private String blankingPlanDate;

    @Schema(description = "吊臂板/中吨位支腿完工计划")
    @ExcelProperty("吊臂板/中吨位支腿完工计划")
    private String boomLegPlanDate;

    @Schema(description = "吊臂或主臂顶底完工计划")
    @ExcelProperty("吊臂或主臂顶底完工计划")
    private String boomTopBottomPlanDate;

    @Schema(description = "转台结构件完工计划")
    @ExcelProperty("转台结构件完工计划")
    private String turntablePlanDate;

    @Schema(description = "车架结构件完工计划")
    @ExcelProperty("车架结构件完工计划")
    private String framePlanDate;

//    @Schema(description = "导入日期（年月）")
//    public String getImportDateYearMonth() {
//        if (this.importDate == null) {
//            return null;
//        }
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy年M月");
//        return this.importDate.format(formatter);
//    }
}