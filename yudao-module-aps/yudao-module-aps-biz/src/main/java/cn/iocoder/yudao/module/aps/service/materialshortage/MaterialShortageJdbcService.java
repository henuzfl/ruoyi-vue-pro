package cn.iocoder.yudao.module.aps.service.materialshortage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
@Slf4j
public class MaterialShortageJdbcService {

    @Resource
    private JdbcTemplate jdbcTemplate;

    /**
     * 调用存储过程刷新缺口数据
     */
    public void callRefreshProcedure() {
        jdbcTemplate.execute("CALL sp_update_material_shortage_report()");
        log.info("存储过程 sp_update_material_shortage_report 执行完成");
    }
}