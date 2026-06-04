package com.warehouse.stock.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.warehouse.stock.entity.Stock;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface StockMapper extends BaseMapper<Stock> {
}