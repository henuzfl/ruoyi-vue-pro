-- 生产订单物料供需报表查询索引。
-- 仅生成脚本，不由应用自动执行；请在目标 Oracle 库评审执行计划后人工执行。
-- 幂等规则：同一表已存在列顺序完全一致的索引时，不重复创建。

DECLARE
    PROCEDURE ensure_index(
        p_index_name IN VARCHAR2,
        p_table_name IN VARCHAR2,
        p_columns    IN VARCHAR2
    ) IS
        v_count NUMBER;
    BEGIN
        SELECT COUNT(*)
          INTO v_count
          FROM (
              SELECT LISTAGG(column_name, ',') WITHIN GROUP (ORDER BY column_position) AS column_list
                FROM user_ind_columns
               WHERE table_name = UPPER(p_table_name)
               GROUP BY index_name
          ) existing_indexes
         WHERE existing_indexes.column_list = UPPER(REPLACE(p_columns, ' ', ''));

        IF v_count = 0 THEN
            EXECUTE IMMEDIATE 'CREATE INDEX ' || p_index_name || ' ON ' || p_table_name
                || ' (' || p_columns || ')';
            DBMS_OUTPUT.PUT_LINE('CREATED: ' || p_index_name);
        ELSE
            DBMS_OUTPUT.PUT_LINE('SKIPPED EQUIVALENT INDEX: ' || p_table_name
                || ' (' || p_columns || ')');
        END IF;
    END ensure_index;
BEGIN
    ensure_index('IDX_TOD_ORD_MAT_DEL', 'TRACE_ORDER_DEMAND',
        'ORDER_NO, MATERIAL_NO, DELETED');
    ensure_index('IDX_AMP_ORD_ASS_DEL_DT', 'APS_MAIN_PLAN',
        'PRODUCTION_ORDER_NO, ASSEMBLY_MATERIAL_NO, DELETED, SCHEDULED_DATE');
    ensure_index('IDX_SO_ORD_ASS_DEL', 'SUB_ORDER',
        'PRODUCTION_ORDER_NO, ASSEMBLY_MATERIAL_NO, DELETED');
    ensure_index('IDX_SO_ASS_STA_DEL', 'SUB_ORDER',
        'ASSEMBLY_MATERIAL_NO, SYSTEM_STATUS, DELETED');
    ensure_index('IDX_RTS_MAT_DEL', 'REAL_TIME_STOCK',
        'MATERIAL_NO, DELETED');
    ensure_index('IDX_BOO_ORD_MAT_DEL_DT', 'BUYER_OPEN_ORDER',
        'PRODUCTION_ORDER_NO, MATERIAL_NO, DELETED, REQUIRED_ARRIVAL_DATE');
    ensure_index('IDX_MMI_MAT_DEL_PROC', 'MATERIAL_MASTER_IMPORT',
        'MATERIAL_NO, DELETED, PROCUREMENT_TYPE');
END;
/
