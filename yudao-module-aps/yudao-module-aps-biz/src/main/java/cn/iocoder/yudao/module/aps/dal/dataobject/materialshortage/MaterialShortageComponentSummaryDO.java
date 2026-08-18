package cn.iocoder.yudao.module.aps.dal.dataobject.materialshortage;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class MaterialShortageComponentSummaryDO {
    private String componentMaterialNo;
    private String componentDesc;
    private BigDecimal totalRequirement;
    private BigDecimal stockQuantity;
    private BigDecimal transit;
    private BigDecimal totalIssue;
    private BigDecimal shortageQty;
    private Integer mainCount;
    private String mainMaterialNos;
    public String getMainMaterialNos() {
        if (mainMaterialNos != null && mainMaterialNos.endsWith(",")) {
            return mainMaterialNos.substring(0, mainMaterialNos.length() - 1);
        }
        return mainMaterialNos;
    }
}