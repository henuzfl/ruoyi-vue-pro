-- 月净需求性能优化：执行前请先在目标环境确认以下索引名称未被占用。

-- 1. 保存导入时计算出的有效计划月份，避免查询时对日期字符串执行正则。
ALTER TABLE buyer_vehicle_plan ADD plan_month VARCHAR2(7);

-- 2. 按既有优先级回填存量计划：臂架上下线日期 > 转台日期 > 成台完工日期。
UPDATE buyer_vehicle_plan p
SET p.plan_month = CASE
    WHEN p.boom_top_bottom_plan_date IS NOT NULL
         AND TRIM(p.boom_top_bottom_plan_date) <> '/'
        THEN REGEXP_SUBSTR(p.boom_top_bottom_plan_date, '[0-9]{4}') || '-' ||
             LPAD(REGEXP_SUBSTR(p.boom_top_bottom_plan_date, '[0-9]+', 1, 2), 2, '0')
    WHEN p.turntable_plan_date IS NOT NULL
         AND TRIM(p.turntable_plan_date) <> '/'
        THEN REGEXP_SUBSTR(p.turntable_plan_date, '[0-9]{4}') || '-' ||
             LPAD(REGEXP_SUBSTR(p.turntable_plan_date, '[0-9]+', 1, 2), 2, '0')
    WHEN p.finished_product_plan_date IS NOT NULL
        THEN TO_CHAR(p.finished_product_plan_date, 'YYYY-MM')
END;

COMMIT;

-- 3. 查询索引。物料编码放在首列，使按本次物料集合查询时能够快速定位。
CREATE INDEX idx_buyer_vehicle_plan_month
    ON buyer_vehicle_plan (import_date, deleted, plan_month);
CREATE INDEX idx_buyer_vehicle_config_net
    ON buyer_vehicle_config (import_date, deleted, vehicle_model, seq_no_2026);
CREATE INDEX idx_buyer_code_config_host
    ON buyer_code_config (host_code, deleted);
CREATE INDEX idx_real_time_stock_material
    ON real_time_stock (material_no, deleted, status);
CREATE INDEX idx_buyer_overseas_inv_net
    ON buyer_overseas_inventory (item_code, deleted);
CREATE INDEX idx_buyer_open_order_material
    ON buyer_open_order (material_no, deleted);

-- 编码缓存通过更新时间判断是否刷新，此索引用于快速取得最新版本值。
CREATE INDEX idx_buyer_code_config_update
    ON buyer_code_config (update_time);
