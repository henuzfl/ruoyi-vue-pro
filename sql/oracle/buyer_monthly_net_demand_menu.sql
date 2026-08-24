-- 月净需求菜单（实时计算，不需要新建业务表）
DECLARE
    v_parent_id NUMBER;
    v_menu_id NUMBER;
BEGIN
    SELECT id INTO v_parent_id
    FROM (SELECT id FROM system_menu WHERE name = '工起' AND deleted = 0 ORDER BY id)
    WHERE ROWNUM = 1;

    SELECT NVL(MAX(id), 0) + 1 INTO v_menu_id FROM system_menu;
    INSERT INTO system_menu
        (id, name, permission, type, sort, parent_id, path, icon, component, component_name,
         status, visible, keep_alive, always_show, creator, create_time, updater, update_time, deleted)
    VALUES
        (v_menu_id, '月净需求', 'buyer:monthly-net-demand:query', 2, 100, v_parent_id,
         'monthly-net-demand', 'ep:data-analysis', 'buyer/monthlynetdemand/index', 'MonthlyNetDemand',
         0, 1, 1, 1, 'admin', SYSDATE, 'admin', SYSDATE, 0);
END;
/

COMMIT;
