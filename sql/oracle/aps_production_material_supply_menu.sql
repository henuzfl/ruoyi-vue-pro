-- 生产订单物料供需菜单（挂载到“计划管理”）
-- 幂等执行：已存在相同 permission 的菜单或按钮时不会重复插入。
DECLARE
    v_parent_id NUMBER;
    v_menu_id NUMBER;
    v_child_id NUMBER;
BEGIN
    SELECT id INTO v_parent_id
    FROM (
        SELECT id
        FROM system_menu
        WHERE name = '计划管理' AND deleted = 0
        ORDER BY id
    )
    WHERE ROWNUM = 1;

    BEGIN
        SELECT id INTO v_menu_id
        FROM (
            SELECT id
            FROM system_menu
            WHERE permission = 'aps:production-material-supply:query' AND type = 2 AND deleted = 0
            ORDER BY id
        )
        WHERE ROWNUM = 1;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN
            SELECT NVL(MAX(id), 0) + 1 INTO v_menu_id FROM system_menu;
            INSERT INTO system_menu
                (id, name, permission, type, sort, parent_id, path, icon, component, component_name,
                 status, visible, keep_alive, always_show, creator, create_time, updater, update_time, deleted)
            VALUES
                (v_menu_id, '生产订单物料供需', 'aps:production-material-supply:query', 2, 100,
                 v_parent_id, 'production-material-supply', 'ep:data-analysis',
                 'aps/productionmaterialsupply/index', 'ProductionMaterialSupply',
                 0, 1, 1, 1, 'admin', SYSDATE, 'admin', SYSDATE, 0);
    END;

    FOR item IN (
        SELECT 1 sort_no, '生产订单物料供需查询' name,
               'aps:production-material-supply:query' permission FROM dual
        UNION ALL
        SELECT 2, '生产订单物料供需导出',
               'aps:production-material-supply:export' FROM dual
    ) LOOP
        BEGIN
            SELECT id INTO v_child_id
            FROM (
                SELECT id
                FROM system_menu
                WHERE permission = item.permission AND type = 3 AND deleted = 0
                ORDER BY id
            )
            WHERE ROWNUM = 1;
        EXCEPTION
            WHEN NO_DATA_FOUND THEN
                SELECT NVL(MAX(id), 0) + 1 INTO v_child_id FROM system_menu;
                INSERT INTO system_menu
                    (id, name, permission, type, sort, parent_id, path, icon, component, component_name,
                     status, visible, keep_alive, always_show, creator, create_time, updater, update_time, deleted)
                VALUES
                    (v_child_id, item.name, item.permission, 3, item.sort_no, v_menu_id,
                     '', '#', NULL, NULL, 0, 1, 1, 1,
                     'admin', SYSDATE, 'admin', SYSDATE, 0);
        END;
    END LOOP;
END;
/

COMMIT;
