package cn.iocoder.yudao.module.wm.service.bom;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.wm.controller.admin.distributiontask.vo.DistributionTaskPageReqVO;
import cn.iocoder.yudao.module.wm.controller.admin.distributiontask.vo.DistributionTaskSaveReqVO;
import cn.iocoder.yudao.module.wm.dal.dataobject.distributiontask.DistributionTaskDO;
import cn.iocoder.yudao.module.wm.dal.mysql.distributiontask.DistributionTaskMapper;
import cn.iocoder.yudao.module.wm.service.distributiontask.DistributionTaskService;
import com.alibaba.druid.util.StringUtils;
import com.baomidou.dynamic.datasource.annotation.DS;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.math.BigDecimal;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.wm.enums.ErrorCodeConstants.DISTRIBUTION_TASK_NOT_EXISTS;
import cn.iocoder.yudao.module.wm.service.bom.BomService;
import cn.iocoder.yudao.module.wm.util.SapRfcUtils;
import com.sap.conn.jco.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

@Service
@Slf4j
public class BomServiceImpl implements BomService {

    @Autowired
    private SapRfcUtils sapRfcUtils;

    @Override
    public List<Map<String, Object>> getBomFromSap(Map<String, Object> conditions) {
        // 这里放之前写的方案1代码
        JCoDestination destination = null;
        JCoFunction function = null;

        try {
            List<Map<String, Object>> result = new ArrayList<>();

            // 获取参数
            String capid = (String) conditions.get("param1");
            String datuv = (String) conditions.get("param2");
            String mehRs = (String) conditions.get("param3");
            String mtnrv = (String) conditions.get("param4");
            String werks = (String) conditions.get("param5");

            // 参数校验
            if (StringUtils.isEmpty(mtnrv) || StringUtils.isEmpty(werks)) {
                throw new IllegalArgumentException("物料号和工厂不能为空");
            }

            // 调用SAP
            destination = sapRfcUtils.getDestination();
            JCoRepository repository = destination.getRepository();
            function = repository.getFunction("ZTLPP0001_BOMEXP");

            if (function == null) {
                throw new RuntimeException("SAP函数 ZTLPP0001_BOMEXP 不存在");
            }

            // 设置参数
            JCoParameterList importParamList = function.getImportParameterList();
            importParamList.setValue("CAPID", capid != null ? capid : "PP01");
            importParamList.setValue("DATUV", datuv != null ? datuv : "99991231");
            importParamList.setValue("MEHRS", mehRs != null ? mehRs : "X");
            importParamList.setValue("MTNRV", mtnrv);
            importParamList.setValue("WERKS", werks);

            // 执行
            function.execute(destination);

            // 处理结果
            JCoTable stbTable = function.getTableParameterList().getTable("STB");

            if (stbTable != null && !stbTable.isEmpty()) {
                for (int i = 0; i < stbTable.getNumRows(); i++) {
                    stbTable.setRow(i);

                    Map<String, Object> item = new HashMap<>();
                    item.put("WERKS", stbTable.getString("WERKS"));
                    item.put("STUFE", stbTable.getString("STUFE"));
                    item.put("WEGXX", stbTable.getString("WEGXX"));
                    item.put("BMTYP", stbTable.getString("BMTYP"));
                    item.put("VWEGX", stbTable.getString("VWEGX"));
                    item.put("OJTXB", stbTable.getString("OJTXB"));
                    item.put("OJTXP", stbTable.getString("OJTXP"));
                    item.put("MTART", stbTable.getString("MTART"));
                    item.put("MENGE", stbTable.getString("MENGE"));
                    item.put("MEINS", stbTable.getString("MEINS"));
                    item.put("IDNRK", stbTable.getString("IDNRK"));

                    result.add(item);
                }
            }

            log.info("成功获取BOM数据，物料: {}, 工厂: {}, 记录数: {}", mtnrv, werks, result.size());
            return result;

        } catch (JCoException e) {
            log.error("SAP BOM查询失败", e);
            throw new RuntimeException("获取BOM数据失败: " + e.getMessage(), e);
        } finally {
            // 清理资源
        }
    }

    @Override
    public List<Map<String, Object>> getBomByMaterial(String materialNumber, String plant) {
        Map<String, Object> conditions = new HashMap<>();
        conditions.put("param1", "PP01");
        conditions.put("param2", "99991231");  // 默认最大日期
        conditions.put("param3", "X");
        conditions.put("param4", materialNumber);
        conditions.put("param5", plant);

        return getBomFromSap(conditions);
    }
}