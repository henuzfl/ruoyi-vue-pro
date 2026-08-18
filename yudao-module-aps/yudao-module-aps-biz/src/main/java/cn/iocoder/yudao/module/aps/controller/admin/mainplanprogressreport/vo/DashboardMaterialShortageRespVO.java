package cn.iocoder.yudao.module.aps.controller.admin.mainplanprogressreport.vo;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class DashboardMaterialShortageRespVO {
    private String componentCode;
    private String componentDesc;
    private BigDecimal shortageQty;
    private Integer affectedOrders;
}