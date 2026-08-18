package cn.iocoder.yudao.module.wm.api.bom.dto;

import lombok.Data;

/**
 * BOM Response DTO (对应 SAP 返回的 BOM 数据结构)
 *
 * @author 芋道源码
 */
@Data
public class BomRespDTO {

    /**
     * 工厂代码
     */
    private String werks;

    /**
     * BOM 等级
     */
    private String stufe;

    /**
     * 路径标识
     */
    private String wegxx;

    /**
     * BOM 类型
     */
    private String bmtyp;

    /**
     * 可选的 BOM
     */
    private String vwegx;

    /**
     * 组件描述
     */
    private String ojtxb;

    /**
     * 物料描述
     */
    private String ojtxp;

    /**
     * 物料类型
     */
    private String mtart;

    /**
     * 数量
     */
    private String menge;

    /**
     * 单位
     */
    private String meins;

    /**
     * 组件物料号
     */
    private String idnrk;

    // 可以添加一个组合字段作为 ID
    private String bomId; // werks + ":" + idnrk

    // 如果需要层级关系，可以添加
    private String parentId;
    private Integer level;
}