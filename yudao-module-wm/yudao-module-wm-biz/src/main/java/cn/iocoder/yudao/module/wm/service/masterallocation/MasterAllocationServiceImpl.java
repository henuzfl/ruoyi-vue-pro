package cn.iocoder.yudao.module.wm.service.masterallocation;

import com.baomidou.dynamic.datasource.annotation.DS;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.CallableStatement;
import java.sql.Connection;

@Service
@DS("oracle")
@Slf4j
public class MasterAllocationServiceImpl implements MasterAllocationService {

    @Autowired
    private DataSource dataSource;

    @Override
    public void executeMasterAllocation() {
        // 按顺序执行三个存储过程
        executeProcedure("PRC_MASTER_ALLOCATION");
        executeProcedure("sp_material_shortage_report");
        executeProcedure("SP_REFRESH_MONTHLY_SUPPLY");
    }

    /**
     * 执行单个存储过程（无参数）
     */
    private void executeProcedure(String procedureName) {
        log.info("开始执行存储过程: {}", procedureName);
        try (Connection conn = dataSource.getConnection();
             CallableStatement cs = conn.prepareCall("{ call " + procedureName + "() }")) {
            cs.execute();
            log.info("存储过程 {} 执行完成", procedureName);
        } catch (Exception e) {
            log.error("执行存储过程 {} 失败", procedureName, e);
            throw new RuntimeException("存储过程 " + procedureName + " 执行失败", e);
        }
    }
}