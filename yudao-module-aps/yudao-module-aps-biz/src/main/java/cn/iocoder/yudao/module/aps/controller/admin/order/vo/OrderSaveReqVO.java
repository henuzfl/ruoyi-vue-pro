package cn.iocoder.yudao.module.aps.controller.admin.order.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 订单表 - SAP订单信息新增/修改 Request VO")
@Data
public class OrderSaveReqVO {

    @Schema(description = "主键ID")
    private long id;

    @Schema(description = "订单号(主键)", requiredMode = Schema.RequiredMode.REQUIRED)
    private String productionOrderNo;

    @Schema(description = "物料号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "物料号不能为空")
    private String assemblyMaterialNo;

    @Schema(description = "物料描述")
    private String mainMaterialDesc;

    @Schema(description = "订单类型(如ZY02)", example = "1")
    private String componentOrderType;

    @Schema(description = "订单数量", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "订单数量不能为空")
    private BigDecimal scheduledQuantity;

    @Schema(description = "已交货数量(已入库数量)")
    private BigDecimal deliveredQuantity;

    @Schema(description = "创建日期")
    private LocalDateTime creationDate;

    @Schema(description = "创建者/输入者")
    private String createdBy;

    @Schema(description = "系统状态(如REL PCNF等)", example = "1")
    private String systemStatus;

    @Schema(description = "计划开始日期")
    private LocalDateTime scheduledDate;

    @Schema(description = "实际开始时间(日期+时间)")
    private LocalDateTime actualStartTime;

    @Schema(description = "计划完成日期")
    private LocalDateTime basicEndDate;

    @Schema(description = "工厂代码")
    private String plant;

    @Schema(description = "MRP控制员代码")
    private String mrpController;

    @Schema(description = "生产主管")
    private String productionWorkshop;

    @Schema(description = "计量单位(如PC)")
    private String unitOfMeasure;

    @Schema(description = "生产版本(如0001)")
    private String productionVersion;

    @Schema(description = "实际完成日期 (Actual End Date)")
    private LocalDateTime actualEndDate;

    @Schema(description = "处理开始日期 (Process Start Date)")
    private LocalDateTime processStartDate;

    @Schema(description = "提交日期 (Submit Date)")
    private LocalDateTime submitDate;

    @Schema(description = "处理下达 (Process Released)")
    private String processReleased;

    @Schema(description = "集中订单处理 (Central Processing)")
    private String centralProc;

    @Schema(description = "更改日期 (Change Date)")
    private LocalDateTime changeDate;

    @Schema(description = "最后更改人 (Last Changed By)")
    private String lastChangedBy;

    @Schema(description = "订单类别 (Order Category)")
    private String orderCategory;

    @Schema(description = "销售订单 (Sales Order)")
    private String salesOrder;

    @Schema(description = "描述 (Description)", example = "你猜")
    private String description;

    @Schema(description = "确认产量")
    private BigDecimal confirmedQuantity;

}