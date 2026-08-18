package cn.iocoder.yudao.module.aps.enums;

import cn.iocoder.yudao.framework.common.exception.ErrorCode;

/**
 * System 错误码枚举类
 *
 * system 系统，使用 1-002-000-000 段
 */
public interface ErrorCodeConstants {
    // ========== 计划模块 1-002-029-000 ==========
    ErrorCode PLAN_NOT_EXISTS = new ErrorCode(1_002_029_000, "设备调度不存在");
    ErrorCode DATA_IMPORT_NOT_EXISTS = new ErrorCode(1_002_030_000, "营销数据导入不存在");
    ErrorCode BOM_IMPORT_NOT_EXISTS = new ErrorCode(1_002_031_000, "物料BOM导入不存在");
    ErrorCode MASTER_IMPORT_NOT_EXISTS = new ErrorCode(1_002_032_000, "物料主数据导入不存在");
    ErrorCode ROUTE_IMPORT_NOT_EXISTS = new ErrorCode(1_002_033_000, "工艺路线导入不存在");
    ErrorCode MAIN_PLAN_NOT_EXISTS = new ErrorCode(1_002_034_000, "主计划不存在");
    ErrorCode ORDER_NOT_EXISTS = new ErrorCode(1_002_035_000, "订单表 - SAP订单信息不存在");
    ErrorCode MATCHING_RESULT_NOT_EXISTS = new ErrorCode(1_002_036_000, "主计划物料需求匹配不存在");
    ErrorCode ASSEM_PART_NOT_EXISTS = new ErrorCode(1_002_037_000, "总成与子件关联表不存在");
    ErrorCode ASSEMBLY_PLAN_NOT_EXISTS = new ErrorCode(1_002_038_000, "各车间开装计划不存在");
    ErrorCode PURCHASE_FEEDBACK_NOT_EXISTS = new ErrorCode(1_002_039_000, "采购反馈不存在");
    ErrorCode MATERIAL_SHORTAGE_NOT_EXISTS = new ErrorCode(2024002001, "物料缺口数据不存在");
    ErrorCode MATERIAL_SHORTAGE_REFRESH_FAILED = new ErrorCode(2024002002, "物料缺口数据刷新失败");
}
