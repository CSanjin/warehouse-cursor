package com.warehouse.stock.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.warehouse.stock.entity.StockRecord;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface StockRecordMapper extends BaseMapper<StockRecord> {
}
