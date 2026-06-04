package com.warehouse.inout.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "warehouse-stock", fallbackFactory = StockFeignClientFallbackFactory.class)
public interface StockFeignClient {
    @PutMapping("/stock/update")
    Boolean updateStock(@RequestParam("goodsId") Long goodsId, @RequestParam("num") Integer count);
}