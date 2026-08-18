package cn.iocoder.yudao.module.wm.controller.admin.material.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.util.StringUtils;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Schema(description = "管理后台 - SAP物料批量查询 Request VO")
@Data
public class SapMaterialQueryReqVO {

    @Schema(description = "物料号列表", required = true, example = "['100001','100002']")
    @NotEmpty(message = "物料号列表不能为空")
    private List<String> materialNumbers;

    @Schema(description = "工厂", required = false, example = "6400", defaultValue = "6400")
    private String plant;

    public String getPlantOrDefault(String defaultPlant) {
        return StringUtils.hasText(plant) ? plant : defaultPlant;
    }
}