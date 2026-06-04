package com.warehouse.goods.api;

import com.warehouse.common.result.Result;
import com.warehouse.goods.entity.Goods;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "warehouse-goods", contextId = "goodsFeignApi", fallbackFactory = GoodsFeignFallbackFactory.class)
public interface GoodsFeignApi {

    @GetMapping("/api/goods/{id}")
    Result<Goods> getById(@PathVariable("id") Long id);
}
