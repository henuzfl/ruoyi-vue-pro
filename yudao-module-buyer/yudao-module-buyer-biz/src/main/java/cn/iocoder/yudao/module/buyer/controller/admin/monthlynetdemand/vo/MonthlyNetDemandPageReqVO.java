package cn.iocoder.yudao.module.buyer.controller.admin.monthlynetdemand.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class MonthlyNetDemandPageReqVO extends PageParam {
    @NotBlank(message = "计划月份不能为空")
    @Pattern(regexp = "\\d{4}-(0[1-9]|1[0-2])", message = "计划月份格式应为 yyyy-MM")
    private String planMonth;
    private String materialNo;
    private String materialName;
    private Boolean mapped;
}
