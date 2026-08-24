package cn.iocoder.yudao.module.buyer.controller.admin.overseasinventory.vo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 驻外库存 Response VO")
@Data
public class OverseasInventoryRespVO {
    @ExcelIgnore
    private String id;
    @ExcelProperty("仓库")
    private String warehouse;
    @ExcelProperty("货主代码")
    private String ownerCode;
    @ExcelProperty("供应商代码")
    private String supplierCode;
    @ExcelProperty("供应商名称")
    private String supplierName;
    @ExcelProperty("货品编码")
    private String itemCode;
    @ExcelProperty("货品名称")
    private String itemName;
    @ExcelProperty("货品规格")
    private String itemSpecification;
    @ExcelProperty("库存数量")
    private Long inventoryQuantity;
    @ExcelProperty("占用数量")
    private Long occupiedQuantity;
    @ExcelProperty("可用量")
    private Long availableQuantity;
    @ExcelProperty("冻结数量")
    private Long frozenQuantity;
    @ExcelIgnore
    private LocalDateTime createTime;
}
