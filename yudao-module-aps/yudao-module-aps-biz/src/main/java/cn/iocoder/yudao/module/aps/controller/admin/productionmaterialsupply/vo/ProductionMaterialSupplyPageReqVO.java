package cn.iocoder.yudao.module.aps.controller.admin.productionmaterialsupply.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import java.util.Calendar;
import java.util.Date;

@Schema(description = "管理后台 - 生产订单物料供需分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
public class ProductionMaterialSupplyPageReqVO extends PageParam {

    @Schema(description = "生产订单号")
    private String productionOrderNo;
    @Schema(description = "总成物料号")
    private String assemblyMaterialNo;
    @Schema(description = "组件物料号")
    private String componentMaterialNo;
    @Schema(description = "组件物料描述")
    private String materialDesc;
    @Schema(description = "采购类型（E=外购，F=内部生产）")
    private String procurementType;
    @Schema(description = "排产日期开始")
    @NotNull(message = "排产日期开始不能为空")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date scheduledDateStart;
    @Schema(description = "排产日期结束")
    @NotNull(message = "排产日期结束不能为空")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date scheduledDateEnd;
    @Schema(description = "是否仅显示有缺口的数据")
    private Boolean onlyShortage;

    @AssertTrue(message = "排产日期开始不能晚于结束日期")
    public boolean isScheduledDateOrderValid() {
        return scheduledDateStart == null || scheduledDateEnd == null
                || !scheduledDateStart.after(scheduledDateEnd);
    }

    @AssertTrue(message = "排产日期范围不能超过 3 个自然月")
    public boolean isScheduledDateRangeValid() {
        if (scheduledDateStart == null || scheduledDateEnd == null
                || scheduledDateStart.after(scheduledDateEnd)) {
            return true;
        }
        Calendar maximumEnd = Calendar.getInstance();
        maximumEnd.setTime(scheduledDateStart);
        maximumEnd.add(Calendar.MONTH, 3);
        return !scheduledDateEnd.after(maximumEnd.getTime());
    }
}
