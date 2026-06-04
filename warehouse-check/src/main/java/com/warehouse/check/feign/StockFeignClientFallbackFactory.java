package com.warehouse.check.feign;

import com.warehouse.common.feign.FeignFallbackFactory;
import com.warehouse.common.result.Result;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class StockFeignClientFallbackFactory extends FeignFallbackFactory<StockFeignClient> {

    @Override
    public StockFeignClient create(Throwable cause) {
        logError("warehouse-stock", cause);
        return (warehouseId, goodsId) -> degradeResult();
    }
}
