package com.warehouse.inout.feign;

import com.warehouse.common.feign.FeignFallbackFactory;
import com.warehouse.common.result.Result;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class GoodsFeignClientFallbackFactory extends FeignFallbackFactory<GoodsFeignClient> {

    @Override
    public GoodsFeignClient create(Throwable cause) {
        logError("warehouse-goods", cause);
        return id -> degradeResult();
    }
}
