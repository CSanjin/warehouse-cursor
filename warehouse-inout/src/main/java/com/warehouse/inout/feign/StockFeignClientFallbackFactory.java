package com.warehouse.inout.feign;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class StockFeignClientFallbackFactory implements FallbackFactory<StockFeignClient> {
    @Override
    public StockFeignClient create(Throwable cause) {
        log.error("远程调用stock库存服务异常:{}",cause.getMessage());
        return (goodsId, count) -> {
            //远程库存服务宕机降级
            return false;
        };
    }
}