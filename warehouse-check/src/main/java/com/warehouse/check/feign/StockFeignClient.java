package com.warehouse.check.feign;

import com.warehouse.common.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name = "warehouse-stock", contextId = "checkStockFeign", fallbackFactory = StockFeignClientFallbackFactory.class)
public interface StockFeignClient {

    @GetMapping("/api/stock/query")
    Result<Map<String, Object>> query(@RequestParam("warehouseId") Long warehouseId,
                                      @RequestParam("goodsId") Long goodsId);
}
