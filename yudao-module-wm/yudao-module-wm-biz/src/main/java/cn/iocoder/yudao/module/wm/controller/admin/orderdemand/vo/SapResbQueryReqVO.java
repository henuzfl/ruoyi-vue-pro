package cn.iocoder.yudao.module.wm.controller.admin.orderdemand.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - SAP预留数据查询 Request VO")
@Data
public class SapResbQueryReqVO {

    @Schema(description = "工厂", example = "6400")
    private String plant;

    @Schema(description = "生产订单号", example = "1000001")
    private String aufnr;

    @Schema(description = "预留号", example = "12345")
    private String rsnum;

    @Schema(description = "日期范围 (格式: yyyyMMdd-yyyyMMdd)", example = "20230101-20231231")
    private String dateRange;
}