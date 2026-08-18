package cn.iocoder.yudao.module.wm.controller.admin.orderdemand.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;

@Schema(description = "管理后台 - SAP订单需求数据 Response VO")
@Data
public class OrderDemandFromSapVO {

    @Schema(description = "预留编号")
    private String rsnum;

    @Schema(description = "需求日期")
    private String bdter;

    @Schema(description = "生产订单号")
    private String aufnr;

    @Schema(description = "项目号")
    private String rspos;

    @Schema(description = "物料编码")
    private String matnr;

    @Schema(description = "物料描述")
    private String maktx;

    @Schema(description = "需求数量")
    private BigDecimal bdmng;

    @Schema(description = "删除数量")
    private BigDecimal enmng;

    @Schema(description = "单位")
    private String meins;

    @Schema(description = "已删除标识")
    private String xloek;

    @Schema(description = "移动标识")
    private String xwaok;

    @Schema(description = "移动类型")
    private String bwart;

    @Schema(description = "仓储地点")
    private String lgort;

    @Schema(description = "散装物料")
    private String schgt;

    @Schema(description = "虚拟项目")
    private String dumps;

    @Schema(description = "预留状态")
    private String rssta;

    @Schema(description = "更改人")
    private String aenam;

    @Schema(description = "成本中心")
    private String sanka;

    @Schema(description = "更改日期")
    private String aedat;

    @Schema(description = "工厂")
    private String werks;

    @Schema(description = "排序字符串")
    private String sortf;
}