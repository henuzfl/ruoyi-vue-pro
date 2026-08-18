package cn.iocoder.yudao.module.buyer.controller.admin.hostrequirementcomparisondiff.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class HostRequirementComparisonDiffReqVO extends PageParam {
    private String currentDate;   // 当前版本日期 (yyyy-MM-dd)
    private String compareDate;   // 对比版本日期 (yyyy-MM-dd)
    private String productModel;
    private String seqNo2026;
    private String bareMachineOrderNo;
    private String materialNo;
}