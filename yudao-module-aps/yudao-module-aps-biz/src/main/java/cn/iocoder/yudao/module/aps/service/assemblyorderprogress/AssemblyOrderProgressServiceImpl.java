package cn.iocoder.yudao.module.aps.service.assemblyorderprogress;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.module.aps.controller.admin.assemblyorderprogress.vo.AssemblyOrderProgressPageReqVO;
import cn.iocoder.yudao.module.aps.controller.admin.assemblyorderprogress.vo.AssemblyOrderProgressRespVO;
import cn.iocoder.yudao.module.aps.controller.admin.assemblyorderprogress.vo.AssemblyOrderShortageRespVO;
import cn.iocoder.yudao.module.aps.dal.mysql.assemblyorderprogress.AssemblyOrderProgressMapper;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.List;
import java.time.LocalDate;
import java.time.temporal.WeekFields;

@Service
@DS("oracle")
@Validated
@Slf4j
public class AssemblyOrderProgressServiceImpl implements AssemblyOrderProgressService {

    @Resource
    private AssemblyOrderProgressMapper assemblyOrderProgressMapper;

    @Override
    public PageResult<AssemblyOrderProgressRespVO> getPage(AssemblyOrderProgressPageReqVO reqVO) {
        Page<AssemblyOrderProgressRespVO> page = new Page<>(reqVO.getPageNo(), reqVO.getPageSize());
        List<AssemblyOrderProgressRespVO> list = assemblyOrderProgressMapper.selectPage(page, reqVO);
        // 注意：selectPage 会修改 page 的 total，但我们返回自定义 PageResult
        return new PageResult<>(list, page.getTotal());
    }

    @Override
    public List<AssemblyOrderShortageRespVO> getShortages(String materialCode, String scheduleTime) {
        String startDate = null;
        String endDate = null;
        if (scheduleTime != null && !scheduleTime.isEmpty()) {
            if (scheduleTime.matches("\\d{4}-\\d{2}-\\d{2}")) { // 日
                startDate = scheduleTime;
                endDate = scheduleTime;
            } else if (scheduleTime.matches("\\d{4}-W\\d{2}")) { // 周
                String[] parts = scheduleTime.split("-W");
                int year = Integer.parseInt(parts[0]);
                int week = Integer.parseInt(parts[1]);
                // ISO 周数：周一为第一天
                LocalDate firstDay = LocalDate.of(year, 1, 1)
                        .with(WeekFields.ISO.weekOfYear(), week)
                        .with(WeekFields.ISO.dayOfWeek(), 1);
                LocalDate lastDay = firstDay.plusDays(6);
                startDate = firstDay.toString();
                endDate = lastDay.toString();
            } else if (scheduleTime.matches("\\d{4}-\\d{2}")) { // 月
                String[] parts = scheduleTime.split("-");
                int year = Integer.parseInt(parts[0]);
                int month = Integer.parseInt(parts[1]);
                LocalDate firstDay = LocalDate.of(year, month, 1);
                LocalDate lastDay = firstDay.withDayOfMonth(firstDay.lengthOfMonth());
                startDate = firstDay.toString();
                endDate = lastDay.toString();
            } else {
                // 未知格式，当作日期字符串直接传递（兼容旧数据）
                startDate = scheduleTime;
                endDate = scheduleTime;
            }
        }
        return assemblyOrderProgressMapper.selectShortagesByDateRange(materialCode, startDate, endDate);
    }

    @Override
    public List<AssemblyOrderProgressRespVO> getExportList(AssemblyOrderProgressPageReqVO reqVO) {
        // 导出时不分页，直接查询全部
        return assemblyOrderProgressMapper.selectExportList(reqVO);
    }
}