package cn.iocoder.yudao.module.wm.api.bom;

import cn.iocoder.yudao.module.wm.api.bom.BomApi;
import cn.iocoder.yudao.module.wm.api.bom.dto.BomRespDTO;
import cn.iocoder.yudao.module.wm.service.bom.BomService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * BOM API 实现类
 */
@Service
public class BomApiImpl implements BomApi {

    @Resource
    private BomService bomService;

    @Override
    public List<BomRespDTO> getBomByMaterial(String materialNumber, String plant) {
        // 调用现有的 SAP 查询
        List<Map<String, Object>> bomData = bomService.getBomByMaterial(materialNumber, plant);

        // 转换为 BomRespDTO
        return convertToBomRespDTOList(bomData);
    }

    @Override
    public BomRespDTO getBom(String bomId) {
        // bomId 格式为 "工厂:物料号"，例如 "1000:MAT001"
        String[] parts = bomId.split(":");
        if (parts.length != 2) {
            throw new IllegalArgumentException("BOM ID 格式错误，应为 '工厂:物料号'");
        }

        String plant = parts[0];
        String materialNumber = parts[1];

        List<BomRespDTO> bomList = getBomByMaterial(materialNumber, plant);

        // 返回第一个匹配的 BOM（或者根据业务需求调整）
        return bomList.stream()
                .filter(bom -> bom.getIdnrk().equals(materialNumber))
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<BomRespDTO> getBomList(Collection<String> bomIds) {
        List<BomRespDTO> result = new ArrayList<>();

        for (String bomId : bomIds) {
            BomRespDTO bom = getBom(bomId);
            if (bom != null) {
                result.add(bom);
            }
        }

        return result;
    }

    @Override
    public void validateBomList(Collection<String> bomIds) {
        List<BomRespDTO> bomList = getBomList(bomIds);

        // 检查是否所有 BOM 都找到了
        if (bomList.size() != bomIds.size()) {
            Set<String> foundIds = bomList.stream()
                    .map(bom -> bom.getWerks() + ":" + bom.getIdnrk())
                    .collect(Collectors.toSet());

            Set<String> missingIds = new HashSet<>(bomIds);
            missingIds.removeAll(foundIds);

            throw new IllegalArgumentException("以下 BOM 不存在或无效: " + missingIds);
        }

        // 这里可以添加其他校验逻辑，比如检查 BOM 状态等
    }

    /**
     * 将 SAP 返回的 Map 转换为 BomRespDTO 列表
     */
    private List<BomRespDTO> convertToBomRespDTOList(List<Map<String, Object>> bomData) {
        List<BomRespDTO> result = new ArrayList<>();

        for (Map<String, Object> item : bomData) {
            BomRespDTO dto = new BomRespDTO();
            dto.setWerks((String) item.get("WERKS"));
            dto.setStufe((String) item.get("STUFE"));
            dto.setWegxx((String) item.get("WEGXX"));
            dto.setBmtyp((String) item.get("BMTYP"));
            dto.setVwegx((String) item.get("VWEGX"));
            dto.setOjtxb((String) item.get("OJTXB"));
            dto.setOjtxp((String) item.get("OJTXP"));
            dto.setMtart((String) item.get("MTART"));
            dto.setMenge((String) item.get("MENGE"));
            dto.setMeins((String) item.get("MEINS"));
            dto.setIdnrk((String) item.get("IDNRK"));

            // 设置组合 ID
            dto.setBomId(dto.getWerks() + ":" + dto.getIdnrk());

            result.add(dto);
        }

        return result;
    }
}