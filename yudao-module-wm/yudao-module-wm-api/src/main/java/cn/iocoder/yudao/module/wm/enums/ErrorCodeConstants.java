package cn.iocoder.yudao.module.wm.enums;

import cn.iocoder.yudao.framework.common.exception.ErrorCode;

/**
 * System 错误码枚举类
 *
 * system 系统，使用 1-002-000-000 段
 */
public interface ErrorCodeConstants {
    // ========== 仓储模块 1-002-029-000 ==========
    ErrorCode MATERIAL_KITTING_TOOL_NOT_EXISTS = new ErrorCode(1_003_001_000, "物料齐套不存在");
    ErrorCode ORDER_DEMAND_NOT_EXISTS = new ErrorCode(1_003_002_000, "订单追溯需求不存在");
    ErrorCode DISTRIBUTION_TASK_NOT_EXISTS = new ErrorCode(1_003_003_000, "配送任务下发不存在");
}
