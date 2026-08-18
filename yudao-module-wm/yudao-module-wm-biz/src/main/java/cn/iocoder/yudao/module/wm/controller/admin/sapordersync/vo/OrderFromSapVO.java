package cn.iocoder.yudao.module.wm.controller.admin.sapordersync.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.math.BigDecimal;

@Schema(description = "管理后台 - SAP订单原始数据 Response VO")
@Data
public class OrderFromSapVO {

    @Schema(description = "订单编号")
    private String aufnr;

    @Schema(description = "物料编号")
    private String matnr;

    @Schema(description = "物料描述")
    private String maktx;

    @Schema(description = "订单项目计划总数量")
    private BigDecimal psmng;

    @Schema(description = "订单项目已收货数量")
    private BigDecimal wemng;

    @Schema(description = "订单类别")
    private String autyp;

    @Schema(description = "订单类型")
    private String auart;

    @Schema(description = "创建日期")
    private String erdat;

    @Schema(description = "输入者")
    private String ernam;

    @Schema(description = "订单状态码")
    private String astnr;

    @Schema(description = "订单状态描述")
    private String zastnr;

    @Schema(description = "计划发布日期")
    private String ftrmp;

    @Schema(description = "实际开始日期")
    private String gstri;

    @Schema(description = "计划完成日期")
    private String gltrs;

    @Schema(description = "工厂")
    private String werks;

    @Schema(description = "MRP控制员")
    private String dispo;

    @Schema(description = "生产主管")
    private String fevor;

    @Schema(description = "基本计量单位")
    private String meins;

    @Schema(description = "生产版本")
    private String verid;

    @Schema(description = "存储地点")
    private String lgort;

    @Schema(description = "实际结束日期")
    private String gltri;

    @Schema(description = "基本完成日期")
    private String gltrp;

    @Schema(description = "基本开始日期")
    private String gstrp;

    @Schema(description = "排产开始日期")
    private String gstrs;

    @Schema(description = "计划下达日期")
    private String ftrms;

    @Schema(description = "交货已完成标识")
    private String elikz;

    @Schema(description = "基本完成日期(项目)")
    private String dgltp;

    @Schema(description = "计划完成(项目)")
    private String dglts;

    @Schema(description = "卸货点")
    private String ablad;

    @Schema(description = "收货方")
    private String wempf;

    @Schema(description = "删除标记")
    private String loekz;

    @Schema(description = "最后更改人")
    private String aenam;

    @Schema(description = "更改日期")
    private String aedat;

    @Schema(description = "销售订单")
    private String kdauf;

    @Schema(description = "确认订单完成日期")
    private String getri;

    @Schema(description = "对象编号")
    private String objnr;
}