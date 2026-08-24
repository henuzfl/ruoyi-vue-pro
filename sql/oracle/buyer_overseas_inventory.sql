-- 驻外库存业务表
CREATE TABLE buyer_overseas_inventory
(
    id                     NUMBER(20)                        NOT NULL,
    warehouse              VARCHAR2(100),
    owner_code             VARCHAR2(100),
    supplier_code          VARCHAR2(100),
    supplier_name          VARCHAR2(255),
    item_code              VARCHAR2(255),
    item_name              VARCHAR2(500),
    item_specification     VARCHAR2(1000),
    inventory_quantity     NUMBER(19),
    occupied_quantity      NUMBER(19),
    available_quantity     NUMBER(19),
    frozen_quantity        NUMBER(19),
    creator                VARCHAR2(64) DEFAULT '',
    create_time            DATE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    updater                VARCHAR2(64) DEFAULT '',
    update_time            DATE DEFAULT CURRENT_TIMESTAMP NOT NULL,
    deleted                NUMBER(1) DEFAULT 0 NOT NULL,
    tenant_id              NUMBER(20) DEFAULT 0 NOT NULL,
    CONSTRAINT pk_buyer_overseas_inventory PRIMARY KEY (id)
);

COMMENT ON TABLE buyer_overseas_inventory IS '驻外库存';
COMMENT ON COLUMN buyer_overseas_inventory.warehouse IS '仓库';
COMMENT ON COLUMN buyer_overseas_inventory.owner_code IS '货主代码';
COMMENT ON COLUMN buyer_overseas_inventory.supplier_code IS '供应商代码';
COMMENT ON COLUMN buyer_overseas_inventory.supplier_name IS '供应商名称';
COMMENT ON COLUMN buyer_overseas_inventory.item_code IS '货品编码';
COMMENT ON COLUMN buyer_overseas_inventory.item_name IS '货品名称';
COMMENT ON COLUMN buyer_overseas_inventory.item_specification IS '货品规格';
COMMENT ON COLUMN buyer_overseas_inventory.inventory_quantity IS '库存数量';
COMMENT ON COLUMN buyer_overseas_inventory.occupied_quantity IS '占用数量';
COMMENT ON COLUMN buyer_overseas_inventory.available_quantity IS '可用量';
COMMENT ON COLUMN buyer_overseas_inventory.frozen_quantity IS '冻结数量';

CREATE INDEX idx_buyer_overseas_inv_owner ON buyer_overseas_inventory (owner_code);
CREATE INDEX idx_buyer_overseas_inv_supplier ON buyer_overseas_inventory (supplier_code);
CREATE INDEX idx_buyer_overseas_inv_item ON buyer_overseas_inventory (item_code);

-- 菜单及按钮权限；父菜单按名称查找，避免依赖环境中的固定菜单 ID。
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
        (v_menu_id, '驻外库存', 'buyer:overseas-inventory:query', 2, 99, v_parent_id,
         'overseas-inventory', 'ep:box', 'buyer/overseasinventory/index', 'OverseasInventory',
         0, 1, 1, 1, 'admin', SYSDATE, 'admin', SYSDATE, 0);

    FOR item IN (
        SELECT 1 sort_no, '驻外库存查询' name, 'buyer:overseas-inventory:query' permission FROM dual UNION ALL
        SELECT 2, '驻外库存新增', 'buyer:overseas-inventory:create' FROM dual UNION ALL
        SELECT 3, '驻外库存修改', 'buyer:overseas-inventory:update' FROM dual UNION ALL
        SELECT 4, '驻外库存删除', 'buyer:overseas-inventory:delete' FROM dual UNION ALL
        SELECT 5, '驻外库存导入', 'buyer:overseas-inventory:import' FROM dual UNION ALL
        SELECT 6, '驻外库存导出', 'buyer:overseas-inventory:export' FROM dual
    ) LOOP
        INSERT INTO system_menu
            (id, name, permission, type, sort, parent_id, path, icon, component, component_name,
             status, visible, keep_alive, always_show, creator, create_time, updater, update_time, deleted)
        VALUES
            (v_menu_id + item.sort_no, item.name, item.permission, 3, item.sort_no, v_menu_id,
             '', '#', NULL, NULL, 0, 1, 1, 1, 'admin', SYSDATE, 'admin', SYSDATE, 0);
    END LOOP;
END;
/

COMMIT;
