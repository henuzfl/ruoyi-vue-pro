package cn.iocoder.yudao.module.aps.controller.admin.assempart.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Data
public class AssemPartImportVO {
    @ExcelProperty(index = 0)
    private String orderNo;      // 总成订单号

    @ExcelProperty(index = 1)
    private BigDecimal quantity;      // 总成数量

    @ExcelProperty(index = 2)
    private Date scheduleTime; // 总成计划日期

    @ExcelProperty(index = 3)
    private String componentOrder;       // 零部件订单号

    @ExcelProperty(index = 4)
    private BigDecimal allocQty;      // 零部件数量
}