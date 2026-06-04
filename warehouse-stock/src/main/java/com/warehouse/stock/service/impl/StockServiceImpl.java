package com.warehouse.stock.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.warehouse.stock.entity.Stock;
import com.warehouse.stock.mapper.StockMapper;
import com.warehouse.stock.service.StockService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;

@Service
public class StockServiceImpl implements StockService {
    @Resource
    private StockMapper mapper;

    //本地事务，RM不需要GlobalTransactional
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean updateStock(Long goodsId, Integer num) {
        LambdaQueryWrapper<Stock> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Stock::getGoodsId, goodsId);
        Stock stock = mapper.selectOne(wrapper);
        if (stock == null) {
            stock = new Stock();
            stock.setGoodsId(goodsId);
            stock.setStockNum(num);
            mapper.insert(stock);
            return true;
        }
        stock.setStockNum(stock.getStockNum() + num);
        mapper.updateById(stock);

        //测试回滚：放开异常，inout数据自动回滚
        //throw new RuntimeException("库存异常");
        return true;
    }
}