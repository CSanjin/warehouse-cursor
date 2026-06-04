package com.warehouse.stock.api;

import com.warehouse.common.feign.FeignFallbackFactory;
import com.warehouse.common.result.Result;
import com.warehouse.stock.entity.StockRecord;
import org.springframework.stereotype.Component;

@Component
public class StockFeignFallbackFactory extends FeignFallbackFactory<StockFeignApi> {

    @Override
    public StockFeignApi create(Throwable cause) {
        logError("warehouse-stock", cause);
        return (warehouseId, goodsId) -> degradeResult();
    }
}
