package cn.iocoder.yudao.module.wm.controller.admin.sapordersync.vo;

import org.springframework.format.annotation.DateTimeFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

@Schema(description = "管理后台 - SAP订单查询 Request VO")
@Data
public class SapOrderQueryReqVO {

    @Schema(description = "工厂", example = "6400")
    private String plant;

    @Schema(description = "生产订单号", example = "1000001")
    private String aufnr;

    @Schema(description = "开始日期（创建/更改日期）")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @Schema(description = "结束日期（创建/更改日期）")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

}