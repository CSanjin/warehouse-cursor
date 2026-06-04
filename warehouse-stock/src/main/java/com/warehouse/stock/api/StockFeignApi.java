package com.warehouse.stock.api;

import com.warehouse.common.result.Result;
import com.warehouse.stock.entity.StockRecord;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "warehouse-stock", contextId = "stockFeignApi", fallbackFactory = StockFeignFallbackFactory.class)
public interface StockFeignApi {

    @GetMapping("/api/stock/query")
    Result<StockRecord> query(@RequestParam("warehouseId") Long warehouseId,
                              @RequestParam("goodsId") Long goodsId);
}
