package com.warehouse.goods.api;

import com.warehouse.common.feign.FeignFallbackFactory;
import com.warehouse.common.result.Result;
import com.warehouse.goods.entity.Goods;
import org.springframework.stereotype.Component;

@Component
public class GoodsFeignFallbackFactory extends FeignFallbackFactory<GoodsFeignApi> {

    @Override
    public GoodsFeignApi create(Throwable cause) {
        logError("warehouse-goods", cause);
        return id -> degradeResult();
    }
}
