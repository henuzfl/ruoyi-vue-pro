package cn.iocoder.yudao.module.marketing.controller.admin.scissorliftdplan.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 高机剪叉日计划新增/修改 Request VO")
@Data
public class ScissorLiftDplanSaveReqVO {

    @Schema(description = "主键ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "96")
    private Long id;

    @Schema(description = "线别", example = "1")
    private String lineType;

    @Schema(description = "精准车型")
    private String preciseModel;

    @Schema(description = "产品型号")
    private String productModel;

    @Schema(description = "ZPS型号")
    private String zpsModel;

    @Schema(description = "精准BOM")
    private String preciseBom;

    @Schema(description = "车号")
    private String carNo;

    @Schema(description = "订单号")
    private String orderNo;

    @Schema(description = "备注", example = "随便")
    private String remark;

    @Schema(description = "内外贸版本")
    private String tradeVersion;

    @Schema(description = "台份", example = "25434")
    private Integer unitCount;

    @Schema(description = "上线计划")
    private LocalDateTime onlinePlan;

    @Schema(description = "成台计划")
    private LocalDateTime completePlan;

    @Schema(description = "报缴日期")
    private LocalDateTime reportDate;

    @Schema(description = "国家")
    private String country;

    @Schema(description = "合同号")
    private String contractNo;

    @Schema(description = "营销通知时间")
    private String marketingNoticeTime;

    @Schema(description = "订单开立时间")
    private LocalDateTime orderCreateTime;

    @Schema(description = "板块")
    private String plate;

    @Schema(description = "导入批次时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "导入批次时间不能为空")
    private LocalDateTime importTime;

}