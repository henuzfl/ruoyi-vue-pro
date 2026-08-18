package cn.iocoder.yudao.module.wm.dal.mysql.realtimestock;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SapStockImportMapper {
    // 这里可以添加SAP拉取导入相关的数据库操作方法
    // 例如：
    // int insertOrUpdateFromSap(@Param("list") List<StockItem> items);
    // void clearTempSapData();
    // List<SapStockVO> selectPendingImport();
}
