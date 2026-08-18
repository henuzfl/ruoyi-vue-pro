package cn.iocoder.yudao.module.aps.controller.admin.purchasefeedback.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import lombok.Data;
import java.util.Date;

@Data
public class PurchaseFeedbackImportVO {

    @ExcelProperty(index = 0)          // 对应 Excel 第一列（A）
    private String orderNo;

    @ExcelProperty(index = 1)          // B 列
    @DateTimeFormat("yyyy-MM-dd")      // 匹配 Excel 中的日期格式
    private Date scheduleTime;

    @ExcelProperty(index = 2)          // C 列
    private String purchaseMaterial;

    @ExcelProperty(index = 3)          // D 列
    private String feedbackRemark;
}