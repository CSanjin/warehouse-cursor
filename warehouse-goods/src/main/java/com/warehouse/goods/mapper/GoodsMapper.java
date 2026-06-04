package com.warehouse.goods.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.warehouse.goods.entity.Goods;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GoodsMapper extends BaseMapper<Goods> {
}
