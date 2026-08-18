package cn.iocoder.yudao.module.buyer.controller.admin.vehicleplan.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 买家车辆营销计划表（主机厂计划）新增/修改 Request VO")
@Data
public class VehiclePlanSaveReqVO {

    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "26324")
    private BigDecimal id;

    @Schema(description = "产品线")
    private String productLine;

    @Schema(description = "产品机型")
    private String productModel;

    @Schema(description = "车型代码")
    private String vehicleCode;

    @Schema(description = "2025年度顺序号/车号")
    private String seqNo2025;

    @Schema(description = "2026年度顺序号/车号")
    private String seqNo2026;

    @Schema(description = "裸机订单号")
    private String bareMachineOrderNo;

    @Schema(description = "行驶单元订单号")
    private String drivingUnitOrderNo;

    @Schema(description = "内外贸（内贸/外贸）", example = "1")
    private String tradeType;

    @Schema(description = "台份数量")
    private BigDecimal unitQuantity;

    @Schema(description = "底盘上线计划日期")
    private LocalDateTime chassisOnlinePlanDate;

    @Schema(description = "成台完工计划日期")
    private LocalDateTime finishedProductPlanDate;

    @Schema(description = "VIN")
    private String vin;

    @Schema(description = "下料完工计划")
    private String blankingPlanDate;

    @Schema(description = "吊臂板/中吨位支腿完工计划")
    private String boomLegPlanDate;

    @Schema(description = "吊臂或主臂顶底完工计划")
    private String boomTopBottomPlanDate;

    @Schema(description = "转台结构件完工计划")
    private String turntablePlanDate;

    @Schema(description = "车架结构件完工计划")
    private String framePlanDate;

}